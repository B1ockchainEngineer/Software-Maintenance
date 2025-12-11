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
 * Unit tests for Signup functionality at Service level.
 * Tests StaffService methods related to staff registration/signup using real data from staff.txt.
 */
@DisplayName("Signup Service Tests")
class SignupServiceTest {

    private static final Logger LOGGER = Logger.getLogger(SignupServiceTest.class.getName());

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
    @DisplayName("Should successfully add new staff during signup")
    void testAddStaff_Success() {
        // Get initial count
        int initialCount = staffService.getAllStaff().size();

        // Create new staff with unique ID and IC
        Staff newStaff = new Staff("SignupTest", "111111111101", 28, 4000.00, "password123");
        newStaff.setId(111101);

        boolean result = staffService.addStaff(newStaff);
        assertTrue(result);

        assertEquals(initialCount + 1, staffService.getAllStaff().size());
        assertNotNull(staffService.findById(111101));
        assertEquals("SignupTest", staffService.findById(111101).getName());
        LOGGER.log(Level.INFO, "Staff added successfully during signup. Total count: " + staffService.getAllStaff().size() + " (ID: 111101)");
        System.out.println("Staff added successfully during signup. Total count: " + staffService.getAllStaff().size());
    }

    @Test
    @DisplayName("Should fail to add staff with duplicate IC during signup")
    void testAddStaff_DuplicateIc() {
        // Get existing staff from file
        List<Staff> existingStaff = staffService.getAllStaff();
        assertFalse(existingStaff.isEmpty(), "Should have at least one staff in file");

        // Use existing IC from first staff in file
        String existingIc = existingStaff.get(0).getIc();
        int initialCount = existingStaff.size();

        Staff duplicateStaff = new Staff("Duplicate", existingIc, 35, 5000.00, "password999");
        duplicateStaff.setId(999996);

        boolean result = staffService.addStaff(duplicateStaff);
        assertFalse(result);
        assertEquals(initialCount, staffService.getAllStaff().size());
        LOGGER.log(Level.INFO, "Validation: Duplicate IC prevented during signup - IC " + existingIc + " already exists. Result: " + result);
        System.out.println("Duplicate IC prevented during signup. Result: " + result);
    }

    @Test
    @DisplayName("Should hash password when adding staff during signup")
    void testAddStaff_PasswordHashed() {
        // Create new staff with known password
        Staff newStaff = new Staff("PasswordHashTest", "111111111100", 25, 3000.00, "plainpassword");
        newStaff.setId(111100);

        boolean result = staffService.addStaff(newStaff);
        assertTrue(result);

        // Retrieve the staff and check if password is hashed
        Staff retrieved = staffService.findById(111100);
        assertNotNull(retrieved);

        String storedPassword = retrieved.getStfPassword();
        assertNotNull(storedPassword);
        // Hashed password should contain colon separator (salt:hash format)
        assertTrue(storedPassword.contains(":"), "Password should be hashed");
        assertNotEquals("plainpassword", storedPassword, "Password should not be plain text");
        LOGGER.log(Level.INFO, "Password correctly hashed during signup for staff ID: 111100");
        System.out.println("Password correctly hashed during signup.");
    }

