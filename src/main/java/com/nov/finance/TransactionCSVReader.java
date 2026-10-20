package com.nov.finance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {
    private TransactionCSVReader() {
    }

    public static List<Transaction> readTransactions(Reader reader) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        BufferedReader buffered = new BufferedReader(reader);
        String line;
        int lineNumber = 0;
        while ((line = buffered.readLine()) != null) {
            lineNumber++;
            if (lineNumber == 1 && line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }
            if (line.isBlank()) {
                continue;
            }
            String[] values = line.split(",", -1);
            if (lineNumber == 1 && values[0].trim().equalsIgnoreCase("date")) {
                continue;
            }
            if (values.length != 3 && values.length != 4) {
                throw new IllegalArgumentException("Line " + lineNumber + ": expected 3 or 4 columns");
            }
            try {
                String date = values[0].trim();
                TransactionAnalyzer.parseDate(date);
                double amount = Double.parseDouble(values[1].trim());
                if (!Double.isFinite(amount)) {
                    throw new NumberFormatException("non-finite amount");
                }
                String description = values[2].trim();
                String category = values.length == 4 ? values[3].trim() : description;
                transactions.add(new Transaction(date, amount, description, category));
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Line " + lineNumber + ": invalid transaction: " + e.getMessage(), e);
            }
        }
        return transactions;
    }
}
