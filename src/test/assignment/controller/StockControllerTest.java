package assignment.controller;

import assignment.model.Stock;
import assignment.repo.MockStockRepository;
import assignment.repo.StockRepository;
import assignment.service.StockService;
import assignment.view.StockView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Minimal controller tests focused on the view flow (no console input required).
 * Injects a test StockView via reflection to observe interactions.
 */
class StockControllerTest {

    private StockRepository stockRepository;
    private StockService stockService;
    private StockController stockController;
    private TestStockView testStockView;

    @BeforeEach
    void setUp() throws Exception {
        stockRepository = new MockStockRepository();
        stockService = new StockService(stockRepository);
        stockController = new StockController(stockService);
        testStockView = new TestStockView();

        // Inject the test view into the controller (private final field)
        Field viewField = StockController.class.getDeclaredField("stockView");
        viewField.setAccessible(true);
        viewField.set(stockController, testStockView);
    }

    @Test
    void view_ShouldDisplayAvailableStock_FromService() {
        // Given: repository has stock items
        stockRepository.appendStock(new Stock(10001, "Product A", 10, 5.0));
        stockRepository.appendStock(new Stock(10002, "Product B", 20, 7.5));

        // When: invoking controller view
        stockController.view();

        // Then: the injected view should have been called with the current stock list
        assertTrue(testStockView.called, "displayAvailableStock should be invoked");
        assertEquals(2, testStockView.capturedStocks.size(), "Should pass all available stocks to view");
        assertEquals(10001, testStockView.capturedStocks.get(0).getStockID());
        assertEquals(10002, testStockView.capturedStocks.get(1).getStockID());
    }

    /**
     * Test double for StockView to capture the stock list passed by controller.
     */
    private static class TestStockView extends StockView {
        boolean called = false;
        List<Stock> capturedStocks;

        @Override
        public void displayAvailableStock(List<Stock> stockList) {
            called = true;
            capturedStocks = stockList;
            // Avoid printing during tests
        }
    }
}


