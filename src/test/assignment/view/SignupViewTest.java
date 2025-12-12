package assignment.view;

import assignment.model.Staff;
import assignment.util.config.SignupConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SignupView class.
 * Tests all display methods by capturing System.out output.
 */
@DisplayName("Signup View Tests")
class SignupViewTest {

    private static final Logger LOGGER = Logger.getLogger(SignupViewTest.class.getName());
    private SignupView signupView;
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
        signupView = new SignupView();
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

    // ================== printSignupHeader TESTS ==================

    @Test
    @DisplayName("Should display signup header")
    void printSignupHeader_ShouldDisplayHeader() {
        // When: Printing signup header
        signupView.printSignupHeader();

        // Then: Should display title and description
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.TITLE_STAFF_REGISTRATION));
        assertTrue(output.contains(SignupConfig.MSG_REGISTRATION_CODE_DESCRIPTION));
        LOGGER.info("PrintSignupHeader test passed.");
    }

    // ================== printRegistrationCodeStep TESTS ==================

    @Test
    @DisplayName("Should display registration code step")
    void printRegistrationCodeStep_ShouldDisplayStep() {
        // When: Printing registration code step
        signupView.printRegistrationCodeStep();

        // Then: Should display step and prompt
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.STEP_REGISTRATION_CODE));
        assertTrue(output.contains(SignupConfig.PROMPT_ENTER_REGISTRATION_CODE));
        LOGGER.info("PrintRegistrationCodeStep test passed.");
    }

    // ================== printInvalidRegistrationCodeFormat TESTS ==================

    @Test
    @DisplayName("Should display invalid registration code format error")
    void printInvalidRegistrationCodeFormat_ShouldDisplayError() {
        // When: Printing invalid format error
        signupView.printInvalidRegistrationCodeFormat();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.INVALID_REGISTRATION_CODE_FORMAT));
        LOGGER.info("PrintInvalidRegistrationCodeFormat test passed.");
    }

    // ================== printIncorrectRegistrationCode TESTS ==================

    @Test
    @DisplayName("Should display incorrect registration code error")
    void printIncorrectRegistrationCode_ShouldDisplayError() {
        // When: Printing incorrect code error
        signupView.printIncorrectRegistrationCode();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.INCORRECT_REGISTRATION_CODE));
        LOGGER.info("PrintIncorrectRegistrationCode test passed.");
    }

    // ================== printRegistrationCodeVerified TESTS ==================

    @Test
    @DisplayName("Should display registration code verified message")
    void printRegistrationCodeVerified_ShouldDisplayMessage() {
        // When: Printing code verified message
        signupView.printRegistrationCodeVerified();

        // Then: Should display verification messages
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.MSG_REGISTRATION_CODE_VERIFIED));
        assertTrue(output.contains(SignupConfig.MSG_PROCEEDING_TO_REGISTRATION));
        LOGGER.info("PrintRegistrationCodeVerified test passed.");
    }

    // ================== printRegisterStaffHeader TESTS ==================

    @Test
    @DisplayName("Should display register staff header")
    void printRegisterStaffHeader_ShouldDisplayHeader() {
        // When: Printing register staff header
        signupView.printRegisterStaffHeader();

        // Then: Should display title and instructions
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.TITLE_REGISTER_STAFF));
        assertTrue(output.contains(SignupConfig.MSG_FILL_INFORMATION));
        assertTrue(output.contains(SignupConfig.MSG_PRESS_E_TO_CANCEL));
        LOGGER.info("PrintRegisterStaffHeader test passed.");
    }

    // ================== printIcStep TESTS ==================

    @Test
    @DisplayName("Should display IC step")
    void printIcStep_ShouldDisplayStep() {
        // When: Printing IC step
        signupView.printIcStep();

        // Then: Should display step and prompt
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.STEP_STAFF_IC));
        assertTrue(output.contains(SignupConfig.PROMPT_ENTER_STAFF_IC));
        LOGGER.info("PrintIcStep test passed.");
    }

    @Test
    @DisplayName("Should display IC prompt only")
    void printIcPrompt_ShouldDisplayPromptOnly() {
        // When: Printing IC prompt
        signupView.printIcPrompt();

        // Then: Should display only prompt (no step header)
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.PROMPT_ENTER_STAFF_IC));
        assertFalse(output.contains(SignupConfig.STEP_STAFF_IC));
        LOGGER.info("PrintIcPrompt test passed.");
    }

    // ================== printIcCollected TESTS ==================

    @Test
    @DisplayName("Should display IC collected message")
    void printIcCollected_ShouldDisplayMessage() {
        // When: Printing IC collected
        signupView.printIcCollected("121212121234");

        // Then: Should display collected message with IC
        String output = getOutput();
        assertTrue(output.contains("121212121234"));
        assertTrue(output.contains("IC collected:")); // Check for actual formatted output
        LOGGER.info("PrintIcCollected test passed.");
    }

    // ================== printNameStep TESTS ==================

    @Test
    @DisplayName("Should display name step")
    void printNameStep_ShouldDisplayStep() {
        // When: Printing name step
        signupView.printNameStep();

        // Then: Should display step and prompt
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.STEP_STAFF_NAME));
        assertTrue(output.contains(SignupConfig.PROMPT_ENTER_STAFF_NAME));
        LOGGER.info("PrintNameStep test passed.");
    }

    // ================== printNameCollected TESTS ==================

    @Test
    @DisplayName("Should display name collected message")
    void printNameCollected_ShouldDisplayMessage() {
        // When: Printing name collected
        signupView.printNameCollected("John Doe");

        // Then: Should display collected message with name
        String output = getOutput();
        assertTrue(output.contains("John Doe"));
        assertTrue(output.contains("Name collected:")); // Check for actual formatted output
        LOGGER.info("PrintNameCollected test passed.");
    }

    // ================== printPasswordStep TESTS ==================

    @Test
    @DisplayName("Should display password step with requirements")
    void printPasswordStep_ShouldDisplayStepAndRequirements() {
        // When: Printing password step
        signupView.printPasswordStep();

        // Then: Should display step, requirements, and prompt
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.STEP_PASSWORD));
        assertTrue(output.contains(SignupConfig.MSG_PASSWORD_REQUIREMENTS));
        assertTrue(output.contains(SignupConfig.MSG_PASSWORD_REQ_1)); // "• Minimum 8 characters"
        // Note: MSG_PASSWORD_REQ_2 and MSG_PASSWORD_REQ_3 are now empty, so they won't be displayed
        assertTrue(output.contains(SignupConfig.PROMPT_ENTER_PASSWORD));
        LOGGER.info("PrintPasswordStep test passed.");
    }

    @Test
    @DisplayName("Should display password prompt only")
    void printPasswordPrompt_ShouldDisplayPromptOnly() {
        // When: Printing password prompt
        signupView.printPasswordPrompt();

        // Then: Should display only prompt (no step header)
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.PROMPT_ENTER_PASSWORD));
        assertFalse(output.contains(SignupConfig.STEP_PASSWORD));
        LOGGER.info("PrintPasswordPrompt test passed.");
    }

    // ================== printEmptyPasswordError TESTS ==================

    @Test
    @DisplayName("Should display empty password error")
    void printEmptyPasswordError_ShouldDisplayError() {
        // When: Printing empty password error
        signupView.printEmptyPasswordError();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.PASSWORD_CANNOT_BE_EMPTY));
        LOGGER.info("PrintEmptyPasswordError test passed.");
    }

    // ================== printPasswordStrength TESTS ==================

    @Test
    @DisplayName("Should display password strength")
    void printPasswordStrength_ShouldDisplayStrength() {
        // When: Printing password strength
        signupView.printPasswordStrength("password123");

        // Then: Should display strength information
        String output = getOutput();
        assertTrue(output.contains("PASSWORD STRENGTH:")); // Check for actual formatted output
        LOGGER.info("PrintPasswordStrength test passed.");
    }

    @Test
    @DisplayName("Should display weak password warning for weak passwords")
    void printPasswordStrength_WithWeakPassword_ShouldDisplayWarning() {
        // When: Printing strength for weak password
        signupView.printPasswordStrength("12345678");

        // Then: Should display weak password warning
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.WEAK_PASSWORD_DETECTED));
        assertTrue(output.contains(SignupConfig.ErrorMessage.RECOMMENDATIONS));
        LOGGER.info("PrintPasswordStrength with weak password test passed.");
    }

    // ================== printInvalidPasswordFormat TESTS ==================

    @Test
    @DisplayName("Should display invalid password format error")
    void printInvalidPasswordFormat_ShouldDisplayError() {
        // When: Printing invalid format error
        signupView.printInvalidPasswordFormat();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.INVALID_PASSWORD_FORMAT));
        LOGGER.info("PrintInvalidPasswordFormat test passed.");
    }

    // ================== printWeakPasswordWarning TESTS ==================

    @Test
    @DisplayName("Should display weak password warning prompt")
    void printWeakPasswordWarning_ShouldDisplayPrompt() {
        // When: Printing weak password warning
        signupView.printWeakPasswordWarning();

        // Then: Should display warning prompt
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.PROMPT_WEAK_PASSWORD_CONTINUE));
        LOGGER.info("PrintWeakPasswordWarning test passed.");
    }

    // ================== printPasswordConfirmPrompt TESTS ==================

    @Test
    @DisplayName("Should display password confirm prompt")
    void printPasswordConfirmPrompt_ShouldDisplayPrompt() {
        // When: Printing password confirm prompt
        signupView.printPasswordConfirmPrompt();

        // Then: Should display confirm prompt
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.PROMPT_CONFIRM_PASSWORD));
        LOGGER.info("PrintPasswordConfirmPrompt test passed.");
    }

    // ================== printPasswordMismatch TESTS ==================

    @Test
    @DisplayName("Should display password mismatch error")
    void printPasswordMismatch_ShouldDisplayError() {
        // When: Printing password mismatch error
        signupView.printPasswordMismatch();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.PASSWORDS_DO_NOT_MATCH));
        LOGGER.info("PrintPasswordMismatch test passed.");
    }

    // ================== printPasswordConfirmed TESTS ==================

    @Test
    @DisplayName("Should display password confirmed message")
    void printPasswordConfirmed_ShouldDisplayMessage() {
        // When: Printing password confirmed
        signupView.printPasswordConfirmed("password123");

        // Then: Should display confirmed message and final strength
        String output = getOutput();
        assertTrue(output.contains("Password confirmed!")); // Check for actual formatted output
        assertTrue(output.contains("FINAL PASSWORD STRENGTH:")); // Check for actual formatted output
        LOGGER.info("PrintPasswordConfirmed test passed.");
    }

    // ================== printPasswordCollected TESTS ==================

    @Test
    @DisplayName("Should display password collected message")
    void printPasswordCollected_ShouldDisplayMessage() {
        // When: Printing password collected
        signupView.printPasswordCollected();

        // Then: Should display collected message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.MSG_PASSWORD_COLLECTED));
        LOGGER.info("PrintPasswordCollected test passed.");
    }

    // ================== printAdditionalInfoStep TESTS ==================

    @Test
    @DisplayName("Should display additional info step")
    void printAdditionalInfoStep_ShouldDisplayStep() {
        // When: Printing additional info step
        signupView.printAdditionalInfoStep();

        // Then: Should display step
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.STEP_ADDITIONAL_INFO));
        LOGGER.info("PrintAdditionalInfoStep test passed.");
    }

    // ================== printAgePrompt TESTS ==================

    @Test
    @DisplayName("Should display age prompt")
    void printAgePrompt_ShouldDisplayPrompt() {
        // When: Printing age prompt
        signupView.printAgePrompt();

        // Then: Should display prompt
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.PROMPT_ENTER_STAFF_AGE));
        LOGGER.info("PrintAgePrompt test passed.");
    }

    // ================== printSalaryPrompt TESTS ==================

    @Test
    @DisplayName("Should display salary prompt")
    void printSalaryPrompt_ShouldDisplayPrompt() {
        // When: Printing salary prompt
        signupView.printSalaryPrompt();

        // Then: Should display prompt
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.PROMPT_ENTER_STAFF_SALARY));
        LOGGER.info("PrintSalaryPrompt test passed.");
    }

    // ================== printRegistrationSummary TESTS ==================

    @Test
    @DisplayName("Should display registration summary")
    void printRegistrationSummary_ShouldDisplaySummary() {
        // Given: Staff with registration details
        Staff staff = new Staff("Test Staff", "121212121234", 28, 4000.50, "pass");
        staff.setId(123456);

        // When: Printing registration summary
        signupView.printRegistrationSummary(staff);

        // Then: Should display summary with all staff information
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.TITLE_REGISTRATION_SUMMARY));
        assertTrue(output.contains(SignupConfig.MSG_REVIEW_DETAILS));
        assertTrue(output.contains("123456"));
        assertTrue(output.contains("Test Staff"));
        assertTrue(output.contains("121212121234"));
        assertTrue(output.contains("28"));
        assertTrue(output.contains("4000.50"));
        assertTrue(output.contains(SignupConfig.PROMPT_CONFIRM_REGISTRATION));
        LOGGER.info("PrintRegistrationSummary test passed.");
    }

    // ================== printSavingRegistration TESTS ==================

    @Test
    @DisplayName("Should display saving registration message")
    void printSavingRegistration_ShouldDisplayMessage() {
        // When: Printing saving registration
        signupView.printSavingRegistration();

        // Then: Should display saving step
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.STEP_SAVING));
        LOGGER.info("PrintSavingRegistration test passed.");
    }

    // ================== printRegistrationSuccess TESTS ==================

    @Test
    @DisplayName("Should display registration success message")
    void printRegistrationSuccess_ShouldDisplaySuccessMessage() {
        // Given: Staff that was successfully registered
        Staff staff = new Staff("Success Test", "121212121234", 30, 3500.00, "pass");
        staff.setId(111111);

        // When: Printing registration success
        signupView.printRegistrationSuccess(staff);

        // Then: Should display success message and staff details
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.SuccessfulMessage.REGISTRATION_SUCCESSFUL));
        assertTrue(output.contains("111111"));
        assertTrue(output.contains("Success Test"));
        assertTrue(output.contains("121212121234"));
        assertTrue(output.contains(SignupConfig.MSG_YOU_CAN_LOGIN));
        LOGGER.info("PrintRegistrationSuccess test passed.");
    }

    // ================== printRegistrationFailed TESTS ==================

    @Test
    @DisplayName("Should display registration failed error")
    void printRegistrationFailed_ShouldDisplayError() {
        // When: Printing registration failed
        signupView.printRegistrationFailed();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.REGISTRATION_FAILED));
        LOGGER.info("PrintRegistrationFailed test passed.");
    }

    // ================== printRegistrationCancelled TESTS ==================

    @Test
    @DisplayName("Should display registration cancelled message")
    void printRegistrationCancelled_ShouldDisplayMessage() {
        // When: Printing registration cancelled
        signupView.printRegistrationCancelled();

        // Then: Should display cancelled message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.MSG_REGISTRATION_CANCELLED));
        LOGGER.info("PrintRegistrationCancelled test passed.");
    }

    // ================== printCancelConfirmationPrompt TESTS ==================

    @Test
    @DisplayName("Should display cancel confirmation prompt")
    void printCancelConfirmationPrompt_ShouldDisplayPrompt() {
        // When: Printing cancel confirmation prompt
        signupView.printCancelConfirmationPrompt("IC entry");

        // Then: Should display prompt with step name
        String output = getOutput();
        assertTrue(output.contains("IC entry"));
        assertTrue(output.contains("Are you sure you want to cancel")); // Check for actual formatted output
        LOGGER.info("PrintCancelConfirmationPrompt test passed.");
    }

    // ================== printInvalidIcFormat TESTS ==================

    @Test
    @DisplayName("Should display invalid IC format error")
    void printInvalidIcFormat_ShouldDisplayError() {
        // When: Printing invalid IC format error
        signupView.printInvalidIcFormat();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.INVALID_IC_FORMAT));
        LOGGER.info("PrintInvalidIcFormat test passed.");
    }

    // ================== printInvalidPlaceOfBirth TESTS ==================

    @Test
    @DisplayName("Should display invalid place of birth error")
    void printInvalidPlaceOfBirth_ShouldDisplayError() {
        // When: Printing invalid place of birth error
        signupView.printInvalidPlaceOfBirth();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.INVALID_PLACE_OF_BIRTH));
        LOGGER.info("PrintInvalidPlaceOfBirth test passed.");
    }

    // ================== printInvalidIcDate TESTS ==================

    @Test
    @DisplayName("Should display invalid IC date error")
    void printInvalidIcDate_ShouldDisplayError() {
        // When: Printing invalid IC date error
        signupView.printInvalidIcDate();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.INVALID_IC_DATE));
        LOGGER.info("PrintInvalidIcDate test passed.");
    }

    // ================== printIcAlreadyExists TESTS ==================

    @Test
    @DisplayName("Should display IC already exists error")
    void printIcAlreadyExists_ShouldDisplayError() {
        // When: Printing IC already exists error
        signupView.printIcAlreadyExists();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.IC_ALREADY_EXISTS));
        LOGGER.info("PrintIcAlreadyExists test passed.");
    }

    // ================== printEmptyNameError TESTS ==================

    @Test
    @DisplayName("Should display empty name error")
    void printEmptyNameError_ShouldDisplayError() {
        // When: Printing empty name error
        signupView.printEmptyNameError();

        // Then: Should display error message
        String output = getOutput();
        assertTrue(output.contains(SignupConfig.ErrorMessage.NAME_CANNOT_BE_EMPTY));
    }
}

