package com.nov.finance;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAnalyzerTest {
    @Test
    void testCalculateTotalBalance() {
        List<Transaction> transactions = List.of(
                new Transaction("2023-01-01", 100.0, "Дохід"),
                new Transaction("2023-01-02", -50.0, "Витрата"),
                new Transaction("2023-01-03", 150.0, "Дохід"));
        assertEquals(200.0, TransactionAnalyzer.calculateTotalBalance(transactions), 0.001);
    }

    @Test
    void testCountTransactionsByMonth() {
        List<Transaction> transactions = List.of(
                new Transaction("01-02-2023", 50.0, "Дохід"),
                new Transaction("15-02-2023", -20.0, "Витрата"),
                new Transaction("05-03-2023", 100.0, "Дохід"));
        assertEquals(2, TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023"));
        assertEquals(1, TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023"));
        assertEquals(0, TransactionAnalyzer.countTransactionsByMonth(transactions, "04-2023"));
    }

    @Test
    void testFindTopExpenses() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("01-01-2024", 999, "income"));
        for (int i = 1; i <= 12; i++) {
            transactions.add(new Transaction("01-01-2024", -i, "expense " + i));
        }
        List<Transaction> result = TransactionAnalyzer.findTopExpenses(transactions);
        assertEquals(10, result.size());
        assertEquals(-12, result.getFirst().getAmount());
        assertEquals(-3, result.getLast().getAmount());
    }

    @Test
    void testExpenseRangeAndSummaries() {
        List<Transaction> transactions = List.of(
                new Transaction("01-01-2024", -1000, "Groceries", "Food"),
                new Transaction("31-01-2024", -100, "Coffee", "Food"),
                new Transaction("01-02-2024", -500, "Bus", "Travel"),
                new Transaction("15-01-2024", 2000, "Salary", "Income"));
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);
        assertEquals(-1000, TransactionAnalyzer.findLargestExpense(transactions, from, to).getAmount());
        assertEquals(-100, TransactionAnalyzer.findSmallestExpense(transactions, from, to).getAmount());
        assertNull(TransactionAnalyzer.findLargestExpense(
                transactions, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
        assertNull(TransactionAnalyzer.findSmallestExpense(
                transactions, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
        assertEquals(1100, TransactionAnalyzer.expensesByCategory(transactions).get("Food"), 0.001);
        assertEquals(500, TransactionAnalyzer.expensesByMonth(transactions).get(YearMonth.of(2024, 2)), 0.001);
        assertThrows(IllegalArgumentException.class, () -> TransactionAnalyzer.findLargestExpense(
                transactions, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
    }
}
