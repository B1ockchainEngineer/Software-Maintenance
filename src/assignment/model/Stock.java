package assignment.model;

/**
 * Model class for a Stock item (data structure).
 */
public class Stock {
    private int stockID;
    private String stockName;
    private int qty;
    private double price;
    private int orderNo;

    public Stock(int stockID, String stockName, int qty, double price) {
        this.stockID = stockID;
        this.stockName = stockName;
        this.qty = qty;
        this.price = price;
        this.orderNo = 0; // Default
    }

    public Stock(int orderNo, int stockID, String stockName, int qty, double price) {
        this.orderNo = orderNo;
        this.stockID = stockID;
        this.stockName = stockName;
        this.qty = qty;
        this.price = price;
    }

    public Stock() {
        this.orderNo = 0;
        this.stockID = 0;
        this.qty = 0;
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

    public int getQty() {
        return qty;
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

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Helper used by sales logic to compute the total cost for an order line
    public double calculateTotalCost() {
        return this.qty * this.price;
    }

    public String toFileString() {
        return stockID + "\t" + getStockName() + "\t" + qty + "\t" + price;
    }

    @Override
    public String toString() {
        // Format for console display
        return "\nPRODUCT ID >> " + getStockID()
                + "\nPRODUCT NAME >> " + getStockName()
                + "\nQUANTITY >> " + getQty()
                + "\nPRODUCT PRICE >> RM" + String.format("%.2f", getPrice());
    }
}