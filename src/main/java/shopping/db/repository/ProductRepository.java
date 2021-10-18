package shopping.db.repository;

import shopping.db.entity.ProductEntity;
import shopping.exception.ShoppingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductRepository {
    private final Connection connection;

    public ProductRepository(Connection connection){
        this.connection = connection;
    }

    public void insertNewProduct(){
        try (Statement stmt = connection.createStatement()) {
            String sql = "INSERT INTO products (name) VALUES ('test')";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }
    public void listProducts(){
        try (Statement stmt = connection.createStatement()) {
            String sql = "SELECT * FROM products";
            try (ResultSet rs = stmt.executeQuery(sql)){
                listProducts(rs);
            }
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }
    private static void listProducts(ResultSet resultSet){
        try {
            while (resultSet.next()){
                ProductEntity entity = new ProductEntity();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setCreated(resultSet.getTimestamp("created"));
                System.out.println(entity);

            }
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
    }
}
