package assignment.controller;

import assignment.model.Membership;
import assignment.model.PaymentResult;
import assignment.model.Transaction;
import assignment.service.MemberService;
import assignment.service.PaymentService;
import assignment.service.SalesService;
import assignment.service.TransactionService;
import assignment.util.ConsoleUtil;
import assignment.util.SalesUtil;
import assignment.util.config.SalesConfig;
import assignment.util.ValidationUtil;
import assignment.view.SalesView;
import java.util.List;

import static assignment.util.SalesUtil.*;

/**
 * Controller for payment processing and transaction management operations.
 * Handles payment calculation, confirmation, transaction creation, and transaction viewing.
 */
public class PaymentController {
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final MemberService memberService;
    private final SalesService salesService;
    private final SalesView salesView;

    public PaymentController(PaymentService paymentService, TransactionService transactionService, 
                            MemberService memberService, SalesService salesService) {
        this.paymentService = paymentService;
        this.transactionService = transactionService;
        this.memberService = memberService;
        this.salesService = salesService;
        this.salesView = new SalesView();
    }

    /**
     * Gets member discount input and calculates discount rate.
     * @return discount rate, or -1 if user cancelled
     */
    private double getMemberDiscount() {
        salesView.printMemberIdPrompt();
        String memberInput = ValidationUtil.scanner.nextLine().trim();

        // Check if user wants to exit
        if (memberInput.equalsIgnoreCase("X")) {
            salesView.printPaymentCancelled();
            return -1;
        }

        // Get discount rate from service (business logic moved to service layer)
        MemberService.DiscountResult discountResult = memberService.getDiscountRate(memberInput);
        double discountRate = discountResult.getDiscountRate();

        // Display error message if any
        if (discountResult.hasError()) {
            salesView.printMemberErrorMessage(discountResult.getErrorMessage());
        } else if (discountResult.getMember() != null) {
            // Display member info if found
            Membership member = discountResult.getMember();
            salesView.printMemberFoundMessage(member.getName(), member.getMemberType(), discountRate);
        }

        return discountRate;
    }

    /**
     * Confirms payment and processes the transaction.
     * @param discountRate The discount rate to apply
     * @return true if payment was successful, false otherwise
     */
    private boolean confirmAndProcessPayment(double discountRate) {
        // Display payment summary
        PaymentResult summary = paymentService.calculatePaymentSummary(discountRate);
        
        if (summary == null) {
            salesView.printPaymentFailure();
            return false;
        }

        salesView.printPaymentSummary(summary);

        // Ask for confirmation
        salesView.printPaymentConfirmationPrompt();
        char confirm = SalesUtil.readYesNo();

        if (confirm != 'Y') {
            salesView.printPaymentConfirmationCancelled();
            return false;
        }

        // Create transaction and save it
        Transaction transaction = paymentService.createTransaction(discountRate);
        
        if (transaction != null) {
            // Save transaction through service layer
            transactionService.saveTransaction(transaction);
            
            // Clear cart after successful save
            paymentService.clearCart();
            
            salesView.printPaymentSuccess();
            return true;
        } else {
            salesView.printPaymentFailure();
            return false;
        }
    }

    /**
     * Lists all orders from the cart for payment display.
     * @param emptyMessage Custom message to display when cart is empty
     * @param showHeader Whether to show "ALL ORDERS:" header
     * @return true if orders exist and were displayed, false if cart is empty
     */
    private boolean listAllOrdersForPayment(String emptyMessage, boolean showHeader) {
        List<assignment.model.Stock> cartItems = salesService.getCartItems();
        
        if (cartItems.isEmpty()) {
            String message = (emptyMessage != null) ? emptyMessage : SalesConfig.MSG_NO_ORDERS_IN_CART;
            salesView.printEmptyCartMessage(message);
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return false;
        }

        // Display all orders with their numbers
        if (showHeader) {
            salesView.printAllOrdersHeader();
        }
        salesView.displayCartItems(cartItems);
        if (showHeader) {
            salesView.printEmptyLine();
        }
        return true;
    }

    /**
     * Processes payment for items in the cart.
     */
    public void makePayment() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        salesView.printPaymentMenu();

        // Display all orders from the cart (no header for payment screen)
        if (!listAllOrdersForPayment(SalesConfig.MSG_CART_EMPTY, false)) {
            return; // Cart is empty, exit early
        }

        // Get member discount
        double discountRate = getMemberDiscount();
        if (discountRate == -1) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        // Confirm and process payment
        confirmAndProcessPayment(discountRate);

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    // ================== TRANSACTION VIEWING ==================

    /**
     * Displays transaction details for selected transaction.
     * @param transactions The list of all transactions
     * @param transactionNo The transaction number selected by user
     */
    private void displayTransactionDetails(List<Transaction> transactions, int transactionNo) {
        if (transactionNo >= 1 && transactionNo <= transactions.size()) {
            Transaction selectedTransaction = transactions.get(transactionNo - 1);
            
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            salesView.printTransactionDetails(selectedTransaction, transactionNo);
            
            ConsoleUtil.systemPause();
        } else {
            salesView.printInvalidTransactionNumber();
            ConsoleUtil.systemPause();
        }
    }

    /**
     * Displays transaction report with summary and allows viewing individual transaction details.
     */
    public void viewTransactionReport() {
        List<Transaction> transactions = transactionService.getAllTransactions();

        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            
            // Show transaction summary
            salesView.printTransactionSummary(transactions);

            if (transactions.isEmpty()) {
                ConsoleUtil.systemPause();
                ConsoleUtil.clearScreen();
                return;
            }

            // Ask user to select transaction
            salesView.printTransactionSelectionPrompt(transactions.size());
            int transactionNo = ValidationUtil.intValidation(0, transactions.size());

            if (transactionNo == INVALID_INPUT) {
                ConsoleUtil.systemPause();
                continue;
            }

            if (transactionNo == 0) {
                // User wants to exit
                ConsoleUtil.clearScreen();
                return;
            }

            // Show selected transaction details
            displayTransactionDetails(transactions, transactionNo);
        }
    }
}

