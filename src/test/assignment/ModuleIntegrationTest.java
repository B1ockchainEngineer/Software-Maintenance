package assignment;

import assignment.model.*;
import assignment.repo.*;
import assignment.service.*;
import assignment.util.config.AppConfig;
import assignment.util.config.MemberConfig;
import assignment.util.config.StaffConfig;
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
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for module interactions.
 * 
 * Module pairs tested:
 * 1. Member and Sales (member discount in payment)
 * 2. Sales and Stock (adding items to cart from stock)
 * 3. Staff and Login (authentication)
 * 4. Sales and Transaction (payment creates transactions)
 * 5. Staff and Signup (new staff registration)
 */
@DisplayName("Module Integration Tests")
class ModuleIntegrationTest {
    
    private final Logger logger = Logger.getLogger(ModuleIntegrationTest.class.getName());
    
    // File paths
    private final String testDataDir = "data/test/";
    private String originalStaffFilePath;
    private String originalStockFilePath;
    private String originalOrderFilePath;
    private String originalTransactionFilePath;
    private String originalMemberFilePath;
    
    // Backup files
    private File backupStaffFile;
    private File backupStockFile;
    private File backupOrderFile;
    private File backupTransactionFile;
    private File backupMemberFile;
    
    // Test files
    private File testStaffFile;
    private File testStockFile;
    private File testOrderFile;
    private File testTransactionFile;
    private File testMemberFile;
    
    // Repositories
    private StaffRepository staffRepo;
    private StockRepository stockRepo;
    private OrderRepository orderRepo;
    private TransactionRepository transactionRepo;
    private MemberRepository memberRepo;
    
    // Services
    private StaffService staffService;
    private StockService stockService;
    private OrderService orderService;
    private TransactionService transactionService;
    private MemberService memberService;
    private PaymentService paymentService;
    
    @BeforeEach
    void setUp() throws IOException {
        logger.info("=========================================");
        logger.info("SETTING UP MODULE INTEGRATION TESTS");
        logger.info("=========================================");
        
        // Initialize file paths
        originalStaffFilePath = StaffConfig.STAFF_FILE_PATH;
        originalStockFilePath = StockConfig.STOCK_FILE_PATH;
        originalOrderFilePath = AppConfig.DATA_DIR + "order.txt";
        originalTransactionFilePath = AppConfig.DATA_DIR + "transaction.txt";
        originalMemberFilePath = AppConfig.DATA_DIR + "members.txt";
        
        // Create test data directory
        new File(testDataDir).mkdirs();
        
        // Backup original files
        backupStaffFile = backupFile(originalStaffFilePath);
        backupStockFile = backupFile(originalStockFilePath);
        backupOrderFile = backupFile(originalOrderFilePath);
        backupTransactionFile = backupFile(originalTransactionFilePath);
        backupMemberFile = backupFile(originalMemberFilePath);
        
        // Create empty test files
        testStaffFile = createTestFile(originalStaffFilePath);
        testStockFile = createTestFile(originalStockFilePath);
        testOrderFile = createTestFile(originalOrderFilePath);
        testTransactionFile = createTestFile(originalTransactionFilePath);
        testMemberFile = createTestFile(originalMemberFilePath);
        
        // Initialize repositories
        staffRepo = new StaffRepository();
        stockRepo = new StockRepository();
        orderRepo = new OrderRepository();
        transactionRepo = new TransactionRepository();
        memberRepo = new MemberRepository();
        
        // Initialize services
        staffService = new StaffService(staffRepo);
        stockService = new StockService(stockRepo);
        orderService = new OrderService(stockRepo, orderRepo);
        transactionService = new TransactionService(transactionRepo);
        memberService = new MemberService(memberRepo);
        paymentService = new PaymentService(stockRepo, orderRepo);
        
        logger.info("Test setup completed");
    }
    
    @AfterEach
    void tearDown() throws IOException {
        logger.info("=========================================");
        logger.info("TEARING DOWN MODULE INTEGRATION TESTS");
        logger.info("=========================================");
        
        // Clean up test files
        deleteFile(testStaffFile);
        deleteFile(testStockFile);
        deleteFile(testOrderFile);
        deleteFile(testTransactionFile);
        deleteFile(testMemberFile);
        
        // Restore original files
        restoreFile(backupStaffFile, originalStaffFilePath);
        restoreFile(backupStockFile, originalStockFilePath);
        restoreFile(backupOrderFile, originalOrderFilePath);
        restoreFile(backupTransactionFile, originalTransactionFilePath);
        restoreFile(backupMemberFile, originalMemberFilePath);
        
        logger.info("Test teardown completed");
    }
    
    // ================== TEST 1: MEMBER AND SALES ==================
    
