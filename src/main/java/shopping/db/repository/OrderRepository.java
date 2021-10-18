package shopping.db.repository;

import shopping.db.entity.OrderEntity;
import shopping.db.entity.ProductEntity;
import shopping.exception.ShoppingException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements Repository<OrderEntity>{
    private final Connection connection;

    public OrderRepository(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<OrderEntity> list() {
        try (Statement stmt = connection.createStatement()) {
            String sql = "SELECT * FROM orders";
            try (ResultSet rs = stmt.executeQuery(sql)) {
                return list(rs);
            }
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    private List<OrderEntity> list(ResultSet resultSet) {
        List<OrderEntity> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(toOrderEntity(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    @Override
    public Optional<OrderEntity> findById(int id) {
        String sql = "SELECT * FROM orders WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    return Optional.of(toOrderEntity(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    @Override
    public void save(OrderEntity order) {
        String sql =
                order.getId() == null
                ?  "INSERT INTO orders (status) VALUES (?)"
                : "UPDATE orders SET status=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, order.getStatus());
            if (order.getId() != null){
                stmt.setInt(2, order.getId());
            }
            stmt.execute();
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM orders WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    private static OrderEntity toOrderEntity(ResultSet resultSet) {
        try {
            OrderEntity entity = new OrderEntity();
            entity.setId(resultSet.getInt("id"));
            entity.setStatus(resultSet.getString("status"));
            entity.setCreated(resultSet.getTimestamp("created"));
            return entity;
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

}
