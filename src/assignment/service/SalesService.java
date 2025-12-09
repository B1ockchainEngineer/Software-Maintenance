package assignment.service;

import assignment.model.Stock;
import assignment.repo.OrderRepository;
import assignment.repo.StockRepository;
import java.util.List;

import assignment.util.SalesUtil;

public class SalesService {
    private final StockRepository stockRepo;
    private final OrderRepository orderRepo;

    public SalesService(StockRepository stockRepo, OrderRepository orderRepo) {
        this.stockRepo = stockRepo;
        this.orderRepo = orderRepo;
        // Ensure stock is loaded into memory when service is initialized
        this.stockRepo.loadStockFromFile();
        // Load orders from file and restore cart
        // Order numbers are assigned based on position (1, 2, 3, ...)
        restoreCartFromOrders();
    }
    
    // ... code truncated ...

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

        if (type == SalesUtil.REDUCE_QUANTITY) { // Reduce Quantity
            if (quantityChange > currentCartQty) return false; // Cannot reduce more than what's ordered

            cartItem.setQty(currentCartQty - quantityChange);
            stockItem.setQty(availableStock + quantityChange); // Refund stock

        } else if (type == SalesUtil.ADD_QUANTITY) { // Add Quantity
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

    /**
     * Restores the cart from saved orders in the file.
     * This ensures orders persist across application restarts.
     * Order numbers are assigned based on position in file (1, 2, 3, ...).
     * 
     * Note: Stock quantities are NOT adjusted here because:
     * - When orders are added, stock is deducted and saved to stock.txt immediately
     * - stock.txt already contains the correct (deducted) quantities
     * - We just need to restore orders to the cart, stock is already correct
     */
    private void restoreCartFromOrders() {
        // loadAllOrders() already assigns orderNo based on position (1, 2, 3, ...)
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
                // Add order to cart (orderNo already assigned by loadAllOrders based on position)
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
     * Order number is calculated as: current cart size + 1
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

        // 2. Calculate order number based on current cart size (position-based)
        int nextOrderNo = stockRepo.getCart().size() + 1;
        
        // 3. Add item to cart
        Stock cartItem = new Stock(
                nextOrderNo,
                foundStock.getStockID(),
                foundStock.getStockName(),
                quantity,
                foundStock.getPrice()
        );
        stockRepo.getCart().add(cartItem);
        
        // 4. Persist the order to file (orderNo not stored in file, determined by position)
        orderRepo.appendOrder(cartItem);
        
        // 5. Persist the stock change immediately
        stockRepo.saveStockToFile();

        return true;
    }

    /**
     * Removes an order from the cart and refunds the quantity back to the stock.
     * After deletion, order numbers are reassigned based on position (1, 2, 3, ...).
     * @param orderNoRemove The order number (position) to remove.
     * @return true if successful, false otherwise.
     */
    public boolean removeOrder(int orderNoRemove) {
        List<Stock> cart = stockRepo.getCart();
        int indexToRemove = -1;

        // Find the order by orderNo (which represents position)
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getOrderNo() == orderNoRemove) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            Stock removedItem = cart.remove(indexToRemove);

            // Delete order from file (by position)
            orderRepo.deleteOrder(orderNoRemove);

            // Reassign order numbers for remaining orders (1, 2, 3, ...)
            for (int i = 0; i < cart.size(); i++) {
                cart.get(i).setOrderNo(i + 1);
            }

            // Refund the stock quantity (Business Rule)
            Stock stockItem = findStockItem(removedItem.getStockID());
            if (stockItem != null) {
                stockItem.setQty(stockItem.getQty() + removedItem.getQty());
            }

            // Persist the refund change
            stockRepo.saveStockToFile();
            
            // Note: File is already correct after deleteOrder() (order removed from file)
            // We just need to reassign orderNos in memory to match file positions
            // This is already done above with the loop
            
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

        if (type == SalesUtil.REDUCE_QUANTITY) { // Reduce Quantity
            if (quantityChange > currentCartQty) return false; // Cannot reduce more than what's ordered

            cartItem.setQty(currentCartQty - quantityChange);
            stockItem.setQty(availableStock + quantityChange); // Refund stock

        } else if (type == SalesUtil.ADD_QUANTITY) { // Add Quantity
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