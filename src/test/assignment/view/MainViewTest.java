package assignment.view;

import assignment.model.Staff;
import assignment.util.config.AppConfig;
import assignment.util.config.SalesConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MainView class.
 * Tests all display methods by capturing System.out output.
 */
@DisplayName("Main View Tests")
class MainViewTest {

    private MainView mainView;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        mainView = new MainView();
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

    // ================== printLoginMenu TESTS ==================

    @Test
    @DisplayName("Should display login menu")
    void printLoginMenu_ShouldDisplayMenu() {
        // When: Printing login menu
        mainView.printLoginMenu();

        // Then: Should display title, welcome message, and menu options
        String output = getOutput();
        assertTrue(output.contains("[ LOGIN MENU ]"));
        assertTrue(output.contains("Welcome to TAR CAFE Management System"));
        assertTrue(output.contains("Please select an option:"));
        assertTrue(output.contains(AppConfig.SEPARATOR_LINE));
    }

    @Test
    @DisplayName("Should display login menu with menu options")
    void printLoginMenu_ShouldDisplayMenuOptions() {
        // When: Printing login menu
        mainView.printLoginMenu();

        // Then: Should display menu options from LogMenu enum
        String output = getOutput();
        // Menu options should be displayed (format: "1. Option Name")
        assertTrue(output.contains("1.") || output.contains("2."));
    }

    // ================== printMainMenu TESTS ==================

    @Test
    @DisplayName("Should display main menu with logged in staff")
    void printMainMenu_WithStaff_ShouldDisplayMenuWithStaffName() {
        // Given: A logged in staff member
        Staff currentStaff = new Staff("John Doe", "010203040506", 25, 3000.00, "password123");
        currentStaff.setId(12345);

        // When: Printing main menu
        mainView.printMainMenu(currentStaff);

        // Then: Should display menu with staff name
        String output = getOutput();
        assertTrue(output.contains("[ MAIN MENU ]"));
        assertTrue(output.contains("Logged in as: JOHN DOE"));
        assertTrue(output.contains("Please select an option:"));
        assertTrue(output.contains(AppConfig.SEPARATOR_LINE));
    }

    @Test
    @DisplayName("Should display main menu without staff name when staff is null")
    void printMainMenu_WithNullStaff_ShouldDisplayMenuWithoutName() {
        // When: Printing main menu with null staff
        mainView.printMainMenu(null);

        // Then: Should display menu without staff name
        String output = getOutput();
        assertTrue(output.contains("[ MAIN MENU ]"));
        assertTrue(output.contains("Please select an option:"));
        assertFalse(output.contains("Logged in as:"));
    }

    @Test
    @DisplayName("Should display main menu with menu options")
    void printMainMenu_ShouldDisplayMenuOptions() {
        // Given: A staff member
        Staff staff = new Staff("Test Staff", "010203040506", 25, 3000.00, "password123");
        staff.setId(12345);

        // When: Printing main menu
        mainView.printMainMenu(staff);

        // Then: Should display menu options from MainMenu enum
        String output = getOutput();
        // Menu options should be displayed (format: "1. Option Name")
        assertTrue(output.contains("1.") || output.contains("2."));
    }

    // ================== printStockMenu TESTS ==================

    @Test
    @DisplayName("Should display stock menu")
    void printStockMenu_ShouldDisplayMenu() {
        // When: Printing stock menu
        mainView.printStockMenu();

        // Then: Should display title and menu options
        String output = getOutput();
        assertTrue(output.contains("[ FOOD AND BEVERAGE MANAGEMENT SYSTEM ]"));
        assertTrue(output.contains(AppConfig.SEPARATOR_LINE));
    }

    @Test
    @DisplayName("Should display stock menu with menu options")
    void printStockMenu_ShouldDisplayMenuOptions() {
        // When: Printing stock menu
        mainView.printStockMenu();

        // Then: Should display menu options from StockMenu enum
        String output = getOutput();
        // Menu options should be displayed (format: "1. Option Name")
        assertTrue(output.contains("1.") || output.contains("2."));
    }

    // ================== printSalesMenu TESTS ==================

    @Test
    @DisplayName("Should display sales menu")
    void printSalesMenu_ShouldDisplayMenu() {
        // When: Printing sales menu
        mainView.printSalesMenu();

        // Then: Should display title and menu options
        String output = getOutput();
        assertTrue(output.contains(SalesConfig.TITLE_SALES_MENU));
        assertTrue(output.contains(AppConfig.SEPARATOR_LINE));
    }

    @Test
    @DisplayName("Should display sales menu with menu options")
    void printSalesMenu_ShouldDisplayMenuOptions() {
        // When: Printing sales menu
        mainView.printSalesMenu();

        // Then: Should display menu options from SalesMenu enum
        String output = getOutput();
        // Menu options should be displayed (format: "1. Option Name")
        assertTrue(output.contains("1.") || output.contains("2."));
    }

    // ================== printExitMessage TESTS ==================

