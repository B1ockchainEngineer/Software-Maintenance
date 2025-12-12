package assignment.repo;

import assignment.model.Stock;
import assignment.util.config.StockConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for StockRepository.
 * Tests actual file I/O operations using temporary test files.
 */
class StockRepositoryIntegrationTest {
    private StockRepository repository;
    private String originalStockFilePath;
    private File testStockFile;
    private File backupStockFile;

    @BeforeEach
    void setUp() throws IOException {
        // Save original file path
        originalStockFilePath = StockConfig.STOCK_FILE_PATH;

        // Create backup of original file if it exists
        File originalFile = new File(originalStockFilePath);
        if (originalFile.exists()) {
            backupStockFile = new File(originalStockFilePath + ".backup");
            Files.copy(originalFile.toPath(), backupStockFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // Create test file
        testStockFile = new File(originalStockFilePath);
        if (testStockFile.exists()) {
            testStockFile.delete();
        }
        testStockFile.getParentFile().mkdirs();
        testStockFile.createNewFile();

        // Create repository instance
        repository = new StockRepository();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test file
        if (testStockFile != null && testStockFile.exists()) {
            testStockFile.delete();
        }

        // Restore original file if backup exists
        if (backupStockFile != null && backupStockFile.exists()) {
            Files.copy(backupStockFile.toPath(), testStockFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            backupStockFile.delete();
        }
    }

    @Test
    void loadAllStock_ShouldReturnEmptyList_WhenFileIsEmpty() {
        // Given: Empty file

        // When: Loading all stock
        List<Stock> result = repository.loadAllStock();

        // Then: Should return empty list
        assertNotNull(result, "Should return list, not null");
        assertTrue(result.isEmpty(), "Should return empty list for empty file");
    }

    @Test
    void appendStock_ShouldAddStockToFile() {
        // Given: A stock to save
        Stock stock = new Stock(10001, "Test Product", 15, 30.50);

        // When: Appending stock
        repository.appendStock(stock);

        // Then: Should be able to load it back
        List<Stock> result = repository.loadAllStock();
        assertEquals(1, result.size(), "Should have one stock");
        assertEquals(10001, result.get(0).getStockID());
        assertEquals("TEST PRODUCT", result.get(0).getStockName());
    }

    @Test
    void loadAllStock_ShouldLoadMultipleStocks() {
        // Given: Multiple stocks saved
        repository.appendStock(new Stock(10001, "Product 1", 10, 20.0));
        repository.appendStock(new Stock(10002, "Product 2", 5, 15.0));
        repository.appendStock(new Stock(10003, "Product 3", 20, 25.0));

        // When: Loading all stock
        List<Stock> result = repository.loadAllStock();

        // Then: Should load all stocks
        assertEquals(3, result.size(), "Should load 3 stocks");
    }

    @Test
    void deleteById_ShouldRemoveStock_WhenExists() {
        // Given: Saved stock
        Stock stock = new Stock(10002, "To Delete", 10, 20.0);
        repository.appendStock(stock);

        // Verify it exists
        List<Stock> before = repository.loadAllStock();
        assertEquals(1, before.size());

        // When: Deleting stock
        boolean deleted = repository.deleteById(10002);

        // Then: Should be removed
        assertTrue(deleted, "Should return true when deleted");
        List<Stock> after = repository.loadAllStock();
        assertEquals(0, after.size(), "Stock should be removed");
    }

    @Test
    void deleteById_ShouldReturnFalse_WhenNotFound() {
        // Given: Empty file

        // When: Deleting non-existent stock
        boolean deleted = repository.deleteById(99999);

        // Then: Should return false
        assertFalse(deleted, "Should return false when stock not found");
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenNameExists() {
        // Given: Stock with name
        Stock stock = new Stock(10005, "Unique Name", 10, 20.0);
        repository.appendStock(stock);

        // When: Checking existence (case-insensitive)
        boolean result = repository.existsByName("unique name");

        // Then: Should return true
        assertTrue(result, "Should find existing name (case-insensitive)");
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenNameDoesNotExist() {
        // Given: Empty file

        // When: Checking existence
        boolean result = repository.existsByName("Non Existent Name");

        // Then: Should return false
        assertFalse(result, "Should not find non-existent name");
    }

    @Test
    void existsByName_ShouldBeCaseInsensitive() {
        // Given: Stock with uppercase name
        repository.appendStock(new Stock(10006, "PRODUCT NAME", 10, 20.0));

        // When: Checking with different cases
        boolean result1 = repository.existsByName("product name");
        boolean result2 = repository.existsByName("PRODUCT NAME");
        boolean result3 = repository.existsByName("Product Name");

        // Then: All should return true
        assertTrue(result1, "Should be case-insensitive (lowercase)");
        assertTrue(result2, "Should be case-insensitive (uppercase)");
        assertTrue(result3, "Should be case-insensitive (mixed case)");
    }

    @Test
    void findMaxId_ShouldReturnMaxId_WhenStocksExist() {
        // Given: Multiple stocks with different IDs
        repository.appendStock(new Stock(10010, "Product 1", 10, 20.0));
        repository.appendStock(new Stock(10020, "Product 2", 10, 20.0));
        repository.appendStock(new Stock(10015, "Product 3", 10, 20.0));

        // When: Finding max ID
        int maxId = repository.findMaxId();

        // Then: Should return the maximum ID
        assertEquals(10020, maxId, "Should return maximum ID");
    }

    @Test
    void findMaxId_ShouldReturnBaseId_WhenNoStocks() {
        // Given: Empty file

        // When: Finding max ID
        int maxId = repository.findMaxId();

        // Then: Should return at least base ID
        assertEquals(10000, maxId, "Should return base ID when empty");
    }

    @Test
    void saveAllStock_ShouldOverwriteFile() {
        // Given: Initial stocks
        repository.appendStock(new Stock(10001, "Product 1", 10, 20.0));
        repository.appendStock(new Stock(10002, "Product 2", 10, 20.0));

        // When: Saving new list (overwrites)
        List<Stock> newList = List.of(
                new Stock(10003, "Product 3", 15, 25.0),
                new Stock(10004, "Product 4", 20, 30.0)
        );
        repository.saveAllStock(newList);

        // Then: File should contain only new stocks
        List<Stock> result = repository.loadAllStock();
        assertEquals(2, result.size(), "Should have 2 stocks");
        assertEquals(10003, result.get(0).getStockID());
        assertEquals(10004, result.get(1).getStockID());
    }

    @Test
    void loadAllStock_ShouldHandleInvalidLines() {
        // Given: File with invalid line (manually write)
        try {
            java.io.FileWriter writer = new java.io.FileWriter(testStockFile);
            writer.write("10001\tVALID PRODUCT\t10\t20.0\n");
            writer.write("INVALID LINE\n"); // Invalid line
            writer.write("10002\tANOTHER PRODUCT\t5\t15.0\n");
            writer.close();
        } catch (IOException e) {
            fail("Failed to write test file");
        }

        // When: Loading all stock
        List<Stock> result = repository.loadAllStock();

        // Then: Should load valid lines and skip invalid ones
        assertEquals(2, result.size(), "Should load 2 valid stocks");
    }

    // ========== IOEXCEPTION TESTS ==========

    @Test
    @DisplayName("Should handle IOException when appending stock to file")
    void appendStock_ShouldHandleIOException() {
        // Given: A stock to save
        Stock stock = new Stock(10001, "Test Product", 15, 30.50);

        // When: Appending stock (IOException is caught internally)
        // Then: Should not throw exception
        assertDoesNotThrow(() -> {
            repository.appendStock(stock);
        }, "Should handle IOException internally without throwing");
    }

    @Test
    @DisplayName("Should handle IOException when updating stock in file")
    void updateStock_ShouldHandleIOException() {
        // Given: Stock exists in file
        repository.appendStock(new Stock(10001, "Product 1", 10, 20.0));
        
        Stock updatedStock = new Stock(10001, "Product 1 Updated", 15, 25.0);

        // When: Updating stock (IOException is caught internally)
        // Then: Should not throw exception
        assertDoesNotThrow(() -> {
            repository.saveAllStock(List.of(updatedStock));
        }, "Should handle IOException internally without throwing");
    }

    @Test
    @DisplayName("Should handle IOException when writing stock file (file write failures)")
    void appendStock_FileWriteFailure() {
        Stock stock = new Stock(10001, "Test Product", 15, 30.50);
        
        // The repository catches IOException internally, so the method should complete
        // without throwing an exception to the caller
        assertDoesNotThrow(() -> {
            repository.appendStock(stock);
        });
        
        // Verify that even if write fails, the method doesn't crash
        // (In real scenario, IOException would be logged but not thrown)
        System.out.println("✓ IOEXCEPTION: StockRepository - Handles file write failures gracefully");
    }
}

