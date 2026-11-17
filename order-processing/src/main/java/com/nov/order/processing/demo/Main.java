package com.nov.order.processing.demo;

import com.github.javafaker.Faker;
import com.nov.order.model.Clothing;
import com.nov.order.model.Electronics;
import com.nov.order.model.Order;
import com.nov.order.model.ProcessedOrder;
import com.nov.order.model.Product;
import com.nov.order.processing.OrderProcessingException;
import com.nov.order.processing.OrderProcessor;
import com.nov.order.storage.OrderRepository;
import com.nov.order.threading.OrderExecutor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class Main {
    public static void main(String[] args) {
        List<Order<Product>> orders = sampleOrders(new Faker());
        OrderRepository<Product> repository = OrderRepository.inMemory();

        try (var executor = new OrderExecutor(3)) {
            List<Future<ProcessedOrder<Product>>> results = orders.stream()
                    .map(order -> {
                        var processor = new OrderProcessor<>(order, repository);
                        return executor.submit(processor::process);
                    })
                    .toList();

            for (int index = 0; index < results.size(); index++) {
                try {
                    printResult(results.get(index).get());
                } catch (ExecutionException exception) {
                    if (exception.getCause() instanceof OrderProcessingException failure) {
                        System.out.printf("Відхилено [%s] %s%n", orders.get(index).id(), failure.getMessage());
                    } else {
                        throw new IllegalStateException("Помилка в потоці обробки замовлень.", exception.getCause());
                    }
                }
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.err.println("Очікування результатів перервано.");
            return;
        }

        List<ProcessedOrder<Product>> saved = repository.findAll();
        long electronics = saved.stream()
                .filter(result -> result.order().product() instanceof Electronics)
                .count();
        System.out.printf("Збережено %d замовлень. Електроніка — %d, одяг — %d.%n",
                saved.size(), electronics, saved.size() - electronics);
    }

    private static List<Order<Product>> sampleOrders(Faker faker) {
        var orders = new ArrayList<Order<Product>>();
        for (int index = 0; index < 3; index++) {
            var electronics = Electronics.builder()
                    .name(faker.options().option("Навушники", "Монітор", "Клавіатура", "Ноутбук"))
                    .brand(faker.company().name())
                    .price(BigDecimal.valueOf(faker.number().numberBetween(25_000, 200_000), 2))
                    .build();
            var clothing = Clothing.builder()
                    .name(faker.color().name() + " " + faker.options().option("T-shirt", "Jacket", "Sweater"))
                    .size(faker.options().option("S", "M", "L", "XL"))
                    .price(BigDecimal.valueOf(faker.number().numberBetween(10_000, 50_000), 2))
                    .build();
            orders.add(new Order<>(UUID.randomUUID(), electronics, faker.number().numberBetween(1, 4)));
            orders.add(new Order<>(UUID.randomUUID(), clothing, faker.number().numberBetween(1, 4)));
        }

        orders.add(orders.getFirst());
        orders.add(new Order<>(UUID.randomUUID(), orders.getFirst().product(), 0));
        return orders;
    }

    private static void printResult(ProcessedOrder<Product> result) {
        Order<Product> order = result.order();
        System.out.printf("[%s] %s × %d — %s грн%n",
                result.threadName(), order.product().name(), order.quantity(), result.total().toPlainString());
    }
}
