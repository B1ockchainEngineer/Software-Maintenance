package assignment;

import assignment.view.MainView;
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
import assignment.repo.*;
import assignment.service.*;
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

    private final MainView mainView;

    // Current logged-in staff
    private Staff currentStaff;


    public static void main(String[] args) {
        Main main = new Main();
        main.entry();
    }

    // Initialise repository, services and controllers and wire them to this Main
    public Main() {
        this.mainView = new MainView();

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

        // Member-related setup
        MemberRepository memberRepo = new MemberRepository();
        MemberService memberService = new MemberService(memberRepo);
        this.memberController = new MemberController(memberService);
    }

    public void entry() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            mainView.printLoginMenu();
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
                    mainView.printExitMessage();
                    System.exit(0);
                    break;
            }
        }
    }

    public void run() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            mainView.printMainMenu(currentStaff);
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
                    mainView.printLoggedOutMessage();
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
            mainView.printStockMenu();
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
                    mainView.printBackToMainMessage();
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
            mainView.printSalesMenu();
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
                    mainView.printBackToMainMessage();
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
            mainView.printCreateOrderMenu();
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
                    mainView.printBackToPreviousMessage();
                    return; // Exit order loop
                }
            }
        }
    }

    public void transactionRecord() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        TransactionRepository transactionRepo = new TransactionRepository();
        java.util.List<TransactionRepository.Transaction> transactions = transactionRepo.loadAllTransactions();
        mainView.printTransactionReport(transactions);
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

}