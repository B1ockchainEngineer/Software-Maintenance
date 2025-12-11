package assignment.util.config;

public class StaffConfig {
    private StaffConfig() {
        // prevent instantiation
    }

    // ================== FILE & PATH CONFIG ==================
    public static final String STAFF_FILE_PATH = AppConfig.DATA_DIR + "staff.txt";

    // ================== UI LABELS / TITLES ==================
    public static final String TITLE_STAFF_SYSTEM = "[ STAFF MANAGEMENT SYSTEM ]";
    public static final String TITLE_ADD_STAFF = "[ ADD NEW STAFF ]";
    public static final String TITLE_UPDATE_STAFF = "[ UPDATE STAFF INFORMATION ]";
    public static final String TITLE_DELETE_STAFF = "[ DELETE STAFF ]";
    public static final String TITLE_DELETE_STAFF_CONFIRMATION = "[ DELETE STAFF - CONFIRMATION ]";
    public static final String TITLE_SEARCH_STAFF = "[ SEARCH STAFF ]";
    public static final String TITLE_SEARCH_RESULTS = "[ SEARCH RESULTS ]";
    public static final String TITLE_VIEW_STAFF = "[ VIEW ALL STAFF ]";

    // ================== UI PROMPTS ==================
    public static final String PROMPT_ENTER_SELECTION = "ENTER YOUR SELECTION: ";
    public static final String PROMPT_ENTER_CHOICE = "ENTER YOUR CHOICE (OR 'E' TO CANCEL): ";
    public static final String PROMPT_CONFIRM_ADD_STAFF = "CONFIRM ADD STAFF? (Y/N): ";
    public static final String PROMPT_CONFIRM_UPDATE = "CONFIRM UPDATE? (Y/N): ";
    public static final String PROMPT_CONFIRM_DELETE = "ARE YOU SURE YOU WANT TO DELETE THIS STAFF? (Y/N): ";
    public static final String PROMPT_ENTER_STAFF_ID_UPDATE = "ENTER STAFF ID TO UPDATE (OR '0' TO CANCEL): S-";
    public static final String PROMPT_ENTER_STAFF_IC_UPDATE = "ENTER STAFF IC TO UPDATE (OR 'E' TO CANCEL): ";
    public static final String PROMPT_ENTER_STAFF_ID_DELETE = "ENTER STAFF ID TO DELETE (OR '0' TO CANCEL): S-";
    public static final String PROMPT_ENTER_STAFF_IC_DELETE = "ENTER STAFF IC TO DELETE (OR 'E' TO CANCEL): ";
    public static final String PROMPT_ENTER_STAFF_ID_SEARCH = "ENTER STAFF ID TO SEARCH (OR '0' TO CANCEL): S-";
    public static final String PROMPT_ENTER_STAFF_IC_SEARCH = "ENTER STAFF IC TO SEARCH (OR 'E' TO CANCEL): ";
    public static final String PROMPT_ENTER_STAFF_NAME_SEARCH = "ENTER STAFF NAME TO SEARCH (OR 'E' TO CANCEL): ";
    public static final String PROMPT_ENTER_NEW_NAME = "ENTER NEW NAME [%s]: ";
    public static final String PROMPT_ENTER_NEW_PASSWORD = "ENTER NEW PASSWORD (Press Enter to skip): ";
    public static final String PROMPT_CONFIRM_PASSWORD = "CONFIRM PASSWORD: ";
    public static final String PROMPT_ENTER_NEW_AGE = "ENTER NEW AGE [%d]: ";
    public static final String PROMPT_ENTER_NEW_SALARY = "ENTER NEW SALARY [RM %.2f]: RM ";
    public static final String PROMPT_ENTER_STAFF_IC = "ENTER STAFF IC (12 digits, e.g., 123456789012): ";
    public static final String PROMPT_ENTER_STAFF_NAME = "ENTER STAFF NAME (alphabets and spaces only, e.g., John Doe): ";
    public static final String PROMPT_ENTER_PASSWORD = "ENTER PASSWORD (8-16 alphanumeric characters, e.g., Staff123): ";
    public static final String PROMPT_ENTER_STAFF_AGE = "ENTER STAFF AGE (18-54 years, e.g., 25): ";
    public static final String PROMPT_ENTER_STAFF_SALARY = "ENTER STAFF SALARY (must be > 0, e.g., 2500.00): RM ";
    public static final String PROMPT_RETRY_SEARCH = "DO YOU WANT TO SEARCH AGAIN? (Y/N): ";