    @Test
    @DisplayName("Module Integration: Member and Sales - Member discount applied in payment")
    void testMemberAndSalesIntegration() {
        logger.info("=========================================");
        logger.info("TEST 1: MEMBER AND SALES INTEGRATION");
        logger.info("=========================================");
        
        // Setup: Create a member
        logger.info("Creating member for discount test...");
        NormalMember member = new NormalMember("Test Member", "010203040506", 101, "0123456789", MemberConfig.MEMBER_TYPE_NORMAL);
        boolean memberAdded = memberService.addMember(member);
        assertTrue(memberAdded, "Member should be added");
        logger.info("Member added: ID=" + member.getId() + ", Name=" + member.getName() + ", Type=" + member.getMemberType());
        
        // Setup: Create stock items
        logger.info("Creating stock items...");
        Stock stock1 = new Stock(10001, "Product A", 10, 50.00);
        stockService.addNewStock(stock1);
        logger.info("Stock created: ID=" + stock1.getStockID() + ", Name=" + stock1.getStockName() + ", Price=" + stock1.getPrice());
        
        // Setup: Add item to cart
        logger.info("Adding item to cart...");
        boolean added = orderService.addToCart(10001, 2);
        assertTrue(added, "Item should be added to cart");
        logger.info("Item added to cart: Stock ID=10001, Quantity=2");
        
        // Test: Get member discount
        logger.info("Retrieving member discount (Member ID: 101)...");
        MemberService.DiscountResult discountResult = memberService.getDiscountRate("101");
        assertFalse(discountResult.hasError(), "Should have valid discount");
        double discountRate = discountResult.getDiscountRate();
        assertTrue(discountRate > 0, "Member should have discount");
        logger.info("Discount rate: " + String.format("%.2f%%", discountRate * 100));
        
        // Test: Calculate payment with member discount
        logger.info("Calculating payment with member discount...");
        double subtotal = paymentService.calculateSubtotal();
        PaymentResult paymentResult = paymentService.calculatePaymentSummary(discountRate);
        
        assertNotNull(paymentResult, "Payment result should not be null");
        double discount = subtotal * discountRate;
        assertEquals(discount, paymentResult.getDiscount(), 0.01, "Discount should be applied");
        logger.info("Payment summary:");
        logger.info("  - Subtotal: RM " + String.format("%.2f", paymentResult.getSubtotal()));
        logger.info("  - Discount: RM " + String.format("%.2f", paymentResult.getDiscount()));
        logger.info("  - Tax: RM " + String.format("%.2f", paymentResult.getTax()));
        logger.info("  - Total: RM " + String.format("%.2f", paymentResult.getTotal()));
        
        // Verify: Member discount is correctly applied
        assertTrue(paymentResult.getTotal() < paymentResult.getSubtotal() + paymentResult.getTax(), 
            "Total with discount should be less than subtotal + tax");
        logger.info("✓ Member and Sales integration test passed");
    }
    
    // ================== TEST 2: SALES AND STOCK ==================
    
    @Test
    @DisplayName("Module Integration: Sales and Stock - Adding items to cart reduces stock quantity")
    void testSalesAndStockIntegration() {
        logger.info("=========================================");
        logger.info("TEST 2: SALES AND STOCK INTEGRATION");
        logger.info("=========================================");
        
        // Setup: Create stock items
        logger.info("Creating stock items...");
        Stock stock1 = new Stock(10001, "Product A", 10, 25.50);
        Stock stock2 = new Stock(10002, "Product B", 5, 50.00);
        stockService.addNewStock(stock1);
        stockService.addNewStock(stock2);
        logger.info("Stock 1: ID=10001, Qty=10, Price=25.50");
        logger.info("Stock 2: ID=10002, Qty=5, Price=50.00");
        
        // Test: Add items to cart
        logger.info("Adding items to cart...");
        boolean added1 = orderService.addToCart(10001, 3);
        assertTrue(added1, "First item should be added to cart");
        logger.info("Added to cart: Stock ID=10001, Quantity=3");
        
        boolean added2 = orderService.addToCart(10002, 2);
        assertTrue(added2, "Second item should be added to cart");
        logger.info("Added to cart: Stock ID=10002, Quantity=2");
        
        // Verify: Stock quantities are reduced
        logger.info("Verifying stock quantities were reduced...");
        Stock updatedStock1 = stockService.getStockByID(10001);
        assertEquals(7, updatedStock1.getQty(), "Stock 1 quantity should be reduced by 3");
        logger.info("Stock 1 quantity: " + updatedStock1.getQty() + " (was 10, reduced by 3)");
        
        Stock updatedStock2 = stockService.getStockByID(10002);
        assertEquals(3, updatedStock2.getQty(), "Stock 2 quantity should be reduced by 2");
        logger.info("Stock 2 quantity: " + updatedStock2.getQty() + " (was 5, reduced by 2)");
        
        // Verify: Cart contains correct items
        logger.info("Verifying cart contents...");
        List<Order> cartItems = orderService.getCartItems();
        assertEquals(2, cartItems.size(), "Cart should have 2 items");
        logger.info("Cart contains " + cartItems.size() + " items");
        
        // Verify: Cannot add more than available stock
        logger.info("Testing: Cannot add more than available stock...");
        boolean cannotAdd = orderService.addToCart(10001, 10); // Only 7 left, trying to add 10
        assertFalse(cannotAdd, "Should not be able to add more than available stock");
        logger.info("Correctly prevented adding more than available stock");
        
        logger.info("✓ Sales and Stock integration test passed");
    }
    
