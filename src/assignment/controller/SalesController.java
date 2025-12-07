package assignment.controller;

import assignment.model.PaymentResult;
import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.service.PaymentService;
import assignment.service.SalesService;
import assignment.service.TransactionService;
import assignment.util.ConsoleUtil;
import assignment.util.config.TransactionConfig;
import assignment.util.ValidationUtil;
import assignment.view.MainView;
import assignment.view.SalesView;
import java.io.IOException;
import java.util.List;

public class SalesController {
    private final SalesService salesService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final SalesView salesView;
    private final MainView mainView;

    public SalesController(SalesService salesService, PaymentService paymentService, TransactionService transactionService) {
        this.salesService = salesService;
        this.paymentService = paymentService;
        this.transactionService = transactionService;
        this.salesView = new SalesView();
        this.mainView = new MainView();
    }

    public void addOrder() throws IOException {
        char nextOrder = 'N';

        do {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            salesView.printOrderMenu();
            salesView.displayAvailableItems(salesService.getAvailableStock());

            int itemID;
            Stock foundStock = null;
            boolean reEnterProduct = false;

            // 1. Get Product ID
            do {
                System.out.print("PRODUCT ID(0 TO STOP ORDER): ");
                itemID = ValidationUtil.intValidation(0, 0);

                if (itemID == 0) break;

                if (itemID == -9999) {
                    reEnterProduct = true;
                    break;
                }

                foundStock = salesService.findStockItem(itemID);

                if (foundStock == null || foundStock.getQty() == 0) {
                    System.out.println("<<<The item ID is not matched or no quantity available, please enter the correct one!>>>");
                    itemID = -9999;
                } else {
                    salesView.printProductDetails(foundStock);
                    break;
                }
            } while (true);

            if (itemID == 0) break;
            if (reEnterProduct) continue;

            // 2. Get Quantity
            int quantity = -1;
            int maxQty = foundStock.getQty();

            do {
                System.out.print("ENTER DESIRED QUANTITY(Enter 999 to re-enter product ID): ");
                // No need for range checking here, as validation is complex (0 to maxQty) and done below
                quantity = ValidationUtil.intValidation(0, 10000);

                if (quantity == -9999) continue;
                if (quantity == 999) break; // Re-enter product ID

                if (quantity <= 0 || quantity > maxQty) {
                    System.out.println("<<<Invalid quantity. Please enter a quantity between 1 and " + maxQty + ">>>");
                } else {
                    // 3. Add to Cart (Business Logic Handled by Service)
                    boolean success = salesService.addToCart(itemID, quantity);
                    if (success) {
                        salesView.printCartSummary(foundStock, quantity);
                    } else {
                        salesView.printAddOrderFailure();
                    }
                    break; // Exit quantity input loop
                }
            } while (true);

            if (quantity == 999) {
                ConsoleUtil.systemPause();
                ConsoleUtil.clearScreen();
                continue; // Restart the outer loop
            }

            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();

            // 4. Ask for next order
            do {
                System.out.print("FINISHED ORDERING? (Y=YES, N=NO): ");
                nextOrder = ValidationUtil.charValidation();
            } while (nextOrder != 'Y' && nextOrder != 'N');

        } while (Character.toUpperCase(nextOrder) != 'Y');

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void searchOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        salesView.printSearchOrderMenu();

        System.out.print("ENTER ORDER NO TO SEARCH: ");
        int orderNoSearch = ValidationUtil.intValidation(1, 10000);

        if (orderNoSearch == -9999) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Stock item = salesService.findCartItemByOrderNo(orderNoSearch);

        if (item != null) {
            salesView.displayOrderDetail(item);
        } else {
            salesView.printOrderNotFound();
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void removeOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        salesView.printRemoveOrderMenu();

        System.out.print("ENTER ORDER NO TO REMOVE: ");
        int orderNoRemove = ValidationUtil.intValidation(1, 10000);

        if (orderNoRemove == -9999) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Stock cartItem = salesService.findCartItemByOrderNo(orderNoRemove);

        if (cartItem == null) {
            salesView.printOrderNotFound();
        } else {
            salesView.printRemoveConfirmation(cartItem);

            System.out.print("DO YOU WANT TO DELETE THIS ORDER (Y = YES, N = NO): ");
            char confirm = ValidationUtil.charValidation();

            if (confirm == 'Y') {
                if (salesService.removeOrder(orderNoRemove)) {
                    salesView.printRemoveSuccess();
                } else {
                    salesView.printRemoveFailure();
                }
            } else {
                salesView.printRemoveCancelled();
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void editOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        salesView.printEditOrderMenu();

        System.out.print("ENTER ORDER NO TO EDIT: ");
        int orderNoEdit = ValidationUtil.intValidation(1, 10000);
        System.out.println("-------------------------------------------------------");

        if (orderNoEdit == -9999) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Stock cartItem = salesService.findCartItemByOrderNo(orderNoEdit);

        if (cartItem == null) {
            System.out.println("<<<NO ORDER FOUND IN THE CART!>>>");
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Stock stockItem = salesService.findStockItem(cartItem.getStockID());

        if (stockItem == null) {
            System.out.println("<<<UNABLE TO FIND THE STOCK IN INVENTORY!>>>");
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        salesView.printEditOrderDetails(cartItem, stockItem);
        salesView.printEditOrderSubMenu();

        System.out.print("ENTER YOUR CHOICE: ");
        int choice = ValidationUtil.intValidation(0, 2);

        if (choice == 0) {
            System.out.println("EDIT CANCELLED");
        } else if (choice == -9999) {
            System.out.println("Invalid choice input.");
        } else {
            // Determine maximum possible change based on choice
            int maxChange = (choice == 1) ? cartItem.getQty() : stockItem.getQty();

            System.out.print((choice == 1 ?
                    "ENTER QUANTITY TO REDUCE (1 to " + maxChange + "): "
                    : "ENTER QUANTITY TO ADD (1 to " + maxChange + "): "));

            int quantityChange = ValidationUtil.intValidation(1, maxChange);

            if (quantityChange == -9999) {
                System.out.println("<<<Invalid quantity input.>>>");
            } else {
                boolean success = salesService.editOrderQuantity(orderNoEdit, quantityChange, choice);
                if (success) {
                    salesView.printEditSuccess((choice == 1 ? "REDUCED" : "ADDED"), salesService.findCartItemByOrderNo(orderNoEdit).getQty());
                } else {
                    salesView.printEditFailure();
                }
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void makePayment() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        salesView.printPaymentMenu();

        List<Stock> cart = salesService.getCartItems();

        if (cart.isEmpty()) {
            System.out.println(TransactionConfig.MSG_CART_EMPTY);
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        salesView.displayCartItems(cart);

        // Ask for member discount
        System.out.print(TransactionConfig.PROMPT_MEMBER_ID);
        String memberInput = ValidationUtil.scanner.nextLine().trim();

        // Check if user wants to exit
        if (memberInput.equalsIgnoreCase("X")) {
            salesView.printPaymentCancelled();
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        double discountRate = 0.0;
        if (!memberInput.equals("0") && !memberInput.isEmpty()) {
            // TODO: Look up member and get discount rate from MemberService
            // For now, we'll ask for discount rate if member ID is provided
            System.out.print(TransactionConfig.PROMPT_DISCOUNT_RATE);
            String discountInput = ValidationUtil.scanner.nextLine().trim();
            if (!discountInput.isEmpty()) {
                try {
                    discountRate = Double.parseDouble(discountInput);
                } catch (NumberFormatException e) {
                    System.out.println(TransactionConfig.ERROR_INVALID_DISCOUNT_RATE);
                }
            }
        }

        // Calculate and show payment summary (without processing)
        PaymentResult summary = paymentService.calculatePaymentSummary(discountRate);
        
        if (summary == null) {
            salesView.printPaymentFailure();
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        // Display payment summary
        salesView.printPaymentSummary(summary);

        // Ask for confirmation
        salesView.printPaymentConfirmationPrompt();
        char confirm = ValidationUtil.charValidation();

        if (confirm != 'Y') {
            salesView.printPaymentConfirmationCancelled();
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        // Create transaction and save it
        Transaction transaction = paymentService.createTransaction(discountRate);
        
        if (transaction != null) {
            // Save transaction through service layer
            transactionService.saveTransaction(transaction);
            
            // Clear cart after successful save
            paymentService.clearCart();
            
            salesView.printPaymentSuccess();
        } else {
            salesView.printPaymentFailure();
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void viewTransactionReport() {
        List<Transaction> transactions = transactionService.getAllTransactions();

        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            
            // Step 1: Show transaction summary
            mainView.printTransactionSummary(transactions);

            if (transactions.isEmpty()) {
                ConsoleUtil.systemPause();
                ConsoleUtil.clearScreen();
                return;
            }

            // Step 2: Ask user to select transaction
            mainView.printTransactionSelectionPrompt(transactions.size());
            int selection = ValidationUtil.intValidation(0, transactions.size());

            if (selection == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            if (selection == 0) {
                // User wants to exit
                ConsoleUtil.clearScreen();
                return;
            }

            // Step 3: Show selected transaction details
            if (selection >= 1 && selection <= transactions.size()) {
                Transaction selectedTransaction = transactions.get(selection - 1);
                
                ConsoleUtil.clearScreen();
                ConsoleUtil.logo();
                mainView.printTransactionDetails(selectedTransaction, selection);
                
                ConsoleUtil.systemPause();
                // Loop back to show summary again
            } else {
                mainView.printInvalidTransactionNumber();
                ConsoleUtil.systemPause();
            }
        }
    }
}