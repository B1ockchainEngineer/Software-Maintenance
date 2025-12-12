package assignment.view;

import assignment.model.Staff;
import assignment.util.config.LoginConfig;
import assignment.view.LoginView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoginView class.
 * Tests all display methods by capturing System.out output.
 */
@DisplayName("Login View Tests")
class LoginViewTest {

    private static final Logger LOGGER = Logger.getLogger(LoginViewTest.class.getName());
    private LoginView loginView;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeAll
    static void setUpLogger() {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
    }

    @BeforeEach
    void setUp() {
        loginView = new LoginView();
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

    // ================== printLoginHeader TESTS ==================

    @Test
    @DisplayName("Should display login header")
    void printLoginHeader_ShouldDisplayHeader() {
        // When: Printing login header
        loginView.printLoginHeader();

        // Then: Should display title and credentials message
        String output = getOutput();
        assertTrue(output.contains(LoginConfig.TITLE_LOGIN));
        assertTrue(output.contains(LoginConfig.MSG_PLEASE_ENTER_CREDENTIALS));
        LOGGER.info("PrintLoginHeader test passed.");
    }

    // ================== printLoginSuccess TESTS ==================

    @Test
    @DisplayName("Should display login success message")
    void printLoginSuccess_ShouldDisplaySuccessMessage() {
        // Given: Staff and login time
        Staff staff = new Staff("John Doe", "121212121234", 25, 3000.00, "pass");
        staff.setId(123456);
        LocalDateTime loginTime = LocalDateTime.of(2024, 1, 15, 9, 30, 0);

        // When: Printing login success
        loginView.printLoginSuccess(staff, loginTime);

        // Then: Should display success message, welcome, and login time
        String output = getOutput();
        assertTrue(output.contains(LoginConfig.SuccessfulMessage.LOGIN_SUCCESSFUL));
        assertTrue(output.contains("JOHN DOE")); // Name should be uppercase
        assertTrue(output.contains("2024-01-15 09:30:00")); // Formatted login time
        assertTrue(output.contains("WELCOME")); // Welcome message
        assertTrue(output.contains("LOGIN TIME:")); // Login time label
        LOGGER.info("PrintLoginSuccess test passed.");
    }

    @Test
    @DisplayName("Should format login time correctly")
    void printLoginSuccess_ShouldFormatLoginTimeCorrectly() {
        Staff staff = new Staff("Test", "121212121234", 25, 3000.00, "pass");
        LocalDateTime loginTime = LocalDateTime.of(2024, 12, 25, 14, 45, 30);

        loginView.printLoginSuccess(staff, loginTime);

        String output = getOutput();
        assertTrue(output.contains("2024-12-25 14:45:30"));
        LOGGER.info("PrintLoginSuccess format login time test passed.");
    }

    // ================== printLoginFailedIcNotFound TESTS ==================

    @Test
    @DisplayName("Should display IC not found error")
    void printLoginFailedIcNotFound_ShouldDisplayError() {
        // When: Printing IC not found error
        loginView.printLoginFailedIcNotFound();

        // Then: Should display error messages
        String output = getOutput();
        assertTrue(output.contains(LoginConfig.ErrorMessage.LOGIN_FAILED));
        assertTrue(output.contains(LoginConfig.ErrorMessage.CHECK_IC_NUMBER));
        LOGGER.info("PrintLoginFailedIcNotFound test passed.");
    }

    // ================== printLoginFailedIncorrectPassword TESTS ==================

    @Test
    @DisplayName("Should display incorrect password error")
    void printLoginFailedIncorrectPassword_ShouldDisplayError() {
        // When: Printing incorrect password error
        loginView.printLoginFailedIncorrectPassword();

        // Then: Should display error messages
        String output = getOutput();
        assertTrue(output.contains(LoginConfig.ErrorMessage.LOGIN_FAILED));
        assertTrue(output.contains(LoginConfig.ErrorMessage.IC_EXISTS_PASSWORD_WRONG));
        LOGGER.info("PrintLoginFailedIncorrectPassword test passed.");
    }

    // ================== printRetryOrExitPrompt TESTS ==================

    @Test
    @DisplayName("Should display retry or exit prompt")
    void printRetryOrExitPrompt_ShouldDisplayPrompt() {
        // When: Printing retry or exit prompt
        loginView.printRetryOrExitPrompt();

        // Then: Should display prompt
        String output = getOutput();
        assertTrue(output.contains(LoginConfig.PROMPT_RETRY_OR_EXIT));
    }

    // ================== printEmptyIcError TESTS ==================

    @Test
    @DisplayName("Should display empty IC error")
    void printEmptyIcError_ShouldDisplayError() {
        // When: Printing empty IC error
        loginView.printEmptyIcError();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(LoginConfig.ErrorMessage.IC_CANNOT_BE_EMPTY));
        LOGGER.info("PrintEmptyIcError test passed.");
    }

    // ================== printEmptyPasswordError TESTS ==================

    @Test
    @DisplayName("Should display empty password error")
    void printEmptyPasswordError_ShouldDisplayError() {
        // When: Printing empty password error
        loginView.printEmptyPasswordError();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(LoginConfig.ErrorMessage.PASSWORD_CANNOT_BE_EMPTY));
        LOGGER.info("PrintEmptyPasswordError test passed.");
    }

    // ================== printLogoutSuccess TESTS ==================

    @Test
    @DisplayName("Should display logout success message")
    void printLogoutSuccess_ShouldDisplaySuccessMessage() {
        // Given: Staff and logout time
        Staff staff = new Staff("Jane Smith", "121212121234", 30, 3500.00, "pass");
        staff.setId(654321);
        LocalDateTime logoutTime = LocalDateTime.of(2024, 1, 15, 17, 45, 0);

        // When: Printing logout success
        loginView.printLogoutSuccess(staff, logoutTime);

        // Then: Should display success message, goodbye, and logout time
        String output = getOutput();
        assertTrue(output.contains(LoginConfig.SuccessfulMessage.LOGOUT_SUCCESSFUL));
        assertTrue(output.contains("JANE SMITH")); // Name should be uppercase
        assertTrue(output.contains("2024-01-15 17:45:00")); // Formatted logout time
        assertTrue(output.contains("GOODBYE")); // Goodbye message
        assertTrue(output.contains("LOGOUT TIME:")); // Logout time label
        LOGGER.info("PrintLogoutSuccess test passed.");
    }

    @Test
    @DisplayName("Should format logout time correctly")
    void printLogoutSuccess_ShouldFormatLogoutTimeCorrectly() {
        Staff staff = new Staff("Test", "121212121234", 25, 3000.00, "pass");
        LocalDateTime logoutTime = LocalDateTime.of(2024, 6, 30, 23, 59, 59);

        loginView.printLogoutSuccess(staff, logoutTime);

        String output = getOutput();
        assertTrue(output.contains("2024-06-30 23:59:59"));
        LOGGER.info("PrintLogoutSuccess format logout time test passed.");
    }
}

