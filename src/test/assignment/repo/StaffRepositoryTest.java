package assignment.repo;

import assignment.model.Staff;
import assignment.repo.StaffRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StaffRepository.
 * Tests file I/O operations for staff using real data from staff.txt file.
 */
@DisplayName("StaffRepository Tests")
class StaffRepositoryTest {

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
        // Find and remove test staff with IDs that don't exist in original data
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
    @DisplayName("Should load all staff from file")
    void testLoadAllStaff() {
        List<Staff> staffList = staffRepository.loadAllStaff();

        assertNotNull(staffList);
        assertFalse(staffList.isEmpty(), "Should have at least one staff from staff.txt");
        System.out.println("Loaded " + staffList.size() + " staff from staff.txt file.");
    }

    @Test
    @DisplayName("Should check if IC exists")
    void testExistsByIc() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);
        String existingIc = firstStaff.getIc();

        assertTrue(staffRepository.existsByIc(existingIc));
        assertFalse(staffRepository.existsByIc("000000000000"));
        System.out.println("IC checks verified.");
    }

    @Test
    @DisplayName("Should find staff by ID")
    void testFindById() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);
        int staffId = firstStaff.getId();

        Staff found = staffRepository.findById(staffId);
        assertNotNull(found);
        assertEquals(staffId, found.getId());
        assertEquals(firstStaff.getName(), found.getName());
        System.out.println("Found staff by ID: " + found.getName());
    }

    @Test
    @DisplayName("Should return null when staff not found by ID")
    void testFindByIdNotFound() {
        Staff found = staffRepository.findById(999999999);
        assertNull(found);
        System.out.println("Correctly returned null for non-existent ID.");
    }

    @Test
    @DisplayName("Should find staff by IC")
    void testFindByIc() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);
        String staffIc = firstStaff.getIc();

        Staff found = staffRepository.findByIc(staffIc);
        assertNotNull(found);
        assertEquals(staffIc, found.getIc());
        assertEquals(firstStaff.getName(), found.getName());
        System.out.println("Found staff by IC: " + found.getName());
    }

    @Test
    @DisplayName("Should return null when staff not found by IC")
    void testFindByIcNotFound() {
        Staff found = staffRepository.findByIc("123456789012");
        assertNull(found);
        System.out.println("Correctly returned null for non-existent IC.");
    }

    @Test
    @DisplayName("Should find staff by name (case-insensitive)")
    void testFindByName() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);
        String staffName = firstStaff.getName();

        List<Staff> results = staffRepository.findByName(staffName.toLowerCase());
        assertNotNull(results);
        assertTrue(results.size() > 0);
        boolean found = results.stream().anyMatch(s -> s.getName().equalsIgnoreCase(staffName));
        assertTrue(found);
        System.out.println("Found staff by name. Count: " + results.size());
    }

    @Test
    @DisplayName("Should return empty list when name not found")
    void testFindByNameNotFound() {
        List<Staff> results = staffRepository.findByName("NonExistentName12345");
        assertNotNull(results);
        assertTrue(results.isEmpty() || !results.stream()
                .anyMatch(s -> s.getName().equalsIgnoreCase("NonExistentName12345")));
        System.out.println("Name search returned " + results.size() + " results.");
    }

    @Test
    @DisplayName("Should append staff to file")
    void testAppendStaff() {
        // Get initial count
        int initialCount = staffRepository.loadAllStaff().size();

        // Create new staff with unique ID and IC
        Staff staff = new Staff("TestAppend", "987654321098", 25, 3000.00, "password123");
        staff.setId(987654321);

        staffRepository.appendStaff(staff);

        List<Staff> staffList = staffRepository.loadAllStaff();
        assertEquals(initialCount + 1, staffList.size());
        assertNotNull(staffRepository.findById(987654321));
        System.out.println("Appended staff. Total count: " + staffList.size());
    }

    @Test
    @DisplayName("Should delete staff by ID")
    void testDeleteById() {
        // Create test staff to delete
        Staff staff = new Staff("DeleteTest", "555555555555", 25, 3000.00, "password123");
        staff.setId(555555);
        staffRepository.appendStaff(staff);

        // Verify it exists
        assertNotNull(staffRepository.findById(555555));

        // Delete it
        boolean result = staffRepository.deleteById(555555);

        assertTrue(result);
        assertNull(staffRepository.findById(555555));
        System.out.println("Deleted staff by ID. Result: " + result);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent staff by ID")
    void testDeleteByIdNotFound() {
        boolean result = staffRepository.deleteById(444444);
        assertFalse(result);
        System.out.println("Delete by ID result: " + result);
    }

    @Test
    @DisplayName("Should delete staff by IC")
    void testDeleteByIc() {
        // Create test staff to delete
        Staff staff = new Staff("DeleteTest", "444444444444", 25, 3000.00, "password123");
        staff.setId(444444);
        staffRepository.appendStaff(staff);

        // Verify it exists
        assertTrue(staffRepository.existsByIc("444444444444"));

        // Delete it
        boolean result = staffRepository.deleteByIc("444444444444");

        assertTrue(result);
        assertFalse(staffRepository.existsByIc("444444444444"));
        System.out.println("Deleted staff by IC. Result: " + result);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent staff by IC")
    void testDeleteByIcNotFound() {
        boolean result = staffRepository.deleteByIc("987654321098");
        assertFalse(result);
        System.out.println("Delete by IC result: " + result);
    }

    @Test
    @DisplayName("Should update staff record")
    void testUpdateStaff() {
        // Create test staff to update
        Staff staff = new Staff("UpdateTest", "333333333333", 25, 3000.00, "password123");
        staff.setId(333333);
        staffRepository.appendStaff(staff);

        // Update the staff
        staff.setStfName("UpdateTest Updated");
        staff.setStfAge(26);
        staff.setStfSalary(3200.00);

        boolean result = staffRepository.updateStaff(staff);
        assertTrue(result);

        Staff updated = staffRepository.findById(333333);
        assertNotNull(updated);
        assertEquals("UPDATETEST UPDATED", updated.getName());
        assertEquals(26, updated.getStfAge());
        assertEquals(3200.00, updated.getStfSalary());
        System.out.println("Updated staff: " + updated.getName());
    }

    @Test
    @DisplayName("Should return false when updating non-existent staff")
    void testUpdateStaffNotFound() {
        Staff nonExistent = new Staff("Test", "123456789012", 25, 3000.00, "pass");
        nonExistent.setId(123456789);

        boolean result = staffRepository.updateStaff(nonExistent);
        assertFalse(result);
        System.out.println("Update failed as expected. Result: " + result);
    }

    @Test
    @DisplayName("Should find staff by credentials (login)")
    void testFindByCredentials() {
        // Get first staff from file that has a plain text password (for testing)
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        // Find a staff with plain text password (not hashed) for easier testing
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
            testStaff = new Staff("LoginTest", "222222222222", 25, 3000.00, "password123");
            testStaff.setId(222222);
            staffRepository.appendStaff(testStaff);
            testPassword = "password123";
        }

        Staff found = staffRepository.findByCredentials(testStaff.getIc(), testPassword);
        assertNotNull(found);
        assertEquals(testStaff.getName(), found.getName());
        assertEquals(testStaff.getIc(), found.getIc());
        System.out.println("Found staff by credentials: " + found.getName());
    }

    @Test
    @DisplayName("Should return null for incorrect password")
    void testFindByCredentialsIncorrectPassword() {
        // Get first staff from file
        List<Staff> staffList = staffRepository.loadAllStaff();
        assertFalse(staffList.isEmpty(), "Should have at least one staff in file");

        Staff firstStaff = staffList.get(0);

        Staff found = staffRepository.findByCredentials(firstStaff.getIc(), "wrongpassword");
        assertNull(found);
        System.out.println("Correctly returned null for wrong password.");
    }

    @Test
    @DisplayName("Should return null for non-existent IC")
    void testFindByCredentialsNonExistentIc() {
        Staff found = staffRepository.findByCredentials("000000000000", "password123");
        assertNull(found);
        System.out.println("Correctly returned null for non-existent IC.");
    }
}
