package assignment.util.config;

public class SalesConfig {
    private SalesConfig() {
        // prevent instantiation
    }

    // ================== UI LABELS / TITLES ==================
    public static final String TITLE_TRANSACTION_REPORT = "[ TRANSACTION REPORT ]";
    public static final String TITLE_TRANSACTION_DETAILS = "TRANSACTION #%d DETAILS";
    
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
    public static final String HEADER_ORDER_NO = "ORDER NO";
    
    // ================== LABELS ==================
    // Base financial labels (reused across payment/transaction)
    public static final String LABEL_SUBTOTAL = "SUBTOTAL:";
    public static final String LABEL_DISCOUNT = "DISCOUNT:";
    public static final String LABEL_TAX = "TAX (6%%):";
    public static final String LABEL_TOTAL = "TOTAL:";
    public static final String LABEL_ITEMS = "ITEMS:";
    public static final String LABEL_NO_ITEMS = "ITEMS: (No items recorded)";
    public static final String LABEL_TOTAL_TRANSACTIONS = "TOTAL TRANSACTIONS:";
    // Alias to avoid duplicate literals for grand totals
    public static final String LABEL_GRAND_TOTAL = LABEL_TOTAL;
    public static final String LABEL_PRODUCT_NAME = "PRODUCT NAME:";
    public static final String LABEL_PRODUCT_PRICE = "PRODUCT PRICE:";
    public static final String LABEL_AVAILABLE_QUANTITY = "AVAILABLE QUANTITY:";
    public static final String LABEL_TOTAL_COST = "TOTAL COST:";
    public static final String LABEL_QUANTITY = "QUANTITY:";
    public static final String LABEL_ORDER_NO = "ORDER NO";
    public static final String LABEL_CURRENT_QUANTITY_IN_ORDER = "CURRENT QUANTITY IN ORDER:";
    public static final String LABEL_AVAILABLE_QUANTITY_IN_INVENTORY = "AVAILABLE QUANTITY IN INVENTORY:";
    public static final String LABEL_NEW_ORDER_QUANTITY = "NEW ORDER QUANTITY:";
    public static final String LABEL_CART_ITEMS = "CART ITEMS:";
    public static final String LABEL_ALL_ORDERS = "ALL ORDERS:";
    public static final String LABEL_ORDER_DETAILS = "ORDER DETAILS:";
    public static final String LABEL_MEMBER_FOUND = "Member found:";
    public static final String LABEL_DISCOUNT_RATE = "Discount rate:";
    
    // ================== MESSAGES ==================
    public static final String MSG_NO_TRANSACTIONS = "NO TRANSACTIONS FOUND.";
    public static final String MSG_SELECT_TRANSACTION = "ENTER TRANSACTION NUMBER TO VIEW DETAILS (1-%d, 0 TO EXIT): ";
    public static final String MSG_INVALID_TRANSACTION = "<<<INVALID TRANSACTION NUMBER!>>>";
    
    // ================== FILE FORMAT MARKERS ==================
    public static final String FILE_MARKER_TRANSACTION = "transaction";
    
    // ================== FILE & PATH CONFIG ==================
    public static final String TRANSACTION_FILE_PATH = AppConfig.DATA_DIR + "transaction.txt";
    
    // ================== BUSINESS CONSTANTS ==================
    public static final double TAX_RATE = 0.06; // 6% tax rate
    
    // ================== SALES / ORDER TITLES ==================
    public static final String TITLE_SALES_MENU = "[ SALES MANAGEMENT SYSTEM ]";
    public static final String TITLE_ORDERING_MANAGEMENT = "[ ORDERING MANAGEMENT]";
    public static final String TITLE_AVAILABLE_ITEMS = "            AVAILABLE ITEMS FOR PURCHASE";
    public static final String TITLE_ORDERING_SYSTEM = "[ ORDERING SYSTEM ]";
    public static final String TITLE_SEARCH_ORDER = "[ SEARCH AN ORDER ]";
    public static final String TITLE_REMOVE_ORDER = "[ REMOVE AN ORDER ]";
    public static final String TITLE_EDIT_ORDER = "[ EDIT AN ORDER ]";
    public static final String TITLE_ORDER_ADDED = "[ORDER ADDED]";
    
