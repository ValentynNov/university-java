package com.nov.finance;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String monthYear = args.length > 1 ? args[1] : "01-2024";
        LocalDate from = args.length > 2 ? TransactionAnalyzer.parseDate(args[2]) : LocalDate.MIN;
        LocalDate to = args.length > 3 ? TransactionAnalyzer.parseDate(args[3]) : LocalDate.MAX;

        Reader reader;
        if (args.length > 0) {
            reader = Files.newBufferedReader(Path.of(args[0]), StandardCharsets.UTF_8);
        } else {
            InputStream input = Main.class.getResourceAsStream("/sample-transactions.csv");
            if (input == null) {
                throw new IOException("Локальний CSV не знайдено");
            }
            reader = new InputStreamReader(input, StandardCharsets.UTF_8);
        }

        List<Transaction> transactions;
        try (reader) {
            transactions = TransactionCSVReader.readTransactions(reader);
        }

        TransactionReportGenerator.printTransactions(transactions);

        TransactionReportGenerator.printBalanceReport(
                TransactionAnalyzer.calculateTotalBalance(transactions));

        TransactionReportGenerator.printTransactionsCountByMonth(monthYear,
                TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear));

        TransactionReportGenerator.printTopExpensesReport(
                TransactionAnalyzer.findTopExpenses(transactions));

        Transaction largest = TransactionAnalyzer.findLargestExpense(transactions, from, to);
        Transaction smallest = TransactionAnalyzer.findSmallestExpense(transactions, from, to);
        TransactionReportGenerator.printExpenseExtremesReport(largest, smallest);

        TransactionReportGenerator.printCategoryExpenses(
                TransactionAnalyzer.expensesByCategory(transactions));

        TransactionReportGenerator.printMonthlyExpenses(
                TransactionAnalyzer.expensesByMonth(transactions));
    }

}
