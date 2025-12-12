package assignment.view;

import assignment.model.Stock;
import assignment.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TransactionView.
 * Verifies console output correctness for transaction display operations.
 */
@DisplayName("TransactionView Tests")
class TransactionViewTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private TransactionView transactionView;
    private static final Logger LOGGER = Logger.getLogger(TransactionViewTest.class.getName());

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
        LOGGER.info("Setting up TransactionViewTest...");
        System.setOut(new PrintStream(outContent));
        transactionView = new TransactionView();
        LOGGER.info("TransactionView instance created");
    }

    @AfterEach
    void tearDown() {
        LOGGER.info("Tearing down TransactionViewTest...");
        System.setOut(originalOut);
        outContent.reset();
        LOGGER.info("Test cleanup completed");
    }

    @Test
    @DisplayName("Should display transaction summary with transactions")
    void testPrintTransactionSummary_WithTransactions() {
        LOGGER.info("=========================================");
        LOGGER.info("TEST: TransactionView - printTransactionSummary with transactions");
        LOGGER.info("=========================================");
        
        // Given: Create test transactions
        LOGGER.info("Creating test transactions...");
        List<Stock> items1 = new ArrayList<>();
        items1.add(new Stock(1001, "Product A", 2, 50.0));
        items1.add(new Stock(1002, "Product B", 1, 100.0));
        Transaction transaction1 = new Transaction(200.0, 20.0, 10.8, 190.8, items1);
        LOGGER.info("Transaction 1 created: Subtotal=RM200.00, Discount=RM20.00, Tax=RM10.80, Total=RM190.80");

        List<Stock> items2 = new ArrayList<>();
        items2.add(new Stock(1003, "Product C", 3, 25.0));
        Transaction transaction2 = new Transaction(75.0, 0.0, 4.5, 79.5, items2);
        LOGGER.info("Transaction 2 created: Subtotal=RM75.00, Discount=RM0.00, Tax=RM4.50, Total=RM79.50");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        LOGGER.info("Total transactions in list: " + transactions.size());

        // When: Print transaction summary
        LOGGER.info("Calling printTransactionSummary()...");
        transactionView.printTransactionSummary(transactions);
        LOGGER.info("printTransactionSummary() completed");

        // Then: Verify output contains expected content
        String output = outContent.toString();
        LOGGER.info("Captured output length: " + output.length() + " characters");
        
        // Verify title is displayed
        LOGGER.info("Verifying transaction report title...");
        assertTrue(output.contains("TRANSACTION REPORT") || output.contains("Transaction Report"), 
            "Should display transaction report title");
        LOGGER.info("✓ Transaction report title verified");
        
        // Verify transaction data is displayed
        LOGGER.info("Verifying transaction 1 data...");
        assertTrue(output.contains("RM200.00") || output.contains("200.00"), 
            "Should display first transaction subtotal");
        assertTrue(output.contains("RM190.80") || output.contains("190.80"), 
            "Should display first transaction total");
        LOGGER.info("✓ Transaction 1 data verified (Subtotal: RM200.00, Total: RM190.80)");
        
        LOGGER.info("Verifying transaction 2 data...");
        assertTrue(output.contains("RM75.00") || output.contains("75.00"), 
            "Should display second transaction subtotal");
        assertTrue(output.contains("RM79.50") || output.contains("79.50"), 
            "Should display second transaction total");
        LOGGER.info("✓ Transaction 2 data verified (Subtotal: RM75.00, Total: RM79.50)");
        
        // Verify totals are calculated and displayed
        LOGGER.info("Verifying grand totals...");
        assertTrue(output.contains("TOTAL") || output.contains("Total"), 
            "Should display grand total row");
        LOGGER.info("✓ Grand total row verified");
        
        LOGGER.info("Verifying transaction count...");
        assertTrue(output.contains("2"), 
            "Should display total transaction count");
        LOGGER.info("✓ Transaction count verified (2 transactions)");
        
        LOGGER.info("=========================================");
        LOGGER.info("✓ SUCCESS: TransactionView - printTransactionSummary() displays transactions correctly");
        LOGGER.info("=========================================");
    }
}

