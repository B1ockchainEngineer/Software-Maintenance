package assignment.service;

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
 * Unit tests for StaffService.
 * Tests business rules using real data from staff.txt file.
 */
@DisplayName("StaffService Tests")
class StaffServiceTest {

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
        List<Staff> currentStaff = staffRepo.loadAllStaff();
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
    @DisplayName("Should return all staff")
    void testGetAllStaff() {
        List<Staff> staff = staffService.getAllStaff();
        assertNotNull(staff);
        assertFalse(staff.isEmpty(), "Should have at least one staff from staff.txt");
        System.out.println("Success. Staff count: " + staff.size());
    }

    @Test
    @DisplayName("Should successfully add a new staff")
    void testAddStaff_Success() {
        // Get initial count
        int initialCount = staffService.getAllStaff().size();

        // Create new staff with unique ID and IC
        Staff newStaff = new Staff("Charlie", "111111111110", 28, 4000.00, "password789");
        newStaff.setId(111110);
        boolean result = staffService.addStaff(newStaff);
        System.out.println("Add result: " + result);

        assertTrue(result);
        assertEquals(initialCount + 1, staffService.getAllStaff().size());
        assertNotNull(staffService.findById(111110));
        assertEquals("Charlie", staffService.findById(111110).getName());
        System.out.println("Staff 111110 verified.");
    }

    @Test
    @DisplayName("Should fail to add staff when IC already exists")
    void testAddStaff_DuplicateIc() {
        // Get existing staff from file
        List<Staff> existingStaff = staffService.getAllStaff();
        assertFalse(existingStaff.isEmpty(), "Should have at least one staff in file");

        // Use existing IC from first staff in file
        String existingIc = existingStaff.get(0).getIc();
        int initialCount = existingStaff.size();

        Staff duplicateStaff = new Staff("Duplicate", existingIc, 35, 5000.00, "password999");
        duplicateStaff.setId(999999);
        boolean result = staffService.addStaff(duplicateStaff);
        System.out.println("Add duplicate result: " + result);

        assertFalse(result);
        assertEquals(initialCount, staffService.getAllStaff().size());
    }

