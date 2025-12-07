package assignment.service;

import assignment.model.Stock;
import assignment.repo.OrderRepository;
import assignment.repo.StockRepository;
import java.util.List;

public class SalesService {
    private final StockRepository stockRepo;
    private final OrderRepository orderRepo;
    private int nextOrderNo;

    public SalesService(StockRepository stockRepo, OrderRepository orderRepo) {
        this.stockRepo = stockRepo;
        this.orderRepo = orderRepo;
        // Ensure stock is loaded into memory when service is initialized
        this.stockRepo.loadStockFromFile();
        // Load orders from file and restore cart
        restoreCartFromOrders();
        // Load last order number from file to ensure uniqueness across restarts
        this.nextOrderNo = orderRepo.getLastOrderNo() + 1;
    }

    /**
     * Restores the cart from saved orders in the file.
     * This ensures orders persist across application restarts.
     * 
     * Note: Stock quantities are NOT adjusted here because:
     * - When orders are added, stock is deducted and saved to stock.txt immediately
     * - stock.txt already contains the correct (deducted) quantities
     * - We just need to restore orders to the cart, stock is already correct
     */
    private void restoreCartFromOrders() {
        List<Stock> savedOrders = orderRepo.loadAllOrders();
        
        if (savedOrders.isEmpty()) {
            return; // No orders to restore
        }

        // Restore each order to the cart
        // Stock quantities are already correct in stock.txt (deducted when orders were placed)
        for (Stock order : savedOrders) {
            // Find the corresponding stock item to verify it exists
            Stock stockItem = findStockItem(order.getStockID());
            
            if (stockItem != null) {
                // Add order to cart
                stockRepo.getCart().add(order);
                // Stock quantity is already correct (was saved when order was originally placed)
            }
        }
        
        // No need to save stock - it's already correct in the file
    }

    public List<Stock> getAvailableStock() {
        return stockRepo.getStocklist();
    }

    public List<Stock> getCartItems() {
        return stockRepo.getCart();
    }

    public Stock findStockItem(int itemID) {
        for (Stock stock : stockRepo.getStocklist()) {
            if (stock.getStockID() == itemID) {
                return stock;
            }
        }
        return null;
    }

    public Stock findCartItemByOrderNo(int orderNo) {
        // We iterate through the list as order numbers may not be sequential due to removals
        for (Stock item : stockRepo.getCart()) {
            if (item.getOrderNo() == orderNo) {
                return item;
            }
        }
        return null;
    }

    /**
     * Attempts to add an item to the cart, updating stock immediately.
     * @param itemID The ID of the product.
     * @param quantity The amount to order.
     * @return true if successful, false otherwise.
     */
    public boolean addToCart(int itemID, int quantity) {
        Stock foundStock = findStockItem(itemID);

        // Business Rule: Check for validity and sufficient stock
        if (foundStock == null || foundStock.getQty() < quantity || quantity <= 0) {
            return false;
        }

        // 1. Update stocklist (deduct quantity) - In-memory change
        foundStock.setQty(foundStock.getQty() - quantity);

        // 2. Add item to cart
        Stock cartItem = new Stock(
                nextOrderNo,
                foundStock.getStockID(),
                foundStock.getStockName(),
                quantity,
                foundStock.getPrice()
        );
        stockRepo.getCart().add(cartItem);
        
        // 3. Persist the order to file
        orderRepo.appendOrder(cartItem);
        
        // 4. Persist the stock change immediately
        stockRepo.saveStockToFile();
        
        nextOrderNo++;

        return true;
    }

    /**
     * Removes an order from the cart and refunds the quantity back to the stock.
     * @param orderNoRemove The order number to remove.
     * @return true if successful, false otherwise.
     */
    public boolean removeOrder(int orderNoRemove) {
        List<Stock> cart = stockRepo.getCart();
        int indexToRemove = -1;

        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getOrderNo() == orderNoRemove) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            Stock removedItem = cart.remove(indexToRemove);

            // Delete order from file
            orderRepo.deleteOrder(removedItem.getOrderNo());

            // Refund the stock quantity (Business Rule)
            Stock stockItem = findStockItem(removedItem.getStockID());
            if (stockItem != null) {
                stockItem.setQty(stockItem.getQty() + removedItem.getQty());
            }

            // Persist the refund change
            stockRepo.saveStockToFile();
            return true;
        }
        return false;
    }

    /**
     * Edits the quantity of an existing order.
     * @param orderNo The order number to edit.
     * @param quantityChange The amount to add or reduce.
     * @param type 1 for Reduce, 2 for Add.
     * @return true if successful, false otherwise.
     */
    public boolean editOrderQuantity(int orderNo, int quantityChange, int type) {
        Stock cartItem = findCartItemByOrderNo(orderNo);
        if (cartItem == null) return false;

        Stock stockItem = findStockItem(cartItem.getStockID());
        if (stockItem == null) return false;

        int currentCartQty = cartItem.getQty();
        int availableStock = stockItem.getQty();

        if (quantityChange <= 0) return false;

        if (type == 1) { // Reduce Quantity
            if (quantityChange > currentCartQty) return false; // Cannot reduce more than what's ordered

            cartItem.setQty(currentCartQty - quantityChange);
            stockItem.setQty(availableStock + quantityChange); // Refund stock

        } else if (type == 2) { // Add Quantity
            if (quantityChange > availableStock) return false; // Insufficient stock

            cartItem.setQty(currentCartQty + quantityChange);
            stockItem.setQty(availableStock - quantityChange); // Deduct stock

        } else {
            return false; // Invalid type
        }

        // Update order in file
        orderRepo.updateOrder(cartItem);
        
        // Persist the stock changes
        stockRepo.saveStockToFile();
        return true;
    }
}