    // ================== MESSAGES ==================
    public static final String MSG_TOTAL_STAFF = "Total Staff: %d";
    public static final String MSG_PLEASE_SELECT_OPTION = "Please select an option:";
    public static final String MSG_FILL_INFORMATION = "Please fill in the following information:";
    public static final String MSG_PRESS_E_TO_CANCEL = "(Press 'E' at any time to cancel)";
    public static final String MSG_CURRENT_STAFF_COUNT = "Current Staff Count: %d";
    public static final String MSG_SUMMARY_REVIEW = "SUMMARY - Please review the information:";
    public static final String MSG_FIND_STAFF_BY = "Find staff by:";
    public static final String MSG_FIND_BY_ID = "1. Staff ID (e.g., S-123456)";
    public static final String MSG_FIND_BY_NAME = "1. Staff Name (e.g., John Doe)";
    public static final String MSG_FIND_BY_IC = "2. Staff IC (e.g., 123456789012)";
    public static final String MSG_CURRENT_STAFF_INFO = "CURRENT STAFF INFORMATION:";
    public static final String MSG_INSTRUCTIONS = "INSTRUCTIONS:";
    public static final String MSG_PRESS_ENTER_KEEP = "  - Press Enter to keep current value";
    public static final String MSG_ENTER_NEW_VALUE = "  - Enter new value to update";
    public static final String MSG_PRESS_E_CANCEL = "  - Press 'E' to cancel";
    public static final String MSG_UPDATED_STAFF_INFO = "UPDATED STAFF INFORMATION:";
    public static final String MSG_CURRENT_STAFF_LIST = "CURRENT STAFF LIST:";
    public static final String MSG_DELETE_BY = "Delete by:";
    public static final String MSG_SEARCH_BY = "Search by:";
    public static final String MSG_SEARCH_BY_ID = "1. Staff ID";
    public static final String MSG_SEARCH_BY_IC = "2. Staff IC";
    public static final String MSG_SEARCH_BY_NAME = "3. Staff Name";
    public static final String MSG_QUICK_REFERENCE = "QUICK REFERENCE - Current Staff List:";
    public static final String MSG_SEARCH_CRITERIA = "Search criteria: %s";
    public static final String MSG_FOUND = "Found: %d staff member(s)";
    public static final String MSG_STAFF_TO_BE_DELETED = "STAFF TO BE DELETED:";
    public static final String MSG_STAFF_WITH = "Staff with %s has been removed from the system.";
    public static final String MSG_REMAINING_STAFF_COUNT = "Remaining staff count: %d";
    public static final String MSG_NEW_STAFF_COUNT = "New Staff Count: %d";
    public static final String MSG_STATISTICS = "STATISTICS:";
    public static final String MSG_STAFF_LIST = "STAFF LIST:";
    public static final String MSG_TOTAL_STAFF_COUNT = "TOTAL STAFF: %d";
    public static final String MSG_RETURNING_TO_MAIN = "RETURNING TO MAIN MENU...";

    // ================== HEADERS ==================
    public static final String HEADER_STAFF_ID = "STAFF ID";
    public static final String HEADER_STAFF_NAME = "STAFF NAME";
    public static final String HEADER_STAFF_IC = "STAFF IC";
    public static final String HEADER_AGE = "AGE";
    public static final String HEADER_SALARY = "SALARY";

    // ================== NESTED ERROR MESSAGE CLASS ==================
    public static final class ErrorMessage {
        private ErrorMessage() {}

