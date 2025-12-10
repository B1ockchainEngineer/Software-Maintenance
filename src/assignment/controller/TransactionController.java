package assignment.controller;

import assignment.model.Transaction;
import assignment.service.TransactionService;
import assignment.util.ConsoleUtil;
import assignment.util.SalesUtil;
import assignment.util.ValidationUtil;
import assignment.view.TransactionView;
import java.util.List;

/**
 * Controller for transaction viewing and management operations.
 */
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionView transactionView;

    /**
     * Constructs a TransactionController with the specified TransactionService.
     * @param transactionService The transaction service for retrieving transactions
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.transactionView = new TransactionView();
    }

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
            transactionView.printTransactionDetails(selectedTransaction, transactionNo);
            
            ConsoleUtil.systemPause();
        } else {
            transactionView.printInvalidTransactionNumber();
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
            transactionView.printTransactionSummary(transactions);

            if (transactions.isEmpty()) {
                ConsoleUtil.systemPause();
                ConsoleUtil.clearScreen();
                return;
            }

            // Ask user to select transaction
            transactionView.printTransactionSelectionPrompt(transactions.size());
            int transactionNo = ValidationUtil.intValidation(0, transactions.size());

            if (transactionNo == SalesUtil.INVALID_INPUT) {
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

