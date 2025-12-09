package assignment;

import assignment.view.MainView;
import assignment.view.SalesView;
import assignment.controller.OrderController;
import assignment.controller.PaymentController;
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

import assignment.util.SalesUtil;

public class Main {

    public static int totalPayment = 0; // Global accumulator

    // Controllers shared for this Main instance
    private final StockController stockController;
    private final OrderController orderController;
    private final PaymentController paymentController;
    private final MemberController memberController;
    private final StaffController staffController;
    private final LoginController loginController;
    private final SignupController signupController;

    private final MainView mainView;
    private final SalesView salesView;

    // Current logged-in staff
    private Staff currentStaff;


    public static void main(String[] args) {
        Main main = new Main();
        main.entry();
    }

    // Initialise repository, services and controllers and wire them to this Main
    public Main() {
        this.mainView = new MainView();
        this.salesView = new SalesView();

        // Stock-related setup
        StockRepository stockRepo = new StockRepository();
        StockService stockService = new StockService(stockRepo);
        this.stockController = new StockController(stockService);

        // Sales-related setup
        OrderRepository orderRepo = new OrderRepository();
        SalesService salesService = new SalesService(stockRepo, orderRepo);
        TransactionRepository transactionRepo = new TransactionRepository();
        TransactionService transactionService = new TransactionService(transactionRepo);
        PaymentService paymentService = new PaymentService(stockRepo, orderRepo);

        // Staff-related setup
        StaffRepository staffRepo = new StaffRepository();
        StaffService staffService = new StaffService(staffRepo);
        this.staffController = new StaffController(staffService);
        this.loginController = new LoginController(staffService);
        this.signupController = new SignupController(staffService);

        // Member-related setup (used by both PaymentController for discounts and MemberController for management)
        MemberRepository memberRepo = new MemberRepository();
        MemberService memberService = new MemberService(memberRepo);
        this.memberController = new MemberController(memberService);
        
        // Initialize controllers
        this.orderController = new OrderController(salesService);
        this.paymentController = new PaymentController(paymentService, transactionService, memberService, salesService);
    }

    public void entry() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            mainView.printLoginMenu();
            mainView.printSelectionPrompt();

            // Input Validation using ValidationUtil (range 1 to 3)
            int logMenuOpt = ValidationUtil.intValidation(1, 3);

            if (logMenuOpt == SalesUtil.INVALID_INPUT) {
                ConsoleUtil.systemPause();
                continue;
            }

            LogMenu logSelection = LogMenu.getByOption(logMenuOpt);
            if (logSelection == null) {
                mainView.printInvalidOptionMessage();
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
            mainView.printSelectionPrompt();

            // Max option is 4 (STOCK_MANAGEMENT)
            int opt = ValidationUtil.intValidation(0, 4);

            if (opt == SalesUtil.INVALID_INPUT) {
                ConsoleUtil.systemPause();
                continue;
            }

            MainMenu selection = MainMenu.getByOption(opt);
            if (selection == null) {
                mainView.printInvalidOptionMessage();
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
            mainView.printSelectionPrompt();

            int stockOpt = ValidationUtil.intValidation(0, 4);

            if (stockOpt == SalesUtil.INVALID_INPUT) {
                ConsoleUtil.systemPause();
                continue;
            }

            StockMenu selection = StockMenu.getByOption(stockOpt);

            if (selection == null) {
                mainView.printInvalidOptionMessage();
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case ADD_PRODUCT -> stockController.add();
                case DELETE_PRODUCT -> stockController.delete();
                case VIEW_PRODUCT_LIST -> stockController.view();
                case EDIT_PRODUCT -> stockController.edit();
                case BACK_TO_MAIN -> {
                    mainView.printBackToMainMessage();
                    ConsoleUtil.systemPause();
                    return;
                }
            }
        }
    }

    public void runSales() throws IOException {
        // *** REFACTORED: Uses SalesMenu and delegates Order logic ***
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            salesView.printSalesMenu();
            mainView.printSelectionPrompt();

            int salesOpt = ValidationUtil.intValidation(0, 2);

            if (salesOpt == SalesUtil.INVALID_INPUT) {
                ConsoleUtil.systemPause();
                continue;
            }

            SalesMenu selection = SalesMenu.getByOption(salesOpt);

            if (selection == null) {
                mainView.printInvalidOptionMessage();
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case MAKE_ORDER -> {
                    runOrder();
                }
                case TRANSACTION_REPORT -> {
                    paymentController.viewTransactionReport();
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
        // *** REFACTORED: Delegates to OrderController and PaymentController ***
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            salesView.printOrderMenu();
            mainView.printSelectionPrompt();

            int orderOpt = ValidationUtil.intValidation(0, 5);

            if (orderOpt == SalesUtil.INVALID_INPUT) {
                ConsoleUtil.systemPause();
                continue;
            }

            OrderMenu selection = OrderMenu.getByOption(orderOpt);

            if (selection == null) {
                mainView.printInvalidOptionMessage();
                ConsoleUtil.systemPause();
                continue;
            }

            switch (selection) {
                case ADD_ORDER -> orderController.addOrder();
                case EDIT_ORDER -> orderController.editOrder();
                case SEARCH_ORDER -> orderController.searchOrder();
                case REMOVE_ORDER -> orderController.removeOrder();
                case MAKE_PAYMENT -> {
                    paymentController.makePayment();
                }
                case BACK_TO_PREVIOUS -> {
                    mainView.printBackToPreviousMessage();
                    return; // Exit order loop
                }
            }
        }
    }


}