    // ================== TEST 3: STAFF AND LOGIN ==================
    
    @Test
    @DisplayName("Module Integration: Staff and Login - Staff authentication")
    void testStaffAndLoginIntegration() {
        logger.info("=========================================");
        logger.info("TEST 3: STAFF AND LOGIN INTEGRATION");
        logger.info("=========================================");
        
        // Setup: Create staff
        logger.info("Creating staff member...");
        Staff staff = new Staff("Test Staff", "010203040506", 25, 3000.00, "password123");
        staff.setId(12345);
        boolean staffAdded = staffService.addStaff(staff);
        assertTrue(staffAdded, "Staff should be added");
        logger.info("Staff created: ID=" + staff.getId() + ", IC=" + staff.getIc() + ", Name=" + staff.getName());
        
        // Test: Find staff by IC (used in login)
        logger.info("Finding staff by IC (010203040506)...");
        Staff foundByIc = staffService.findByIc("010203040506");
        assertNotNull(foundByIc, "Staff should be found by IC");
        assertEquals(staff.getId(), foundByIc.getId());
        logger.info("Staff found: " + foundByIc.getName());
        
        // Test: Login with correct credentials
        logger.info("Testing login with correct credentials...");
        Staff loggedIn = staffService.login("010203040506", "password123");
        assertNotNull(loggedIn, "Login should succeed with correct credentials");
        assertEquals(staff.getId(), loggedIn.getId());
        logger.info("Login successful: " + loggedIn.getName());
        
        // Test: Login with incorrect password
        logger.info("Testing login with incorrect password...");
        Staff failedLogin = staffService.login("010203040506", "wrongpassword");
        assertNull(failedLogin, "Login should fail with incorrect password");
        logger.info("Login correctly failed with wrong password");
        
        // Test: Login with non-existent IC
        logger.info("Testing login with non-existent IC...");
        Staff notFound = staffService.login("999999999999", "password123");
        assertNull(notFound, "Login should fail with non-existent IC");
        logger.info("Login correctly failed with non-existent IC");
        
        logger.info("✓ Staff and Login integration test passed");
    }
    
    // ================== TEST 4: SALES AND TRANSACTION ==================
    
    @Test
    @DisplayName("Module Integration: Sales and Transaction - Payment creates transaction")
    void testSalesAndTransactionIntegration() {
        logger.info("=========================================");
        logger.info("TEST 4: SALES AND TRANSACTION INTEGRATION");
        logger.info("=========================================");
        
        // Setup: Create stock and add to cart
        logger.info("Setting up stock and cart...");
        Stock stock = new Stock(10001, "Product A", 10, 50.00);
        stockService.addNewStock(stock);
        orderService.addToCart(10001, 2);
        logger.info("Stock created and added to cart: ID=10001, Qty=2");
        
        // Test: Calculate payment
        logger.info("Calculating payment...");
        double subtotal = paymentService.calculateSubtotal();
        PaymentResult paymentResult = paymentService.calculatePaymentSummary(0.0);
        logger.info("Subtotal: RM " + String.format("%.2f", subtotal));
        logger.info("Total: RM " + String.format("%.2f", paymentResult.getTotal()));
        
        // Test: Create transaction from payment
        logger.info("Creating transaction from payment...");
        Transaction transaction = paymentService.createTransaction(0.0);
        assertNotNull(transaction, "Transaction should be created");
        assertEquals(subtotal, transaction.getSubtotal(), 0.01);
        assertEquals(paymentResult.getTotal(), transaction.getTotal(), 0.01);
        logger.info("Transaction created: Subtotal=RM " + String.format("%.2f", transaction.getSubtotal()) + 
                   ", Total=RM " + String.format("%.2f", transaction.getTotal()));
        
        // Test: Save transaction
        logger.info("Saving transaction...");
        transactionService.saveTransaction(transaction);
        logger.info("Transaction saved");
        
        // Verify: Transaction exists in repository
        logger.info("Verifying transaction was saved...");
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertFalse(transactions.isEmpty(), "Should have saved transactions");
        assertEquals(1, transactions.size(), "Should have 1 transaction");
        
        Transaction savedTransaction = transactions.get(0);
        assertEquals(transaction.getSubtotal(), savedTransaction.getSubtotal(), 0.01);
        assertEquals(transaction.getTotal(), savedTransaction.getTotal(), 0.01);
        logger.info("Transaction verified: " + transactions.size() + " transaction(s) found");
        
        // Verify: Transaction persisted to file
        logger.info("Verifying transaction persistence...");
        TransactionRepository newRepo = new TransactionRepository();
        List<Transaction> persistedTransactions = newRepo.loadAllTransactions();
        assertEquals(1, persistedTransactions.size(), "Transaction should be persisted to file");
        logger.info("Transaction persisted to file: " + persistedTransactions.size() + " transaction(s)");
        
        logger.info("✓ Sales and Transaction integration test passed");
    }
    
