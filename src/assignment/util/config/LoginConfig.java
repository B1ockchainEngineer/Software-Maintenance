package assignment.util.config;

public class LoginConfig {
    private LoginConfig() {
        // prevent instantiation
    }

    // ================== UI LABELS / TITLES ==================
    public static final String TITLE_LOGIN = "[ LOG IN ]";
    public static final String TITLE_LOGOUT = "[ LOGOUT ]";

    // ================== UI PROMPTS ==================
    public static final String PROMPT_ENTER_IC = "ENTER IC: ";
    public static final String PROMPT_ENTER_PASSWORD = "ENTER PASSWORD: ";
    public static final String PROMPT_RETRY_OR_EXIT = "PRESS 'E' TO RETURN TO MENU OR ANY OTHER KEY TO RETRY: ";

    // ================== MESSAGES ==================
    public static final String MSG_PLEASE_ENTER_CREDENTIALS = "Please enter your credentials to access the system.";
    public static final String MSG_WELCOME = "WELCOME, %s";
    public static final String MSG_GOODBYE = "GOODBYE, %s";
    public static final String MSG_LOGIN_TIME = "LOGIN TIME: %s";
    public static final String MSG_LOGOUT_TIME = "LOGOUT TIME: %s";

    // ================== NESTED ERROR MESSAGE CLASS ==================
    public static final class ErrorMessage {
        private ErrorMessage() {}

        public static final String IC_CANNOT_BE_EMPTY = "<<<IC CANNOT BE EMPTY!>>>";
        public static final String PASSWORD_CANNOT_BE_EMPTY = "<<<PASSWORD CANNOT BE EMPTY!>>>";
        public static final String LOGIN_FAILED = "<<<LOGIN FAILED!>>>";
        public static final String CHECK_IC_NUMBER = "Please check your IC number and try again.";
        public static final String IC_EXISTS_PASSWORD_WRONG = "The IC exists but the password is incorrect.";
    }

    // ================== NESTED SUCCESS MESSAGE CLASS ==================
    public static final class SuccessfulMessage {
        private SuccessfulMessage() {}

        public static final String LOGIN_SUCCESSFUL = "  ✓ LOGIN SUCCESSFUL!";
        public static final String LOGOUT_SUCCESSFUL = "  LOGOUT SUCCESSFUL";
    }
}

