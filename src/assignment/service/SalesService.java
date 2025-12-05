package assignment.service;

import assignment.model.Stock;
import assignment.repo.StockRepository;
import java.util.List;

public class SalesService {
    private final StockRepository stockRepo;
    // nextOrderNo should ideally be loaded from a file/persistence layer
    // to ensure uniqueness across application restarts, but we initialize to 1 here.
    private static int nextOrderNo = 1;

    public SalesService(StockRepository stockRepo) {
        this.stockRepo = stockRepo;
        // Ensure stock is loaded into memory when service is initialized
        this.stockRepo.loadStockFromFile();
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
        nextOrderNo++;

        // 3. Persist the stock change immediately
        stockRepo.saveStockToFile();

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

        // Persist the changes
        stockRepo.saveStockToFile();
        return true;
    }
}