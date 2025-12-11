package assignment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Staff model class.
 * Tests constructors, getters, setters, and utility methods.
 */
@DisplayName("Staff Model Tests")
class StaffModelTest {

    private Staff staff;

    @BeforeEach
    void setUp() {
        staff = new Staff("John Doe", "123456789012", 30, 5000.00, "password123");
        staff.setId(123456);
    }

    // ================== CONSTRUCTOR TESTS ==================

    @Test
    @DisplayName("Constructor with parameters should initialize correctly")
    void constructor_WithParameters_ShouldInitializeCorrectly() {
        // Given: Staff with all parameters
        Staff s = new Staff("Jane Smith", "987654321098", 25, 4500.00, "pass456");

        // Then: All fields should be set correctly
        // Note: Person constructor doesn't uppercase name, only setName() does
        assertEquals("Jane Smith", s.getName());
        assertEquals("987654321098", s.getIc());
        assertEquals(321098, s.getId()); // ID is extracted from IC (last 6 digits)
        assertEquals(25, s.getStfAge());
        assertEquals(4500.00, s.getStfSalary(), 0.01);
        assertEquals("pass456", s.getStfPassword());
    }

    @Test
    @DisplayName("Default constructor should initialize with defaults")
    void constructor_NoParameters_ShouldInitializeWithDefaults() {
        // Given: Default constructor
        Staff s = new Staff();

        // Then: All fields should be default values
        assertEquals(0, s.getId());
        assertNull(s.getName()); // Person default constructor sets name to null
        assertNull(s.getIc()); // Person default constructor sets ic to null
        assertEquals(0, s.getStfAge());
        assertEquals(0.0, s.getStfSalary(), 0.01);
        assertNull(s.getStfPassword());
        assertNull(s.getClockIn());
        assertNull(s.getClockOut());
    }

    @Test
    @DisplayName("Constructor should extract ID from IC correctly")
    void constructor_ShouldExtractIdFromIc() {
        // Given: Staff with IC ending in specific digits
        Staff s = new Staff("Test", "123456789012", 30, 5000.00, "pass");

        // Then: ID should be extracted from last 6 digits of IC
        assertEquals(789012, s.getId());
    }

    // ================== GETTER TESTS ==================

    @Test
    @DisplayName("getStfIC should return IC")
    void getStfIC_ShouldReturnIc() {
        assertEquals("123456789012", staff.getStfIC());
    }

    @Test
    @DisplayName("getStfAge should return age")
    void getStfAge_ShouldReturnAge() {
        assertEquals(30, staff.getStfAge());
    }

    @Test
    @DisplayName("getStfSalary should return salary")
    void getStfSalary_ShouldReturnSalary() {
        assertEquals(5000.00, staff.getStfSalary(), 0.01);
    }

    @Test
    @DisplayName("getStfPassword should return password")
    void getStfPassword_ShouldReturnPassword() {
        assertEquals("password123", staff.getStfPassword());
    }

    @Test
    @DisplayName("getClockIn should return clock in time")
    void getClockIn_ShouldReturnClockInTime() {
        LocalDateTime now = LocalDateTime.now();
        staff.setClockIn(now);
        assertEquals(now, staff.getClockIn());
    }

    @Test
    @DisplayName("getClockOut should return clock out time")
    void getClockOut_ShouldReturnClockOutTime() {
        LocalDateTime now = LocalDateTime.now();
        staff.setClockOut(now);
        assertEquals(now, staff.getClockOut());
    }

    // ================== SETTER TESTS ==================

    @Test
    @DisplayName("setStfName should update name and uppercase it")
    void setStfName_ShouldUpdateNameAndUppercase() {
        staff.setStfName("New Name");
        assertEquals("NEW NAME", staff.getName());
    }

    @Test
    @DisplayName("setStfIC should update IC")
    void setStfIC_ShouldUpdateIc() {
        staff.setStfIC("999999999999");
        assertEquals("999999999999", staff.getIc());
        assertEquals("999999999999", staff.getStfIC());
    }

    @Test
    @DisplayName("setStfId should update ID")
    void setStfId_ShouldUpdateId() {
        staff.setStfId(999999);
        assertEquals(999999, staff.getId());
    }

    @Test
    @DisplayName("setStfAge should update age")
    void setStfAge_ShouldUpdateAge() {
        staff.setStfAge(35);
        assertEquals(35, staff.getStfAge());
    }

    @Test
    @DisplayName("setStfSalary should update salary")
    void setStfSalary_ShouldUpdateSalary() {
        staff.setStfSalary(6000.00);
        assertEquals(6000.00, staff.getStfSalary(), 0.01);
    }

