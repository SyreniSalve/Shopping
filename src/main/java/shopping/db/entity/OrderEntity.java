package shopping.db.entity;

import java.sql.Timestamp;

public class OrderEntity implements Entity{

    private Integer id;
    private String status = "NEW";
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
                ", created=" + created +
                '}';
    }
}
