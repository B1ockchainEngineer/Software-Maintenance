package assignment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PaymentResult model class.
 * Tests constructor, getters, and immutability.
 */
@DisplayName("PaymentResult Model Tests")
class PaymentResultModelTest {

    private PaymentResult paymentResult;

    @BeforeEach
    void setUp() {
        paymentResult = new PaymentResult(200.0, 20.0, 10.8, 190.8);
    }

    // ================== CONSTRUCTOR TESTS ==================

    @Test
    @DisplayName("Constructor should initialize all fields correctly")
    void constructor_ShouldInitializeAllFieldsCorrectly() {
        // Given: PaymentResult with all parameters
        PaymentResult pr = new PaymentResult(100.0, 10.0, 5.4, 95.4);

        // Then: All fields should be set correctly
        assertEquals(100.0, pr.getSubtotal(), 0.01);
        assertEquals(10.0, pr.getDiscount(), 0.01);
        assertEquals(5.4, pr.getTax(), 0.01);
        assertEquals(95.4, pr.getTotal(), 0.01);
    }

    @Test
    @DisplayName("Constructor with zero values should work")
    void constructor_WithZeroValues_ShouldWork() {
        // Given: PaymentResult with all zeros
        PaymentResult pr = new PaymentResult(0.0, 0.0, 0.0, 0.0);

        // Then: Should initialize correctly
        assertEquals(0.0, pr.getSubtotal(), 0.01);
        assertEquals(0.0, pr.getDiscount(), 0.01);
        assertEquals(0.0, pr.getTax(), 0.01);
        assertEquals(0.0, pr.getTotal(), 0.01);
    }

    @Test
    @DisplayName("Constructor with no discount should work")
    void constructor_WithNoDiscount_ShouldWork() {
        // Given: PaymentResult with no discount
        double subtotal = 100.0;
        double discount = 0.0;
        double tax = subtotal * 0.06; // 6.0
        double total = subtotal + tax; // 106.0
        PaymentResult pr = new PaymentResult(subtotal, discount, tax, total);

        // Then: Should have correct values
        assertEquals(100.0, pr.getSubtotal(), 0.01);
        assertEquals(0.0, pr.getDiscount(), 0.01);
        assertEquals(6.0, pr.getTax(), 0.01);
        assertEquals(106.0, pr.getTotal(), 0.01);
    }

    @Test
    @DisplayName("Constructor with discount should work")
    void constructor_WithDiscount_ShouldWork() {
        // Given: PaymentResult with discount
        double subtotal = 200.0;
        double discount = 20.0; // 10%
        double amountAfterDiscount = subtotal - discount; // 180.0
        double tax = amountAfterDiscount * 0.06; // 10.8
        double total = amountAfterDiscount + tax; // 190.8
        PaymentResult pr = new PaymentResult(subtotal, discount, tax, total);

        // Then: Should have correct values
        assertEquals(200.0, pr.getSubtotal(), 0.01);
        assertEquals(20.0, pr.getDiscount(), 0.01);
        assertEquals(10.8, pr.getTax(), 0.01);
        assertEquals(190.8, pr.getTotal(), 0.01);
    }

    // ================== GETTER TESTS ==================

    @Test
    @DisplayName("getSubtotal should return subtotal")
    void getSubtotal_ShouldReturnSubtotal() {
        assertEquals(200.0, paymentResult.getSubtotal(), 0.01);
    }

    @Test
    @DisplayName("getDiscount should return discount")
    void getDiscount_ShouldReturnDiscount() {
        assertEquals(20.0, paymentResult.getDiscount(), 0.01);
    }

    @Test
    @DisplayName("getTax should return tax")
    void getTax_ShouldReturnTax() {
        assertEquals(10.8, paymentResult.getTax(), 0.01);
    }

    @Test
    @DisplayName("getTotal should return total")
    void getTotal_ShouldReturnTotal() {
        assertEquals(190.8, paymentResult.getTotal(), 0.01);
    }

    // ================== IMMUTABILITY TESTS ==================

    @Test
    @DisplayName("PaymentResult should be immutable (fields are final)")
    void paymentResult_ShouldBeImmutable() {
        // Given: PaymentResult object
        // Note: We can't directly test final fields, but we can verify
        // that getters return consistent values
        double initialSubtotal = paymentResult.getSubtotal();
        double initialDiscount = paymentResult.getDiscount();
        double initialTax = paymentResult.getTax();
        double initialTotal = paymentResult.getTotal();

        // When: Multiple calls to getters
        // Then: Values should remain the same
        assertEquals(initialSubtotal, paymentResult.getSubtotal(), 0.01);
        assertEquals(initialDiscount, paymentResult.getDiscount(), 0.01);
        assertEquals(initialTax, paymentResult.getTax(), 0.01);
        assertEquals(initialTotal, paymentResult.getTotal(), 0.01);
    }

    // ================== EDGE CASE TESTS ==================

