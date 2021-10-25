package shopping.db.entity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class OrderEntity implements Entity{

    private Integer id;
    private String status = "NEW";
    private Map<ProductEntity, Integer> items = new HashMap<>();
    private Timestamp created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<ProductEntity, Integer> getItems() {
        return items;
    }

    public void setItems(Map<ProductEntity, Integer> items) {
        this.items = items;
    }

    public void addItem(ProductEntity product, Integer quantity){
        items.put(product, items.getOrDefault(product,0) + quantity);
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", items=" + items +
                ", created=" + created +
                '}';
    }
}
