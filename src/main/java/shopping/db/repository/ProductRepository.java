package shopping.db.repository;

import shopping.db.entity.ProductEntity;
import shopping.exception.ShoppingException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    public void create(String name) {
        String sql = "INSERT INTO products (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.execute();
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    public List<ProductEntity> list() {
        try (Statement stmt = connection.createStatement()) {
            String sql = "SELECT * FROM products";
            try (ResultSet rs = stmt.executeQuery(sql)) {
                return list(rs);
            }
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    public ProductEntity getById(int id) {
        return  findById(id).orElse(null);
    }

    public Optional<ProductEntity> findById(int id) {
        String sql = "SELECT * FROM products WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    return Optional.of(toProductEntity(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    private List<ProductEntity> list(ResultSet resultSet) {
        List<ProductEntity> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(toProductEntity(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }

    private static ProductEntity toProductEntity(ResultSet resultSet) {
        try {
            ProductEntity entity = new ProductEntity();
            entity.setId(resultSet.getInt("id"));
            entity.setName(resultSet.getString("name"));
            entity.setCreated(resultSet.getTimestamp("created"));
            return entity;
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }
    public void update(ProductEntity entity) {
        String sql = "UPDATE products SET name=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getName());
            stmt.setInt(2, entity.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }
    public boolean delete(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }
}
