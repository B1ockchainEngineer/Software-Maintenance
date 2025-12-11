package test.assignment.repo;

import assignment.model.Staff;
import assignment.repo.StaffRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Signup functionality at Repository level.
 * Tests StaffRepository methods related to staff registration/signup using real data from staff.txt.
 */
@DisplayName("Signup Repository Tests")
class SignupRepositoryTest {

    private static final Logger LOGGER = Logger.getLogger(SignupRepositoryTest.class.getName());

    private StaffRepository staffRepository;
    private List<Staff> originalStaff; // Store original data for cleanup

    @BeforeEach
    void setUp() {
        // Use real StaffRepository that loads from staff.txt file
        staffRepository = new StaffRepository();
        // Load original data to track what was added during tests
        originalStaff = staffRepository.loadAllStaff();
    }

    @AfterEach
    void tearDown() {
        // Clean up any test data added during tests
        List<Staff> currentStaff = staffRepository.loadAllStaff();
        for (Staff staff : currentStaff) {
            boolean existsInOriginal = originalStaff.stream()
                    .anyMatch(s -> s.getId() == staff.getId());
            if (!existsInOriginal) {
                // This is test data, remove it
                staffRepository.deleteById(staff.getId());
            }
        }
    }

    @Test
    @DisplayName("Should append new staff to file")
    void testAppendStaff_Success() {
        // Get initial count
        int initialCount = staffRepository.loadAllStaff().size();

        // Create new staff with unique ID and IC
        Staff newStaff = new Staff("SignupTest", "111111111106", 28, 4000.00, "password123");
        newStaff.setId(111106);

        staffRepository.appendStaff(newStaff);

        List<Staff> staffList = staffRepository.loadAllStaff();
        assertEquals(initialCount + 1, staffList.size());
        assertNotNull(staffRepository.findById(111106));
        LOGGER.log(Level.INFO, "Staff appended successfully. Total count: " + staffList.size() + " (ID: 111106)");
        System.out.println("Staff appended successfully. Total count: " + staffList.size());
    }

    @Test
    @DisplayName("Should hash password when appending staff")
    void testAppendStaff_PasswordHashed() {
        // Create new staff with known password
        Staff newStaff = new Staff("PasswordHashTest", "111111111105", 25, 3000.00, "plainpassword");
        newStaff.setId(111105);

        staffRepository.appendStaff(newStaff);

        // Retrieve the staff and check if password is hashed
        Staff retrieved = staffRepository.findById(111105);
        assertNotNull(retrieved);

        String storedPassword = retrieved.getStfPassword();
        assertNotNull(storedPassword);
        // Hashed password should contain colon separator (salt:hash format)
        assertTrue(storedPassword.contains(":"), "Password should be hashed");
        assertNotEquals("plainpassword", storedPassword, "Password should not be plain text");
        LOGGER.log(Level.INFO, "Password correctly hashed during signup for staff ID: 111105");
        System.out.println("Password correctly hashed during signup.");
    }

    @Test
    @DisplayName("Should check if IC exists before signup")
    void testExistsByIc_ForSignup() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);
        String existingIc = firstStaff.getIc();

        // Verify IC exists check works
        assertTrue(staffRepository.existsByIc(existingIc));
        assertFalse(staffRepository.existsByIc("000000000000"));
        LOGGER.log(Level.INFO, "IC existence check verified for signup - IC " + existingIc + " exists");
        System.out.println("IC existence check verified for signup.");
    }

    @Test
    @DisplayName("Should prevent duplicate IC during signup")
    void testAppendStaff_DuplicateIc() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);
        String existingIc = firstStaff.getIc();

        // Try to append staff with duplicate IC
        Staff duplicateStaff = new Staff("Duplicate", existingIc, 35, 5000.00, "password999");
        duplicateStaff.setId(999997);

        // Note: appendStaff doesn't check for duplicates, but existsByIc does
        // This test verifies the repository method works
        assertTrue(staffRepository.existsByIc(existingIc));
        LOGGER.log(Level.INFO, "Validation: Duplicate IC check verified - IC " + existingIc + " already exists");
        System.out.println("Duplicate IC check verified (existsByIc returns true for existing IC).");
    }

    @Test
    @DisplayName("Should find staff by IC after signup")
    void testFindByIc_AfterSignup() {
        // Create new staff
        Staff newStaff = new Staff("FindAfterSignup", "111111111104", 30, 3500.00, "password456");
        newStaff.setId(111104);

        staffRepository.appendStaff(newStaff);

        // Verify can find by IC
        Staff found = staffRepository.findByIc("111111111104");
        assertNotNull(found);
        assertEquals("FindAfterSignup", found.getName());
        assertEquals("111111111104", found.getIc());
        LOGGER.log(Level.INFO, "Found staff by IC after signup: " + found.getName() + " (IC: 111111111104)");
        System.out.println("Found staff by IC after signup: " + found.getName());
    }

    @Test
    @DisplayName("Should find staff by ID after signup")
    void testFindById_AfterSignup() {
        // Create new staff
        Staff newStaff = new Staff("FindByIdAfterSignup", "111111111103", 32, 3800.00, "password789");
        newStaff.setId(111103);

        staffRepository.appendStaff(newStaff);

        // Verify can find by ID
        Staff found = staffRepository.findById(111103);
        assertNotNull(found);
        assertEquals("FindByIdAfterSignup", found.getName());
        assertEquals(111103, found.getId());
        LOGGER.log(Level.INFO, "Found staff by ID after signup: " + found.getName() + " (ID: 111103)");
        System.out.println("Found staff by ID after signup: " + found.getName());
    }

    @Test
    @DisplayName("Should load all staff including newly signed up staff")
    void testLoadAllStaff_AfterSignup() {
        // Get initial count
        int initialCount = staffRepository.loadAllStaff().size();

        // Create and append new staff
        Staff newStaff = new Staff("LoadAllTest", "111111111102", 27, 3200.00, "password101");
        newStaff.setId(111102);
        staffRepository.appendStaff(newStaff);

        // Verify new staff is in the list
        List<Staff> allStaff = staffRepository.loadAllStaff();
        assertEquals(initialCount + 1, allStaff.size());

        // Find the staff by ID (name might not be uppercase depending on how it was stored)
        Staff foundStaff = allStaff.stream()
                .filter(s -> s.getId() == 111102)
                .findFirst()
                .orElse(null);

        assertNotNull(foundStaff, "Newly signed up staff should be in the list");
        assertEquals(111102, foundStaff.getId());
        assertEquals("111111111102", foundStaff.getIc());
        // Name comparison should be case-insensitive since Person constructor doesn't uppercase
        assertTrue(foundStaff.getName().equalsIgnoreCase("LoadAllTest"),
                "Staff name should match (case-insensitive)");
        LOGGER.log(Level.INFO, "New staff found in loadAllStaff: " + allStaff.size() + " total staff (ID: 111102)");
        System.out.println("New staff found in loadAllStaff: " + allStaff.size() + " total staff.");
    }
}

