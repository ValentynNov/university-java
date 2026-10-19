package com.nov.finance;

import lombok.AllArgsConstructor;
import lombok.Value;

/** One CSV transaction. A three-column record uses its description as its category. */
@Value
@AllArgsConstructor
public class Transaction {
    String date;
    double amount;
    String description;
    String category;

    public Transaction(String date, double amount, String description) {
        this(date, amount, description, description);
    }
}
