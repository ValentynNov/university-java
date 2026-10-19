package com.nov.finance;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCSVReaderTest {
    @Test
    void readsSampleCsvFromFileSource() throws Exception {
        Path path = Path.of("src/main/resources/sample-transactions.csv");
        List<Transaction> transactions = TransactionCSVReader.readTransactions(
                TransactionDataSource.fromPath(path));
        assertEquals(5, transactions.size());
        assertEquals("Продукти", transactions.get(1).getCategory());
        assertEquals(-1200.0, transactions.get(1).getAmount());
    }

    @Test
    void parsesThreeColumnsAndQuotedComma() throws Exception {
        String csv = "\uFEFFdate,amount,description\n"
                + "01-02-2024,-100.50,\"Shop, downtown\"\n"
                + "02-02-2024,50,Salary\n";
        List<Transaction> transactions = TransactionCSVReader.parse(new StringReader(csv));
        assertEquals(2, transactions.size());
        assertEquals("Shop, downtown", transactions.getFirst().getDescription());
        assertEquals("Shop, downtown", transactions.getFirst().getCategory());
    }

    @Test
    void rejectsInvalidAmountWithLineNumber() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> TransactionCSVReader.parse(new StringReader("date,amount,description\n01-01-2024,abc,X\n")));
        assertTrue(error.getMessage().contains("Line 2"));
    }
}
