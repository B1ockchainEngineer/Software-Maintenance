package assignment.util.config;

public class SignupConfig {
    private SignupConfig() {
        // prevent instantiation
    }

    // ================== UI LABELS / TITLES ==================
    public static final String TITLE_STAFF_REGISTRATION = "[ STAFF REGISTRATION ]";
    public static final String TITLE_REGISTER_STAFF = "[ REGISTER STAFF ]";
    public static final String TITLE_REGISTRATION_SUMMARY = "[ REGISTRATION SUMMARY ]";
    public static final String TITLE_SAVING_REGISTRATION = "[ SAVING REGISTRATION ]";

    // ================== REGISTRATION CODE ==================
    public static final int REGISTRATION_CODE = 1234;

    // ================== UI PROMPTS ==================
    public static final String PROMPT_ENTER_REGISTRATION_CODE = "ENTER REGISTRATION CODE (OR 'E' TO RETURN): ";
    public static final String PROMPT_ENTER_STAFF_IC = "ENTER NEW STAFF IC (12 digits, or 'E' to cancel): ";
    public static final String PROMPT_ENTER_STAFF_NAME = "ENTER NEW STAFF NAME (alphabets only, or 'E' to cancel): ";
    public static final String PROMPT_ENTER_PASSWORD = "ENTER NEW PASSWORD (or 'E' to cancel): ";
    public static final String PROMPT_CONFIRM_PASSWORD = "CONFIRM PASSWORD: ";
    public static final String PROMPT_ENTER_STAFF_AGE = "ENTER NEW STAFF AGE (18-54, or 'E' to cancel): ";
    public static final String PROMPT_ENTER_STAFF_SALARY = "ENTER NEW STAFF SALARY (must be > 0, or 'E' to cancel): ";
    public static final String PROMPT_CONFIRM_REGISTRATION = "CONFIRM REGISTRATION? (Y/N): ";
    public static final String PROMPT_CANCEL_CONFIRMATION = "⚠ Are you sure you want to cancel %s? (Y/N): ";
    public static final String PROMPT_WEAK_PASSWORD_CONTINUE = "⚠ Your password is weak. Continue anyway? (Y/N): ";

    // ================== MESSAGES ==================
    public static final String MSG_REGISTRATION_CODE_DESCRIPTION = "Please enter the registration code to create a new staff account.";
    public static final String MSG_FILL_INFORMATION = "Please fill in the following information:";
    public static final String MSG_PRESS_E_TO_CANCEL = "(You can press 'E' at any time to cancel)";
    public static final String MSG_REVIEW_DETAILS = "Please review your registration details:";
    public static final String MSG_YOU_CAN_LOGIN = "You can now log in with your IC and password.";
    public static final String MSG_REGISTRATION_CANCELLED = "REGISTRATION CANCELLED BY USER.";

    // ================== STEP MESSAGES ==================
    public static final String STEP_REGISTRATION_CODE = "STEP 1/6: REGISTRATION CODE VERIFICATION";
    public static final String STEP_STAFF_IC = "STEP 2/6: STAFF IC";
    public static final String STEP_STAFF_NAME = "STEP 3/6: STAFF NAME";
    public static final String STEP_PASSWORD = "STEP 4/6: PASSWORD";
    public static final String STEP_AGE = "STEP 5/6: STAFF AGE";
    public static final String STEP_SALARY = "STEP 6/6: STAFF SALARY";
    public static final String STEP_SAVING = "STEP 8/8: SAVING REGISTRATION...";

    // ================== PASSWORD REQUIREMENTS ==================
    public static final String MSG_PASSWORD_REQUIREMENTS = "PASSWORD REQUIREMENTS:";
    public static final String MSG_PASSWORD_REQ_1 = "• 8-16 characters";
    public static final String MSG_PASSWORD_REQ_2 = "• Alphanumeric characters (a-z, A-Z, 0-9)";
    public static final String MSG_PASSWORD_REQ_3 = "• For stronger passwords, include uppercase, lowercase, numbers, and special characters";

