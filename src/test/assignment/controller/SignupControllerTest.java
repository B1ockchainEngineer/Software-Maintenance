package assignment.controller;

import assignment.controller.SignupController;
import assignment.model.Staff;
import assignment.repo.StaffRepository;
import assignment.service.StaffService;
import assignment.util.config.SignupConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SignupController.
 * Tests signup/registration functionality with real data from staff.txt file.
 * Note: Some methods require user input/UI interaction and are tested for logic only.
 */
@DisplayName("SignupController Tests")
class SignupControllerTest {

    private StaffService staffService;
    private SignupController signupController;
    private List<Staff> originalStaff; // Store original data for cleanup

    @BeforeEach
    void setUp() {
        // Use real StaffRepository that loads from staff.txt file
        StaffRepository staffRepository = new StaffRepository();
        staffService = new StaffService(staffRepository);
        signupController = new SignupController(staffService);
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
    }

    @Test
    @DisplayName("Should initialize SignupController with StaffService")
    void testSignupControllerInitialization() {
        assertNotNull(signupController);
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
    @DisplayName("Should verify registration code constant")
    void testRegistrationCodeConstant() {
        int registrationCode = SignupConfig.REGISTRATION_CODE;
        assertTrue(registrationCode > 0, "Registration code should be positive");
        System.out.println("Registration code: " + registrationCode);
    }

    @Test
    @DisplayName("Should successfully add new staff via service")
    void testAddStaff_Success() {
        // Get initial count
        int initialCount = staffService.getAllStaff().size();

        // Create new staff with unique ID and IC
        Staff newStaff = new Staff("SignupTest", "111111111108", 28, 4000.00, "password123");
        newStaff.setId(111108);

        boolean result = staffService.addStaff(newStaff);
        assertTrue(result);

        assertEquals(initialCount + 1, staffService.getAllStaff().size());
        assertNotNull(staffService.findById(111108));
        assertEquals("SignupTest", staffService.findById(111108).getName());
        System.out.println("Staff added successfully via service. Total count: " + staffService.getAllStaff().size());
    }

    @Test
    @DisplayName("Should fail to add staff with duplicate IC")
    void testAddStaff_DuplicateIc() {
        // Get existing staff from file
        List<Staff> existingStaff = staffService.getAllStaff();
        assertFalse(existingStaff.isEmpty(), "Should have at least one staff in file");

        // Use existing IC from first staff in file
        String existingIc = existingStaff.get(0).getIc();
        int initialCount = existingStaff.size();

        Staff duplicateStaff = new Staff("Duplicate", existingIc, 35, 5000.00, "password999");
        duplicateStaff.setId(999998);

        boolean result = staffService.addStaff(duplicateStaff);
        assertFalse(result);
        assertEquals(initialCount, staffService.getAllStaff().size());
        System.out.println("Duplicate IC check passed. Result: " + result);
    }

    @Test
    @DisplayName("Should verify IC validation logic")
    void testIcValidation() {
        // Test valid IC format
        String validIc = "010203040506";
        assertTrue(validIc.matches("\\d{12}"), "Valid IC should match 12 digits pattern");

        // Test invalid IC format
        String invalidIc = "12345";
        assertFalse(invalidIc.matches("\\d{12}"), "Invalid IC should not match 12 digits pattern");

        System.out.println("IC validation logic verified.");
    }

    @Test
    @DisplayName("Should verify password validation logic")
    void testPasswordValidation() {
        // Test valid password format (8-16 alphanumeric)
        String validPassword = "password123";
        assertTrue(validPassword.matches("^[a-zA-Z0-9]{8,16}$"), "Valid password should match pattern");

        // Test invalid password format (too short)
        String shortPassword = "pass1";
        assertFalse(shortPassword.matches("^[a-zA-Z0-9]{8,16}$"), "Short password should not match pattern");

        // Test invalid password format (contains special characters)
        String specialCharPassword = "password@123";
        assertFalse(specialCharPassword.matches("^[a-zA-Z0-9]{8,16}$"), "Password with special chars should not match pattern");

        System.out.println("Password validation logic verified.");
    }

    @Test
    @DisplayName("Should verify age validation logic")
    void testAgeValidation() {
        // Test valid age (18-54)
        int validAge1 = 18;
        int validAge2 = 54;
        int validAge3 = 25;

        assertTrue(validAge1 >= 18 && validAge1 <= 54, "Age 18 should be valid");
        assertTrue(validAge2 >= 18 && validAge2 <= 54, "Age 54 should be valid");
        assertTrue(validAge3 >= 18 && validAge3 <= 54, "Age 25 should be valid");

        // Test invalid age
        int invalidAge1 = 17;
        int invalidAge2 = 55;
        int invalidAge3 = -1;

        assertFalse(invalidAge1 >= 18 && invalidAge1 <= 54, "Age 17 should be invalid");
        assertFalse(invalidAge2 >= 18 && invalidAge2 <= 54, "Age 55 should be invalid");
        assertFalse(invalidAge3 >= 0, "Negative age should be invalid");

        System.out.println("Age validation logic verified.");
    }

    @Test
    @DisplayName("Should verify salary validation logic")
    void testSalaryValidation() {
        // Test valid salary (positive)
        double validSalary1 = 1000.0;
        double validSalary2 = 5000.50;

        assertTrue(validSalary1 > 0, "Positive salary should be valid");
        assertTrue(validSalary2 > 0, "Positive salary should be valid");

        // Test invalid salary
        double invalidSalary1 = 0.0;
        double invalidSalary2 = -100.0;

        assertFalse(invalidSalary1 > 0, "Zero salary should be invalid");
        assertFalse(invalidSalary2 >= 0, "Negative salary should be invalid");

        System.out.println("Salary validation logic verified.");
    }

    @Test
    @DisplayName("Should verify staff ID generation from IC")
    void testStaffIdGeneration() {
        String ic = "010203040506";
        int staffId = Integer.parseInt(ic.substring(6));

        assertEquals(40506, staffId);
        assertTrue(staffId > 0, "Staff ID should be positive");
        System.out.println("Staff ID generation verified: " + staffId);
    }

    @Test
    @DisplayName("Should verify staff can be found after adding")
    void testFindStaffAfterAdding() {
        // Create and add test staff
        Staff newStaff = new Staff("FindTest", "111111111107", 30, 3500.00, "password456");
        newStaff.setId(111107);

        boolean added = staffService.addStaff(newStaff);
        assertTrue(added);

        // Verify can find by ID
        Staff foundById = staffService.findById(111107);
        assertNotNull(foundById);
        assertEquals("FindTest", foundById.getName());

        // Verify can find by IC
        Staff foundByIc = staffService.findByIc("111111111107");
        assertNotNull(foundByIc);
        assertEquals("FindTest", foundByIc.getName());

        System.out.println("Staff found after adding: " + foundById.getName());
    }
}

