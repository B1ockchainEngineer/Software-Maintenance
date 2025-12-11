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
        public static final String INVALID_PRICE =
                "<<<PRICE CANNOT BE LESS THAN RM 1.00!!!>>>";
        public static final String INVALID_OPTION =
                "<<<INVALID OPTION>>>";

        // Generic formatted error (for exceptions)
        public static final String ADD_STOCK_FAILED_TEMPLATE =
                "<<< ERROR ADDING STOCK: %s >>>";
        public static final String DELETE_FAILED =
                "PRODUCT DELETION FAILED.";
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
    }
}
