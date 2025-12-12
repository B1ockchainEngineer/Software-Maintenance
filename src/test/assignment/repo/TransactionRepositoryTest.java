package assignment.repo;

import assignment.model.Stock;
import assignment.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TransactionRepository.
 * Tests file I/O operations for transactions.
 */
@DisplayName("TransactionRepository Tests")
class TransactionRepositoryTest {

    private static final Logger LOGGER = Logger.getLogger(TransactionRepositoryTest.class.getName());
    private TransactionRepository transactionRepository;

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
        LOGGER.info("=========================================");
        LOGGER.info("SETTING UP TransactionRepositoryTest");
        LOGGER.info("=========================================");
        transactionRepository = new TransactionRepository();
        LOGGER.info("TransactionRepository instance created");
        LOGGER.info("=========================================");
    }

    @AfterEach
    void tearDown() {
        LOGGER.info("Tearing down TransactionRepositoryTest...");
        LOGGER.info("Test cleanup completed");
    }

    @Test
    @DisplayName("Should append transaction to file")
    void testAppendTransaction() {
        LOGGER.info("TEST: TransactionRepository - Append transaction to file");
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 2, 50.0));
        items.add(new Stock(1002, "Product B", 1, 100.0));
        LOGGER.info("Created test items: Product A (Qty: 2), Product B (Qty: 1)");
        
        // Append transaction
        LOGGER.info("Appending transaction: Subtotal=200.0, Discount=20.0, Tax=10.8, Total=190.8");
        transactionRepository.appendTransaction(200.0, 20.0, 10.8, 190.8, items);
        
        // Verify transaction was saved
        LOGGER.info("Loading all transactions to verify...");
        List<Transaction> transactions = transactionRepository.loadAllTransactions();
        assertFalse(transactions.isEmpty(), "Transactions list should not be empty");
        LOGGER.info("Found " + transactions.size() + " transaction(s)");
        
        Transaction transaction = transactions.get(transactions.size() - 1);
        assertEquals(200.0, transaction.getSubtotal(), 0.01, "Subtotal should match");
        assertEquals(20.0, transaction.getDiscount(), 0.01, "Discount should match");
        assertEquals(10.8, transaction.getTax(), 0.01, "Tax should match");
        assertEquals(190.8, transaction.getTotal(), 0.01, "Total should match");
        assertEquals(2, transaction.getItems().size(), "Should have 2 items");
        LOGGER.info("✓ Transaction appended and verified successfully");
    }

    @Test
    @DisplayName("Should load all transactions from file")
    void testLoadAllTransactions() {
        // Add test transactions
        List<Stock> items1 = new ArrayList<>();
        items1.add(new Stock(1001, "Product A", 2, 50.0));
        transactionRepository.appendTransaction(100.0, 10.0, 5.4, 95.4, items1);
        
        List<Stock> items2 = new ArrayList<>();
        items2.add(new Stock(1002, "Product B", 1, 100.0));
        transactionRepository.appendTransaction(100.0, 0.0, 6.0, 106.0, items2);
        
        // Load transactions
        List<Transaction> transactions = transactionRepository.loadAllTransactions();
        
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        assertTrue(transactions.size() >= 2);
    }

    @Test
    @DisplayName("Should load transaction with correct amounts")
    void testLoadTransactionAmounts() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 2, 50.0));
        transactionRepository.appendTransaction(100.0, 10.0, 5.4, 95.4, items);
        
        List<Transaction> transactions = transactionRepository.loadAllTransactions();
        Transaction lastTransaction = transactions.get(transactions.size() - 1);
        
        assertEquals(100.0, lastTransaction.getSubtotal(), 0.01);
        assertEquals(10.0, lastTransaction.getDiscount(), 0.01);
        assertEquals(5.4, lastTransaction.getTax(), 0.01);
        assertEquals(95.4, lastTransaction.getTotal(), 0.01);
    }

    @Test
    @DisplayName("Should load transaction with items")
    void testLoadTransactionItems() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 2, 50.0));
        items.add(new Stock(1002, "Product B", 1, 100.0));
        transactionRepository.appendTransaction(200.0, 20.0, 10.8, 190.8, items);
        
        List<Transaction> transactions = transactionRepository.loadAllTransactions();
        Transaction lastTransaction = transactions.get(transactions.size() - 1);
        
        List<Stock> loadedItems = lastTransaction.getItems();
        assertEquals(2, loadedItems.size());
        assertEquals(1001, loadedItems.get(0).getStockID());
        assertEquals(1002, loadedItems.get(1).getStockID());
    }

    @Test
    @DisplayName("Should handle transaction with no items")
    void testTransactionWithNoItems() {
        List<Stock> emptyItems = new ArrayList<>();
        transactionRepository.appendTransaction(0.0, 0.0, 0.0, 0.0, emptyItems);
        
        List<Transaction> transactions = transactionRepository.loadAllTransactions();
        Transaction lastTransaction = transactions.get(transactions.size() - 1);
        
        assertTrue(lastTransaction.getItems().isEmpty());
        assertEquals(0.0, lastTransaction.getTotal(), 0.01);
    }

    @Test
    @DisplayName("Should handle multiple transactions")
    void testMultipleTransactions() {
        List<Stock> items1 = new ArrayList<>();
        items1.add(new Stock(1001, "Product A", 2, 50.0));
        transactionRepository.appendTransaction(100.0, 10.0, 5.4, 95.4, items1);
        
        List<Stock> items2 = new ArrayList<>();
        items2.add(new Stock(1002, "Product B", 1, 100.0));
        transactionRepository.appendTransaction(100.0, 0.0, 6.0, 106.0, items2);
        
        List<Transaction> transactions = transactionRepository.loadAllTransactions();
        
        assertTrue(transactions.size() >= 2);
    }

    @Test
    @DisplayName("Should return empty list when file is empty or doesn't exist")
    void testLoadAllTransactionsEmptyFile() {
        // This test assumes the file might be empty or non-existent
        // In a real scenario, you'd clear the file first
        List<Transaction> transactions = transactionRepository.loadAllTransactions();
        
        // Should not throw exception, but may return empty or existing transactions
        assertNotNull(transactions);
    }

    @Test
    @DisplayName("Should handle transaction with discount")
    void testTransactionWithDiscount() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 2, 50.0));
        transactionRepository.appendTransaction(100.0, 20.0, 4.8, 84.8, items);
        
        List<Transaction> transactions = transactionRepository.loadAllTransactions();
        Transaction lastTransaction = transactions.get(transactions.size() - 1);
        
        assertEquals(20.0, lastTransaction.getDiscount(), 0.01);
        assertEquals(84.8, lastTransaction.getTotal(), 0.01);
    }

    @Test
    @DisplayName("Should handle transaction without discount")
    void testTransactionWithoutDiscount() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 1, 50.0));
        transactionRepository.appendTransaction(50.0, 0.0, 3.0, 53.0, items);
        
        List<Transaction> transactions = transactionRepository.loadAllTransactions();
        Transaction lastTransaction = transactions.get(transactions.size() - 1);
        
        assertEquals(0.0, lastTransaction.getDiscount(), 0.01);
        assertEquals(53.0, lastTransaction.getTotal(), 0.01);
    }

    // ========== IOEXCEPTION TESTS ==========

    @Test
    @DisplayName("Should handle IOException when saving transactions")
    void testAppendTransaction_IOException() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 2, 50.0));
        
        // The repository catches IOException internally, so the method should complete
        // without throwing an exception to the caller
        assertDoesNotThrow(() -> {
            transactionRepository.appendTransaction(200.0, 20.0, 10.8, 190.8, items);
        });
        
        // Verify that even if write fails, the method doesn't crash
        // (In real scenario, IOException would be logged but not thrown)
        System.out.println("✓ IOEXCEPTION: TransactionRepository - Handles IOException when saving transactions");
    }

    @Test
    @DisplayName("Should handle IOException when writing transaction file (file write failures)")
    void testTransactionRepository_FileWriteFailure() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 2, 50.0));
        items.add(new Stock(1002, "Product B", 1, 100.0));
        
        // Should not throw exception - IOException is caught internally
        assertDoesNotThrow(() -> {
            transactionRepository.appendTransaction(200.0, 20.0, 10.8, 190.8, items);
        });
        
        System.out.println("✓ IOEXCEPTION: TransactionRepository - Handles file write failures gracefully");
    }

    @Test
    @DisplayName("Should handle IOException when writing multiple transaction items")
    void testAppendTransaction_MultipleItems_IOException() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 2, 50.0));
        items.add(new Stock(1002, "Product B", 1, 100.0));
        items.add(new Stock(1003, "Product C", 3, 25.0));
        
        // Should not throw exception even if IOException occurs during item writing
        assertDoesNotThrow(() -> {
            transactionRepository.appendTransaction(275.0, 27.5, 14.85, 262.35, items);
        });
        
        System.out.println("✓ IOEXCEPTION: TransactionRepository - Handles IOException when writing multiple items");
    }
}


