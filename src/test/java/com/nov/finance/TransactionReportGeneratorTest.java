package com.nov.finance;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionReportGeneratorTest {
    @Test
    void categoryReportShowsAmountAndThousandHryvniaBars() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            TransactionReportGenerator.printCategoryExpenses(Map.of("Food", 2500.0), 1000, out);
        }
        String report = bytes.toString(StandardCharsets.UTF_8);
        assertTrue(report.contains("Food: *** 2500.00 грн"));
        assertTrue(report.contains("1 * = 1000.00 грн"));
    }
}
