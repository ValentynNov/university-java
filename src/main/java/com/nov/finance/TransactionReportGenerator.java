package com.nov.finance;

import java.io.PrintStream;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/** Stateless console reports. */
public abstract class TransactionReportGenerator {
    private TransactionReportGenerator() {
    }

    public static void printTransactions(List<Transaction> transactions, PrintStream out) {
        out.println("Транзакції:");
        transactions.forEach(out::println);
    }

    public static void printBalanceReport(double balance, PrintStream out) {
        out.printf(Locale.ROOT, "Загальний баланс: %.2f грн%n", balance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count, PrintStream out) {
        out.printf("Кількість транзакцій за %s: %d%n", monthYear, count);
    }

    public static void printTopExpensesReport(List<Transaction> expenses, PrintStream out) {
        out.println("10 найбільших витрат:");
        expenses.forEach(t -> out.printf(Locale.ROOT, "%s: %.2f грн%n", t.getDescription(), t.getAmount()));
    }

    public static void printExpenseExtremesReport(Optional<ExpenseExtremes> extremes, PrintStream out) {
        if (extremes.isEmpty()) {
            out.println("За вказаний період витрат немає.");
            return;
        }
        out.printf("Найбільша витрата: %s%n", extremes.orElseThrow().largest());
        out.printf("Найменша витрата: %s%n", extremes.orElseThrow().smallest());
    }

    public static void printCategoryExpenses(Map<String, Double> totals, double scale, PrintStream out) {
        printBars("Витрати за категоріями", totals, scale, out);
    }

    public static void printMonthlyExpenses(Map<YearMonth, Double> totals, double scale, PrintStream out) {
        printBars("Витрати за місяцями", totals, scale, out);
    }

    private static <K> void printBars(String title, Map<K, Double> totals, double scale, PrintStream out) {
        if (!Double.isFinite(scale) || scale <= 0) {
            throw new IllegalArgumentException("Scale must be positive and finite");
        }
        out.printf(Locale.ROOT, "%s (1 * = %.2f грн):%n", title, scale);
        if (totals.isEmpty()) {
            out.println("Немає витрат.");
        }
        totals.forEach((key, total) -> {
            int stars = (int) Math.ceil(total / scale);
            out.printf(Locale.ROOT, "%s: %s %.2f грн%n", key, "*".repeat(stars), total);
        });
    }
}
