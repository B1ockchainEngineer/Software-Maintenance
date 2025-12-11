package test.assignment.controller;

import assignment.controller.StaffController;
import assignment.model.Staff;
import assignment.repo.StaffRepository;
import assignment.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StaffController.
 * Tests staff management operations with real data from staff.txt file.
 * Note: Some methods require user input/UI interaction and are tested for logic only.
 */
@DisplayName("StaffController Tests")
class StaffControllerTest {

    private StaffService staffService;
    private StaffController staffController;

    @BeforeEach
    void setUp() {
        // Use real StaffRepository that loads from staff.txt file
        StaffRepository staffRepository = new StaffRepository();
        staffService = new StaffService(staffRepository);
        staffController = new StaffController(staffService);
    }

    @Test
    @DisplayName("Should initialize StaffController with StaffService")
    void testStaffControllerInitialization() {
        assertNotNull(staffController);
        System.out.println("Initialization successful.");
    }

    @Test
    @DisplayName("Should have access to all staff from file")
    void testGetAllStaff() {
        List<Staff> staff = staffService.getAllStaff();
        assertNotNull(staff);
        assertFalse(staff.isEmpty(), "Should have at least one staff from staff.txt");
        System.out.println("Got " + staff.size() + " staff from staff.txt file.");
    }

    @Test
    @DisplayName("Should be able to add a new staff")
    void testAddStaff() {
        // Get initial count
        int initialCount = staffService.getAllStaff().size();

        // Create new staff with unique IC to avoid conflicts
        Staff newStaff = new Staff("TestStaff", "999999999999", 32, 4500.00, "password999");
        newStaff.setId(999999);
        boolean result = staffService.addStaff(newStaff);

        assertTrue(result);
        assertEquals(initialCount + 1, staffService.getAllStaff().size());
        assertNotNull(staffService.findById(999999));
        System.out.println("Staff added successfully. Total count: " + staffService.getAllStaff().size());

        // Clean up: delete the test staff
        staffService.deleteById(999999);
    }

    @Test
    @DisplayName("Should not add staff with duplicate IC")
    void testAddStaffDuplicateIc() {
        // Get existing staff from file
        List<Staff> existingStaff = staffService.getAllStaff();
        assertFalse(existingStaff.isEmpty(), "Should have at least one staff in file");

        // Use existing IC from first staff in file
        String existingIc = existingStaff.get(0).getIc();
        int initialCount = existingStaff.size();

        Staff duplicateStaff = new Staff("Duplicate", existingIc, 35, 5000.00, "password888");
        duplicateStaff.setId(888888);
        boolean result = staffService.addStaff(duplicateStaff);

        assertFalse(result);
        assertEquals(initialCount, staffService.getAllStaff().size());
        System.out.println("Duplicate IC check passed. Result: " + result);
    }

