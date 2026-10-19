package com.nov.finance;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@FunctionalInterface
public interface TransactionDataSource {
    Reader open() throws IOException;

    static TransactionDataSource fromPath(Path path) {
        return () -> Files.newBufferedReader(path, StandardCharsets.UTF_8);
    }

    static TransactionDataSource fromResource(String name) {
        return () -> {
            var input = TransactionDataSource.class.getResourceAsStream(name);
            if (input == null) {
                throw new IOException("CSV resource not found: " + name);
            }
            return new InputStreamReader(input, StandardCharsets.UTF_8);
        };
    }
}