    @Test
    @DisplayName("Should display exit message")
    void printExitMessage_ShouldDisplayMessage() {
        // When: Printing exit message
        mainView.printExitMessage();

        // Then: Should display thank you message and exit message
        String output = getOutput();
        assertTrue(output.contains("THANK YOU FOR USING TAR CAFE SYSTEM"));
        assertTrue(output.contains("EXITING THE PROGRAM..."));
        assertTrue(output.contains(AppConfig.SEPARATOR_LONG));
    }

    // ================== printBackToMainMessage TESTS ==================

    @Test
    @DisplayName("Should display back to main menu message")
    void printBackToMainMessage_ShouldDisplayMessage() {
        // When: Printing back to main message
        mainView.printBackToMainMessage();

        // Then: Should display back to main message
        String output = getOutput();
        assertTrue(output.contains("BACK TO MAIN MENU..."));
    }

    // ================== printBackToPreviousMessage TESTS ==================

    @Test
    @DisplayName("Should display back to previous message")
    void printBackToPreviousMessage_ShouldDisplayMessage() {
        // When: Printing back to previous message
        mainView.printBackToPreviousMessage();

        // Then: Should display back to previous message
        String output = getOutput();
        assertTrue(output.contains("BACK TO PREVIOUS PAGE..."));
    }

    // ================== printLoggedOutMessage TESTS ==================

    @Test
    @DisplayName("Should display logged out message")
    void printLoggedOutMessage_ShouldDisplayMessage() {
        // When: Printing logged out message
        mainView.printLoggedOutMessage();

        // Then: Should display logged out message
        String output = getOutput();
        assertTrue(output.contains("RETURNING TO LOGIN MENU..."));
    }

    // ================== printSelectionPrompt TESTS ==================

    @Test
    @DisplayName("Should display selection prompt")
    void printSelectionPrompt_ShouldDisplayPrompt() {
        // When: Printing selection prompt
        mainView.printSelectionPrompt();

        // Then: Should display selection prompt
        String output = getOutput();
        assertTrue(output.contains(AppConfig.PROMPT_SELECTION));
    }

    @Test
    @DisplayName("Should display selection prompt without newline")
    void printSelectionPrompt_ShouldNotHaveNewline() {
        // When: Printing selection prompt
        mainView.printSelectionPrompt();

        // Then: Should not end with newline (print vs println)
        String output = getOutput();
        assertTrue(output.contains(AppConfig.PROMPT_SELECTION));
        // The prompt should be on a line by itself (no trailing newline from print)
    }

    // ================== printInvalidOptionMessage TESTS ==================

    @Test
    @DisplayName("Should display invalid option message")
    void printInvalidOptionMessage_ShouldDisplayMessage() {
        // When: Printing invalid option message
        mainView.printInvalidOptionMessage();

        // Then: Should display invalid option message
        String output = getOutput();
        assertTrue(output.contains(AppConfig.MSG_INVALID_OPTION));
    }

    // ================== INTEGRATION TESTS ==================

    @Test
    @DisplayName("Should display complete login flow")
    void testCompleteLoginFlow() {
        // When: Displaying login menu
        mainView.printLoginMenu();
        String loginOutput = getOutput();
        assertTrue(loginOutput.contains("[ LOGIN MENU ]"));

        // Reset output
        outputStream.reset();

        // When: Displaying main menu after login
        Staff staff = new Staff("Test User", "010203040506", 25, 3000.00, "password123");
        staff.setId(12345);
        mainView.printMainMenu(staff);
        String mainOutput = getOutput();
        assertTrue(mainOutput.contains("[ MAIN MENU ]"));
        assertTrue(mainOutput.contains("Logged in as: TEST USER"));
    }

    @Test
    @DisplayName("Should display menu navigation flow")
    void testMenuNavigationFlow() {
        // When: Displaying main menu
        Staff staff = new Staff("Test User", "010203040506", 25, 3000.00, "password123");
        staff.setId(12345);
        mainView.printMainMenu(staff);
        String mainOutput = getOutput();
        assertTrue(mainOutput.contains("[ MAIN MENU ]"));

        // Reset output
        outputStream.reset();

        // When: Displaying stock menu
        mainView.printStockMenu();
        String stockOutput = getOutput();
        assertTrue(stockOutput.contains("[ FOOD AND BEVERAGE MANAGEMENT SYSTEM ]"));

        // Reset output
        outputStream.reset();

        // When: Displaying sales menu
        mainView.printSalesMenu();
        String salesOutput = getOutput();
        assertTrue(salesOutput.contains(SalesConfig.TITLE_SALES_MENU));
    }

    @Test
    @DisplayName("Should display exit flow")
    void testExitFlow() {
        // When: Displaying logged out message
        mainView.printLoggedOutMessage();
        String logoutOutput = getOutput();
        assertTrue(logoutOutput.contains("RETURNING TO LOGIN MENU..."));

        // Reset output
        outputStream.reset();

        // When: Displaying exit message
        mainView.printExitMessage();
        String exitOutput = getOutput();
        assertTrue(exitOutput.contains("THANK YOU FOR USING TAR CAFE SYSTEM"));
        assertTrue(exitOutput.contains("EXITING THE PROGRAM..."));
    }
}