        public static final String INVALID_OPTION = "<<<INVALID OPTION!>>>";
        public static final String STAFF_ADDITION_CANCELLED = "<<<STAFF ADDITION CANCELLED!>>>";
        public static final String FAILED_TO_ADD_STAFF = "<<<FAILED TO ADD STAFF!>>>";
        public static final String REASON_IC_EXISTS = "Reason: IC already exists in the system.";
        public static final String USE_DIFFERENT_IC = "Please use a different IC number.";
        public static final String INVALID_CHOICE = "<<<INVALID CHOICE! Please enter 1 or 2!>>>";
        public static final String INVALID_CHOICE_SEARCH = "<<<INVALID CHOICE! Please enter 1, 2, or 3!>>>";
        public static final String INVALID_IC_FORMAT = "<<<INVALID IC FORMAT! IC must be 12 digits!>>>";
        public static final String INVALID_INPUT = "<<<INVALID INPUT!>>>";
        public static final String STAFF_NOT_FOUND = "<<<STAFF NOT FOUND!>>>";
        public static final String STAFF_WITH_NOT_FOUND = "<<<STAFF WITH %s NOT FOUND!>>>";
        public static final String NO_STAFF_FOUND = "<<<NO STAFF FOUND WITH %s!>>>";
        public static final String UPDATE_CANCELLED = "<<<UPDATE CANCELLED!>>>";
        public static final String DELETION_CANCELLED = "<<<DELETION CANCELLED!>>>";
        public static final String FAILED_TO_UPDATE_STAFF = "<<<FAILED TO UPDATE STAFF!>>>";
        public static final String FAILED_TO_DELETE_STAFF = "<<<FAILED TO DELETE STAFF!>>>";
        public static final String NO_STAFF_TO_DISPLAY = "THERE IS NO STAFF TO DISPLAY...";
        public static final String NO_STAFF_TO_DELETE = "THERE IS NO STAFF TO DELETE...";
        public static final String NAME_CANNOT_BE_EMPTY = "<<<NAME CANNOT BE EMPTY!>>>";
        public static final String IC_CANNOT_BE_EMPTY = "IC cannot be empty. Please enter a 12-digit IC number.";
        public static final String IC_INVALID_FORMAT = "Invalid format! IC must be exactly 12 digits.";
        public static final String IC_ALREADY_EXISTS = "This IC already exists in the system!";
        public static final String NAME_INVALID_CHARS = "Invalid characters! Name should contain only letters and spaces.";
        public static final String NAME_TOO_SHORT = "Name is too short. Please enter at least 2 characters.";
        public static final String NAME_TOO_LONG = "Name is too long. Maximum 50 characters allowed.";
        public static final String PASSWORD_CANNOT_BE_EMPTY = "Password cannot be empty.";
        public static final String PASSWORD_TOO_SHORT = "Password too short! Minimum 8 characters required.";
        public static final String PASSWORD_TOO_LONG = "Password too long! Maximum 16 characters allowed.";
        public static final String PASSWORD_INVALID_CHARS = "Password can only contain letters and numbers.";
        public static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match! Keeping current password.";
        public static final String PASSWORD_INVALID_FORMAT = "Invalid password format! Keeping current password.";
        public static final String AGE_CANNOT_BE_NEGATIVE = "Age cannot be negative! Please enter a positive number.";
        public static final String AGE_TOO_YOUNG = "Age too young! Minimum age is 18 years.";
        public static final String AGE_TOO_OLD = "Age too old! Maximum age is 54 years.";
        public static final String SALARY_CANNOT_BE_NEGATIVE = "Salary cannot be negative! Please enter a positive amount.";
        public static final String SALARY_MUST_BE_POSITIVE = "Salary must be greater than 0!";
        public static final String SALARY_UNUSUALLY_HIGH = "Warning: Salary seems unusually high (>RM 1,000,000). Are you sure? (Y/N): ";
        public static final String INVALID_NUMBER = "Invalid input! Please enter a valid number.";
        public static final String NAME_UPDATED = "Name updated successfully!";
        public static final String NAME_INVALID_FORMAT = "Invalid name format! Keeping current value.";
        public static final String NAME_FORMAT_REQUIREMENTS = "(Name must be 2-50 characters, letters and spaces only)";
        public static final String KEEPING_CURRENT_NAME = "Keeping current name.";
        public static final String PASSWORD_UPDATED = "Password updated successfully!";
        public static final String PASSWORD_FORMAT_REQUIREMENTS = "(Password must be 8-16 alphanumeric characters)";
        public static final String KEEPING_CURRENT_PASSWORD = "Keeping current password.";
        public static final String AGE_UPDATED = "Age updated successfully!";
        public static final String AGE_INVALID = "Invalid age! Age must be between 18-54. Keeping current value.";
        public static final String KEEPING_CURRENT_AGE = "Keeping current age.";
        public static final String SALARY_UPDATED = "Salary updated successfully!";
        public static final String SALARY_INVALID = "Invalid salary! Salary must be greater than 0. Keeping current value.";
        public static final String KEEPING_CURRENT_SALARY = "Keeping current salary.";
        public static final String UPDATE_POSSIBLE_REASONS = "Possible reasons:";
        public static final String UPDATE_REASON_NOT_FOUND = "  - Staff record not found";
        public static final String UPDATE_REASON_IC_EXISTS = "  - New IC already exists (if IC was changed)";
        public static final String UPDATE_TRY_AGAIN = "Please try again.";
        public static final String NO_CHANGES_MADE = "No changes were made.";
        public static final String STAFF_RECORD_SAFE = "Staff record is safe.";
        public static final String CONTACT_ADMIN = "Please try again or contact system administrator.";
        public static final String TIP_VIEW_LIST = "TIP: You can:";
        public static final String TIP_VERIFY_ID = "  - View the staff list to verify the ID/IC";
        public static final String TIP_USE_SEARCH = "  - Use the search function to find the staff";
        public static final String TIP_CHECK_TYPOS = "  - Check for typos in the ID/IC";
        public static final String TIP_ADD_STAFF = "TIP: Use 'Add New Staff' option to register staff members.";
        public static final String TIP_SEARCH_DIFFERENT = "TIP: Try searching with:";
        public static final String TIP_DIFFERENT_SPELLING = "  - Different spelling";
        public static final String TIP_PARTIAL_NAME = "  - Partial name (for name search)";
        public static final String TIP_CHECK_LIST = "  - Check the staff list to verify the ID/IC";
        public static final String CANNOT_DELETE_YOURSELF = "<<<YOU CANNOT DELETE YOURSELF!>>>";
        public static final String CANNOT_DELETE_YOURSELF_REASON = "For security reasons, staff members cannot delete their own account.";
        public static final String CANNOT_DELETE_YOURSELF_TIP = "TIP: Only other staff members can delete your account.";
    }

    // ================== NESTED SUCCESS MESSAGE CLASS ==================
    public static final class SuccessfulMessage {
        private SuccessfulMessage() {}

        public static final String STAFF_ADDED = "  STAFF ADDED SUCCESSFULLY!";
        public static final String STAFF_UPDATED = "  STAFF UPDATED SUCCESSFULLY!";
        public static final String STAFF_DELETED = "  STAFF DELETED SUCCESSFULLY!";
        public static final String IC_VALIDATED = "IC validated successfully!";
        public static final String NAME_VALIDATED = "Name validated successfully!";
        public static final String PASSWORD_CONFIRMED = "Password confirmed successfully!";
        public static final String AGE_VALIDATED = "Age validated successfully!";
        public static final String SALARY_VALIDATED = "Salary validated successfully!";
        public static final String OPERATION_CANCELLED = "Operation cancelled.";
    }

    // ================== WARNING MESSAGES ==================
    public static final String WARNING_DELETE_IRREVERSIBLE = "WARNING: This action cannot be undone!";
    public static final String WARNING_SALARY_HIGH = "Warning: Salary seems unusually high. Continue? (Y/N): ";
}

