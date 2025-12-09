package assignment.service;

import assignment.model.Stock;
import assignment.repo.MockStockRepository;
import assignment.repo.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StockService.
 * Tests business logic using a mock repository to isolate from file I/O.
 */
class StockServiceTest {
    private StockRepository mockRepository;
    private StockService stockService;

    @BeforeEach
    void setUp() {
        mockRepository = new MockStockRepository();
        stockService = new StockService(mockRepository);
    }

    @Test
    void getAllStock_ShouldReturnAllStocks() {
        // Given: Multiple stocks in repository
        Stock stock1 = new Stock(10001, "Product 1", 10, 20.0);
        Stock stock2 = new Stock(10002, "Product 2", 5, 15.0);
        mockRepository.appendStock(stock1);
        mockRepository.appendStock(stock2);
        
        // When: Getting all stock
        var result = stockService.getAllStock();
        
        // Then: Should return all stocks
        assertEquals(2, result.size(), "Should return 2 stocks");
        assertTrue(result.contains(stock1));
        assertTrue(result.contains(stock2));
    }

    @Test
    void getAllStock_ShouldReturnEmptyList_WhenNoStocks() {
        // Given: Empty repository
        
        // When: Getting all stock
        var result = stockService.getAllStock();
        
        // Then: Should return empty list
        assertTrue(result.isEmpty(), "Should return empty list");
    }

    @Test
    void getAvailableStock_ShouldReturnSameAsGetAllStock() {
        // Given: Stock in repository
        Stock stock = new Stock(10001, "Test Product", 10, 20.0);
        mockRepository.appendStock(stock);
        
        // When: Getting available stock
        var result = stockService.getAvailableStock();
        
        // Then: Should return same as getAllStock
        assertEquals(stockService.getAllStock(), result, "getAvailableStock should return same as getAllStock");
    }

    @Test
    void getStockByID_ShouldReturnStock_WhenExists() {
        // Given: Stock with ID 10001
        Stock stock = new Stock(10001, "Test Product", 10, 20.0);
        mockRepository.appendStock(stock);
        
        // When: Getting stock by ID
        Stock result = stockService.getStockByID(10001);
        
        // Then: Should return the stock
        assertNotNull(result);
        assertEquals(10001, result.getStockID());
        assertEquals("TEST PRODUCT", result.getStockName());
    }

    @Test
    void getStockByID_ShouldReturnNull_WhenNotFound() {
        // Given: Empty repository
        
        // When: Getting stock by non-existent ID
        Stock result = stockService.getStockByID(99999);
        
        // Then: Should return null
        assertNull(result, "Should return null when stock not found");
    }

    @Test
    void getNextStockID_ShouldReturnIncrementedId() {
        // Given: Stocks with IDs 10001, 10002, 10005
        mockRepository.appendStock(new Stock(10001, "Product 1", 10, 20.0));
        mockRepository.appendStock(new Stock(10002, "Product 2", 10, 20.0));
        mockRepository.appendStock(new Stock(10005, "Product 3", 10, 20.0));
        
        // When: Getting next ID
        int nextId = stockService.getNextStockID();
        
        // Then: Should return max ID + 1
        assertEquals(10006, nextId, "Should return max ID + 1");
    }

    @Test
    void getNextStockID_ShouldReturnBaseIdPlusOne_WhenNoStocks() {
        // Given: Empty repository
        
        // When: Getting next ID
        int nextId = stockService.getNextStockID();
        
        // Then: Should return base ID + 1
        assertEquals(10001, nextId, "Should return base ID + 1 when empty");
    }

    @Test
    void isStockNameUnique_ShouldReturnTrue_WhenNameDoesNotExist() {
        // Given: No existing stocks
        
        // When: Checking uniqueness
        boolean result = stockService.isStockNameUnique("New Product");
        
        // Then: Should return true
        assertTrue(result, "Non-existent name should be unique");
    }

    @Test
    void isStockNameUnique_ShouldReturnFalse_WhenNameExists() {
        // Given: Existing stock
        mockRepository.appendStock(new Stock(10001, "EXISTING PRODUCT", 10, 20.0));
        
        // When: Checking uniqueness (case-insensitive)
        boolean result = stockService.isStockNameUnique("existing product");
        
        // Then: Should return false
        assertFalse(result, "Existing name should not be unique");
    }

    @Test
    void isStockNameUnique_ShouldBeCaseInsensitive() {
        // Given: Existing stock with uppercase name
        mockRepository.appendStock(new Stock(10001, "PRODUCT NAME", 10, 20.0));
        
        // When: Checking with different case
        boolean result1 = stockService.isStockNameUnique("product name");
        boolean result2 = stockService.isStockNameUnique("PRODUCT NAME");
        boolean result3 = stockService.isStockNameUnique("Product Name");
        
        // Then: All should return false (not unique)
        assertFalse(result1, "Should be case-insensitive (lowercase)");
        assertFalse(result2, "Should be case-insensitive (uppercase)");
        assertFalse(result3, "Should be case-insensitive (mixed case)");
    }

    @Test
    void addNewStock_ShouldSaveStock_WhenNameIsUnique() {
        // Given: Valid stock with unique name
        Stock newStock = new Stock(0, "New Product", 10, 25.99);
        
        // When: Adding stock
        boolean result = stockService.addNewStock(newStock);
        
        // Then: Should succeed and stock should have assigned ID
        assertTrue(result, "Should return true when added successfully");
        assertTrue(newStock.getStockID() > 0, "Stock should have assigned ID");
        
        // Verify stock was saved
        Stock saved = stockService.getStockByID(newStock.getStockID());
        assertNotNull(saved);
        assertEquals("NEW PRODUCT", saved.getStockName());
    }