    // ================== ORDER MESSAGES ==================
    public static final String MSG_ADD_ORDER_FAILURE = "<<<Failed to add order. Invalid item or quantity.>>>";
    // Consolidated: single order not found (used for search, remove, edit operations)
    public static final String MSG_ORDER_NOT_FOUND = "<<<NO ORDER FOUND IN THE CART!>>>";
    // Plural: entire cart is empty
    public static final String MSG_NO_ORDERS_IN_CART = "<<<NO ORDERS FOUND IN THE CART!>>>";
    public static final String MSG_ORDER_REMOVED_SUCCESS = "ORDER REMOVED SUCCESSFULLY";
    public static final String MSG_ORDER_REMOVAL_FAILED = "ORDER REMOVAL FAILED";
    public static final String MSG_ORDER_REMOVAL_CANCELLED = "ORDER REMOVAL CANCELLED";
    public static final String MSG_STOCK_NOT_FOUND = "<<<UNABLE TO FIND THE STOCK IN INVENTORY!>>>";
    public static final String MSG_EDIT_CANCELLED = "EDIT CANCELLED";
    public static final String MSG_INVALID_CHOICE = "Invalid choice input.";
    public static final String MSG_INVALID_QUANTITY_LIMITS = "<<<Invalid quantity. Check limits.>>>";
    public static final String MSG_INVALID_QUANTITY_INPUT = "<<<Invalid quantity input.>>>";
    public static final String MSG_QUANTITY_SUCCESS = "QUANTITY %s SUCCESSFULLY";
    public static final String MSG_FULL_QUANTITY_DELETE_WARNING = "REDUCING FULL QUANTITY WILL DELETE THE ORDER";
    public static final String MSG_INVALID_ITEM_ID = "<<<The item ID is not matched or no quantity available, please enter the correct one!>>>";
    public static final String MSG_INVALID_QUANTITY_RANGE = "<<<Invalid quantity. Please enter a quantity between 1 and %d>>>";
    
    // ================== ORDER PROMPTS ==================
    public static final String PROMPT_PRODUCT_ID = "PRODUCT ID(0 TO STOP ORDER): ";
    public static final String PROMPT_DESIRED_QUANTITY = "ENTER DESIRED QUANTITY(Enter 999 to re-enter product ID): ";
    public static final String PROMPT_FINISHED_ORDERING = "FINISHED ORDERING? (Y=YES, N=NO): ";
    public static final String PROMPT_ORDER_NO_SEARCH = "ENTER ORDER NO TO SEARCH: ";
    public static final String PROMPT_ORDER_NO_REMOVE = "ENTER ORDER NO TO REMOVE: ";
    public static final String PROMPT_DELETE_ORDER_CONFIRM = "DO YOU WANT TO DELETE THIS ORDER (Y = YES, N = NO): ";
    public static final String PROMPT_ORDER_NO_EDIT = "ENTER ORDER NO TO EDIT: ";
    public static final String PROMPT_EDIT_CHOICE = "ENTER YOUR CHOICE: ";
    public static final String PROMPT_QUANTITY_TO_REDUCE = "ENTER QUANTITY TO REDUCE (1 to %d): ";
    public static final String PROMPT_QUANTITY_TO_ADD = "ENTER QUANTITY TO ADD (1 to %d): ";
    
    // ================== ORDER MENU OPTIONS ==================
    public static final String OPTION_REDUCE_QUANTITY = "1. REDUCE QUANTITY";
    public static final String OPTION_ADD_QUANTITY = "2. ADD QUANTITY";
    public static final String OPTION_CANCEL = "0. CANCEL";
    
    // ================== PAYMENT UI ==================
    public static final String TITLE_PAYMENT_MENU = "[ MAKE PAYMENT ]";
    public static final String TITLE_PAYMENT_SUMMARY = "PAYMENT SUMMARY";
    
    // ================== PAYMENT LABELS ==================
    // Payment labels reuse base financial labels to stay DRY
    public static final String LABEL_PAYMENT_SUBTOTAL = LABEL_SUBTOTAL;
    public static final String LABEL_PAYMENT_DISCOUNT = LABEL_DISCOUNT;
    public static final String LABEL_PAYMENT_TAX = LABEL_TAX;
    public static final String LABEL_PAYMENT_TOTAL = LABEL_TOTAL;
    
    // ================== PAYMENT MESSAGES ==================
    public static final String MSG_PAYMENT_SUCCESS = "PAYMENT PROCESSED SUCCESSFULLY!";
    public static final String MSG_PAYMENT_FAILED = "<<<PAYMENT FAILED.>>>";
    public static final String MSG_PAYMENT_CANCELLED = "PAYMENT CANCELLED";
    public static final String MSG_CART_EMPTY = "<<<CART IS EMPTY. PLEASE ADD ITEMS BEFORE PAYMENT.>>>";
    
    // ================== PAYMENT PROMPTS ==================
    public static final String PROMPT_MEMBER_ID = "ENTER MEMBER ID FOR DISCOUNT (OR PRESS '0' FOR NO DISCOUNT/ PRESS X TO EXIT): M-";
    public static final String PROMPT_CONFIRM_PAYMENT = "\nCONFIRM PAYMENT? (Y=YES, N=NO): ";
    
}

