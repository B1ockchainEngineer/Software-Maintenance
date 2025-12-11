package test.assignment.service;

import assignment.model.Staff;
import assignment.repo.StaffRepository;
import assignment.service.StaffService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Login functionality at Service level.
 * Tests StaffService methods related to login/authentication using real data from staff.txt.
 */
@DisplayName("Login Service Tests")
class LoginServiceTest {

    private static final Logger LOGGER = Logger.getLogger(LoginServiceTest.class.getName());

    private StaffRepository staffRepo;
    private StaffService staffService;
    private List<Staff> originalStaff; // Store original data for cleanup

    @BeforeEach
    void setUp() {
        // Use real StaffRepository that loads from staff.txt file
        staffRepo = new StaffRepository();
        staffService = new StaffService(staffRepo);
        // Load original data to track what was added during tests
        originalStaff = staffRepo.loadAllStaff();
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
    }

    @Test
    @DisplayName("Should successfully login with correct credentials")
    void testLogin_Success() {
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
            testStaff = new Staff("LoginTest", "222222222216", 25, 3000.00, "password123");
            testStaff.setId(222216);
            staffService.addStaff(testStaff);
            testPassword = "password123";
        }

        Staff loggedIn = staffService.login(testStaff.getIc(), testPassword);

        assertNotNull(loggedIn);
        assertEquals(testStaff.getName(), loggedIn.getName());
        assertEquals(testStaff.getIc(), loggedIn.getIc());
        LOGGER.log(Level.INFO, "Login successful: " + loggedIn.getName() + " (IC: " + loggedIn.getIc() + ")");
        System.out.println("Login successful: " + loggedIn.getName());
    }

    @Test
    @DisplayName("Should return null for incorrect password")
    void testLogin_IncorrectPassword() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);

        Staff loggedIn = staffService.login(firstStaff.getIc(), "wrongpassword");

        assertNull(loggedIn);
        LOGGER.log(Level.INFO, "Validation: Login correctly failed with wrong password for IC: " + firstStaff.getIc());
        System.out.println("Login failed with wrong password.");
    }

    @Test
    @DisplayName("Should return null for non-existent IC")
    void testLogin_NonExistentIc() {
        Staff loggedIn = staffService.login("000000000000", "password123");

        assertNull(loggedIn);
        LOGGER.log(Level.INFO, "Validation: Login correctly failed with non-existent IC: 000000000000");
        System.out.println("Login failed with non-existent IC.");
    }

    @Test
    @DisplayName("Should return null for empty IC")
    void testLogin_EmptyIc() {
        Staff loggedIn = staffService.login("", "password123");

        assertNull(loggedIn);
        LOGGER.log(Level.INFO, "Validation: Login correctly failed with empty IC");
        System.out.println("Login failed with empty IC.");
    }

    @Test
    @DisplayName("Should return null for empty password")
    void testLogin_EmptyPassword() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);

        Staff loggedIn = staffService.login(firstStaff.getIc(), "");

        assertNull(loggedIn);
        LOGGER.log(Level.INFO, "Validation: Login correctly failed with empty password for IC: " + firstStaff.getIc());
        System.out.println("Login failed with empty password.");
    }

    @Test
    @DisplayName("Should handle hashed passwords correctly")
    void testLogin_HashedPassword() {
        // Create a test staff with known password that will be hashed
        Staff testStaff = new Staff("HashedPasswordTest", "222222222215", 25, 3000.00, "testpass123");
        testStaff.setId(222215);
        staffService.addStaff(testStaff); // This will hash the password

        // Now try to login with the original plain password
        Staff loggedIn = staffService.login("222222222215", "testpass123");

        assertNotNull(loggedIn, "Should login successfully even with hashed password");
        assertEquals("HashedPasswordTest", loggedIn.getName());
        LOGGER.log(Level.INFO, "Login successful with hashed password: " + loggedIn.getName());
        System.out.println("Login successful with hashed password: " + loggedIn.getName());
    }

    @Test
    @DisplayName("Should verify findByIc works for login verification")
    void testFindByIc_ForLogin() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);
        String staffIc = firstStaff.getIc();

        Staff found = staffService.findByIc(staffIc);
        assertNotNull(found);
        assertEquals(staffIc, found.getIc());
        assertEquals(firstStaff.getName(), found.getName());
        LOGGER.log(Level.INFO, "Found staff by IC for login verification: " + found.getName() + " (IC: " + staffIc + ")");
        System.out.println("Found staff by IC for login verification: " + found.getName());
    }

    @Test
    @DisplayName("Should verify IC exists before login attempt")
    void testIcExists_ForLogin() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);

        // Check if staff exists by IC
        Staff found = staffService.findByIc(firstStaff.getIc());
        assertNotNull(found, "Staff should exist for login");
        LOGGER.log(Level.INFO, "IC existence verified for login: " + found.getName() + " (IC: " + firstStaff.getIc() + ")");
        System.out.println("IC existence verified for login: " + found.getName());
    }
}

