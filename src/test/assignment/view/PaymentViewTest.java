package assignment.view;

import assignment.model.Order;
import assignment.model.PaymentResult;
import assignment.view.PaymentView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PaymentView.
 * Tests view output formatting and display.
 */
@DisplayName("PaymentView Tests")
class PaymentViewTest {

    private static final Logger LOGGER = Logger.getLogger(PaymentViewTest.class.getName());
    private PaymentView paymentView;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeAll
    static void setUpLogger() {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
    }

    @BeforeEach
    void setUp() {
        paymentView = new PaymentView();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should display cart items with correct format")
    void testDisplayCartItems() {
        List<Order> cart = new ArrayList<>();
        cart.add(new Order(1, 1001, "Product A", 2, 50.0));
        cart.add(new Order(2, 1002, "Product B", 1, 100.0));

        paymentView.displayCartItems(cart);

        String output = outputStream.toString();
        assertTrue(output.contains("ORDER NO"));
        assertTrue(output.contains("PRODUCT NAME"));
        assertTrue(output.contains("QUANTITY"));
        assertTrue(output.contains("1"));
        assertTrue(output.contains("2"));
        assertTrue(output.contains("PRODUCT A"));
        assertTrue(output.contains("PRODUCT B"));
        assertTrue(output.contains("RM"));
        LOGGER.info("DisplayCartItems test passed.");
    }

    @Test
    @DisplayName("Should display payment summary with correct format")
    void testPrintPaymentSummary() {
        PaymentResult result = new PaymentResult(200.0, 20.0, 10.8, 190.8);
        paymentView.printPaymentSummary(result);

        String output = outputStream.toString();
        assertTrue(output.contains("SUBTOTAL"));
        assertTrue(output.contains("DISCOUNT"));
        assertTrue(output.contains("TAX"));
        assertTrue(output.contains("TOTAL"));
        assertTrue(output.contains("200.00"));
        assertTrue(output.contains("20.00"));
        assertTrue(output.contains("10.80"));
        assertTrue(output.contains("190.80"));
        LOGGER.info("PrintPaymentSummary test passed.");
    }

    @Test
    @DisplayName("Should display payment summary with no discount")
    void testPrintPaymentSummary_NoDiscount() {
        PaymentResult result = new PaymentResult(100.0, 0.0, 6.0, 106.0);
        paymentView.printPaymentSummary(result);

        String output = outputStream.toString();
        assertTrue(output.contains("100.00"));
        assertTrue(output.contains("0.00"));
        assertTrue(output.contains("6.00"));
        assertTrue(output.contains("106.00"));
        LOGGER.info("PrintPaymentSummary with no discount test passed.");
    }

    @Test
    @DisplayName("Should display payment success message")
    void testPrintPaymentSuccess() {
        paymentView.printPaymentSuccess();
        String output = outputStream.toString();
        assertFalse(output.trim().isEmpty());
        LOGGER.info("PrintPaymentSuccess test passed.");
    }

    @Test
    @DisplayName("Should display payment failure message")
    void testPrintPaymentFailure() {
        paymentView.printPaymentFailure();
        String output = outputStream.toString();
        assertFalse(output.trim().isEmpty());
        LOGGER.info("PrintPaymentFailure test passed.");
    }

    @Test
    @DisplayName("Should display member found message")
    void testPrintMemberFoundMessage() {
        paymentView.printMemberFoundMessage("John Doe", "Gold", 0.10);
        String output = outputStream.toString();
        assertTrue(output.contains("John Doe"));
        assertTrue(output.contains("Gold"));
        assertTrue(output.contains("10.0%"));
        LOGGER.info("PrintMemberFoundMessage test passed.");
    }

    @Test
    @DisplayName("Should display empty cart message")
    void testPrintEmptyCartMessage() {
        paymentView.printEmptyCartMessage("Cart is empty");
        String output = outputStream.toString();
        assertTrue(output.contains("Cart is empty"));
        LOGGER.info("PrintEmptyCartMessage test passed.");
    }
}

