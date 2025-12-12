package assignment.view;

import assignment.model.Order;
import assignment.model.Stock;
import assignment.view.OrderView;
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
 * Unit tests for OrderView.
 * Tests view output formatting and display.
 */
@DisplayName("OrderView Tests")
class OrderViewTest {

    private static final Logger LOGGER = Logger.getLogger(OrderViewTest.class.getName());
    private OrderView orderView;
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
        orderView = new OrderView();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should display available items with correct format")
    void testDisplayAvailableItems() {
        List<Stock> stockList = new ArrayList<>();
        stockList.add(new Stock(1001, "Product A", 10, 50.0));
        stockList.add(new Stock(1002, "Product B", 5, 100.0));

        orderView.displayAvailableItems(stockList);

        String output = outputStream.toString();
        assertTrue(output.contains("PRODUCT ID"));
        assertTrue(output.contains("PRODUCT NAME"));
        assertTrue(output.contains("QUANTITY"));
        assertTrue(output.contains("PRICE"));
        assertTrue(output.contains("1001"));
        assertTrue(output.contains("PRODUCT A"));
        assertTrue(output.contains("50.00"));
        LOGGER.info("DisplayAvailableItems test passed.");
    }

    @Test
    @DisplayName("Should display cart items with correct format")
    void testDisplayCartItems() {
        List<Order> cart = new ArrayList<>();
        cart.add(new Order(1, 1001, "Product A", 2, 50.0));
        cart.add(new Order(2, 1002, "Product B", 1, 100.0));

        orderView.displayCartItems(cart);

        String output = outputStream.toString();
        assertTrue(output.contains("ORDER NO"));
        assertTrue(output.contains("PRODUCT NAME"));
        assertTrue(output.contains("QUANTITY"));
        assertTrue(output.contains("1"));
        assertTrue(output.contains("2"));
        assertTrue(output.contains("PRODUCT A"));
        assertTrue(output.contains("PRODUCT B"));
        LOGGER.info("DisplayCartItems test passed.");
    }

    @Test
    @DisplayName("Should display order detail correctly")
    void testDisplayOrderDetail() {
        Order order = new Order(1, 1001, "Product A", 2, 50.0);
        orderView.displayOrderDetail(order);

        String output = outputStream.toString();
        assertTrue(output.contains("ORDER NO >> 1"));
        assertTrue(output.contains("PRODUCT NAME"));
        assertTrue(output.contains("QUANTITY"));
        assertTrue(output.contains("RM"));
        LOGGER.info("DisplayOrderDetail test passed.");
    }

    @Test
    @DisplayName("Should display product details correctly")
    void testPrintProductDetails() {
        Stock stock = new Stock(1001, "Product A", 10, 50.0);
        orderView.printProductDetails(stock);

        String output = outputStream.toString();
        assertTrue(output.contains("PRODUCT NAME"));
        assertTrue(output.contains("PRODUCT PRICE"));
        assertTrue(output.contains("AVAILABLE QUANTITY"));
        assertTrue(output.contains("PRODUCT A"));
        assertTrue(output.contains("50.00"));
        LOGGER.info("PrintProductDetails test passed.");
    }

    @Test
    @DisplayName("Should display cart summary correctly")
    void testPrintCartSummary() {
        Stock stock = new Stock(1001, "Product A", 10, 50.0);
        orderView.printCartSummary(stock, 2);

        String output = outputStream.toString();
        assertTrue(output.contains("TOTAL COST"));
        assertTrue(output.contains("100.00"));
        LOGGER.info("PrintCartSummary test passed.");
    }
}

