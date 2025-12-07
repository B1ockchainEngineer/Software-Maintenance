package assignment.util.config;

public class TransactionConfig {
    private TransactionConfig() {
        // prevent instantiation
    }

    // ================== UI LABELS / TITLES ==================
    public static final String TITLE_TRANSACTION_REPORT = "[ TRANSACTION REPORT ]";
    public static final String TITLE_TRANSACTION_DETAILS = "TRANSACTION #%d DETAILS";
    public static final String TITLE_SUMMARY = "SUMMARY";
    
    // ================== COLUMN HEADERS ==================
    public static final String HEADER_NO = "NO.";
    public static final String HEADER_SUBTOTAL = "SUBTOTAL";
    public static final String HEADER_DISCOUNT = "DISCOUNT";
    public static final String HEADER_TAX = "TAX";
    public static final String HEADER_TOTAL = "TOTAL";
    public static final String HEADER_PRODUCT_ID = "PRODUCT ID";
    public static final String HEADER_PRODUCT_NAME = "PRODUCT NAME";
    public static final String HEADER_QUANTITY = "QUANTITY";
    public static final String HEADER_PRICE = "PRICE (RM)";
    public static final String HEADER_ITEM_TOTAL = "TOTAL (RM)";
    
    // ================== LABELS ==================
    public static final String LABEL_SUBTOTAL = "SUBTOTAL:";
    public static final String LABEL_DISCOUNT = "DISCOUNT:";
    public static final String LABEL_TAX = "TAX (6%%):";
    public static final String LABEL_TOTAL = "TOTAL:";
    public static final String LABEL_ITEMS = "ITEMS:";
    public static final String LABEL_NO_ITEMS = "ITEMS: (No items recorded)";
    public static final String LABEL_TOTAL_TRANSACTIONS = "TOTAL TRANSACTIONS:";
    
    // ================== MESSAGES ==================
    public static final String MSG_NO_TRANSACTIONS = "NO TRANSACTIONS FOUND.";
    public static final String MSG_SELECT_TRANSACTION = "ENTER TRANSACTION NUMBER TO VIEW DETAILS (1-%d, 0 TO EXIT): ";
    public static final String MSG_INVALID_TRANSACTION = "<<<INVALID TRANSACTION NUMBER!>>>";
    
    // ================== FILE FORMAT MARKERS ==================
    public static final String FILE_MARKER_TRANSACTION = "transaction";
    public static final String FILE_MARKER_TRANSACTION_OLD = "TRANSACTION"; // Backward compatibility
    public static final String FILE_MARKER_ITEM = "ITEM";
    
    // ================== FILE & PATH CONFIG ==================
    public static final String DATA_DIR = "data/";
    public static final String TRANSACTION_FILE_PATH = DATA_DIR + "transaction.txt";
    
    // ================== BUSINESS CONSTANTS ==================
    public static final double TAX_RATE = 0.06; // 6% tax rate
    
    // ================== PAYMENT UI ==================
    public static final String TITLE_PAYMENT_MENU = "[ MAKE PAYMENT ]";
    public static final String TITLE_PAYMENT_SUMMARY = "PAYMENT SUMMARY";
    
    // ================== PAYMENT LABELS ==================
    public static final String LABEL_PAYMENT_SUBTOTAL = "SUBTOTAL:";
    public static final String LABEL_PAYMENT_DISCOUNT = "DISCOUNT:";
    public static final String LABEL_PAYMENT_TAX = "TAX (6%%):";
    public static final String LABEL_PAYMENT_TOTAL = "TOTAL:";
    
    // ================== PAYMENT MESSAGES ==================
    public static final String MSG_PAYMENT_SUCCESS = "PAYMENT PROCESSED SUCCESSFULLY!";
    public static final String MSG_PAYMENT_SAVED = "Transaction and items have been saved to transaction.txt";
    public static final String MSG_PAYMENT_FAILED = "<<<PAYMENT FAILED.>>>";
    public static final String MSG_PAYMENT_CANCELLED = "PAYMENT CANCELLED";
    public static final String MSG_PAYMENT_CONFIRMATION_CANCELLED = "PAYMENT CANCELLED BY USER";
    public static final String MSG_CART_EMPTY = "<<<CART IS EMPTY. PLEASE ADD ITEMS BEFORE PAYMENT.>>>";
    
    // ================== PAYMENT PROMPTS ==================
    public static final String PROMPT_MEMBER_ID = "ENTER MEMBER ID FOR DISCOUNT (OR PRESS '0' FOR NO DISCOUNT/ PRESS X TO EXIT): M-";
    public static final String PROMPT_DISCOUNT_RATE = "ENTER DISCOUNT RATE (e.g., 0.1 for 10%): ";
    public static final String PROMPT_CONFIRM_PAYMENT = "\nCONFIRM PAYMENT? (Y=YES, N=NO): ";
    
    // ================== PAYMENT ERRORS ==================
    public static final String ERROR_INVALID_DISCOUNT_RATE = "<<<Invalid discount rate. Proceeding without discount.>>>";
    
    // ================== SUMMARY LABELS ==================
    public static final String LABEL_GRAND_TOTAL = "TOTAL:";
}