    // ================== COLLECTION CONFIRMATIONS ==================
    public static final String MSG_IC_COLLECTED = "✓ IC collected: %s";
    public static final String MSG_NAME_COLLECTED = "✓ Name collected: %s";
    public static final String MSG_PASSWORD_COLLECTED = "✓ Password collected";
    public static final String MSG_REGISTRATION_CODE_VERIFIED = "✓ REGISTRATION CODE VERIFIED!";
    public static final String MSG_PROCEEDING_TO_REGISTRATION = "PROCEEDING TO STAFF REGISTRATION...";
    public static final String MSG_PASSWORD_CONFIRMED = "✓ Password confirmed!";
    public static final String MSG_PASSWORD_STRENGTH = "PASSWORD STRENGTH: %s %s";
    public static final String MSG_FINAL_PASSWORD_STRENGTH = "FINAL PASSWORD STRENGTH: %s %s";

    // ================== NESTED ERROR MESSAGE CLASS ==================
    public static final class ErrorMessage {
        private ErrorMessage() {}

        public static final String INVALID_REGISTRATION_CODE_FORMAT = "<<<INVALID INPUT! REGISTRATION CODE MUST BE A NUMBER!>>>";
        public static final String INCORRECT_REGISTRATION_CODE = "<<<INCORRECT REGISTRATION CODE! REGISTRATION DENIED!>>>";
        public static final String INVALID_IC_FORMAT = "<<<INVALID INPUT! Please enter a 12-digit IC number!>>>";
        public static final String INVALID_PLACE_OF_BIRTH = "<<<INVALID PLACE OF BIRTH CODE (7th-8th digits): MUST BE 01-16 >>>";
        public static final String INVALID_IC_DATE = "<<<INVALID IC FORMAT! Please check date and place of birth codes!>>>";
        public static final String IC_ALREADY_EXISTS = "<<<IC ALREADY EXISTS! Please use a different IC!>>>";
        public static final String NAME_CANNOT_BE_EMPTY = "<<<NAME CANNOT BE EMPTY!>>>";
        public static final String PASSWORD_CANNOT_BE_EMPTY = "<<<PASSWORD CANNOT BE EMPTY! Please enter a password!>>>";
        public static final String INVALID_PASSWORD_FORMAT = "<<<INVALID INPUT! Password must be 8-16 alphanumeric characters!>>>";
        public static final String CURRENT_ISSUES = "CURRENT ISSUES:";
        public static final String PASSWORDS_DO_NOT_MATCH = "<<<PASSWORDS DO NOT MATCH! Please try again!>>>";
        public static final String AGE_CANNOT_BE_NEGATIVE = "<<<INVALID INPUT! Age cannot be negative! Please enter a positive number.>>>";
        public static final String INVALID_AGE = "<<<INVALID INPUT! Age must be between 18 and 54!>>>";
        public static final String SALARY_CANNOT_BE_NEGATIVE = "<<<INVALID INPUT! Salary cannot be negative! Please enter a positive amount.>>>";
        public static final String INVALID_SALARY = "<<<INVALID INPUT! Salary must be greater than 0!>>>";
        public static final String INVALID_NUMBER = "<<<INVALID INPUT! Please enter a valid number!>>>";
        public static final String REGISTRATION_FAILED = "<<<REGISTRATION FAILED! IC ALREADY EXISTS IN THE SYSTEM!>>>";
        public static final String WEAK_PASSWORD_DETECTED = "⚠ WEAK PASSWORD DETECTED!";
        public static final String RECOMMENDATIONS = "RECOMMENDATIONS:";
        public static final String PLEASE_STRONGER_PASSWORD = "Please enter a stronger password.";
    }

    // ================== NESTED SUCCESS MESSAGE CLASS ==================
    public static final class SuccessfulMessage {
        private SuccessfulMessage() {}

        public static final String REGISTRATION_SUCCESSFUL = "  ✓ REGISTRATION SUCCESSFUL!";
    }

    // ================== REGISTRATION SUMMARY FIELDS ==================
    public static final String SUMMARY_STAFF_ID = "STAFF ID: S-%d";
    public static final String SUMMARY_NAME = "NAME: %s";
    public static final String SUMMARY_IC = "IC: %s";
    public static final String SUMMARY_AGE = "AGE: %d";
    public static final String SUMMARY_SALARY = "SALARY: RM %.2f";
}

