package com.nov.finance;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Stateless operations on a supplied transaction list. */
public abstract class TransactionAnalyzer {
    private static final DateTimeFormatter CSV_DATE = DateTimeFormatter.ofPattern("dd-MM-uuuu");
    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MM-uuuu");

    private TransactionAnalyzer() {
    }

    public static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value, CSV_DATE);
        } catch (DateTimeParseException ignored) {
            return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public static double calculateTotalBalance(List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        YearMonth wanted = YearMonth.parse(monthYear, MONTH);
        return (int) transactions.stream()
                .filter(t -> YearMonth.from(parseDate(t.getDate())).equals(wanted))
                .count();
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .limit(10)
                .toList();
    }

    public static Optional<ExpenseExtremes> findExpenseExtremes(
            List<Transaction> transactions, LocalDate from, LocalDate to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Period start must not be after end");
        }
        List<Transaction> expenses = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = parseDate(t.getDate());
                    return !date.isBefore(from) && !date.isAfter(to);
                })
                .toList();
        if (expenses.isEmpty()) {
            return Optional.empty();
        }
        Comparator<Transaction> byAmount = Comparator.comparingDouble(Transaction::getAmount);
        return Optional.of(new ExpenseExtremes(
                expenses.stream().min(byAmount).orElseThrow(),
                expenses.stream().max(byAmount).orElseThrow()));
    }

    public static Map<String, Double> expensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory, TreeMap::new,
                        Collectors.summingDouble(t -> -t.getAmount())));
    }

    public static Map<YearMonth, Double> expensesByMonth(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(parseDate(t.getDate())), TreeMap::new,
                        Collectors.summingDouble(t -> -t.getAmount())));
    }
}
