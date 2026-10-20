package com.nov.finance;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public abstract class TransactionReportGenerator {
    private TransactionReportGenerator() {
    }

    public static void printTransactions(List<Transaction> transactions) {
        System.out.println("Транзакції:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public static void printBalanceReport(double balance) {
        System.out.println("Загальний баланс: " + balance + " грн");
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> expenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : expenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount() + " грн");
        }
    }

    public static void printExpenseExtremesReport(Transaction largest, Transaction smallest) {
        if (largest == null) {
            System.out.println("За вказаний період витрат немає.");
            return;
        }
        System.out.println("Найбільша витрата: " + largest);
        System.out.println("Найменша витрата: " + smallest);
    }

    public static void printCategoryExpenses(Map<String, Double> totals) {
        System.out.println("Витрати за категоріями (1 * = 1000 грн):");
        if (totals.isEmpty()) {
            System.out.println("Немає витрат.");
        }
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            int stars = (int) Math.ceil(entry.getValue() / 1000);
            System.out.println(entry.getKey() + ": " + "*".repeat(stars) + " " + entry.getValue() + " грн");
        }
    }

    public static void printMonthlyExpenses(Map<YearMonth, Double> totals) {
        System.out.println("Витрати за місяцями (1 * = 1000 грн):");
        if (totals.isEmpty()) {
            System.out.println("Немає витрат.");
        }
        for (Map.Entry<YearMonth, Double> entry : totals.entrySet()) {
            int stars = (int) Math.ceil(entry.getValue() / 1000);
            System.out.println(entry.getKey() + ": " + "*".repeat(stars) + " " + entry.getValue() + " грн");
        }
    }
}
