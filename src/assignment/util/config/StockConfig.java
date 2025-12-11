package assignment.util.config;

public class StockConfig {
    private StockConfig() {
        // prevent instantiation
    }

    // ================== FILE & PATH CONFIG ==================
    public static final String DATA_DIR = "data/";
    public static final String TEMP_DIR = DATA_DIR + "temp/";
    public static final String STOCK_FILE_PATH = DATA_DIR + "stock.txt";
    public static final String TEMP_DELETE_FILE_PATH = TEMP_DIR + "dltStkTemp.txt";
    public static final String TEMP_UPDATE_FILE_PATH = TEMP_DIR + "stkUpdateTemp.txt";

    // ================== UI LABELS / TITLES ==================
    public static final String TITLE_STOCK_SYSTEM = "[ STOCK MANAGEMENT SYSTEM ]";
    public static final String TITLE_VIEW_STOCK = "[ VIEW ALL PRODUCTS IN STOCK ]";
    public static final String TITLE_ADD_PRODUCT = "[ ADD NEW PRODUCT ]";
    public static final String TITLE_DELETE_PRODUCT = "[ DELETE A PRODUCT ]";
    public static final String TITLE_EDIT_PRODUCT = "[ EDIT PRODUCT ]";
    public static final String TITLE_EDIT_QUANTITY = "[ EDIT QUANTITY ]";
    
    // ================== PROMPTS ==================
    public static final String PROMPT_ENTER_PRODUCT_NAME = "ENTER PRODUCT NAME [OR 'E' TO Exit]: ";
    public static final String PROMPT_ENTER_PRODUCT_QUANTITY = "ENTER PRODUCT QUANTITY (Must be %d-%d): ";
    public static final String PROMPT_ENTER_PRICE = "ENTER PRICE (Must be >= RM %.2f): RM ";
    public static final String PROMPT_ENTER_PRODUCT_ID_TO_EDIT = "ENTER PRODUCT ID TO EDIT [OR '0' TO EXIT]: ";
    public static final String PROMPT_ENTER_PRODUCT_ID_TO_DELETE = "ENTER PRODUCT ID TO BE DELETED [OR '0' TO EXIT]: ";
    public static final String PROMPT_ENTER_NEW_PRODUCT_NAME = "ENTER NEW PRODUCT NAME: ";
    public static final String PROMPT_ENTER_NEW_PRICE = "ENTER NEW PRICE (>= RM %.2f): RM ";
    public static final String PROMPT_EDIT_CHOICE = "YOUR CHOICE: ";
    public static final String PROMPT_CONFIRM_ADD_PRODUCT = "DO YOU WANT TO ADD THIS PRODUCT? (Y = YES / N = NO): ";
    public static final String PROMPT_CONFIRM_DELETE_PRODUCT = "ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT? (Y = YES, N = CANCEL): ";
    public static final String PROMPT_ADD_ANOTHER_PRODUCT = "DO YOU WANT TO ADD ANOTHER PRODUCT? (Y FOR YES, ANY KEY TO EXIT): ";
    public static final String PROMPT_DELETE_ANOTHER_PRODUCT = "DO YOU WANT TO DELETE ANOTHER PRODUCT? (Y FOR YES, ANY KEY TO EXIT): ";
    public static final String PROMPT_EDIT_MORE_FIELDS = "EDIT MORE FIELDS FOR THIS PRODUCT? (Y = YES, N = NO): ";
    
    // ================== QUANTITY EDIT MENU ==================
    public static final String MSG_QUANTITY_EDIT_MENU = "SELECT QUANTITY OPERATION:";
    public static final String OPTION_ADD_STOCK = "1. ADD STOCK";
    public static final String OPTION_REDUCE_STOCK = "2. REDUCE STOCK";
    public static final String PROMPT_QUANTITY_CHOICE = "YOUR CHOICE: ";
    public static final String PROMPT_ENTER_QUANTITY_TO_ADD = "ENTER QUANTITY TO ADD: ";
    public static final String PROMPT_ENTER_QUANTITY_TO_REDUCE = "ENTER QUANTITY TO REDUCE: ";
    
    // ================== MESSAGES ==================
    public static final String MSG_CURRENT_PRODUCT_DETAILS = "CURRENT PRODUCT DETAILS:";
    public static final String MSG_WHAT_TO_EDIT = "WHAT DO YOU WANT TO EDIT?";
    public static final String MSG_OPTION_PRODUCT_NAME = "1. PRODUCT NAME";
    public static final String MSG_OPTION_QUANTITY = "2. QUANTITY";
    public static final String MSG_OPTION_PRICE = "3. PRICE";
    public static final String MSG_OPTION_BACK = "0. BACK";
    public static final String MSG_CURRENT_QUANTITY = "CURRENT QUANTITY: %d";
    public static final String MSG_NEW_QUANTITY = "New quantity: %d";

