import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private Category category;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", назва='" + name + '\'' +
                ", ціна=" + price +
                ", опис='" + description + '\'' +
                ", категорія=" + category.getName() +
                '}';
    }
}