    @Test
    @DisplayName("setStfPassword should update password")
    void setStfPassword_ShouldUpdatePassword() {
        staff.setStfPassword("newpassword");
        assertEquals("newpassword", staff.getStfPassword());
    }

    @Test
    @DisplayName("setClockIn should update clock in time")
    void setClockIn_ShouldUpdateClockInTime() {
        LocalDateTime clockIn = LocalDateTime.of(2024, 1, 1, 9, 0);
        staff.setClockIn(clockIn);
        assertEquals(clockIn, staff.getClockIn());
    }

    @Test
    @DisplayName("setClockOut should update clock out time")
    void setClockOut_ShouldUpdateClockOutTime() {
        LocalDateTime clockOut = LocalDateTime.of(2024, 1, 1, 17, 0);
        staff.setClockOut(clockOut);
        assertEquals(clockOut, staff.getClockOut());
    }

    // ================== INHERITANCE TESTS ==================

    @Test
    @DisplayName("Staff should inherit from Person")
    void staff_ShouldInheritFromPerson() {
        assertTrue(staff instanceof Person);
    }

    @Test
    @DisplayName("Should access Person methods")
    void shouldAccessPersonMethods() {
        // Note: Constructor doesn't uppercase name, only setName() does
        assertEquals("John Doe", staff.getName());
        assertEquals("123456789012", staff.getIc());
        assertEquals(123456, staff.getId());
    }

    // ================== UNSUPPORTED OPERATION TESTS ==================

    @Test
    @DisplayName("add() should throw UnsupportedOperationException")
    void add_ShouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> staff.add());
    }

    @Test
    @DisplayName("delete() should throw UnsupportedOperationException")
    void delete_ShouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> staff.delete());
    }

    @Test
    @DisplayName("view() should throw UnsupportedOperationException")
    void view_ShouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> staff.view());
    }

    @Test
    @DisplayName("search() should throw UnsupportedOperationException")
    void search_ShouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> staff.search());
    }

    // ================== toString TESTS ==================

    @Test
    @DisplayName("toString should format correctly")
    void toString_ShouldFormatCorrectly() {
        // Given: Staff with known values
        Staff s = new Staff("Test Staff", "111111111111", 28, 5500.00, "testpass");
        s.setId(111111);

        // When: Converting to string
        String result = s.toString();

        // Then: Should contain all staff information
        // Note: Constructor doesn't uppercase name, so getName() returns original case
        assertTrue(result.contains("STAFF ID >> S-111111"));
        assertTrue(result.contains("STAFF NAME: Test Staff"));
        assertTrue(result.contains("STAFF IC: 111111111111"));
        assertTrue(result.contains("STAFF AGE: 28"));
        assertTrue(result.contains("STAFF SALARY: RM 5500.00"));
    }

    @Test
    @DisplayName("toString should format salary with two decimals")
    void toString_ShouldFormatSalaryWithTwoDecimals() {
        Staff s = new Staff("Test", "222222222222", 30, 1234.5, "pass");
        s.setId(222222);
        String result = s.toString();
        assertTrue(result.contains("RM 1234.50")); // Should format to 2 decimals
    }

    @Test
    @DisplayName("toString should format zero salary correctly")
    void toString_WithZeroSalary_ShouldDisplayZero() {
        Staff s = new Staff("Test", "333333333333", 25, 0.0, "pass");
        s.setId(333333);
        String result = s.toString();
        assertTrue(result.contains("RM 0.00"));
    }

    // ================== INTEGRATION TESTS ==================

    @Test
    @DisplayName("Should handle complete staff lifecycle")
    void shouldHandleCompleteStaffLifecycle() {
        // Create staff
        Staff s = new Staff("Lifecycle Test", "444444444444", 32, 6000.00, "lifecycle");
        s.setId(444444);

        // Verify initial state
        // Note: Constructor doesn't uppercase name, only setName() does
        assertEquals("Lifecycle Test", s.getName());
        assertEquals(32, s.getStfAge());
        assertEquals(6000.00, s.getStfSalary(), 0.01);

        // Update information
        s.setStfName("Updated Name");
        s.setStfAge(33);
        s.setStfSalary(6500.00);
        s.setStfPassword("newpassword");

        // Verify updates
        assertEquals("UPDATED NAME", s.getName());
        assertEquals(33, s.getStfAge());
        assertEquals(6500.00, s.getStfSalary(), 0.01);
        assertEquals("newpassword", s.getStfPassword());

        // Set clock in/out
        LocalDateTime clockIn = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime clockOut = LocalDateTime.of(2024, 1, 15, 17, 30);
        s.setClockIn(clockIn);
        s.setClockOut(clockOut);

        // Verify clock times
        assertEquals(clockIn, s.getClockIn());
        assertEquals(clockOut, s.getClockOut());
    }
}

