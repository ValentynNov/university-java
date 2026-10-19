package com.nov.finance;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws IOException {
        TransactionDataSource source = args.length > 0
                ? TransactionDataSource.fromPath(Path.of(args[0]))
                : TransactionDataSource.fromResource("/sample-transactions.csv");
        String monthYear = args.length > 1 ? args[1] : "01-2024";
        LocalDate from = args.length > 2 ? TransactionAnalyzer.parseDate(args[2]) : LocalDate.MIN;
        LocalDate to = args.length > 3 ? TransactionAnalyzer.parseDate(args[3]) : LocalDate.MAX;

        List<Transaction> transactions = TransactionCSVReader.readTransactions(source);

        TransactionReportGenerator.printTransactions(transactions, System.out);

        TransactionReportGenerator.printBalanceReport(
                TransactionAnalyzer.calculateTotalBalance(transactions), System.out);

        TransactionReportGenerator.printTransactionsCountByMonth(monthYear,
                TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear), System.out);

        TransactionReportGenerator.printTopExpensesReport(
                TransactionAnalyzer.findTopExpenses(transactions), System.out);

        TransactionReportGenerator.printExpenseExtremesReport(
                TransactionAnalyzer.findExpenseExtremes(transactions, from, to), System.out);

        TransactionReportGenerator.printCategoryExpenses(
                TransactionAnalyzer.expensesByCategory(transactions), 1000, System.out);

        TransactionReportGenerator.printMonthlyExpenses(
                TransactionAnalyzer.expensesByMonth(transactions), 1000, System.out);
    }

}