    // ================== STOCK ID GENERATION ==================
    public static final int BASE_STOCK_ID = 10000;

    // ================== VALIDATION RULES ==================
    public static final double MIN_PRICE = 1.0;
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 100000;

    // ================== NESTED ERROR MESSAGE CLASS ==================
    public static final class ErrorMessage {

        private ErrorMessage() {}

        // File / IO
        public static final String FILE_CREATE_ERROR =
                "Error creating stock file";
        public static final String FILE_READ_ERROR =
                "Error reading stock file";
        public static final String FILE_WRITE_ERROR =
                "Error writing stock record";
        public static final String FILE_DELETE_ERROR =
                "Error finalizing stock deletion.";
        public static final String FILE_UPDATE_ERROR =
                "Error saving stock updates to file";

        // Stock / validation
        public static final String NO_STOCK_TO_DISPLAY =
                "No products found in the inventory file.";
        public static final String STOCK_NOT_FOUND =
                "PRODUCT WITH ID %d NOT FOUND";
        public static final String NAME_ALREADY_EXISTS =
                "<<<The item name already exists, try another name!>>>";
        public static final String INVALID_PRODUCT_NAME =
                "<<<INVALID PRODUCT NAME! Please enter a valid name.>>>";
        public static final String INVALID_QUANTITY =
                "<<<QUANTITY OUT OF RANGE, PLEASE INPUT A CORRECT ONE!!>>>";
        public static final String INVALID_QUANTITY_CHOICE =
                "<<<INVALID CHOICE! Please enter 1 or 2!>>>";
        public static final String CANNOT_REDUCE_MORE_THAN_CURRENT =
                "<<<CANNOT REDUCE MORE THAN CURRENT QUANTITY!>>>";
        public static final String INVALID_PRICE =
                "<<<PRICE CANNOT BE LESS THAN RM 1.00!!!>>>";
        public static final String INVALID_OPTION =
                "<<<INVALID OPTION>>>";

        // Generic formatted error (for exceptions)
        public static final String ADD_STOCK_FAILED_TEMPLATE =
                "<<< ERROR ADDING STOCK: %s >>>";
        public static final String DELETE_FAILED =
                "PRODUCT DELETION FAILED.";
        public static final String CANNOT_DELETE_WITH_QUANTITY =
                "<<< CANNOT DELETE: PRODUCT STILL HAS QUANTITY > 0. PLEASE ADJUST STOCK TO 0 BEFORE DELETING. >>>";
        public static final String UPDATE_FAILED_CHECK_DUPLICATES =
                "<<< UPDATE FAILED. CHECK NAME DUPLICATES. >>>";
        public static final String QUANTITY_AT_MAXIMUM =
                "Current quantity is already at maximum (%d). Cannot add more.";
    }

    // ================== NESTED SUCCESS MESSAGE CLASS ==================
    public static final class SuccessfulMessage {
        private SuccessfulMessage() {}

        public static final String PRODUCT_ADDED =
                "NEW PRODUCT ADDED TO THE SYSTEM...";
        public static final String PRODUCT_DELETED =
                "PRODUCT WITH ID %d HAS BEEN DELETED";
        public static final String PRODUCT_FOUND =
                "\tPRODUCT FOUND !";
        public static final String PRODUCT_UPDATED =
                "PRODUCT DETAILS UPDATED.";
        public static final String QUANTITY_ADDED_SUCCESS =
                "QUANTITY ADDED SUCCESSFULLY!";
        public static final String QUANTITY_REDUCED_SUCCESS =
                "QUANTITY REDUCED SUCCESSFULLY!";
    }
    
    // ================== MESSAGE CLASS ==================
    public static final class Message {
        private Message() {}
        
        public static final String EXITING_PRODUCT_ADDITION = "\nEXITING PRODUCT ADDITION";
        public static final String PRODUCT_NOT_ADDED = "\nPRODUCT NOT ADDED. RETURNING TO THE MAIN MENU...";
        public static final String EXITING_PRODUCT_ADDITION_2 = "EXITING PRODUCT ADDITION...";
        public static final String EXITING_DELETE_OPERATION = "EXISITING DELETE OPERATION.";
        public static final String DELETION_CANCELLED = "DELETION CANCELLED.";
        public static final String EXITING_PRODUCT_DELETION = "EXITING PRODUCT DELETION";
    }
}