    // ================== TEST 5: STAFF AND SIGNUP ==================
    
    @Test
    @DisplayName("Module Integration: Staff and Signup - New staff registration creates staff member")
    void testStaffAndSignupIntegration() {
        logger.info("=========================================");
        logger.info("TEST 5: STAFF AND SIGNUP INTEGRATION");
        logger.info("=========================================");
        
        // Setup: Create a new staff member through signup process
        logger.info("Creating new staff member through signup...");
        Staff newStaff = new Staff("New Staff Member", "999999999999", 28, 3500.00, "newpassword123");
        newStaff.setId(99999);
        
        // Test: Add staff through service (simulating signup)
        logger.info("Adding staff member (simulating signup process)...");
        boolean staffAdded = staffService.addStaff(newStaff);
        assertTrue(staffAdded, "Staff should be added through signup");
        logger.info("Staff added: ID=" + newStaff.getId() + ", IC=" + newStaff.getIc() + ", Name=" + newStaff.getName());
        
        // Verify: Staff exists in repository
        logger.info("Verifying staff exists in repository...");
        Staff foundStaff = staffService.findByIc("999999999999");
        assertNotNull(foundStaff, "Staff should exist after signup");
        assertEquals(newStaff.getId(), foundStaff.getId());
        assertEquals(newStaff.getName(), foundStaff.getName());
        assertEquals(newStaff.getIc(), foundStaff.getIc());
        logger.info("Staff verified: " + foundStaff.getName() + " (ID: " + foundStaff.getId() + ")");
        
        // Verify: Staff can login after signup
        logger.info("Verifying staff can login after signup...");
        Staff loggedInStaff = staffService.login("999999999999", "newpassword123");
        assertNotNull(loggedInStaff, "Staff should be able to login after signup");
        assertEquals(newStaff.getId(), loggedInStaff.getId());
        logger.info("Login successful: " + loggedInStaff.getName());
        
        // Verify: Staff is persisted to file
        logger.info("Verifying staff persistence to file...");
        StaffRepository newRepo = new StaffRepository();
        List<Staff> allStaff = newRepo.loadAllStaff();
        boolean staffFoundInFile = allStaff.stream()
            .anyMatch(s -> s.getIc().equals("999999999999"));
        assertTrue(staffFoundInFile, "Staff should be persisted to file");
        logger.info("Staff found in file: " + staffFoundInFile);
        
        // Verify: Staff cannot be added again with same IC (duplicate prevention)
        logger.info("Testing duplicate IC prevention...");
        Staff duplicateStaff = new Staff("Duplicate Staff", "999999999999", 30, 4000.00, "password456");
        duplicateStaff.setId(99998);
        boolean duplicateAdded = staffService.addStaff(duplicateStaff);
        assertFalse(duplicateAdded, "Should not be able to add staff with duplicate IC");
        logger.info("Duplicate IC correctly prevented: " + !duplicateAdded);
        
        // Verify: Only one staff with this IC exists
        List<Staff> staffWithIc = allStaff.stream()
            .filter(s -> s.getIc().equals("999999999999"))
            .toList();
        assertEquals(1, staffWithIc.size(), "Should have only one staff with this IC");
        logger.info("Verified: Only " + staffWithIc.size() + " staff member(s) with IC 999999999999");
        
        logger.info("✓ Staff and Signup integration test passed");
    }
    
    // Helper methods
    private File backupFile(String filePath) throws IOException {
        File originalFile = new File(filePath);
        if (originalFile.exists()) {
            File backup = new File(filePath + ".backup");
            Files.copy(originalFile.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return backup;
        }
        return null;
    }
    
    private File createTestFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }
    
    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }
    
    private void restoreFile(File backup, String originalPath) throws IOException {
        if (backup != null && backup.exists()) {
            File original = new File(originalPath);
            Files.copy(backup.toPath(), original.toPath(), StandardCopyOption.REPLACE_EXISTING);
            backup.delete();
        }
    }
}

