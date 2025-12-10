package assignment.controller;

import assignment.model.Membership;
import assignment.model.Order;
import assignment.model.PaymentResult;
import assignment.model.Transaction;
import assignment.service.MemberService;
import assignment.service.PaymentService;
import assignment.service.OrderService;
import assignment.service.TransactionService;
import assignment.util.ConsoleUtil;
import assignment.util.config.SalesConfig;
import assignment.util.ValidationUtil;
import assignment.view.PaymentView;
import java.util.List;


/**
 * Controller for payment processing operations.
 */
public class PaymentController {
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final MemberService memberService;
    private final OrderService orderService;
    private final PaymentView paymentView;

    /**
     * Constructs a PaymentController with the specified services.
     * @param paymentService The payment service for payment calculations
     * @param transactionService The transaction service for transaction persistence
     * @param memberService The member service for member discount lookup
     * @param orderService The order service for cart operations
     */
    public PaymentController(PaymentService paymentService, TransactionService transactionService, 
                            MemberService memberService, OrderService orderService) {
        this.paymentService = paymentService;
        this.transactionService = transactionService;
        this.memberService = memberService;
        this.orderService = orderService;
        this.paymentView = new PaymentView();
    }

    /**
     * Gets member discount input and calculates discount rate.
     * @return discount rate, or -1 if user cancelled
     */
    private double getMemberDiscount() {
        paymentView.printMemberIdPrompt();
        String memberInput = ValidationUtil.scanner.nextLine().trim();

        // Check if user wants to exit
        if (memberInput.equalsIgnoreCase("X")) {
            paymentView.printPaymentCancelled();
            return -1;
        }

        // Get discount rate from service (business logic moved to service layer)
        MemberService.DiscountResult discountResult = memberService.getDiscountRate(memberInput);
        double discountRate = discountResult.getDiscountRate();

        // Display error message if any
        if (discountResult.hasError()) {
            paymentView.printMemberErrorMessage(discountResult.getErrorMessage());
        } else if (discountResult.getMember() != null) {
            // Display member info if found
            Membership member = discountResult.getMember();
            paymentView.printMemberFoundMessage(member.getName(), member.getMemberType(), discountRate);
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
            paymentView.printPaymentFailure();
            return false;
        }

        paymentView.printPaymentSummary(summary);

        // Ask for confirmation
        char confirm = ValidationUtil.confirmValidation(SalesConfig.PROMPT_CONFIRM_PAYMENT);

        if (confirm != 'Y') {
            paymentView.printPaymentConfirmationCancelled();
            return false;
        }

        // Create transaction and save it
        Transaction transaction = paymentService.createTransaction(discountRate);
        
        if (transaction != null) {
            // Save transaction through service layer
            transactionService.saveTransaction(transaction);
            
            // Clear cart after successful save
            paymentService.clearCart();
            
            paymentView.printPaymentSuccess();
            return true;
        } else {
            paymentView.printPaymentFailure();
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
        List<Order> cartItems = orderService.getCartItems();
        
        if (cartItems.isEmpty()) {
            String message = (emptyMessage != null) ? emptyMessage : SalesConfig.MSG_NO_ORDERS_IN_CART;
            paymentView.printEmptyCartMessage(message);
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return false;
        }

        // Display all orders with their numbers
        if (showHeader) {
            paymentView.printAllOrdersHeader();
        }
        paymentView.displayCartItems(cartItems);
        if (showHeader) {
            paymentView.printEmptyLine();
        }
        return true;
    }

    /**
     * Processes payment for items in the cart.
     * Displays cart items, prompts for member discount, calculates payment summary,
     * asks for confirmation, and processes the transaction if confirmed.
     * Cart is cleared after successful payment.
     */
    public void makePayment() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        paymentView.printPaymentMenu();

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

}

