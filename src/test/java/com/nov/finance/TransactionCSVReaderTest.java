package com.nov.finance;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCSVReaderTest {
    @Test
    void readsSampleCsvFromFileSource() throws Exception {
        Path path = Path.of("src/main/resources/sample-transactions.csv");
        List<Transaction> transactions;
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            transactions = TransactionCSVReader.readTransactions(reader);
        }
        assertEquals(5, transactions.size());
        assertEquals("Продукти", transactions.get(1).getCategory());
        assertEquals(-1200.0, transactions.get(1).getAmount());
    }

    @Test
    void parsesThreeColumns() throws Exception {
        String csv = "\uFEFFdate,amount,description\n"
                + "01-02-2024,-100.50,Shop\n"
                + "02-02-2024,50,Salary\n";
        List<Transaction> transactions = TransactionCSVReader.readTransactions(new StringReader(csv));
        assertEquals(2, transactions.size());
        assertEquals("Shop", transactions.getFirst().getDescription());
        assertEquals("Shop", transactions.getFirst().getCategory());
    }

    @Test
    void rejectsInvalidAmountWithLineNumber() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> TransactionCSVReader.readTransactions(new StringReader("date,amount,description\n01-01-2024,abc,X\n")));
        assertTrue(error.getMessage().contains("Line 2"));
    }
}