    @Test
    @DisplayName("Should verify IC uniqueness check before signup")
    void testIcExists_ForSignup() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);
        String existingIc = firstStaff.getIc();

        // Check if IC exists (should return true for existing IC)
        Staff found = staffService.findByIc(existingIc);
        assertNotNull(found, "IC should exist");

        // Try to add with same IC should fail
        Staff duplicateStaff = new Staff("Duplicate", existingIc, 35, 5000.00, "password999");
        duplicateStaff.setId(999995);
        boolean result = staffService.addStaff(duplicateStaff);
        assertFalse(result, "Should fail to add staff with duplicate IC");
        LOGGER.log(Level.INFO, "Validation: IC uniqueness check verified - IC " + existingIc + " already exists. Add result: " + result);
        System.out.println("IC uniqueness check verified for signup.");
    }

    @Test
    @DisplayName("Should find staff by IC after successful signup")
    void testFindByIc_AfterSignup() {
        // Create new staff
        Staff newStaff = new Staff("FindAfterSignup", "111111111099", 30, 3500.00, "password456");
        newStaff.setId(111099);

        boolean added = staffService.addStaff(newStaff);
        assertTrue(added);

        // Verify can find by IC
        Staff found = staffService.findByIc("111111111099");
        assertNotNull(found);
        assertEquals("FindAfterSignup", found.getName());
        assertEquals("111111111099", found.getIc());
        LOGGER.log(Level.INFO, "Found staff by IC after signup: " + found.getName() + " (IC: 111111111099)");
        System.out.println("Found staff by IC after signup: " + found.getName());
    }

    @Test
    @DisplayName("Should find staff by ID after successful signup")
    void testFindById_AfterSignup() {
        // Create new staff
        Staff newStaff = new Staff("FindByIdAfterSignup", "111111111098", 32, 3800.00, "password789");
        newStaff.setId(111098);

        boolean added = staffService.addStaff(newStaff);
        assertTrue(added);

        // Verify can find by ID
        Staff found = staffService.findById(111098);
        assertNotNull(found);
        assertEquals("FindByIdAfterSignup", found.getName());
        assertEquals(111098, found.getId());
        LOGGER.log(Level.INFO, "Found staff by ID after signup: " + found.getName() + " (ID: 111098)");
        System.out.println("Found staff by ID after signup: " + found.getName());
    }

    @Test
    @DisplayName("Should get all staff including newly signed up staff")
    void testGetAllStaff_AfterSignup() {
        // Get initial count
        int initialCount = staffService.getAllStaff().size();

        // Create and add new staff
        Staff newStaff = new Staff("GetAllTest", "111111111097", 27, 3200.00, "password101");
        newStaff.setId(111097);
        boolean added = staffService.addStaff(newStaff);
        assertTrue(added);

        // Verify new staff is in the list
        List<Staff> allStaff = staffService.getAllStaff();
        assertEquals(initialCount + 1, allStaff.size());

        // Find the staff by ID (name might not be uppercase depending on how it was stored)
        Staff foundStaff = allStaff.stream()
                .filter(s -> s.getId() == 111097)
                .findFirst()
                .orElse(null);

        assertNotNull(foundStaff, "Newly signed up staff should be in the list");
        assertEquals(111097, foundStaff.getId());
        assertEquals("111111111097", foundStaff.getIc());
        // Name comparison should be case-insensitive since Person constructor doesn't uppercase
        assertTrue(foundStaff.getName().equalsIgnoreCase("GetAllTest"),
                "Staff name should match (case-insensitive)");
        LOGGER.log(Level.INFO, "New staff found in getAllStaff: " + allStaff.size() + " total staff (ID: 111097)");
        System.out.println("New staff found in getAllStaff: " + allStaff.size() + " total staff.");
    }

    @Test
    @DisplayName("Should verify staff can login after signup")
    void testLogin_AfterSignup() {
        // Create new staff with known password
        Staff newStaff = new Staff("LoginAfterSignup", "111111111096", 29, 3600.00, "signuppass123");
        newStaff.setId(111096);

        boolean added = staffService.addStaff(newStaff);
        assertTrue(added);

        // Try to login with the new staff credentials
        Staff loggedIn = staffService.login("111111111096", "signuppass123");
        assertNotNull(loggedIn, "Should be able to login after signup");
        assertEquals("LoginAfterSignup", loggedIn.getName());
        assertEquals("111111111096", loggedIn.getIc());
        LOGGER.log(Level.INFO, "Login successful after signup: " + loggedIn.getName() + " (IC: 111111111096)");
        System.out.println("Login successful after signup: " + loggedIn.getName());
    }
}