    @Test
    void addNewStock_ShouldReturnFalse_WhenNameIsDuplicate() {
        // Given: Existing stock with same name
        mockRepository.appendStock(new Stock(10001, "EXISTING", 10, 20.0));
        Stock duplicate = new Stock(0, "existing", 10, 20.0);
        
        // When: Adding stock with duplicate name
        boolean result = stockService.addNewStock(duplicate);
        
        // Then: Should return false
        assertFalse(result, "Should return false when name is duplicate");
    }

    @Test
    void addNewStock_ShouldAssignNextAvailableID() {
        // Given: Existing stocks
        mockRepository.appendStock(new Stock(10001, "Product 1", 10, 20.0));
        mockRepository.appendStock(new Stock(10003, "Product 2", 10, 20.0));
        Stock newStock = new Stock(0, "New Product", 10, 25.99);
        
        // When: Adding new stock
        stockService.addNewStock(newStock);
        
        // Then: Should assign next ID (10004)
        assertEquals(10004, newStock.getStockID(), "Should assign next available ID");
    }

    @Test
    void deleteStock_ShouldRemoveStock_WhenExists() {
        // Given: Existing stock
        Stock stock = new Stock(10001, "To Delete", 10, 20.0);
        mockRepository.appendStock(stock);
        
        // Verify it exists
        assertNotNull(stockService.getStockByID(10001));
        
        // When: Deleting stock
        boolean result = stockService.deleteStock(10001);
        
        // Then: Should succeed and stock should be removed
        assertTrue(result, "Should return true when deleted successfully");
        assertNull(stockService.getStockByID(10001), "Stock should be removed");
    }

    @Test
    void deleteStock_ShouldReturnFalse_WhenNotFound() {
        // Given: Empty repository
        
        // When: Deleting non-existent stock
        boolean result = stockService.deleteStock(99999);
        
        // Then: Should return false
        assertFalse(result, "Should return false when stock not found");
    }

    @Test
    void updateStock_ShouldUpdateQuantity_WhenValid() {
        // Given: Existing stock
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        mockRepository.appendStock(stock);
        
        // When: Updating quantity
        boolean updated = stockService.updateStock(10001, "TEST PRODUCT", 30, 10.0);
        
        // Then: Should succeed and stock should be updated
        assertTrue(updated, "Should return true when updated successfully");
        Stock updatedStock = stockService.getStockByID(10001);
        assertNotNull(updatedStock);
        assertEquals(30, updatedStock.getQty(), "Quantity should be updated to 30");
        assertEquals(10.0, updatedStock.getPrice(), 0.01, "Price should remain unchanged");
    }

    @Test
    void updateStock_ShouldUpdatePrice_WhenValid() {
        // Given: Existing stock
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        mockRepository.appendStock(stock);
        
        // When: Updating price
        boolean updated = stockService.updateStock(10001, "TEST PRODUCT", 50, 15.5);
        
        // Then: Should succeed and stock should be updated
        assertTrue(updated, "Should return true when updated successfully");
        Stock updatedStock = stockService.getStockByID(10001);
        assertNotNull(updatedStock);
        assertEquals(15.5, updatedStock.getPrice(), 0.01, "Price should be updated to 15.5");
        assertEquals(50, updatedStock.getQty(), "Quantity should remain unchanged");
    }

    @Test
    void updateStock_ShouldUpdateName_WhenValid() {
        // Given: Existing stock
        Stock stock = new Stock(10001, "Old Name", 50, 10.0);
        mockRepository.appendStock(stock);
        
        // When: Updating name
        boolean updated = stockService.updateStock(10001, "NEW NAME", 50, 10.0);
        
        // Then: Should succeed and stock should be updated
        assertTrue(updated, "Should return true when updated successfully");
        Stock updatedStock = stockService.getStockByID(10001);
        assertNotNull(updatedStock);
        assertEquals("NEW NAME", updatedStock.getStockName(), "Name should be updated");
    }

    @Test
    void updateStock_ShouldReturnFalse_WhenDuplicateName() {
        // Given: Two stocks with different names
        Stock stock1 = new Stock(10001, "Product 1", 50, 10.0);
        Stock stock2 = new Stock(10002, "Product 2", 30, 15.0);
        mockRepository.appendStock(stock1);
        mockRepository.appendStock(stock2);
        
        // When: Trying to update stock1 with stock2's name
        boolean updated = stockService.updateStock(10001, "PRODUCT 2", 50, 10.0);
        
        // Then: Should fail due to duplicate name
        assertFalse(updated, "Should return false when name is duplicate");
    }

    @Test
    void updateStock_ShouldAllowSameName_WhenUpdatingSameStock() {
        // Given: Existing stock
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        mockRepository.appendStock(stock);
        
        // When: Updating with same name (but different quantity/price)
        boolean updated = stockService.updateStock(10001, "TEST PRODUCT", 30, 15.0);
        
        // Then: Should succeed (same name is allowed for the same stock)
        assertTrue(updated, "Should allow same name when updating same stock");
        Stock updatedStock = stockService.getStockByID(10001);
        assertEquals("TEST PRODUCT", updatedStock.getStockName());
        assertEquals(30, updatedStock.getQty());
        assertEquals(15.0, updatedStock.getPrice(), 0.01);
    }

    @Test
    void updateStock_ShouldReturnFalse_WhenStockNotFound() {
        // Given: Empty repository
        
        // When: Trying to update non-existent stock
        boolean updated = stockService.updateStock(99999, "New Name", 50, 10.0);
        
        // Then: Should return false
        assertFalse(updated, "Should return false when stock not found");
    }
}