    @Test
    @DisplayName("PaymentResult with very small values should work")
    void paymentResult_WithVerySmallValues_ShouldWork() {
        PaymentResult pr = new PaymentResult(0.01, 0.001, 0.00054, 0.00954);

        assertEquals(0.01, pr.getSubtotal(), 0.0001);
        assertEquals(0.001, pr.getDiscount(), 0.0001);
        assertEquals(0.00054, pr.getTax(), 0.0001);
        assertEquals(0.00954, pr.getTotal(), 0.0001);
    }

    @Test
    @DisplayName("PaymentResult with large values should work")
    void paymentResult_WithLargeValues_ShouldWork() {
        PaymentResult pr = new PaymentResult(1000000.0, 100000.0, 54000.0, 954000.0);

        assertEquals(1000000.0, pr.getSubtotal(), 0.01);
        assertEquals(100000.0, pr.getDiscount(), 0.01);
        assertEquals(54000.0, pr.getTax(), 0.01);
        assertEquals(954000.0, pr.getTotal(), 0.01);
    }

    @Test
    @DisplayName("PaymentResult with negative discount should work")
    void paymentResult_WithNegativeDiscount_ShouldWork() {
        // Note: In real scenarios, negative discount might represent surcharge
        PaymentResult pr = new PaymentResult(100.0, -10.0, 6.6, 116.6);

        assertEquals(100.0, pr.getSubtotal(), 0.01);
        assertEquals(-10.0, pr.getDiscount(), 0.01);
        assertEquals(6.6, pr.getTax(), 0.01);
        assertEquals(116.6, pr.getTotal(), 0.01);
    }

    @Test
    @DisplayName("PaymentResult with 100% discount should work")
    void paymentResult_With100PercentDiscount_ShouldWork() {
        double subtotal = 100.0;
        double discount = 100.0; // 100% discount
        double amountAfterDiscount = subtotal - discount; // 0.0
        double tax = amountAfterDiscount * 0.06; // 0.0
        double total = amountAfterDiscount + tax; // 0.0
        PaymentResult pr = new PaymentResult(subtotal, discount, tax, total);

        assertEquals(100.0, pr.getSubtotal(), 0.01);
        assertEquals(100.0, pr.getDiscount(), 0.01);
        assertEquals(0.0, pr.getTax(), 0.01);
        assertEquals(0.0, pr.getTotal(), 0.01);
    }

    // ================== CALCULATION VERIFICATION TESTS ==================

    @Test
    @DisplayName("PaymentResult should represent correct payment calculation")
    void paymentResult_ShouldRepresentCorrectPaymentCalculation() {
        // Given: Standard payment calculation
        double subtotal = 150.0;
        double discountRate = 0.10; // 10%
        double discount = subtotal * discountRate; // 15.0
        double amountAfterDiscount = subtotal - discount; // 135.0
        double taxRate = 0.06; // 6%
        double tax = amountAfterDiscount * taxRate; // 8.1
        double total = amountAfterDiscount + tax; // 143.1

        PaymentResult pr = new PaymentResult(subtotal, discount, tax, total);

        // Then: Should match calculation
        assertEquals(150.0, pr.getSubtotal(), 0.01);
        assertEquals(15.0, pr.getDiscount(), 0.01);
        assertEquals(8.1, pr.getTax(), 0.01);
        assertEquals(143.1, pr.getTotal(), 0.01);
    }

    @Test
    @DisplayName("PaymentResult with member discount should work")
    void paymentResult_WithMemberDiscount_ShouldWork() {
        // Given: Premium member with 15% discount
        double subtotal = 200.0;
        double discount = subtotal * 0.15; // 30.0 (15%)
        double amountAfterDiscount = subtotal - discount; // 170.0
        double tax = amountAfterDiscount * 0.06; // 10.2
        double total = amountAfterDiscount + tax; // 180.2

        PaymentResult pr = new PaymentResult(subtotal, discount, tax, total);

        assertEquals(200.0, pr.getSubtotal(), 0.01);
        assertEquals(30.0, pr.getDiscount(), 0.01);
        assertEquals(10.2, pr.getTax(), 0.01);
        assertEquals(180.2, pr.getTotal(), 0.01);
    }

    // ================== INTEGRATION TESTS ==================

    @Test
    @DisplayName("Should handle complete payment calculation flow")
    void shouldHandleCompletePaymentCalculationFlow() {
        // Simulate complete payment calculation
        double subtotal = 500.0;
        double discountRate = 0.10; // 10% member discount
        double discount = subtotal * discountRate;
        double amountAfterDiscount = subtotal - discount;
        double taxRate = 0.06; // 6% tax
        double tax = amountAfterDiscount * taxRate;
        double total = amountAfterDiscount + tax;

        PaymentResult pr = new PaymentResult(subtotal, discount, tax, total);

        // Verify all calculations
        assertEquals(500.0, pr.getSubtotal(), 0.01);
        assertEquals(50.0, pr.getDiscount(), 0.01);
        assertEquals(27.0, pr.getTax(), 0.01); // 450 * 0.06 = 27.0
        assertEquals(477.0, pr.getTotal(), 0.01); // 450 + 27 = 477.0
    }
}
