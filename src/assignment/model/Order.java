package assignment.model;

/**
 * Model class for an Order (cart item).
 * 
 */
public class Order {
    private int orderNo;
    private int stockID;
    private String stockName;
    private int quantity;  // ordered quantity
    private double price;

    /**
     * Constructs an Order with order number.
     * @param orderNo The order number
     * @param stockID The stock ID of the product
     * @param stockName The name of the product
     * @param quantity The ordered quantity
     * @param price The price per unit
     */
    public Order(int orderNo, int stockID, String stockName, int quantity, double price) {
        this.orderNo = orderNo;
        this.stockID = stockID;
        this.stockName = stockName;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Constructs an Order without order number (orderNo defaults to 0).
     * @param stockID The stock ID of the product
     * @param stockName The name of the product
     * @param quantity The ordered quantity
     * @param price The price per unit
     */
    public Order(int stockID, String stockName, int quantity, double price) {
        this.orderNo = 0;
        this.stockID = stockID;
        this.stockName = stockName;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Default constructor.
     */
    public Order() {
        this.orderNo = 0;
        this.stockID = 0;
        this.quantity = 0;
        this.price = 0.0;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public int getStockID() {
        return stockID;
    }

    public String getStockName() {
        return stockName != null ? stockName.toUpperCase() : "";
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Calculates the total cost for this order line.
     * @return The total cost (quantity * price)
     */
    public double calculateTotalCost() {
        return this.quantity * this.price;
    }

    /**
     * Converts order to file string format.
     * Format: stockID \t stockName \t quantity \t price
     * @return String representation for file storage
     */
    public String toFileString() {
        return stockID + "\t" + getStockName() + "\t" + quantity + "\t" + price;
    }

    @Override
    public String toString() {
        // Format for console display
        return "\nPRODUCT ID >> " + getStockID()
                + "\nPRODUCT NAME >> " + getStockName()
                + "\nQUANTITY >> " + getQuantity()
                + "\nPRODUCT PRICE >> RM" + String.format("%.2f", getPrice());
    }
}

