package com.nov.practice3;

import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        run(new Scanner(System.in), System.out);
    }

    static void run(Scanner scanner, PrintStream out) {
        Calculator calculator = new Calculator();

        try {
            out.print("Введіть перше число: ");
            double first = Double.parseDouble(scanner.nextLine().trim());

            out.print("Введіть друге число: ");
            double second = Double.parseDouble(scanner.nextLine().trim());

            out.print("Оберіть операцію (+, -, *, /): ");
            String operation = scanner.nextLine().trim();

            double result = switch (operation) {
                case "+" -> calculator.add(first, second);
                case "-" -> calculator.subtract(first, second);
                case "*" -> calculator.multiply(first, second);
                case "/" -> calculator.divide(first, second);
                default -> throw new InvalidInputException("Невідома операція: " + operation);
            };
            out.println("Результат: " + result);
        } catch (NumberFormatException exception) {
            out.println("Помилка: введіть коректне число.");
        } catch (InvalidInputException | ArithmeticException exception) {
            out.println("Помилка: " + exception.getMessage());
        } catch (java.util.NoSuchElementException exception) {
            out.println("Помилка: даних для обчислення недостатньо.");
        } finally {
            out.println("Обробку запиту завершено.");
        }
    }
}
