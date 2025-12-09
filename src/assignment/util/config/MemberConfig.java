package assignment.util.config;

public class MemberConfig {
    private MemberConfig() {
        // prevent instantiation
    }

    // ================== FILE & PATH CONFIG ==================
    public static final String MEMBER_FILE_PATH = AppConfig.DATA_DIR + "members.txt";
    public static final String TEMP_DELETE_FILE_PATH = AppConfig.TEMP_DIR + "dltTemp.txt";
    
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

    // ================== UI PROMPTS ==================
    public static final String PROMPT_ENTER_SELECTION = "ENTER YOUR SELECTION: ";
    public static final String PROMPT_YOUR_CHOICE = "YOUR CHOICE: ";
    public static final String PROMPT_ENTER_CHOICE_ARROW = "ENTER YOUR CHOICE > ";
    public static final String PROMPT_PRESS_ENTER = "Press enter key to continue...";
    public static final String PROMPT_FILTER_BY_TYPE = "FILTER MEMBER BY MEMBERSHIP TYPE:";
    
    // Add Member Prompts
    public static final String PROMPT_ENTER_IC = "ENTER MEMBER IC: ";
    public static final String PROMPT_ENTER_NAME = "ENTER MEMBER NAME: ";
    public static final String PROMPT_ENTER_HP = "ENTER MEMBER HP: ";
    public static final String PROMPT_ADD_MORE = "ADD MORE MEMBER? (Y = YES , N = NO): ";
    public static final String PROMPT_CONFIRM_DETAILS = "ARE YOU CONFIRM THE MEMBER DETAILS ABOVE ARE CORRECT ?";
    public static final String PROMPT_CONFIRM_OPTION = "ENTER YOUR OPTION (Y = YES, N = No): ";
    public static final String MSG_YOUR_MEMBER_ID = "[THIS IS YOUR MEMBER ID]";
    
    // Delete/Search Prompts
    public static final String PROMPT_DELETE_ID = "ENTER MEMBER ID TO DELETE (ENTER 'E' TO CANCEL): M-";
    public static final String MSG_DETAILS_TO_DELETE = "Member Details to Delete:";
    public static final String PROMPT_CONFIRM_DELETE = "CONFIRM DELETION? (Y = YES, N = No): ";
    public static final String PROMPT_SEARCH_ID = "ENTER MEMBER ID TO SEARCH (3 DIGIT ONLY) OR 'E' TO CANCEL: M-";
    
    // Edit Prompts
    public static final String PROMPT_EDIT_ID = "ENTER MEMBER ID (e.g. 741 ): M-";
    public static final String MSG_CURRENT_DETAILS = "CURRENT MEMBER DETAILS:";
    public static final String MSG_WHAT_TO_EDIT = "WHAT DO YOU WANT TO EDIT?";
    public static final String PROMPT_NEW_NAME = "ENTER NEW MEMBER NAME: ";
    public static final String PROMPT_NEW_HP = "ENTER NEW MEMBER HP (10–11 digits): ";
    public static final String PROMPT_NEW_IC = "ENTER NEW MEMBER IC: ";
    public static final String MSG_SELECT_NEW_TYPE = "SELECT NEW MEMBER TYPE:";
    public static final String MSG_UPDATED_DETAILS = "UPDATED MEMBER DETAILS:";
    public static final String PROMPT_EDIT_MORE = "EDIT MORE FIELDS FOR THIS MEMBER? (Y = YES, N = NO):";
    
    // Headers / Labels
    public static final String HEADER_MEMBER_ID = "MEMBER ID";
    public static final String HEADER_MEMBER_NAME = "MEMBER NAME";
    public static final String HEADER_MEMBER_HP = "MEMBER HP";
    public static final String HEADER_MEMBER_TYPE = "MEMBER TYPE";
    public static final String HEADER_MEMBER_IC = "MEMBER IC";
    
    public static final String MSG_BACK_TO_MAIN = "BACK TO MAIN MENU...";


    // ================== DISCOUNT RATES ==================
    public static final double DISCOUNT_RATE_NORMAL = 0.05;
    public static final double DISCOUNT_RATE_GOLD = 0.10;
    public static final double DISCOUNT_RATE_PREMIUM = 0.15;

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

        // Generic formatted error (for exceptions)
        public static final String SAVE_MEMBERS_FAILED_TEMPLATE =
                "<<< ERROR SAVING MEMBERS: %s >>>";
        public static final String DELETE_FAILED =
                "Failed to delete member from file.";
        public static final String DELETE_CANCELLED_OR_NOT_FOUND =
                "MEMBER WITH ID M-%d NOT FOUND.";
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
    
    // ================== PAYMENT-RELATED MEMBER MESSAGES ==================
    // These messages are used when member lookup fails during payment processing
    public static final String MSG_MEMBER_NOT_FOUND_PAYMENT = "<<<MEMBER NOT FOUND!>>>\nProceeding with no discount...";
    public static final String MSG_INVALID_MEMBER_ID_FORMAT_PAYMENT = "<<<INVALID MEMBER ID FORMAT!>>>\nProceeding with no discount...";
}

