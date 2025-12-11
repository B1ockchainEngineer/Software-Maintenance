package assignment.view;

import assignment.model.Staff;
import assignment.util.config.AppConfig;
import assignment.util.config.SignupConfig;
import assignment.util.PasswordStrengthUtil;
import assignment.util.PasswordStrengthUtil.PasswordStrength;

import java.util.logging.Logger;

/**
 * View class for Signup/Registration functionality.
 * Handles all print outputs and display logic for staff registration.
 */
public class SignupView {
    private static final Logger LOGGER = Logger.getLogger(SignupView.class.getName());

    public void printSignupHeader() {
        System.out.println(SignupConfig.TITLE_STAFF_REGISTRATION);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(SignupConfig.MSG_REGISTRATION_CODE_DESCRIPTION);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();
    }

    public void printRegistrationCodeStep() {
        System.out.println(SignupConfig.STEP_REGISTRATION_CODE);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.print(SignupConfig.PROMPT_ENTER_REGISTRATION_CODE);
    }

    public void printInvalidRegistrationCodeFormat() {
        System.out.println();
        LOGGER.warning(SignupConfig.ErrorMessage.INVALID_REGISTRATION_CODE_FORMAT);
        System.out.println(SignupConfig.ErrorMessage.INVALID_REGISTRATION_CODE_FORMAT);
    }

    public void printIncorrectRegistrationCode() {
        System.out.println();
        LOGGER.warning(SignupConfig.ErrorMessage.INCORRECT_REGISTRATION_CODE);
        System.out.println(SignupConfig.ErrorMessage.INCORRECT_REGISTRATION_CODE);
    }

    public void printRegistrationCodeVerified() {
        System.out.println();
        System.out.println(SignupConfig.MSG_REGISTRATION_CODE_VERIFIED);
        System.out.println(SignupConfig.MSG_PROCEEDING_TO_REGISTRATION);
        System.out.println();
    }

    public void printRegisterStaffHeader() {
        System.out.println(SignupConfig.TITLE_REGISTER_STAFF);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(SignupConfig.MSG_FILL_INFORMATION);
        System.out.println(SignupConfig.MSG_PRESS_E_TO_CANCEL);
        System.out.println();
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();
    }

    public void printIcStep() {
        System.out.println(SignupConfig.STEP_STAFF_IC);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.print(SignupConfig.PROMPT_ENTER_STAFF_IC);
    }

    public void printIcPrompt() {
        System.out.print(SignupConfig.PROMPT_ENTER_STAFF_IC);
    }

    public void printIcCollected(String ic) {
        System.out.println(String.format(SignupConfig.MSG_IC_COLLECTED, ic));
        System.out.println();
    }

    public void printNameStep() {
        System.out.println(SignupConfig.STEP_STAFF_NAME);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.print(SignupConfig.PROMPT_ENTER_STAFF_NAME);
    }

    public void printNameCollected(String name) {
        System.out.println(String.format(SignupConfig.MSG_NAME_COLLECTED, name));
        System.out.println();
    }

