package com.nov.practice3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {
    private final Calculator calculator = new Calculator();

    @Test
    void basicOperationsAcceptNegativeNumbers() {
        assertEquals(1.5, calculator.add(-2.5, 4.0));
        assertEquals(-6.5, calculator.subtract(-2.5, 4.0));
        assertEquals(-10.0, calculator.multiply(-2.5, 4.0));
        assertEquals(-0.625, calculator.divide(-2.5, 4.0));
    }

    @Test
    void divisionByZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(5.0, 0.0));
        assertThrows(ArithmeticException.class, () -> calculator.divide(5.0, -0.0));
    }

    @Test
    void nonFiniteInputsThrowCustomExceptionForEveryOperation() {
        assertThrows(InvalidInputException.class, () -> calculator.add(Double.NaN, 1.0));
        assertThrows(InvalidInputException.class, () -> calculator.subtract(1.0, Double.POSITIVE_INFINITY));
        assertThrows(InvalidInputException.class, () -> calculator.multiply(Double.NEGATIVE_INFINITY, 1.0));
        assertThrows(InvalidInputException.class, () -> calculator.divide(1.0, Double.NaN));
    }
}
