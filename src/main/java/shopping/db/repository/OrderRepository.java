package shopping.db.repository;

import shopping.db.entity.OrderEntity;
import shopping.db.entity.ProductEntity;
import shopping.exception.ShoppingException;

import java.sql.*;
import java.util.*;

public class OrderRepository implements Repository<OrderEntity>{
    private final Connection connection;
    private final ProductRepository productRepository;

    public OrderRepository(Connection connection, ProductRepository productRepository) {
        this.connection = connection;
        this.productRepository = productRepository;
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
                ? "INSERT INTO orders (status) VALUES (?)"
                : "UPDATE orders SET status=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, order.getStatus());
            if (order.getId() != null){
                stmt.setInt(2, order.getId());
            }
            stmt.execute();
            Integer orderId = getLastInsertId();
            order.getItems().forEach(((product, quantity) ->
                saveItem(orderId, product, quantity)
            ));
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    private Integer getLastInsertId(){
        String sql = "SELECT LAST_INSERT_ID();";
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    return rs.getInt(1);
                }
                return -1;
            }
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    private void saveItem(Integer orderId, ProductEntity product, Integer quantity){
        String sql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setDouble(2, product.getId());
            stmt.setInt(3, quantity);
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

    private  OrderEntity toOrderEntity(ResultSet resultSet) {
        try {
            Integer id = resultSet.getInt("id");
            OrderEntity entity = new OrderEntity();
            entity.setId(id);
            entity.setStatus(resultSet.getString("status"));
            entity.setCreated(resultSet.getTimestamp("created"));
            entity.setItems(loadItems(id));
            return entity;
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    private Map<ProductEntity, Integer> loadItems(Integer orderId){
        String sql = "SELECT * FROM order_items WHERE order_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<ProductEntity, Integer> items = new HashMap<>();
                while (rs.next()) {
                    ProductEntity product = productRepository.getById(rs.getInt("product_id"));
                    items.put(product, rs.getInt("quantity"));
                }
                return items;
            }
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

}