    public void printPasswordStep() {
        System.out.println(SignupConfig.STEP_PASSWORD);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();
        System.out.println(SignupConfig.MSG_PASSWORD_REQUIREMENTS);
        System.out.println(SignupConfig.MSG_PASSWORD_REQ_1);
        if (!SignupConfig.MSG_PASSWORD_REQ_2.isEmpty()) {
            System.out.println(SignupConfig.MSG_PASSWORD_REQ_2);
        }
        if (!SignupConfig.MSG_PASSWORD_REQ_3.isEmpty()) {
            System.out.println(SignupConfig.MSG_PASSWORD_REQ_3);
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();
        System.out.print(SignupConfig.PROMPT_ENTER_PASSWORD);
    }

    public void printPasswordStepPrompt() {
        System.out.print(SignupConfig.PROMPT_ENTER_PASSWORD);
    }

    public void printPasswordPrompt() {
        System.out.print(SignupConfig.PROMPT_ENTER_PASSWORD);
    }

    public void printPasswordStrength(String password) {
        PasswordStrength strength = PasswordStrengthUtil.checkPasswordStrength(password);
        String strengthBar = PasswordStrengthUtil.getStrengthBar(strength);

        System.out.println();
        System.out.println(String.format(SignupConfig.MSG_PASSWORD_STRENGTH, strengthBar, strength.getDisplayName().toUpperCase()));

        if (strength.getLevel() <= 2) {
            System.out.println();
            LOGGER.warning(SignupConfig.ErrorMessage.WEAK_PASSWORD_DETECTED);
            System.out.println(SignupConfig.ErrorMessage.WEAK_PASSWORD_DETECTED);
            System.out.println(SignupConfig.ErrorMessage.RECOMMENDATIONS);
            System.out.print(PasswordStrengthUtil.getPasswordFeedback(password));
        }
    }

    public void printInvalidPasswordFormat() {
        System.out.println();
        LOGGER.warning(SignupConfig.ErrorMessage.INVALID_PASSWORD_FORMAT);
        System.out.println(SignupConfig.ErrorMessage.INVALID_PASSWORD_FORMAT);
        System.out.println(SignupConfig.ErrorMessage.CURRENT_ISSUES);
    }

    public void printPasswordFeedback(String password) {
        System.out.print(PasswordStrengthUtil.getPasswordFeedback(password));
        System.out.println();
    }

    public void printWeakPasswordWarning() {
        System.out.print(SignupConfig.PROMPT_WEAK_PASSWORD_CONTINUE);
    }

    public void printPasswordConfirmPrompt() {
        System.out.print(SignupConfig.PROMPT_CONFIRM_PASSWORD);
    }

    public void printPasswordMismatch() {
        LOGGER.warning(SignupConfig.ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        System.out.println(SignupConfig.ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        System.out.println();
    }

    public void printPasswordConfirmed(String password) {
        PasswordStrength strength = PasswordStrengthUtil.checkPasswordStrength(password);
        String strengthBar = PasswordStrengthUtil.getStrengthBar(strength);

        System.out.println();
        System.out.println(SignupConfig.MSG_PASSWORD_CONFIRMED);
        System.out.println(String.format(SignupConfig.MSG_FINAL_PASSWORD_STRENGTH, strengthBar, strength.getDisplayName().toUpperCase()));
    }

    public void printPasswordCollected() {
        System.out.println(SignupConfig.MSG_PASSWORD_COLLECTED);
        System.out.println();
    }

    public void printAdditionalInfoStep() {
        System.out.println(SignupConfig.STEP_ADDITIONAL_INFO);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printAgeStep() {
        System.out.println(SignupConfig.STEP_AGE);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.print(SignupConfig.PROMPT_ENTER_STAFF_AGE);
    }

    public void printAgePrompt() {
        System.out.print(SignupConfig.PROMPT_ENTER_STAFF_AGE);
    }

    public void printSalaryStep() {
        System.out.println(SignupConfig.STEP_SALARY);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.print(SignupConfig.PROMPT_ENTER_STAFF_SALARY);
    }

    public void printSalaryPrompt() {
        System.out.print(SignupConfig.PROMPT_ENTER_STAFF_SALARY);
    }

    public void printRegistrationSummary(Staff staff) {
        System.out.println(SignupConfig.TITLE_REGISTRATION_SUMMARY);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(SignupConfig.MSG_REVIEW_DETAILS);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println();
        System.out.println(String.format(SignupConfig.SUMMARY_STAFF_ID, staff.getId()));
        System.out.println(String.format(SignupConfig.SUMMARY_NAME, staff.getName()));
        System.out.println(String.format(SignupConfig.SUMMARY_IC, staff.getStfIC()));
        System.out.println(String.format(SignupConfig.SUMMARY_AGE, staff.getStfAge()));
        System.out.println(String.format(SignupConfig.SUMMARY_SALARY, staff.getStfSalary()));
        System.out.println();
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.print(SignupConfig.PROMPT_CONFIRM_REGISTRATION);
    }

    public void printSavingRegistration() {
        System.out.println();
        System.out.println(SignupConfig.STEP_SAVING);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printRegistrationSuccess(Staff staff) {
        System.out.println();
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(SignupConfig.SuccessfulMessage.REGISTRATION_SUCCESSFUL);
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(String.format(SignupConfig.SUMMARY_STAFF_ID, staff.getId()));
        System.out.println(String.format(SignupConfig.SUMMARY_NAME, staff.getName()));
        System.out.println(String.format(SignupConfig.SUMMARY_IC, staff.getStfIC()));
        System.out.println();
        System.out.println(SignupConfig.MSG_YOU_CAN_LOGIN);
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println();
    }

    public void printRegistrationFailed() {
        System.out.println();
        LOGGER.severe(SignupConfig.ErrorMessage.REGISTRATION_FAILED);
        System.out.println(SignupConfig.ErrorMessage.REGISTRATION_FAILED);
    }

    public void printRegistrationCancelled() {
        System.out.println();
        System.out.println(SignupConfig.MSG_REGISTRATION_CANCELLED);
        System.out.println();
    }

    public void printCancelConfirmationPrompt(String step) {
        System.out.print(String.format(SignupConfig.PROMPT_CANCEL_CONFIRMATION, step));
    }

    public void printInvalidIcFormat() {
        LOGGER.warning(SignupConfig.ErrorMessage.INVALID_IC_FORMAT);
        System.out.println(SignupConfig.ErrorMessage.INVALID_IC_FORMAT);
        System.out.println();
    }

    public void printInvalidPlaceOfBirth() {
        LOGGER.warning(SignupConfig.ErrorMessage.INVALID_PLACE_OF_BIRTH);
        System.out.println(SignupConfig.ErrorMessage.INVALID_PLACE_OF_BIRTH);
        System.out.println();
    }

    public void printInvalidIcDate() {
        LOGGER.warning(SignupConfig.ErrorMessage.INVALID_IC_DATE);
        System.out.println(SignupConfig.ErrorMessage.INVALID_IC_DATE);
        System.out.println();
    }

    public void printIcAlreadyExists() {
        LOGGER.warning(SignupConfig.ErrorMessage.IC_ALREADY_EXISTS);
        System.out.println(SignupConfig.ErrorMessage.IC_ALREADY_EXISTS);
        System.out.println();
    }

    public void printEmptyNameError() {
        LOGGER.warning(SignupConfig.ErrorMessage.NAME_CANNOT_BE_EMPTY);
        System.out.println(SignupConfig.ErrorMessage.NAME_CANNOT_BE_EMPTY);
        System.out.println();
    }

    public void printEmptyPasswordError() {
        System.out.println();
        LOGGER.warning(SignupConfig.ErrorMessage.PASSWORD_CANNOT_BE_EMPTY);
        System.out.println(SignupConfig.ErrorMessage.PASSWORD_CANNOT_BE_EMPTY);
        System.out.println();
    }
}

