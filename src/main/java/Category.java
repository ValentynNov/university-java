import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Category {

    private int id;
    private String name;

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", назва='" + name + '\'' +
                '}';
    }

}
