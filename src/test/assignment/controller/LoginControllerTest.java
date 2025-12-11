package test.assignment.controller;

import assignment.controller.LoginController;
import assignment.model.Staff;
import assignment.repo.StaffRepository;
import assignment.service.StaffService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoginController.
 * Tests login functionality with real data from staff.txt file.
 * Note: Some methods require user input/UI interaction and are tested for logic only.
 */
@DisplayName("LoginController Tests")
class LoginControllerTest {

    private StaffService staffService;
    private LoginController loginController;
    private List<Staff> originalStaff; // Store original data for cleanup

    @BeforeEach
    void setUp() {
        // Use real StaffRepository that loads from staff.txt file
        StaffRepository staffRepository = new StaffRepository();
        staffService = new StaffService(staffRepository);
        loginController = new LoginController(staffService);
        // Load original data to track what was added during tests
        originalStaff = staffRepository.loadAllStaff();
    }

    @AfterEach
    void tearDown() {
        // Clean up any test data added during tests
        List<Staff> currentStaff = staffService.getAllStaff();
        for (Staff staff : currentStaff) {
            boolean existsInOriginal = originalStaff.stream()
                    .anyMatch(s -> s.getId() == staff.getId());
            if (!existsInOriginal) {
                // This is test data, remove it
                staffService.deleteById(staff.getId());
            }
        }
        // Logout if logged in
        loginController.logout();
    }

    @Test
    @DisplayName("Should initialize LoginController with StaffService")
    void testLoginControllerInitialization() {
        assertNotNull(loginController);
        System.out.println("Initialization successful.");
    }

    @Test
    @DisplayName("Should have access to staff service")
    void testGetStaffService() {
        List<Staff> staff = staffService.getAllStaff();
        assertNotNull(staff);
        assertFalse(staff.isEmpty(), "Should have at least one staff from staff.txt");
        System.out.println("Got " + staff.size() + " staff from staff.txt file.");
    }

    @Test
    @DisplayName("Should return null for getCurrentStaff when not logged in")
    void testGetCurrentStaff_NotLoggedIn() {
        Staff current = loginController.getCurrentStaff();
        assertNull(current);
        System.out.println("Correctly returned null when not logged in.");
    }

    @Test
    @DisplayName("Should logout successfully")
    void testLogout() {
        // Logout when not logged in should not cause errors
        loginController.logout();
        assertNull(loginController.getCurrentStaff());
        System.out.println("Logout successful (no errors).");
    }

    @Test
    @DisplayName("Should find staff with plain text password for login testing")
    void testFindStaffWithPlainTextPassword() {
        // Find a staff with plain text password (not hashed) for easier testing
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff testStaff = null;
        String testPassword = null;

        for (Staff s : allStaff) {
            String storedPassword = s.getStfPassword();
            // Check if password is plain text (doesn't contain colon separator)
            if (storedPassword != null && !storedPassword.contains(":")) {
                testStaff = s;
                testPassword = storedPassword;
                break;
            }
        }

        // If no plain text password found, create a test staff
        if (testStaff == null) {
            testStaff = new Staff("LoginTest", "222222222220", 25, 3000.00, "password123");
            testStaff.setId(222220);
            staffService.addStaff(testStaff);
            testPassword = "password123";
        }

        assertNotNull(testStaff);
        assertNotNull(testPassword);
        System.out.println("Found staff with plain text password: " + testStaff.getName());
    }

    @Test
    @DisplayName("Should verify login service method works correctly")
    void testLoginServiceMethod() {
        // Find a staff with plain text password
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff testStaff = null;
        String testPassword = null;

        for (Staff s : allStaff) {
            String storedPassword = s.getStfPassword();
            if (storedPassword != null && !storedPassword.contains(":")) {
                testStaff = s;
                testPassword = storedPassword;
                break;
            }
        }

        // If no plain text password found, create a test staff
        if (testStaff == null) {
            testStaff = new Staff("LoginTest", "222222222219", 25, 3000.00, "password123");
            testStaff.setId(222219);
            staffService.addStaff(testStaff);
            testPassword = "password123";
        }

        // Test login with correct credentials
        Staff loggedIn = staffService.login(testStaff.getIc(), testPassword);
        assertNotNull(loggedIn);
        assertEquals(testStaff.getName(), loggedIn.getName());
        assertEquals(testStaff.getIc(), loggedIn.getIc());
        System.out.println("Login service method works correctly: " + loggedIn.getName());

        // Test login with incorrect password
        Staff wrongPassword = staffService.login(testStaff.getIc(), "wrongpassword");
        assertNull(wrongPassword);
        System.out.println("Login correctly failed with wrong password.");

        // Test login with non-existent IC
        Staff nonExistent = staffService.login("000000000000", "password123");
        assertNull(nonExistent);
        System.out.println("Login correctly failed with non-existent IC.");
    }

    @Test
    @DisplayName("Should verify findByIc method works correctly")
    void testFindByIc() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);
        String staffIc = firstStaff.getIc();

        Staff found = staffService.findByIc(staffIc);
        assertNotNull(found);
        assertEquals(staffIc, found.getIc());
        assertEquals(firstStaff.getName(), found.getName());
        System.out.println("Found staff by IC: " + found.getName());

        // Test with non-existent IC
        Staff notFound = staffService.findByIc("000000000000");
        assertNull(notFound);
        System.out.println("Correctly returned null for non-existent IC.");
    }
}

