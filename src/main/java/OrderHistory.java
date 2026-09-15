import java.util.ArrayList;
import java.util.List;

public class OrderHistory {

    private List<Order> orders;

    public OrderHistory() {
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Історія замовлень порожня.";
        }

        StringBuilder sb = new StringBuilder("Історія замовлень:\n");
        for (int i = 0; i < orders.size(); i++) {
            sb.append("Замовлення #").append(i + 1).append(":\n");
            sb.append(orders.get(i)).append("\n");
        }
        return sb.toString();
    }
}
