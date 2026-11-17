# Практична робота 4

Багатопотокова система обробки замовлень на електроніку й одяг. Для збірки потрібні JDK 21 або новіша та Maven.

## Збірка й запуск

У корені проєкту виконайте:

```sh
mvn clean package

java --module-path "order-storage/target/classes:order-threading/target/classes:order-processing/target/classes:order-processing/target/dependency" \
  --module order.processing/com.nov.order.processing.demo.Main
```

У Windows роздільник шляхів у `--module-path` — крапка з комою.

Демонстрація створює шість замовлень за допомогою JavaFaker та Lombok `@Builder`. Три робочі потоки їх обробляють і виводять суму, а повторне замовлення та замовлення з нульовою кількістю відхиляються. Наприкінці програма показує, скільки замовлень кожного типу збережено в пам’яті.

## Модулі

- `order.storage` експортує моделі та інтерфейс сховища. Реалізація на базі `ConcurrentHashMap` міститься в неекспортованому пакеті. Метод `putIfAbsent` атомарно запобігає повторному збереженню.
- `order.threading` приймає завдання як `Callable`, повертає `Future` і завершує пул після обробки прийнятих завдань.
- `order.processing` підключає обидва модулі через `requires`. `OrderProcessor<T extends Product>` перевіряє замовлення, обчислює суму та зберігає результат. Лямбда створює обробник для кожного замовлення, а `processor::process` передає його метод до пулу.

`OrderProcessingException` передає повідомлення про помилки обробки через `Future`. Параметр типу міститься в `OrderProcessor`, оскільки в Java підкласи `Throwable` не можуть бути узагальненими.
