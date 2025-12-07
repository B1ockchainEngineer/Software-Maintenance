package assignment.service;

import assignment.model.Transaction;
import assignment.repo.TransactionRepository;
import java.util.List;

/**
 * Service layer for transaction management.
 * Handles business logic for transaction operations (retrieval, reporting).
 * Separates business logic from data access layer.
 */
public class TransactionService {
    private final TransactionRepository transactionRepo;

    public TransactionService(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    /**
     * Retrieves all transactions with their items.
     * @return List of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepo.loadAllTransactions();
    }

    /**
     * Saves a transaction to the repository.
     * @param transaction The transaction to save
     */
    public void saveTransaction(Transaction transaction) {
        transactionRepo.appendTransaction(
            transaction.getSubtotal(),
            transaction.getDiscount(),
            transaction.getTax(),
            transaction.getTotal(),
            transaction.getItems()
        );
    }
}

