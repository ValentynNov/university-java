package com.nov.finance;

import java.io.PrintStream;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public abstract class TransactionReportGenerator {
    private TransactionReportGenerator() {
    }

    public static void printTransactions(List<Transaction> transactions, PrintStream out) {
        out.println("Транзакції:");
        for (Transaction transaction : transactions) {
            out.println(transaction);
        }
    }

    public static void printBalanceReport(double balance, PrintStream out) {
        out.printf(Locale.ROOT, "Загальний баланс: %.2f грн%n", balance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count, PrintStream out) {
        out.printf("Кількість транзакцій за %s: %d%n", monthYear, count);
    }

    public static void printTopExpensesReport(List<Transaction> expenses, PrintStream out) {
        out.println("10 найбільших витрат:");
        for (Transaction expense : expenses) {
            out.printf(Locale.ROOT, "%s: %.2f грн%n", expense.getDescription(), expense.getAmount());
        }
    }

    public static void printExpenseExtremesReport(Optional<ExpenseExtremes> extremes, PrintStream out) {
        if (extremes.isEmpty()) {
            out.println("За вказаний період витрат немає.");
            return;
        }
        ExpenseExtremes result = extremes.get();
        out.printf("Найбільша витрата: %s%n", result.largest());
        out.printf("Найменша витрата: %s%n", result.smallest());
    }

    public static void printCategoryExpenses(Map<String, Double> totals, double scale, PrintStream out) {
        checkScale(scale);
        out.printf(Locale.ROOT, "Витрати за категоріями (1 * = %.2f грн):%n", scale);
        if (totals.isEmpty()) {
            out.println("Немає витрат.");
        }
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            printExpenseLine(entry.getKey(), entry.getValue(), scale, out);
        }
    }

    public static void printMonthlyExpenses(Map<YearMonth, Double> totals, double scale, PrintStream out) {
        checkScale(scale);
        out.printf(Locale.ROOT, "Витрати за місяцями (1 * = %.2f грн):%n", scale);
        if (totals.isEmpty()) {
            out.println("Немає витрат.");
        }
        for (Map.Entry<YearMonth, Double> entry : totals.entrySet()) {
            printExpenseLine(entry.getKey().toString(), entry.getValue(), scale, out);
        }
    }

    private static void checkScale(double scale) {
        if (!Double.isFinite(scale) || scale <= 0) {
            throw new IllegalArgumentException("Scale must be positive and finite");
        }
    }

    private static void printExpenseLine(String name, double total, double scale, PrintStream out) {
        int stars = (int) Math.ceil(total / scale);
        out.printf(Locale.ROOT, "%s: %s %.2f грн%n", name, "*".repeat(stars), total);
    }
}
