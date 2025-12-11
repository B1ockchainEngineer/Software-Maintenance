package test.assignment.model;

import assignment.model.PaymentResult;

/**
 * Test fixture for creating PaymentResult objects in tests.
 * Provides factory methods and builders for easy test data creation.
 */
public class PaymentResultModelTest {
    
    /**
     * Creates a default test payment result with standard values.
     * @return PaymentResult with subtotal=200.0, discount=20.0, tax=10.8, total=190.8
     */
    public static PaymentResult createDefault() {
        return new PaymentResult(200.0, 20.0, 10.8, 190.8);
    }
    
    /**
     * Creates a test payment result with specified values.
     * @param subtotal The subtotal amount
     * @param discount The discount amount
     * @param tax The tax amount
     * @param total The total amount
     * @return PaymentResult with specified values
     */
    public static PaymentResult create(double subtotal, double discount, double tax, double total) {
        return new PaymentResult(subtotal, discount, tax, total);
    }
    
    /**
     * Creates a test payment result with no discount.
     * @param subtotal The subtotal amount
     * @return PaymentResult with no discount (discount=0.0, tax=6% of subtotal, total=subtotal+tax)
     */
    public static PaymentResult createWithoutDiscount(double subtotal) {
        double tax = subtotal * 0.06;
        double total = subtotal + tax;
        return new PaymentResult(subtotal, 0.0, tax, total);
    }
    
    /**
     * Creates a test payment result with discount rate.
     * @param subtotal The subtotal amount
     * @param discountRate The discount rate (e.g., 0.10 for 10%)
     * @return PaymentResult with discount applied
     */
    public static PaymentResult createWithDiscountRate(double subtotal, double discountRate) {
        double discount = subtotal * discountRate;
        double amountAfterDiscount = subtotal - discount;
        double tax = amountAfterDiscount * 0.06;
        double total = amountAfterDiscount + tax;
        return new PaymentResult(subtotal, discount, tax, total);
    }
    
    /**
     * Creates a test payment result for premium member (15% discount).
     * @param subtotal The subtotal amount
     * @return PaymentResult with 15% discount applied
     */
    public static PaymentResult createPremiumMember(double subtotal) {
        return createWithDiscountRate(subtotal, 0.15);
    }
    
    /**
     * Creates a test payment result for regular member (10% discount).
     * @param subtotal The subtotal amount
     * @return PaymentResult with 10% discount applied
     */
    public static PaymentResult createRegularMember(double subtotal) {
        return createWithDiscountRate(subtotal, 0.10);
    }
    
    /**
     * Creates an empty payment result (all values zero).
     * @return PaymentResult with all values set to 0.0
     */
    public static PaymentResult createEmpty() {
        return new PaymentResult(0.0, 0.0, 0.0, 0.0);
    }
    
    /**
     * Builder class for creating PaymentResult objects with fluent API.
     */
    public static class Builder {
        private double subtotal = 200.0;
        private double discount = 20.0;
        private double tax = 10.8;
        private double total = 190.8;
        
        public Builder withSubtotal(double subtotal) {
            this.subtotal = subtotal;
            return this;
        }
        
        public Builder withDiscount(double discount) {
            this.discount = discount;
            return this;
        }
        
        public Builder withTax(double tax) {
            this.tax = tax;
            return this;
        }
        
        public Builder withTotal(double total) {
            this.total = total;
            return this;
        }
        
        public Builder withDiscountRate(double discountRate) {
            this.discount = subtotal * discountRate;
            return this;
        }
        
        public Builder calculateTax() {
            double amountAfterDiscount = subtotal - discount;
            this.tax = amountAfterDiscount * 0.06;
            this.total = amountAfterDiscount + tax;
            return this;
        }
        
        public PaymentResult build() {
            return new PaymentResult(subtotal, discount, tax, total);
        }
    }
    
    /**
     * Creates a new builder instance.
     * @return Builder for creating PaymentResult objects
     */
    public static Builder builder() {
        return new Builder();
    }
}

