package assignment.model;

/**
 * Test fixture for creating Order objects in tests.
 * Provides factory methods and builders for easy test data creation.
 */
public class OrderModelTest {
    
    /**
     * Creates a default test order with standard values.
     * @return Order with orderNo=1, stockID=1001, stockName="Product A", quantity=2, price=50.0
     */
    public static Order createDefault() {
        return new Order(1, 1001, "Product A", 2, 50.0);
    }
    
    /**
     * Creates a test order with specified values.
     * @param orderNo The order number
     * @param stockID The stock ID
     * @param stockName The stock name
     * @param quantity The quantity
     * @param price The price per unit
     * @return Order with specified values
     */
    public static Order create(int orderNo, int stockID, String stockName, int quantity, double price) {
        return new Order(orderNo, stockID, stockName, quantity, price);
    }
    
    /**
     * Creates a test order without order number.
     * @param stockID The stock ID
     * @param stockName The stock name
     * @param quantity The quantity
     * @param price The price per unit
     * @return Order with specified values (orderNo=0)
     */
    public static Order createWithoutOrderNo(int stockID, String stockName, int quantity, double price) {
        return new Order(stockID, stockName, quantity, price);
    }
    
    /**
     * Creates a test order for Product A.
     * @param orderNo The order number
     * @param quantity The quantity
     * @return Order for Product A (stockID=1001, price=50.0)
     */
    public static Order createProductA(int orderNo, int quantity) {
        return new Order(orderNo, 1001, "Product A", quantity, 50.0);
    }
    
    /**
     * Creates a test order for Product B.
     * @param orderNo The order number
     * @param quantity The quantity
     * @return Order for Product B (stockID=1002, price=100.0)
     */
    public static Order createProductB(int orderNo, int quantity) {
        return new Order(orderNo, 1002, "Product B", quantity, 100.0);
    }
    
    /**
     * Creates a test order for Product C.
     * @param orderNo The order number
     * @param quantity The quantity
     * @return Order for Product C (stockID=1003, price=25.0)
     */
    public static Order createProductC(int orderNo, int quantity) {
        return new Order(orderNo, 1003, "Product C", quantity, 25.0);
    }
    
    /**
     * Creates an empty order (default constructor).
     * @return Empty Order with all fields set to default values
     */
    public static Order createEmpty() {
        return new Order();
    }
    
    /**
     * Builder class for creating Order objects with fluent API.
     */
    public static class Builder {
        private int orderNo = 1;
        private int stockID = 1001;
        private String stockName = "Product A";
        private int quantity = 1;
        private double price = 50.0;
        
        public Builder withOrderNo(int orderNo) {
            this.orderNo = orderNo;
            return this;
        }
        
        public Builder withStockID(int stockID) {
            this.stockID = stockID;
            return this;
        }
        
        public Builder withStockName(String stockName) {
            this.stockName = stockName;
            return this;
        }
        
        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }
        
        public Builder withPrice(double price) {
            this.price = price;
            return this;
        }
        
        public Order build() {
            return new Order(orderNo, stockID, stockName, quantity, price);
        }
    }
    
    /**
     * Creates a new builder instance.
     * @return Builder for creating Order objects
     */
    public static Builder builder() {
        return new Builder();
    }
}