    @Test
    @DisplayName("Should find staff by ID")
    void testFindById() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);
        int staffId = firstStaff.getId();

        Staff found = staffService.findById(staffId);
        assertNotNull(found);
        assertEquals(staffId, found.getId());
        assertEquals(firstStaff.getName(), found.getName());
        System.out.println("Found: " + found.getName());
    }

    @Test
    @DisplayName("Should return null when staff not found by ID")
    void testFindById_NotFound() {
        Staff found = staffService.findById(999999999);
        assertNull(found);
        System.out.println("Correctly returned null.");
    }

    @Test
    @DisplayName("Should find staff by IC")
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
        System.out.println("Found by IC: " + found.getName());
    }

    @Test
    @DisplayName("Should return null when staff not found by IC")
    void testFindByIc_NotFound() {
        Staff found = staffService.findByIc("000000000000");
        assertNull(found);
        System.out.println("Correctly returned null for IC.");
    }

    @Test
    @DisplayName("Should find staff by name (case-insensitive)")
    void testFindByName() {
        // Get first staff from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = allStaff.get(0);
        String staffName = firstStaff.getName();

        List<Staff> results = staffService.findByName(staffName.toLowerCase());
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find at least one staff with name: " + staffName);
        boolean found = results.stream().anyMatch(s -> s.getName().equalsIgnoreCase(staffName));
        assertTrue(found);
        System.out.println("Found by name: " + results.get(0).getName() + " (count: " + results.size() + ")");
    }

    @Test
    @DisplayName("Should return empty list when name not found")
    void testFindByName_NotFound() {
        List<Staff> results = staffService.findByName("NonExistentName12345");
        assertNotNull(results);
        assertTrue(results.isEmpty());
        System.out.println("Correctly returned empty list.");
    }

    @Test
    @DisplayName("Should successfully update staff")
    void testUpdateStaff_Success() {
        // Create test staff to update
        Staff testStaff = new Staff("UpdateTest", "333333333333", 25, 3000.00, "password123");
        testStaff.setId(333333);
        staffService.addStaff(testStaff);

        // Update the staff
        testStaff.setStfName("UpdateTest Updated");
        testStaff.setStfAge(26);
        testStaff.setStfSalary(3200.00);

        boolean result = staffService.updateStaff(testStaff);
        assertTrue(result);

        Staff updated = staffService.findById(333333);
        assertNotNull(updated);
        assertEquals("UPDATETEST UPDATED", updated.getName());
        assertEquals(26, updated.getStfAge());
        assertEquals(3200.00, updated.getStfSalary());
        System.out.println("Updated name: " + updated.getName());
    }

    @Test
    @DisplayName("Should fail to update when staff not found")
    void testUpdateStaff_NotFound() {
        Staff nonExistent = new Staff("Test", "123456789012", 25, 3000.00, "pass");
        nonExistent.setId(123456789);

        boolean result = staffService.updateStaff(nonExistent);
        assertFalse(result);
        System.out.println("Update failed as expected. Result: " + result);
    }

    @Test
    @DisplayName("Should fail to update when new IC already exists")
    void testUpdateStaff_DuplicateIc() {
        // Create test staff to update
        Staff testStaff = new Staff("UpdateTest", "333333333332", 25, 3000.00, "password123");
        testStaff.setId(333332);
        staffService.addStaff(testStaff);

        // Get another staff's IC from file
        List<Staff> allStaff = staffService.getAllStaff();
        assertFalse(allStaff.isEmpty(), "Should have at least one staff in file");

        // Find an IC that's different from our test staff
        String existingIc = null;
        for (Staff s : allStaff) {
            if (!s.getIc().equals("333333333332")) {
                existingIc = s.getIc();
                break;
            }
        }
        assertNotNull(existingIc, "Should find an existing IC to use for duplicate test");

        // Try to change IC to an existing IC
        testStaff.setStfIC(existingIc);

        boolean result = staffService.updateStaff(testStaff);
        assertFalse(result);
        System.out.println("Update failed due to duplicate IC. Result: " + result);
    }

    @Test
    @DisplayName("Should successfully delete staff by ID")
    void testDeleteById_Success() {
        // Create test staff to delete
        Staff testStaff = new Staff("DeleteTest", "555555555555", 25, 3000.00, "password123");
        testStaff.setId(555555);
        staffService.addStaff(testStaff);

        // Verify it exists
        assertNotNull(staffService.findById(555555));

        boolean result = staffService.deleteById(555555);
        assertTrue(result);

        assertNull(staffService.findById(555555));
        System.out.println("Deleted 555555. Result: " + result);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent staff")
    void testDeleteById_NotFound() {
        boolean result = staffService.deleteById(999999999);
        assertFalse(result);
        System.out.println("Deletion failed. Result: " + result);
    }

    @Test
    @DisplayName("Should successfully delete staff by IC")
    void testDeleteByIc_Success() {
        // Create test staff to delete
        Staff testStaff = new Staff("DeleteTest", "444444444444", 25, 3000.00, "password123");
        testStaff.setId(444444);
        staffService.addStaff(testStaff);

        // Verify it exists
        assertTrue(staffService.findByIc("444444444444") != null);

        boolean result = staffService.deleteByIc("444444444444");
        assertTrue(result);

        assertNull(staffService.findByIc("444444444444"));
        System.out.println("Deleted by IC. Result: " + result);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent IC")
    void testDeleteByIc_NotFound() {
        boolean result = staffService.deleteByIc("000000000000");
        assertFalse(result);
        System.out.println("Deletion by IC failed. Result: " + result);
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
            testStaff = new Staff("LoginTest", "222222222221", 25, 3000.00, "password123");
            testStaff.setId(222221);
            staffService.addStaff(testStaff);
            testPassword = "password123";
        }

        Staff loggedIn = staffService.login(testStaff.getIc(), testPassword);

        assertNotNull(loggedIn);
        assertEquals(testStaff.getName(), loggedIn.getName());
        assertEquals(testStaff.getIc(), loggedIn.getIc());
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
        System.out.println("Login failed with wrong password.");
    }

    @Test
    @DisplayName("Should return null for non-existent IC")
    void testLogin_NonExistentIc() {
        Staff loggedIn = staffService.login("000000000000", "password123");

        assertNull(loggedIn);
        System.out.println("Login failed with non-existent IC.");
    }
}
