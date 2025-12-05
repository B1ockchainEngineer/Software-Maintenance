package assignment;

import assignment.controller.SalesController;
import assignment.controller.StockController;
import assignment.controller.MemberController;
import assignment.controller.StaffController;
import assignment.controller.LoginController;
import assignment.controller.SignupController;
import assignment.model.Staff;
import assignment.enums.LogMenu;
import assignment.enums.MainMenu;
import assignment.enums.OrderMenu;
import assignment.enums.SalesMenu;
import assignment.enums.StockMenu;
import assignment.repo.PaidItemRepository;
import assignment.repo.StaffRepository;
import assignment.repo.StockRepository;
import assignment.repo.TransactionRepository;
import assignment.service.PaymentService;
import assignment.service.SalesService;
import assignment.service.StaffService;
import assignment.service.StockService;
import assignment.util.ConsoleUtil; // Utility for logo, clearScreen, pause
import assignment.util.ValidationUtil; // Utility for input validation
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static int totalPayment = 0; // Global accumulator

    // Controllers shared for this Main instance
    private final StockController stockController;
    private final SalesController salesController;
    private final MemberController memberController;
    private final StaffController staffController;
    private final LoginController loginController;
    private final SignupController signupController;

    // Current logged-in staff
    private Staff currentStaff;


    public static void main(String[] args) {
        Main main = new Main();
        main.entry();
    }

    // Initialise repository, services and controllers and wire them to this Main
    public Main() {
        // Stock-related setup
        StockRepository stockRepo = new StockRepository();
        StockService stockService = new StockService(stockRepo);
        this.stockController = new StockController(stockService);

        // Sales-related setup
        SalesService salesService = new SalesService(stockRepo);
        PaidItemRepository paidItemRepo = new PaidItemRepository();
        TransactionRepository transactionRepo = new TransactionRepository();
        PaymentService paymentService = new PaymentService(stockRepo, paidItemRepo, transactionRepo);
        this.salesController = new SalesController(salesService, paymentService);

        // Staff-related setup
        StaffRepository staffRepo = new StaffRepository();
        StaffService staffService = new StaffService(staffRepo);
        this.staffController = new StaffController(staffService);
        this.loginController = new LoginController(staffService);
        this.signupController = new SignupController(staffService);

        // Member controller (relies directly on models/utilities)
        this.memberController = new MemberController();
    }

    // --- Menu Presentation (Uses Enums) ---

    public void logMenu() {
        System.out.println("[ LOGIN MENU ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("Welcome to TAR CAFE Management System");
        System.out.println("Please select an option:");
        System.out.println("-------------------------------------------------------");
        for (LogMenu menu : LogMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void menu() {
        System.out.println("[ MAIN MENU ]");
        System.out.println("-------------------------------------------------------");
        if (currentStaff != null) {
            System.out.println("Logged in as: " + currentStaff.getName().toUpperCase());
            System.out.println("-------------------------------------------------------");
        }
        System.out.println("Please select an option:");
        System.out.println("-------------------------------------------------------");
        for (MainMenu menu : MainMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void stockMenu() {
        System.out.println("[ FOOD AND BEVERAGE MANAGEMENT SYSTEM ]");
        System.out.println("-------------------------------------------------------");
        for (StockMenu menu : StockMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void salesMenu() {
        System.out.println("[ SALES MANAGEMENT SYSTEM ]");
        System.out.println("-------------------------------------------------------");
        for (SalesMenu menu : SalesMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void createOrderMenu() {
        System.out.println("[ ORDERING MANAGEMENT]");
        System.out.println("-------------------------------------------------------");
        for (OrderMenu menu : OrderMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void entry() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            logMenu();
            System.out.print("ENTER YOUR SELECTION: ");

            // Input Validation using ValidationUtil (range 1 to 3)
            int logMenuOpt = ValidationUtil.intValidation(1, 3);

            if (logMenuOpt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            LogMenu logSelection = LogMenu.getByOption(logMenuOpt);
            if (logSelection == null) {
                System.out.println("<<<INVALID OPTION!>>>");
                ConsoleUtil.systemPause();
                continue;
            }

            switch (logSelection) {
                case SIGN_UP:
                    // SIGN UP/REGISTRATION - Delegated to SignupController
                    signupController.performSignup();
                    break;

                case LOG_IN:
                    // LOG IN - Delegated to LoginController
                    Staff loggedInStaff = loginController.performLogin();
                    if (loggedInStaff != null) {
                        this.currentStaff = loggedInStaff;
                        run();
                        return; // Exit entry loop after successful login
                    }
                    // If login was cancelled, continue to show menu again
                    break;

                case EXIT:
                    // EXIT
                    ConsoleUtil.clearScreen();
                    ConsoleUtil.logo();
                    System.out.println("\n========================================");
                    System.out.println("  THANK YOU FOR USING TAR CAFE SYSTEM");
                    System.out.println("========================================");
                    System.out.println("EXITING THE PROGRAM...\n");
                    System.exit(0);
                    break;
            }
        }
    }

    public void run() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            menu();
            System.out.print("ENTER YOUR SELECTION: ");

            // Max option is 4 (STOCK_MANAGEMENT)
            int opt = ValidationUtil.intValidation(0, 4);

            if (opt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            MainMenu selection = MainMenu.getByOption(opt);
            if (selection == null) {
                System.out.println("<<<INVALID OPTION!>>>");
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case MEMBERSHIP_MANAGEMENT -> {
                    memberController.manageMembers();
                }
                case SALES_MANAGEMENT -> {
                    try {
                        runSales();
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                case STAFF_MANAGEMENT -> {
                    staffController.manageStaff();
                }
                case STOCK_MANAGEMENT -> {
                    runStock(); // Calls StockController methods
                }
                case LOGOUT -> {
                    // Logout current staff
                    if (currentStaff != null) {
                        loginController.logout();
                        currentStaff = null;
                    }
                    System.out.println("\nRETURNING TO LOGIN MENU...");
                    ConsoleUtil.systemPause();
                    return; // Return to entry loop
                }
            }
        }
    }

    public void runStock() {
        // *** REFACACTORED: Delegates to StockController ***
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            stockMenu();
            System.out.print("ENTER YOUR SELECTION: ");

            int stockOpt = ValidationUtil.intValidation(0, 3);

            if (stockOpt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            StockMenu selection = StockMenu.getByOption(stockOpt);

            if (selection == null) {
                System.out.println("<<<INVALID OPTION!>>>");
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case ADD_PRODUCT -> stockController.add();
                case DELETE_PRODUCT -> stockController.delete();
                case VIEW_PRODUCT_LIST -> stockController.view();
                case BACK_TO_MAIN -> {
                    System.out.println("BACK TO MAIN MENU...");
                    ConsoleUtil.systemPause();
                    return;
                }
            }
        }
    }

    public void runSales() throws IOException {
        // *** REFACACTORED: Uses SalesMenu and delegates Order logic ***
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            salesMenu();
            System.out.print("ENTER YOUR SELECTION: ");

            int salesOpt = ValidationUtil.intValidation(0, 2);

            if (salesOpt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            SalesMenu selection = SalesMenu.getByOption(salesOpt);

            if (selection == null) {
                System.out.println("<<<INVALID OPTION!>>>");
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case MAKE_ORDER -> {
                    runOrder();
                }
                case TRANSACTION_REPORT -> {
                    transactionRecord();
                }
                case BACK_TO_MAIN -> {
                    System.out.println("BACK TO MAIN MENU...");
                    ConsoleUtil.systemPause();
                    return; // Exit sales loop
                }
            }
        }
    }

    public void runOrder() throws IOException {
        // *** REFACACTORED: Delegates to SalesController ***
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            createOrderMenu();
            System.out.print("ENTER YOUR SELECTION: ");

            int orderOpt = ValidationUtil.intValidation(0, 5);

            if (orderOpt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            OrderMenu selection = OrderMenu.getByOption(orderOpt);

            if (selection == null) {
                System.out.println("<<<INVALID OPTION!>>>");
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case ADD_ORDER -> salesController.addOrder();
                case EDIT_ORDER -> salesController.editOrder();
                case SEARCH_ORDER -> salesController.searchOrder();
                case REMOVE_ORDER -> salesController.removeOrder();
                case MAKE_PAYMENT -> {
                    salesController.makePayment();
                }
                case BACK_TO_PREVIOUS -> {
                    System.out.println("BACK TO PREVIOUS PAGE...");
                    return; // Exit order loop
                }
            }
        }
    }

    public static void transactionRecord() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ TRANSACTION REPORT ]");
        System.out.println("-------------------------------------------------------");

        TransactionRepository transactionRepo = new TransactionRepository();
        java.util.List<TransactionRepository.Transaction> transactions = transactionRepo.loadAllTransactions();

        if (transactions.isEmpty()) {
            System.out.println("NO TRANSACTIONS FOUND.");
        } else {
            System.out.printf("%-10s %-15s %-15s %-15s %-15s\n", "NO.", "SUBTOTAL", "DISCOUNT", "TAX", "TOTAL");
            System.out.println("-------------------------------------------------------");

            int transactionNo = 1;
            double grandTotalSubtotal = 0.0;
            double grandTotalDiscount = 0.0;
            double grandTotalTax = 0.0;
            double grandTotal = 0.0;

            for (TransactionRepository.Transaction transaction : transactions) {
                System.out.printf("%-10d RM%-14.2f RM%-14.2f RM%-14.2f RM%-14.2f\n",
                        transactionNo++,
                        transaction.getSubtotal(),
                        transaction.getDiscount(),
                        transaction.getTax(),
                        transaction.getTotal());

                grandTotalSubtotal += transaction.getSubtotal();
                grandTotalDiscount += transaction.getDiscount();
                grandTotalTax += transaction.getTax();
                grandTotal += transaction.getTotal();
            }

            System.out.println("-------------------------------------------------------");
            System.out.printf("%-10s RM%-14.2f RM%-14.2f RM%-14.2f RM%-14.2f\n",
                    "TOTAL:",
                    grandTotalSubtotal,
                    grandTotalDiscount,
                    grandTotalTax,
                    grandTotal);
            System.out.println("-------------------------------------------------------");
            System.out.println("TOTAL TRANSACTIONS: " + transactions.size());
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

}