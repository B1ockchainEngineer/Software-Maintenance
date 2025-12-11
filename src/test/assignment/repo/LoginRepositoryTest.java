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
 * Unit tests for Login functionality at Repository level.
 * Tests StaffRepository methods related to login/authentication using real data from staff.txt.
 */
@DisplayName("Login Repository Tests")
class LoginRepositoryTest {

    private static final Logger LOGGER = Logger.getLogger(LoginRepositoryTest.class.getName());

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
    @DisplayName("Should find staff by credentials with correct IC and password")
    void testFindByCredentials_Success() {
        // Find a staff with plain text password (not hashed) for easier testing
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff testStaff = null;
        String testPassword = null;

        for (Staff s : staffList) {
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
            testStaff = new Staff("LoginTest", "222222222218", 25, 3000.00, "password123");
            testStaff.setId(222218);
            staffRepository.appendStaff(testStaff);
            testPassword = "password123";
        }

        Staff found = staffRepository.findByCredentials(testStaff.getIc(), testPassword);
        assertNotNull(found);
        assertEquals(testStaff.getName(), found.getName());
        assertEquals(testStaff.getIc(), found.getIc());
        LOGGER.log(Level.INFO, "Login successful: Found staff by credentials - " + found.getName());
        System.out.println("Login successful: Found staff by credentials - " + found.getName());
    }

    @Test
    @DisplayName("Should return null for incorrect password")
    void testFindByCredentials_IncorrectPassword() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);

        Staff found = staffRepository.findByCredentials(firstStaff.getIc(), "wrongpassword");
        assertNull(found);
        LOGGER.log(Level.INFO, "Validation: Login correctly failed with wrong password for IC: " + firstStaff.getIc());
        System.out.println("Login correctly failed with wrong password.");
    }

    @Test
    @DisplayName("Should return null for non-existent IC")
    void testFindByCredentials_NonExistentIc() {
        Staff found = staffRepository.findByCredentials("000000000000", "password123");
        assertNull(found);
        LOGGER.log(Level.INFO, "Validation: Login correctly failed with non-existent IC: 000000000000");
        System.out.println("Login correctly failed with non-existent IC.");
    }

    @Test
    @DisplayName("Should return null for empty IC")
    void testFindByCredentials_EmptyIc() {
        Staff found = staffRepository.findByCredentials("", "password123");
        assertNull(found);
        LOGGER.log(Level.INFO, "Validation: Login correctly failed with empty IC");
        System.out.println("Login correctly failed with empty IC.");
    }

    @Test
    @DisplayName("Should return null for empty password")
    void testFindByCredentials_EmptyPassword() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);

        Staff found = staffRepository.findByCredentials(firstStaff.getIc(), "");
        assertNull(found);
        LOGGER.log(Level.INFO, "Validation: Login correctly failed with empty password for IC: " + firstStaff.getIc());
        System.out.println("Login correctly failed with empty password.");
    }

    @Test
    @DisplayName("Should handle hashed passwords correctly")
    void testFindByCredentials_HashedPassword() {
        // Find a staff with hashed password
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff hashedStaff = null;
        String plainPassword = null;

        for (Staff s : staffList) {
            String storedPassword = s.getStfPassword();
            // Check if password is hashed (contains colon separator)
            if (storedPassword != null && storedPassword.contains(":")) {
                hashedStaff = s;
                // Try common passwords or create a test staff with known password
                break;
            }
        }

        // If we found a hashed password staff, we can't test without knowing the password
        // So create a test staff with known password that will be hashed
        if (hashedStaff == null || plainPassword == null) {
            Staff testStaff = new Staff("HashedPasswordTest", "222222222217", 25, 3000.00, "testpass123");
            testStaff.setId(222217);
            staffRepository.appendStaff(testStaff); // This will hash the password

            // Now try to login with the original plain password
            Staff found = staffRepository.findByCredentials("222222222217", "testpass123");
            assertNotNull(found, "Should find staff even with hashed password");
            assertEquals("HashedPasswordTest", found.getName());
            LOGGER.log(Level.INFO, "Login successful with hashed password: " + found.getName());
            System.out.println("Login successful with hashed password: " + found.getName());
        }
    }

    @Test
    @DisplayName("Should verify IC exists before login attempt")
    void testExistsByIc_ForLogin() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);

        // Verify IC exists
        assertTrue(staffRepository.existsByIc(firstStaff.getIc()));
        assertFalse(staffRepository.existsByIc("000000000000"));
        LOGGER.log(Level.INFO, "IC existence check verified for login - IC " + firstStaff.getIc() + " exists");
        System.out.println("IC existence check verified for login.");
    }

    @Test
    @DisplayName("Should find staff by IC for login verification")
    void testFindByIc_ForLogin() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);
        String staffIc = firstStaff.getIc();

        Staff found = staffRepository.findByIc(staffIc);
        assertNotNull(found);
        assertEquals(staffIc, found.getIc());
        assertEquals(firstStaff.getName(), found.getName());
        LOGGER.log(Level.INFO, "Found staff by IC for login verification: " + found.getName() + " (IC: " + staffIc + ")");
        System.out.println("Found staff by IC for login verification: " + found.getName());
    }
}

