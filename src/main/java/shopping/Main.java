package shopping;


import shopping.db.repository.ProductRepository;
import shopping.exception.ShoppingException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Main implements AutoCloseable {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_SCHEMA = "shopping";

    private final Connection connection;
    private final ProductRepository productRepository;

    public Main() {
        Properties databaseProperties = loadDatabaseProperties();
        try {
            connection = DriverManager.getConnection(DB_URL + DB_SCHEMA,databaseProperties);
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }

        this.productRepository = new ProductRepository(connection);
    }

    public void run(){
        productRepository.insertNewProduct();
        productRepository.listProducts();
    }

    public static void main(String[] args) throws SQLException {
        new Main().run();
    }

    private static Properties loadDatabaseProperties(){
        try (InputStream is = ClassLoader.getSystemResourceAsStream("database.properties")){
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new ShoppingException(e);
        }
    }

    @Override
    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw  new ShoppingException(e);
        }
    }
}
