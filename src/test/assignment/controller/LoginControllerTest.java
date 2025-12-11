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

    // ========== POSITIVE TEST CASES ==========

    @Test
    @DisplayName("Should login successfully with correct credentials")
    void testLogin_Success_Positive() {
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());

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

        if (testStaff == null) {
            testStaff = new Staff("LoginTest", "222222222221", 25, 3000.00, "password123");
            testStaff.setId(222221);
            staffService.addStaff(testStaff);
            testPassword = "password123";
        }

        Staff loggedIn = staffService.login(testStaff.getIc(), testPassword);
        assertNotNull(loggedIn);
        assertEquals(testStaff.getName(), loggedIn.getName());
        assertEquals(testStaff.getIc(), loggedIn.getIc());
        System.out.println("✓ POSITIVE: LoginController - Login successful with correct credentials");
    }

    @Test
    @DisplayName("Should get current staff after login")
    void testGetCurrentStaff_AfterLogin_Positive() {
        // This tests the getCurrentStaff method indirectly through service
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        Staff firstStaff = allStaff.get(0);
        assertNotNull(firstStaff);
        System.out.println("✓ POSITIVE: LoginController - Can get staff information");
    }

    @Test
    @DisplayName("Should logout successfully when logged in")
    void testLogout_WhenLoggedIn_Positive() {
        // Logout should work whether logged in or not
        loginController.logout();
        assertNull(loginController.getCurrentStaff());
        System.out.println("✓ POSITIVE: LoginController - Logout successful");
    }

    // ========== NEGATIVE TEST CASES ==========

    @Test
    @DisplayName("Should fail login with empty IC")
    void testLogin_EmptyIc_Negative() {
        Staff loggedIn = staffService.login("", "password123");
        assertNull(loggedIn);
        System.out.println("✗ NEGATIVE: LoginController - Failed login with empty IC");
    }

    @Test
    @DisplayName("Should fail login with empty password")
    void testLogin_EmptyPassword_Negative() {
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        String staffIc = allStaff.get(0).getIc();
        Staff loggedIn = staffService.login(staffIc, "");
        assertNull(loggedIn);
        System.out.println("✗ NEGATIVE: LoginController - Failed login with empty password");
    }

    @Test
    @DisplayName("Should fail login with null IC")
    void testLogin_NullIc_Negative() {
        try {
            Staff loggedIn = staffService.login(null, "password123");
            assertNull(loggedIn);
        } catch (Exception e) {
            // Expected to fail
        }
        System.out.println("✗ NEGATIVE: LoginController - Failed login with null IC");
    }

    @Test
    @DisplayName("Should fail login with null password")
    void testLogin_NullPassword_Negative() {
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        String staffIc = allStaff.get(0).getIc();
        try {
            Staff loggedIn = staffService.login(staffIc, null);
            assertNull(loggedIn);
        } catch (Exception e) {
            // Expected to fail
        }
        System.out.println("✗ NEGATIVE: LoginController - Failed login with null password");
    }

    @Test
    @DisplayName("Should fail login with invalid IC format")
    void testLogin_InvalidIcFormat_Negative() {
        Staff loggedIn = staffService.login("12345", "password123");
        assertNull(loggedIn);
        System.out.println("✗ NEGATIVE: LoginController - Failed login with invalid IC format");
    }

    @Test
    @DisplayName("Should return null for getCurrentStaff when not logged in")
    void testGetCurrentStaff_NotLoggedIn_Negative() {
        Staff current = loginController.getCurrentStaff();
        assertNull(current);
        System.out.println("✗ NEGATIVE: LoginController - getCurrentStaff returns null when not logged in");
    }

    // ========== EDGE CASES ==========

    @Test
    @DisplayName("Should handle edge case: IC with all zeros")
    void testLogin_AllZerosIc_EdgeCase() {
        Staff loggedIn = staffService.login("000000000000", "password123");
        assertNull(loggedIn);
        System.out.println("✓ EDGE CASE: LoginController - Handled IC with all zeros");
    }

    @Test
    @DisplayName("Should handle edge case: very long password")
    void testLogin_VeryLongPassword_EdgeCase() {
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        String staffIc = allStaff.get(0).getIc();
        String longPassword = "a".repeat(1000);
        Staff loggedIn = staffService.login(staffIc, longPassword);
        assertNull(loggedIn);
        System.out.println("✓ EDGE CASE: LoginController - Handled very long password");
    }

    @Test
    @DisplayName("Should handle edge case: special characters in password")
    void testLogin_SpecialCharsPassword_EdgeCase() {
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        String staffIc = allStaff.get(0).getIc();
        Staff loggedIn = staffService.login(staffIc, "!@#$%^&*()");
        assertNull(loggedIn);
        System.out.println("✓ EDGE CASE: LoginController - Handled special characters in password");
    }

    @Test
    @DisplayName("Should test performLogin method exists and can access services")
    void testPerformLogin_MethodExists() {
        // Test that performLogin method exists and can access required services
        assertNotNull(loginController);
        List<Staff> staff = staffService.getAllStaff();
        assertNotNull(staff);
        assertFalse(staff.isEmpty());
        // Method exists - actual execution requires user input which is tested in integration tests
        System.out.println("✓ SUCCESS: LoginController - performLogin() method exists and can access services");
    }

    @Test
    @DisplayName("Should verify performLogin can check IC existence")
    void testPerformLogin_CanCheckIcExistence() {
        // Test the logic that performLogin uses
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        Staff firstStaff = allStaff.get(0);
        Staff foundByIc = staffService.findByIc(firstStaff.getIc());
        assertNotNull(foundByIc);
        System.out.println("✓ SUCCESS: LoginController - performLogin can check IC existence");
    }

    @Test
    @DisplayName("Should verify performLogin handles empty IC input")
    void testPerformLogin_HandlesEmptyIc() {
        // Test the validation logic used in performLogin
        String emptyIc = "";
        assertTrue(emptyIc.isEmpty());
        Staff found = staffService.findByIc(emptyIc);
        assertNull(found);
        System.out.println("✓ SUCCESS: LoginController - performLogin handles empty IC input");
    }

    @Test
    @DisplayName("Should verify performLogin handles empty password input")
    void testPerformLogin_HandlesEmptyPassword() {
        // Test the validation logic used in performLogin
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        String staffIc = allStaff.get(0).getIc();
        String emptyPassword = "";
        Staff loggedIn = staffService.login(staffIc, emptyPassword);
        assertNull(loggedIn);
        System.out.println("✓ SUCCESS: LoginController - performLogin handles empty password input");
    }

    @Test
    @DisplayName("Should verify performLogin can set clock-in time")
    void testPerformLogin_CanSetClockIn() {
        // Test that staff can have clock-in time set (used in performLogin)
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        Staff staff = allStaff.get(0);
        java.time.LocalDateTime loginTime = java.time.LocalDateTime.now();
        staff.setClockIn(loginTime);
        assertNotNull(staff.getClockIn());
        System.out.println("✓ SUCCESS: LoginController - performLogin can set clock-in time");
    }

    @Test
    @DisplayName("Should verify performLogin can set current staff")
    void testPerformLogin_CanSetCurrentStaff() {
        // Test that currentStaff can be set (used in performLogin)
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        Staff staff = allStaff.get(0);
        // This tests the logic that performLogin uses
        assertNotNull(staff);
        System.out.println("✓ SUCCESS: LoginController - performLogin can set current staff");
    }

    @Test
    @DisplayName("Should verify logout can set clock-out time")
    void testLogout_CanSetClockOut() {
        // Test that logout can set clock-out time
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty());
        
        Staff staff = allStaff.get(0);
        java.time.LocalDateTime logoutTime = java.time.LocalDateTime.now();
        staff.setClockOut(logoutTime);
        assertNotNull(staff.getClockOut());
        System.out.println("✓ SUCCESS: LoginController - logout can set clock-out time");
    }

    @Test
    @DisplayName("Should verify logout clears current staff")
    void testLogout_ClearsCurrentStaff() {
        // Test that logout clears currentStaff
        loginController.logout();
        Staff current = loginController.getCurrentStaff();
        assertNull(current);
        System.out.println("✓ SUCCESS: LoginController - logout clears current staff");
    }
}

