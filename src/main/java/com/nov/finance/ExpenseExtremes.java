package com.nov.finance;

/** Largest expense is the most negative amount; smallest is closest to zero. */
public record ExpenseExtremes(Transaction largest, Transaction smallest) {
}
