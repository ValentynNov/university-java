package com.nov.finance;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        YearMonth wanted = YearMonth.parse(monthYear, MONTH);
        int count = 0;
        for (Transaction transaction : transactions) {
            YearMonth current = YearMonth.from(parseDate(transaction.getDate()));
            if (current.equals(wanted)) {
                count++;
            }
        }
        return count;
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        List<Transaction> expenses = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                expenses.add(transaction);
            }
        }
        expenses.sort(Comparator.comparingDouble(Transaction::getAmount));
        return expenses.subList(0, Math.min(10, expenses.size()));
    }

    public static Transaction findLargestExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Period start must not be after end");
        }
        Transaction largest = null;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() >= 0) {
                continue;
            }
            LocalDate date = parseDate(transaction.getDate());
            if (date.isBefore(from) || date.isAfter(to)) {
                continue;
            }
            if (largest == null || transaction.getAmount() < largest.getAmount()) {
                largest = transaction;
            }
        }
        return largest;
    }

    public static Transaction findSmallestExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Period start must not be after end");
        }
        Transaction smallest = null;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() >= 0) {
                continue;
            }
            LocalDate date = parseDate(transaction.getDate());
            if (date.isBefore(from) || date.isAfter(to)) {
                continue;
            }
            if (smallest == null || transaction.getAmount() > smallest.getAmount()) {
                smallest = transaction;
            }
        }
        return smallest;
    }

    public static Map<String, Double> expensesByCategory(List<Transaction> transactions) {
        Map<String, Double> totals = new TreeMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                String category = transaction.getCategory();
                double total = totals.getOrDefault(category, 0.0);
                totals.put(category, total - transaction.getAmount());
            }
        }
        return totals;
    }

    public static Map<YearMonth, Double> expensesByMonth(List<Transaction> transactions) {
        Map<YearMonth, Double> totals = new TreeMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                YearMonth month = YearMonth.from(parseDate(transaction.getDate()));
                double total = totals.getOrDefault(month, 0.0);
                totals.put(month, total - transaction.getAmount());
            }
        }
        return totals;
    }
}
