package test.assignment.service;

import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.repo.TransactionRepository;
import assignment.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TransactionService.
 * Tests transaction persistence and retrieval.
 */
@DisplayName("TransactionService Tests")
class TransactionServiceTest {
    private static final Logger LOGGER = Logger.getLogger(TransactionServiceTest.class.getName());

    private TransactionRepository transactionRepo;
    private TransactionService transactionService;
    private List<Transaction> savedTransactions;

    @BeforeEach
    void setUp() {
        // Setup mock TransactionRepository
        savedTransactions = new ArrayList<>();
        transactionRepo = new TransactionRepository() {
            @Override
            public void appendTransaction(double subtotal, double discount, double tax, 
                                        double total, List<Stock> items) {
                // Mock: Store transaction in memory
                List<Stock> itemsCopy = new ArrayList<>(items);
                Transaction transaction = new Transaction(subtotal, discount, tax, total, itemsCopy);
                savedTransactions.add(transaction);
            }

            @Override
            public List<Transaction> loadAllTransactions() {
                return new ArrayList<>(savedTransactions);
            }
        };

        transactionService = new TransactionService(transactionRepo);
    }

    @Test
    @DisplayName("Should return empty list when no transactions exist")
    void testGetAllTransactions_Empty() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
        LOGGER.info("✓ SUCCESS: TransactionService - Returned empty list when no transactions exist");
    }

    @Test
    @DisplayName("Should retrieve all saved transactions")
    void testGetAllTransactions() {
        // Create and save transactions
        List<Stock> items1 = new ArrayList<>();
        items1.add(new Stock(1, 1001, "Product A", 2, 50.0));
        Transaction transaction1 = new Transaction(100.0, 10.0, 5.4, 95.4, items1);
        transactionService.saveTransaction(transaction1);

        List<Stock> items2 = new ArrayList<>();
        items2.add(new Stock(1, 1002, "Product B", 1, 100.0));
        Transaction transaction2 = new Transaction(100.0, 0.0, 6.0, 106.0, items2);
        transactionService.saveTransaction(transaction2);

        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(2, transactions.size());
        LOGGER.info("✓ SUCCESS: TransactionService - Retrieved all saved transactions (2 transactions found)");
    }

    @Test
    @DisplayName("Should save transaction correctly")
    void testSaveTransaction() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1, 1001, "Product A", 2, 50.0));
        items.add(new Stock(2, 1002, "Product B", 1, 100.0));

        Transaction transaction = new Transaction(200.0, 20.0, 10.8, 190.8, items);
        transactionService.saveTransaction(transaction);

        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(1, transactions.size());

        Transaction saved = transactions.get(0);
        assertEquals(200.0, saved.getSubtotal(), 0.01);
        assertEquals(20.0, saved.getDiscount(), 0.01);
        assertEquals(10.8, saved.getTax(), 0.01);
        assertEquals(190.8, saved.getTotal(), 0.01);
        assertEquals(2, saved.getItems().size());
        LOGGER.info("✓ SUCCESS: TransactionService - Saved transaction correctly (Subtotal: RM200.00, Discount: RM20.00, Tax: RM10.80, Total: RM190.80, Items: 2)");
    }

    @Test
    @DisplayName("Should save multiple transactions")
    void testSaveTransaction_Multiple() {
        List<Stock> items1 = new ArrayList<>();
        items1.add(new Stock(1, 1001, "Product A", 2, 50.0));
        Transaction transaction1 = new Transaction(100.0, 10.0, 5.4, 95.4, items1);
        transactionService.saveTransaction(transaction1);

        List<Stock> items2 = new ArrayList<>();
        items2.add(new Stock(1, 1002, "Product B", 1, 100.0));
        Transaction transaction2 = new Transaction(100.0, 0.0, 6.0, 106.0, items2);
        transactionService.saveTransaction(transaction2);

        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(2, transactions.size());
        LOGGER.info("✓ SUCCESS: TransactionService - Saved multiple transactions (2 transactions saved)");
    }

    @Test
    @DisplayName("Should preserve transaction items when saving")
    void testSaveTransaction_PreserveItems() {
        List<Stock> items = new ArrayList<>();
        Stock item1 = new Stock(1, 1001, "Product A", 2, 50.0);
        Stock item2 = new Stock(2, 1002, "Product B", 1, 100.0);
        items.add(item1);
        items.add(item2);

        Transaction transaction = new Transaction(200.0, 20.0, 10.8, 190.8, items);
        transactionService.saveTransaction(transaction);

        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction saved = transactions.get(0);
        List<Stock> savedItems = saved.getItems();

        assertEquals(2, savedItems.size());
        assertEquals(1001, savedItems.get(0).getStockID());
        assertEquals(1002, savedItems.get(1).getStockID());
        LOGGER.info("✓ SUCCESS: TransactionService - Preserved transaction items when saving (Items: Product 1001, Product 1002)");
    }

    @Test
    @DisplayName("Should handle transaction with no discount")
    void testSaveTransaction_NoDiscount() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1, 1001, "Product A", 2, 50.0));

        Transaction transaction = new Transaction(100.0, 0.0, 6.0, 106.0, items);
        transactionService.saveTransaction(transaction);

        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction saved = transactions.get(0);
        assertEquals(0.0, saved.getDiscount(), 0.01);
        LOGGER.info("✓ SUCCESS: TransactionService - Handled transaction with no discount (Discount: RM0.00)");
    }

    @Test
    @DisplayName("Should handle transaction with empty items list")
    void testSaveTransaction_EmptyItems() {
        List<Stock> items = new ArrayList<>();
        Transaction transaction = new Transaction(0.0, 0.0, 0.0, 0.0, items);
        transactionService.saveTransaction(transaction);

        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(1, transactions.size());
        assertTrue(transactions.get(0).getItems().isEmpty());
        LOGGER.info("✓ SUCCESS: TransactionService - Handled transaction with empty items list");
    }
}

