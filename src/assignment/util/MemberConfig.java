package assignment.util;

public class MemberConfig {
    private MemberConfig() {
        // prevent instantiation
    }

    // ================== FILE & PATH CONFIG ==================
    public static final String MEMBER_FILE_PATH = "members.txt";
    public static final String TEMP_DELETE_FILE_PATH = "dltTemp.txt";
    
    // ================== UI LABELS / TITLES ==================
    public static final String TITLE_MEMBER_SYSTEM = "[ MEMBER MANAGEMENT SYSTEM ]";
    public static final String TITLE_VIEW_MEMBERS  = "[ VIEW ALL MEMBERS ]";
    public static final String TITLE_REGISTER_MEMBER = "[ REGISTER MEMBER ]";
    public static final String TITLE_DELETE_MEMBER   = "[ DELETE MEMBER ]";
    public static final String TITLE_SEARCH_MEMBER   = "[ SEARCH MEMBER BY ID ]";
    public static final String TITLE_EDIT_MEMBER     = "[ EDIT MEMBER ]";

    // ================== MEMBER TYPES ==================
    public static final String MEMBER_TYPE_NORMAL  = "Normal";
    public static final String MEMBER_TYPE_GOLD    = "Gold";
    public static final String MEMBER_TYPE_PREMIUM = "Premium";

    // ================== NESTED ERROR MESSAGE CLASS ==================
    public static final class ErrorMessage {

        private ErrorMessage() {}

        // File / IO
        public static final String FILE_CREATE_ERROR =
                "Error creating member file";
        public static final String FILE_READ_ERROR =
                "Error reading members file";
        public static final String FILE_WRITE_ERROR =
                "Error writing member record";
        public static final String FILE_DELETE_ERROR =
                "Error finalizing member deletion.";

        // Member / validation
        public static final String NO_MEMBER_TO_DISPLAY =
                "THERE IS NO MEMBER TO DISPLAY...";
        public static final String MEMBER_NOT_FOUND =
                "<<< MEMBER NOT FOUND >>>";
        public static final String IC_ALREADY_EXISTS =
                "<<<IC already exists in the file. Please reenter!>>>";
        public static final String INVALID_HP =
                "<<<Invalid HP. Must start with '01'. Enter 11 digits for 011 prefix, or 10 digits for others.>>>";
        public static final String INVALID_OPTION =
                "<<<INVALID OPTION>>>";
        public static final String INVALID_CHOICE =
                "<<<Invalid choice. Please try again.>>>";

        // Generic formatted error (for exceptions)
        public static final String SAVE_MEMBERS_FAILED_TEMPLATE =
                "<<< ERROR SAVING MEMBERS: %s >>>";
        public static final String DELETE_FAILED =
                "Failed to delete member from file.";
        public static final String DELETE_CANCELLED_OR_NOT_FOUND =
                "MEMBER WITH ID M-%d NOT FOUND OR DELETION CANCELLED.";
        public static final String INVALID_MEMBER_ID_FORMAT =
                "<<<Invalid input. Please enter a valid member ID or 'E' to cancel.>>>";
        public static final String IC_ALREADY_EXISTS_OTHER =
                "<<<IC already exists for another member. Please reenter!>>>";
        public static final String NO_MEMBERS_TYPE_FOUND =
                "NO %s MEMBERS FOUND.";
    }

    // ================== NESTED SUCCESS MESSAGE CLASS ==================
    public static final class SuccessfulMessage {
        private SuccessfulMessage() {}

        public static final String MEMBER_ADDED =
                "NEW MEMBER ADDED TO THE SYSTEM...";
        public static final String MEMBER_DELETED =
                "MEMBER WITH ID M-%d HAS BEEN DELETED.";
        public static final String MEMBER_FOUND =
                "\tMEMBER FOUND !";
        public static final String MEMBER_NAME_UPDATED =
                "<<< MEMBER NAME UPDATED >>>";
        public static final String MEMBER_HP_UPDATED =
                "<<< MEMBER HP UPDATED >>>";
        public static final String MEMBER_IC_UPDATED =
                "<<< MEMBER IC UPDATED >>>";
        public static final String MEMBER_SAVED =
                "<<< MEMBER DETAILS SAVED >>>";
    }
}

