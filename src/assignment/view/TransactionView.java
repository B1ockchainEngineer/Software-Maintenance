package assignment.view;

import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.util.config.AppConfig;
import assignment.util.config.SalesConfig;
import java.util.List;

/**
 * View class for Transaction viewing operations.
 * Handles all display outputs for transaction operations.
 */
public class TransactionView {

    /**
     * Displays transaction summary report with all transactions.
     */
    public void printTransactionSummary(List<Transaction> transactions) {
        System.out.println(SalesConfig.TITLE_TRANSACTION_REPORT);
        System.out.println(AppConfig.SEPARATOR_LINE);

        if (transactions.isEmpty()) {
            System.out.println(SalesConfig.MSG_NO_TRANSACTIONS);
        } else {
            // Header - right align money columns
            System.out.printf("%-6s  %15s  %15s  %15s  %15s%n", 
                    SalesConfig.HEADER_NO,
                    SalesConfig.HEADER_SUBTOTAL,
                    SalesConfig.HEADER_DISCOUNT,
                    SalesConfig.HEADER_TAX,
                    SalesConfig.HEADER_TOTAL);
            System.out.println(AppConfig.SEPARATOR_LINE);

            int transactionNo = 1;
            double grandTotalSubtotal = 0.0;
            double grandTotalDiscount = 0.0;
            double grandTotalTax = 0.0;
            double grandTotal = 0.0;

            for (Transaction transaction : transactions) {
                System.out.printf("%-6d  %15s  %15s  %15s  %15s%n",
                        transactionNo++,
                        String.format("RM%.2f", transaction.getSubtotal()),
                        String.format("RM%.2f", transaction.getDiscount()),
                        String.format("RM%.2f", transaction.getTax()),
                        String.format("RM%.2f", transaction.getTotal()));

                grandTotalSubtotal += transaction.getSubtotal();
                grandTotalDiscount += transaction.getDiscount();
                grandTotalTax += transaction.getTax();
                grandTotal += transaction.getTotal();
            }

            // Summary row
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.printf("%-6s  %15s  %15s  %15s  %15s%n",
                    SalesConfig.LABEL_TOTAL,
                    String.format("RM%.2f", grandTotalSubtotal),
                    String.format("RM%.2f", grandTotalDiscount),
                    String.format("RM%.2f", grandTotalTax),
                    String.format("RM%.2f", grandTotal));
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.println(SalesConfig.LABEL_TOTAL_TRANSACTIONS + " " + transactions.size());
            System.out.println(AppConfig.SEPARATOR_LINE);
        }
    }

    /**
     * Displays detailed transaction information.
     */
    public void printTransactionDetails(Transaction transaction, int transactionNo) {
        System.out.println("\n" + AppConfig.SEPARATOR_LONG);
        System.out.printf(SalesConfig.TITLE_TRANSACTION_DETAILS + "\n", transactionNo);
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%-20s RM%.2f%n", SalesConfig.LABEL_SUBTOTAL, transaction.getSubtotal());
        System.out.printf("%-20s RM%.2f%n", SalesConfig.LABEL_DISCOUNT, transaction.getDiscount());
        System.out.printf("%-20s RM%.2f%n", SalesConfig.LABEL_TAX, transaction.getTax());
        System.out.printf("%-20s RM%.2f%n", SalesConfig.LABEL_TOTAL, transaction.getTotal());
        System.out.println(AppConfig.SEPARATOR_LINE);

        List<Stock> items = transaction.getItems();
        if (items.isEmpty()) {
            System.out.println(SalesConfig.LABEL_NO_ITEMS);
        } else {
            System.out.println(SalesConfig.LABEL_ITEMS);
            System.out.printf("%-10s      %-25s%-10s  %-12s  %-12s%n", 
                    SalesConfig.HEADER_PRODUCT_ID,
                    SalesConfig.HEADER_PRODUCT_NAME,
                    SalesConfig.HEADER_QUANTITY,
                    SalesConfig.HEADER_PRICE,
                    SalesConfig.HEADER_ITEM_TOTAL);
            System.out.println(AppConfig.SEPARATOR_LONG);
            for (Stock item : items) {
                double itemTotal = item.getQty() * item.getPrice();
                System.out.printf("%-10d      %-25s%-10d  RM%-12.2f  RM%-12.2f%n",
                        item.getStockID(),
                        item.getStockName(),
                        item.getQty(),
                        item.getPrice(),
                        itemTotal);
                System.out.println(AppConfig.SEPARATOR_LONG);
            }
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(AppConfig.SEPARATOR_LONG);
    }

    /**
     * Prompts for transaction number selection.
     */
    public void printTransactionSelectionPrompt(int maxTransaction) {
        System.out.printf("%n" + SalesConfig.MSG_SELECT_TRANSACTION, maxTransaction);
    }

    /**
     * Displays message when invalid transaction number is entered.
     */
    public void printInvalidTransactionNumber() {
        System.out.println(SalesConfig.MSG_INVALID_TRANSACTION);
    }
}

