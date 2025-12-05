package assignment.controller;

import assignment.model.Stock;
import assignment.service.PaymentService;
import assignment.service.SalesService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import java.io.IOException;
import java.util.List;

public class SalesController {
    private final SalesService salesService;
    private final PaymentService paymentService;

    public SalesController(SalesService salesService, PaymentService paymentService) {
        this.salesService = salesService;
        this.paymentService = paymentService;
    }

    // --- Helper Method for Display ---

    public void displayAvailableItems() {
        System.out.println("-------------------------------------------------------");
        System.out.println("            AVAILABLE ITEMS FOR PURCHASE");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-15s %-20s %-15s %-10s\n", "PRODUCT ID", "PRODUCT NAME", "PRICE (RM)", "QUANTITY");
        System.out.println("-------------------------------------------------------");

        List<Stock> availableStock = salesService.getAvailableStock();
        for (Stock stockItem : availableStock) {
            if (stockItem.getQty() > 0) {
                System.out.printf("%-15d %-20s %-15.2f %-10d\n",
                        stockItem.getStockID(),
                        stockItem.getStockName(),
                        stockItem.getPrice(),
                        stockItem.getQty());
            }
        }
        System.out.println("-------------------------------------------------------");
    }

    // --- Main Controller Methods ---

    public void addOrder() throws IOException {
        char nextOrder = 'N';

        do {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            System.out.println("[ ORDERING SYSTEM ]");
            System.out.println("-------------------------------------------------------");
            displayAvailableItems();

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
                    System.out.printf("PRODUCT NAME: %s\n", foundStock.getStockName());
                    System.out.printf("PRODUCT PRICE: RM%.2f\n", foundStock.getPrice());
                    System.out.printf("AVAILABLE QUANTITY: %d\n", foundStock.getQty());
                    System.out.println("-------------------------------------------------------");
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
                        System.out.println("-------------------------------------------------------");
                        System.out.printf("TOTAL COST: RM%.2f\n", foundStock.getPrice() * quantity);
                        System.out.println("-------------------------------------------------------");
                        System.out.println("[ORDER ADDED]");
                    } else {
                        System.out.println("<<<Failed to add order. Invalid item or quantity.>>>");
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
        System.out.println("[ SEARCH AN ORDER ]");
        System.out.println("-------------------------------------------------------");

        System.out.print("ENTER ORDER NO TO SEARCH: ");
        int orderNoSearch = ValidationUtil.intValidation(1, 10000);

        if (orderNoSearch == -9999) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Stock item = salesService.findCartItemByOrderNo(orderNoSearch);

        if (item != null) {
            System.out.println("ORDER NO " + item.getOrderNo());
            System.out.println("PRODUCT NAME: " + item.getStockName());
            System.out.println("QUANTITY: " + item.getQty());
            System.out.printf("TOTAL COST: RM%.2f\n", item.calculateTotalCost());
            System.out.println("-----------------------");
        } else {
            System.out.println("The order is not found in the cart. Please try again.");
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void removeOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ REMOVE AN ORDER ]");
        System.out.println("-------------------------------------------------------");

        System.out.print("ENTER ORDER NO TO REMOVE: ");
        int orderNoRemove = ValidationUtil.intValidation(1, 10000);

        if (orderNoRemove == -9999) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Stock cartItem = salesService.findCartItemByOrderNo(orderNoRemove);

        if (cartItem == null) {
            System.out.println("<<<The order is not found in the cart. Please try again.>>>");
        } else {
            System.out.println("-------------------------------------------------------");
            System.out.println("ORDER DETAILS:");
            System.out.println(cartItem.toString());
            System.out.println("-------------------------------------------------------");

            System.out.print("DO YOU WANT TO DELETE THIS ORDER (Y = YES, N = NO): ");
            char confirm = ValidationUtil.charValidation();

            if (confirm == 'Y') {
                if (salesService.removeOrder(orderNoRemove)) {
                    System.out.println("ORDER REMOVED SUCCESSFULLY");
                } else {
                    System.out.println("ORDER REMOVAL FAILED (Error in Service)");
                }
            } else {
                System.out.println("ORDER REMOVAL CANCELLED");
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void editOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ EDIT AN ORDER ]");
        System.out.println("-------------------------------------------------------");

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

        System.out.println("ORDER NO " + cartItem.getOrderNo());
        System.out.println("-------------------------------------------------------");
        System.out.println("PRODUCT NAME: " + cartItem.getStockName());
        System.out.println("CURRENT QUANTITY IN ORDER: " + cartItem.getQty());
        System.out.println("AVAILABLE QUANTITY IN INVENTORY: " + stockItem.getQty());
        System.out.println("-------------------------------------------------------");
        System.out.println("1. REDUCE QUANTITY");
        System.out.println("2. ADD QUANTITY");
        System.out.println("0. CANCEL");
        System.out.println("-------------------------------------------------------");

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
                    System.out.println("QUANTITY " + (choice == 1 ? "REDUCED" : "ADDED") + " SUCCESSFULLY");
                    System.out.printf("NEW ORDER QUANTITY: %d\n", salesService.findCartItemByOrderNo(orderNoEdit).getQty());
                } else {
                    System.out.println("<<<Invalid quantity. Check limits.>>>");
                }
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void makePayment() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ MAKE PAYMENT ]");
        System.out.println("-------------------------------------------------------");

        List<Stock> cart = salesService.getCartItems();
        
        if (cart.isEmpty()) {
            System.out.println("<<<CART IS EMPTY. PLEASE ADD ITEMS BEFORE PAYMENT.>>>");
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        // Display cart items
        System.out.println("CART ITEMS:");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-15s %-20s %-10s %-10s %-15s\n", "ORDER NO", "PRODUCT NAME", "QUANTITY", "PRICE", "TOTAL");
        System.out.println("-------------------------------------------------------");
        
        for (Stock item : cart) {
            System.out.printf("%-15d %-20s %-10d RM%-9.2f RM%-14.2f\n",
                    item.getOrderNo(),
                    item.getStockName(),
                    item.getQty(),
                    item.getPrice(),
                    item.calculateTotalCost());
        }
        System.out.println("-------------------------------------------------------");

        // Ask for member discount
        System.out.print("ENTER MEMBER ID FOR DISCOUNT (OR PRESS '0' FOR NO DISCOUNT): M-");
        String memberInput = ValidationUtil.scanner.nextLine().trim();
        
        double discountRate = 0.0;
        if (!memberInput.equals("0") && !memberInput.isEmpty()) {
            // TODO: Look up member and get discount rate from MemberService
            // For now, we'll ask for discount rate if member ID is provided
            System.out.print("ENTER DISCOUNT RATE (e.g., 0.1 for 10%): ");
            String discountInput = ValidationUtil.scanner.nextLine().trim();
            if (!discountInput.isEmpty()) {
                try {
                    discountRate = Double.parseDouble(discountInput);
                } catch (NumberFormatException e) {
                    System.out.println("<<<Invalid discount rate. Proceeding without discount.>>>");
                }
            }
        }

        // Process payment
        PaymentService.PaymentResult result = paymentService.processPayment(discountRate);
        
        if (result != null) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("PAYMENT SUMMARY");
            System.out.println("-------------------------------------------------------");
            System.out.printf("SUBTOTAL:        RM%.2f\n", result.getSubtotal());
            System.out.printf("DISCOUNT:        RM%.2f\n", result.getDiscount());
            System.out.printf("TAX (6%%):        RM%.2f\n", result.getTax());
            System.out.println("-------------------------------------------------------");
            System.out.printf("TOTAL:           RM%.2f\n", result.getTotal());
            System.out.println("-------------------------------------------------------");
            System.out.println("\nPAYMENT PROCESSED SUCCESSFULLY!");
            System.out.println("Items have been saved to PaidItem.txt");
            System.out.println("Transaction has been recorded in Transaction.txt");
        } else {
            System.out.println("<<<PAYMENT FAILED.>>>");
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }
}