    @Test
    @DisplayName("Should be able to search staff by ID")
    void testSearchStaffById() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);
        int staffId = firstStaff.getId();

        Staff found = staffService.findById(staffId);
        assertNotNull(found);
        assertEquals(staffId, found.getId());
        System.out.println("Found staff by ID: " + found.getName() + " (ID: " + staffId + ")");
    }

    @Test
    @DisplayName("Should return null for non-existent staff ID")
    void testSearchStaffByIdNotFound() {
        Staff staff = staffService.findById(999999);
        assertNull(staff);
        System.out.println("Staff correctly not found.");
    }

    @Test
    @DisplayName("Should be able to search staff by IC")
    void testSearchStaffByIc() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);
        String staffIc = firstStaff.getIc();

        Staff found = staffService.findByIc(staffIc);
        assertNotNull(found);
        assertEquals(staffIc, found.getIc());
        System.out.println("Found staff by IC: " + found.getName() + " (IC: " + staffIc + ")");
    }

    @Test
    @DisplayName("Should return null for non-existent staff IC")
    void testSearchStaffByIcNotFound() {
        Staff staff = staffService.findByIc("000000000000");
        assertNull(staff);
        System.out.println("Staff correctly not found by IC.");
    }

    @Test
    @DisplayName("Should be able to search staff by name")
    void testSearchStaffByName() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        String searchName = allStaff.get(0).getName().toLowerCase();
        List<Staff> results = staffService.findByName(searchName);

        assertNotNull(results);
        assertFalse(results.isEmpty(), "Should find at least one staff with name: " + searchName);
        System.out.println("Found " + results.size() + " staff by name: " + searchName);
    }

    @Test
    @DisplayName("Should return empty list for non-existent name")
    void testSearchStaffByNameNotFound() {
        List<Staff> results = staffService.findByName("NonExistentName12345XYZ");
        assertNotNull(results);
        assertTrue(results.isEmpty());
        System.out.println("Staff correctly not found by name.");
    }

    @Test
    @DisplayName("Should be able to delete staff by ID")
    void testDeleteStaffById() {
        // First add a test staff to delete
        Staff testStaff = new Staff("DeleteTest", "888888888888", 25, 3000.00, "password123");
        testStaff.setId(888888);
        staffService.addStaff(testStaff);

        int countBefore = staffService.getAllStaff().size();
        boolean result = staffService.deleteById(888888);

        assertTrue(result);
        assertNull(staffService.findById(888888));
        assertEquals(countBefore - 1, staffService.getAllStaff().size());
        System.out.println("Staff deleted. Result: " + result);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent staff by ID")
    void testDeleteStaffByIdNotFound() {
        int countBefore = staffService.getAllStaff().size();
        boolean result = staffService.deleteById(999999);
        assertFalse(result);
        assertEquals(countBefore, staffService.getAllStaff().size());
        System.out.println("Staff deletion failed as expected. Result: " + result);
    }

    @Test
    @DisplayName("Should be able to delete staff by IC")
    void testDeleteStaffByIc() {
        // First add a test staff to delete
        Staff testStaff = new Staff("DeleteTest", "777777777777", 25, 3000.00, "password123");
        testStaff.setId(777777);
        staffService.addStaff(testStaff);

        int countBefore = staffService.getAllStaff().size();
        boolean result = staffService.deleteByIc("777777777777");

        assertTrue(result);
        assertNull(staffService.findByIc("777777777777"));
        assertEquals(countBefore - 1, staffService.getAllStaff().size());
        System.out.println("Staff deleted by IC. Result: " + result);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent staff by IC")
    void testDeleteStaffByIcNotFound() {
        int countBefore = staffService.getAllStaff().size();
        boolean result = staffService.deleteByIc("000000000000");
        assertFalse(result);
        assertEquals(countBefore, staffService.getAllStaff().size());
        System.out.println("Staff deletion by IC failed as expected. Result: " + result);
    }

    @Test
    @DisplayName("Should be able to edit staff details")
    void testEditStaff() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff staff = allStaff.get(0);
        int staffId = staff.getId();
        String originalName = staff.getName();
        int originalAge = staff.getStfAge();
        double originalSalary = staff.getStfSalary();

        // Modify staff details
        staff.setStfName("Updated Name");
        staff.setStfAge(originalAge + 1);
        staff.setStfSalary(originalSalary + 100.00);

        boolean result = staffService.updateStaff(staff);
        assertTrue(result);

        Staff updatedStaff = staffService.findById(staffId);
        assertEquals("UPDATED NAME", updatedStaff.getName());
        assertEquals(originalAge + 1, updatedStaff.getStfAge());
        assertEquals(originalSalary + 100.00, updatedStaff.getStfSalary());
        System.out.println("Staff edited: " + updatedStaff.getName());

        // Restore original values
        staff.setStfName(originalName);
        staff.setStfAge(originalAge);
        staff.setStfSalary(originalSalary);
        staffService.updateStaff(staff);
    }

    @Test
    @DisplayName("Should fail to update when staff not found")
    void testEditStaffNotFound() {
        Staff nonExistent = new Staff("Test", "000000000000", 25, 3000.00, "pass");
        nonExistent.setId(0);

        boolean result = staffService.updateStaff(nonExistent);
        assertFalse(result);
        System.out.println("Staff update failed as expected. Result: " + result);
    }

    @Test
    @DisplayName("Should fail to update when new IC already exists")
    void testEditStaffDuplicateIc() {
        // Get at least 2 staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertTrue(allStaff.size() >= 2, "Should have at least 2 staff in file for this test");

        Staff staff1 = allStaff.get(0);
        Staff staff2 = allStaff.get(1);

        // Try to change staff1's IC to staff2's IC (which already exists)
        Staff updatedStaff = new Staff(staff1.getName(), staff2.getIc(), staff1.getStfAge(), staff1.getStfSalary(), staff1.getStfPassword());
        updatedStaff.setId(staff1.getId());

        boolean result = staffService.updateStaff(updatedStaff);
        assertFalse(result);
        System.out.println("Staff update failed due to duplicate IC. Result: " + result);
    }

    @Test
    @DisplayName("Should successfully login with correct credentials")
    void testLogin() {
        // Find a staff member with plain text password (not hashed) from the file
        // Or create a test staff if all passwords are hashed
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff testStaff = null;
        String testIc = null;
        String testPassword = null;

        // Try to find a staff with plain text password (doesn't contain ':')
        for (Staff staff : allStaff) {
            String storedPassword = staff.getStfPassword();
            // If password doesn't contain ':', it's plain text (legacy)
            if (storedPassword != null && !storedPassword.contains(":")) {
                testStaff = staff;
                testIc = staff.getIc();
                testPassword = storedPassword; // Use the plain text password
                break;
            }
        }

        // If no plain text password found, create a test staff
        if (testStaff == null) {
            testStaff = new Staff("TestLogin", "555555555555", 25, 3000.00, "testpass123");
            testStaff.setId(555555);
            boolean added = staffService.addStaff(testStaff);
            assertTrue(added, "Test staff should be added successfully");
            testIc = "555555555555";
            testPassword = "testpass123"; // Original plain text password
        }

        // Try to login with the plain text password
        Staff loggedIn = staffService.login(testIc, testPassword);

        assertNotNull(loggedIn, "Login should succeed with correct credentials");
        assertEquals(testStaff.getName(), loggedIn.getName());
        assertEquals(testIc, loggedIn.getIc());
        System.out.println("Login successful: " + loggedIn.getName());

        // Clean up: delete test staff if we created one
        if (testIc.equals("555555555555")) {
            staffService.deleteById(555555);
        }
    }

    @Test
    @DisplayName("Should return null for incorrect password")
    void testLoginIncorrectPassword() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        String staffIc = allStaff.get(0).getIc();
        Staff loggedIn = staffService.login(staffIc, "wrongPassword");

        assertNull(loggedIn);
        System.out.println("Login failed with wrong password.");
    }

    @Test
    @DisplayName("Should return null for non-existent IC")
    void testLoginNonExistentIc() {
        Staff loggedIn = staffService.login("000000000000", "password123");

        assertNull(loggedIn);
        System.out.println("Login failed with non-existent IC.");
    }
}
