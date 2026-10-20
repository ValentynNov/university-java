package com.nov.finance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class TransactionCSVReader {
    private TransactionCSVReader() {
    }

    public static List<Transaction> readTransactions(Reader reader) throws IOException {
        Objects.requireNonNull(reader, "reader");
        List<Transaction> transactions = new ArrayList<>();
        BufferedReader buffered = reader instanceof BufferedReader b ? b : new BufferedReader(reader);
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
            List<String> values = splitCsvLine(line, lineNumber);
            if (lineNumber == 1 && values.size() >= 2
                    && values.get(0).trim().equalsIgnoreCase("date")
                    && values.get(1).trim().equalsIgnoreCase("amount")) {
                continue;
            }
            if (values.size() != 3 && values.size() != 4) {
                throw new IllegalArgumentException("Line " + lineNumber + ": expected 3 or 4 columns");
            }
            try {
                String date = values.get(0).trim();
                TransactionAnalyzer.parseDate(date);
                double amount = Double.parseDouble(values.get(1).trim());
                if (!Double.isFinite(amount)) {
                    throw new NumberFormatException("non-finite amount");
                }
                String description = values.get(2).trim();
                String category = values.size() == 4 ? values.get(3).trim() : description;
                transactions.add(new Transaction(date, amount, description, category));
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Line " + lineNumber + ": invalid transaction: " + e.getMessage(), e);
            }
        }
        return transactions;
    }

    private static List<String> splitCsvLine(String line, int lineNumber) {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (c == ',' && !quoted) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(c);
            }
        }
        if (quoted) {
            throw new IllegalArgumentException("Line " + lineNumber + ": unclosed quoted field");
        }
        values.add(value.toString());
        return values;
    }
}
