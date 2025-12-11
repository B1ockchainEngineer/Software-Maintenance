package assignment.view;

import assignment.model.Staff;
import assignment.util.config.AppConfig;
import assignment.util.config.StaffConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StaffView class.
 * Tests all display methods by capturing System.out output.
 */
@DisplayName("Staff View Tests")
class StaffViewTest {

    private StaffView staffView;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        staffView = new StaffView();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String getOutput() {
        return outputStream.toString();
    }

    // ================== printStaffMenu TESTS ==================

    @Test
    @DisplayName("Should display staff menu with total staff count")
    void printStaffMenu_ShouldDisplayMenu() {
        // When: Printing staff menu
        staffView.printStaffMenu(5);

        // Then: Should display title, total staff, and menu options
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_STAFF_SYSTEM));
        assertTrue(output.contains("Total Staff: 5"));
        assertTrue(output.contains(StaffConfig.MSG_PLEASE_SELECT_OPTION));
        assertTrue(output.contains(AppConfig.SEPARATOR_LINE));
    }

    @Test
    @DisplayName("Should display staff menu with zero staff")
    void printStaffMenu_WithZeroStaff_ShouldDisplayZero() {
        // When: Printing staff menu with zero staff
        staffView.printStaffMenu(0);

        // Then: Should display zero total staff
        String output = getOutput();
        assertTrue(output.contains("Total Staff: 0"));
    }

    // ================== printAddStaffHeader TESTS ==================

    @Test
    @DisplayName("Should display add staff header")
    void printAddStaffHeader_ShouldDisplayHeader() {
        // When: Printing add staff header
        staffView.printAddStaffHeader(3);

        // Then: Should display title and current count
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_ADD_STAFF));
        assertTrue(output.contains(StaffConfig.MSG_FILL_INFORMATION));
        assertTrue(output.contains(StaffConfig.MSG_PRESS_E_TO_CANCEL));
        assertTrue(output.contains("Current Staff Count: 3"));
    }

    // ================== printNewStaffSummary TESTS ==================

    @Test
    @DisplayName("Should display new staff summary")
    void printNewStaffSummary_ShouldDisplaySummary() {
        // Given: A new staff member
        Staff newStaff = new Staff("John Doe", "010203040506", 25, 3000.00, "password123");
        newStaff.setId(12345);

        // When: Printing new staff summary
        staffView.printNewStaffSummary(newStaff);

        // Then: Should display all staff details
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.MSG_SUMMARY_REVIEW));
        assertTrue(output.contains("S-12345"));
        assertTrue(output.contains("John Doe"));
        assertTrue(output.contains("010203040506"));
        assertTrue(output.contains("25 years"));
        assertTrue(output.contains("RM 3,000.00"));
    }

    // ================== printStaffAddedSuccess TESTS ==================

    @Test
    @DisplayName("Should display staff added success message")
    void printStaffAddedSuccess_ShouldDisplaySuccess() {
        // Given: A new staff member
        Staff newStaff = new Staff("Jane Smith", "020304050607", 30, 4000.00, "password456");
        newStaff.setId(12346);

        // When: Printing success message
        staffView.printStaffAddedSuccess(newStaff, 6);

        // Then: Should display success message and new count
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.SuccessfulMessage.STAFF_ADDED));
        assertTrue(output.contains("S-12346"));
        assertTrue(output.contains("Jane Smith"));
        assertTrue(output.contains("New Staff Count: 6"));
    }

    // ================== printUpdateStaffMenu TESTS ==================

    @Test
    @DisplayName("Should display update staff menu")
    void printUpdateStaffMenu_ShouldDisplayMenu() {
        // When: Printing update staff menu
        staffView.printUpdateStaffMenu();

        // Then: Should display title and find options
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_UPDATE_STAFF));
        assertTrue(output.contains(StaffConfig.MSG_FIND_STAFF_BY));
        assertTrue(output.contains(StaffConfig.MSG_FIND_BY_NAME));
        assertTrue(output.contains(StaffConfig.MSG_FIND_BY_IC));
    }

    // ================== displayStaffDetails TESTS ==================

    @Test
    @DisplayName("Should display staff details")
    void displayStaffDetails_ShouldDisplayDetails() {
        // Given: A staff member
        Staff staff = new Staff("Test Staff", "030405060708", 28, 3500.00, "password789");
        staff.setId(12347);

        // When: Displaying staff details
        staffView.displayStaffDetails(staff);

        // Then: Should display staff information
        String output = getOutput();
        assertTrue(output.contains("Test Staff") || output.contains("TEST STAFF"));
    }

    // ================== printDeleteStaffMenu TESTS ==================

    @Test
    @DisplayName("Should display delete staff menu with staff list")
    void printDeleteStaffMenu_WithStaffList_ShouldDisplayMenu() {
        // Given: A list of staff members
        List<Staff> staffList = new ArrayList<>();
        staffList.add(new Staff("Staff One", "010101010101", 25, 2500.00, "pass1"));
        staffList.get(0).setId(1001);
        staffList.add(new Staff("Staff Two", "020202020202", 30, 3000.00, "pass2"));
        staffList.get(1).setId(1002);

        // When: Printing delete staff menu
        staffView.printDeleteStaffMenu(staffList);

        // Then: Should display title, warning, and staff list
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_DELETE_STAFF));
        assertTrue(output.contains(StaffConfig.WARNING_DELETE_IRREVERSIBLE));
        assertTrue(output.contains(StaffConfig.MSG_CURRENT_STAFF_LIST));
        assertTrue(output.contains("S-1001"));
        assertTrue(output.contains("S-1002"));
        assertTrue(output.contains("TOTAL STAFF: 2"));
    }

    @Test
    @DisplayName("Should display delete staff menu with empty list")
    void printDeleteStaffMenu_WithEmptyList_ShouldDisplayMenu() {
        // Given: Empty staff list
        List<Staff> emptyList = new ArrayList<>();

        // When: Printing delete staff menu
        staffView.printDeleteStaffMenu(emptyList);

        // Then: Should display menu with zero count
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_DELETE_STAFF));
        assertTrue(output.contains("TOTAL STAFF: 0"));
    }

    // ================== printDeleteConfirmation TESTS ==================

    @Test
    @DisplayName("Should display delete confirmation")
    void printDeleteConfirmation_ShouldDisplayConfirmation() {
        // Given: A staff member to delete
        Staff staffToDelete = new Staff("Delete Me", "040405050606", 35, 5000.00, "pass123");
        staffToDelete.setId(2001);

        // When: Printing delete confirmation
        staffView.printDeleteConfirmation(staffToDelete);

        // Then: Should display confirmation with staff details
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_DELETE_STAFF_CONFIRMATION));
        assertTrue(output.contains(StaffConfig.WARNING_DELETE_IRREVERSIBLE));
        assertTrue(output.contains(StaffConfig.MSG_STAFF_TO_BE_DELETED));
    }

    // ================== printSearchStaffMenu TESTS ==================

    @Test
    @DisplayName("Should display search staff menu with quick reference")
    void printSearchStaffMenu_WithQuickReference_ShouldDisplayMenu() {
        // Given: A small list of staff (<= 10)
        List<Staff> quickList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Staff staff = new Staff("Staff " + i, String.format("%012d", i), 25 + i, 2000.00 + i * 100, "pass" + i);
            staff.setId(3000 + i);
            quickList.add(staff);
        }

        // When: Printing search staff menu
        staffView.printSearchStaffMenu(quickList);

        // Then: Should display quick reference table
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_SEARCH_STAFF));
        assertTrue(output.contains(StaffConfig.MSG_QUICK_REFERENCE));
        assertTrue(output.contains(StaffConfig.MSG_SEARCH_BY));
    }

    @Test
    @DisplayName("Should display search staff menu without quick reference when list is large")
    void printSearchStaffMenu_WithLargeList_ShouldNotDisplayQuickReference() {
        // Given: A large list of staff (> 10)
        List<Staff> largeList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Staff staff = new Staff("Staff " + i, String.format("%012d", i), 25, 2000.00, "pass" + i);
            staff.setId(4000 + i);
            largeList.add(staff);
        }

        // When: Printing search staff menu
        staffView.printSearchStaffMenu(largeList);

        // Then: Should not display quick reference
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_SEARCH_STAFF));
        assertFalse(output.contains(StaffConfig.MSG_QUICK_REFERENCE));
    }

    @Test
    @DisplayName("Should display search staff menu with empty list")
    void printSearchStaffMenu_WithEmptyList_ShouldDisplayMenu() {
        // Given: Empty staff list
        List<Staff> emptyList = new ArrayList<>();

        // When: Printing search staff menu
        staffView.printSearchStaffMenu(emptyList);

        // Then: Should display menu without quick reference
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_SEARCH_STAFF));
        assertTrue(output.contains(StaffConfig.MSG_SEARCH_BY));
    }

    // ================== displaySearchResults TESTS ==================

    @Test
    @DisplayName("Should display search results with found staff")
    void displaySearchResults_WithResults_ShouldDisplayResults() {
        // Given: Search results
        List<Staff> results = new ArrayList<>();
        Staff staff1 = new Staff("Found One", "050505050505", 25, 2500.00, "pass1");
        staff1.setId(5001);
        Staff staff2 = new Staff("Found Two", "060606060606", 30, 3000.00, "pass2");
        staff2.setId(5002);
        results.add(staff1);
        results.add(staff2);

        // When: Displaying search results
        staffView.displaySearchResults(results, "NAME");

        // Then: Should display results with details
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_SEARCH_RESULTS));
        assertTrue(output.contains("Found: 2 staff member(s)"));
        assertTrue(output.contains("[1]"));
        assertTrue(output.contains("[2]"));
    }

    @Test
    @DisplayName("Should display no results message when search returns empty")
    void displaySearchResults_WithNoResults_ShouldDisplayNoResults() {
        // Given: Empty search results
        List<Staff> emptyResults = new ArrayList<>();

        // When: Displaying search results
        staffView.displaySearchResults(emptyResults, "ID");

        // Then: Should display no results message and tips
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_SEARCH_RESULTS));
        assertTrue(output.contains(String.format(StaffConfig.ErrorMessage.NO_STAFF_FOUND, "ID")));
        assertTrue(output.contains(StaffConfig.ErrorMessage.TIP_SEARCH_DIFFERENT));
        assertTrue(output.contains(StaffConfig.ErrorMessage.TIP_DIFFERENT_SPELLING));
    }

    // ================== displayStaffList TESTS ==================

    @Test
    @DisplayName("Should display staff list with statistics")
    void displayStaffList_WithStaff_ShouldDisplayListAndStats() {
        // Given: A list of staff members
        List<Staff> staffList = new ArrayList<>();
        staffList.add(new Staff("Staff A", "010101010101", 25, 2500.00, "pass1"));
        staffList.get(0).setId(6001);
        staffList.add(new Staff("Staff B", "020202020202", 30, 3000.00, "pass2"));
        staffList.get(1).setId(6002);
        staffList.add(new Staff("Staff C", "030303030303", 35, 3500.00, "pass3"));
        staffList.get(2).setId(6003);

        // When: Displaying staff list
        staffView.displayStaffList(staffList);

        // Then: Should display statistics and staff list
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_VIEW_STAFF));
        assertTrue(output.contains(StaffConfig.MSG_STATISTICS));
        assertTrue(output.contains("Total Staff Members: 3"));
        assertTrue(output.contains("Average Age:"));
        assertTrue(output.contains("Highest Salary:"));
        assertTrue(output.contains("Lowest Salary:"));
        assertTrue(output.contains("Total Payroll:"));
        assertTrue(output.contains("S-6001"));
        assertTrue(output.contains("S-6002"));
        assertTrue(output.contains("S-6003"));
    }

    @Test
    @DisplayName("Should display empty message when staff list is empty")
    void displayStaffList_WithEmptyList_ShouldDisplayEmptyMessage() {
        // Given: Empty staff list
        List<Staff> emptyList = new ArrayList<>();

        // When: Displaying staff list
        staffView.displayStaffList(emptyList);

        // Then: Should display empty message and tip
        String output = getOutput();
        assertTrue(output.contains(StaffConfig.TITLE_VIEW_STAFF));
        assertTrue(output.contains(StaffConfig.ErrorMessage.NO_STAFF_TO_DISPLAY));
        assertTrue(output.contains(StaffConfig.ErrorMessage.TIP_ADD_STAFF));
    }

    @Test
    @DisplayName("Should calculate statistics correctly")
    void displayStaffList_ShouldCalculateStatisticsCorrectly() {
        // Given: Staff with known values
        List<Staff> staffList = new ArrayList<>();
        staffList.add(new Staff("Staff 1", "010101010101", 20, 2000.00, "pass1"));
        staffList.get(0).setId(7001);
        staffList.add(new Staff("Staff 2", "020202020202", 30, 4000.00, "pass2"));
        staffList.get(1).setId(7002);
        staffList.add(new Staff("Staff 3", "030303030303", 40, 3000.00, "pass3"));
        staffList.get(2).setId(7003);

        // When: Displaying staff list
        staffView.displayStaffList(staffList);

        // Then: Should display correct statistics
        String output = getOutput();
        assertTrue(output.contains("Total Staff Members: 3"));
        assertTrue(output.contains("Average Age: 30.0 years")); // (20+30+40)/3 = 30
        assertTrue(output.contains("Highest Salary: RM 4000.00"));
        assertTrue(output.contains("Lowest Salary: RM 2000.00"));
        assertTrue(output.contains("Total Payroll: RM 9000.00")); // 2000+4000+3000
    }
}
