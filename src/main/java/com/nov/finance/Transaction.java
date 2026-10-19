package com.nov.finance;

import lombok.AllArgsConstructor;
import lombok.Value;

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
