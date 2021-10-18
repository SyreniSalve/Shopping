package shopping;


import shopping.db.entity.ProductEntity;
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
        productRepository.create("bla");
        System.out.println("________________________________");
        ProductEntity entity = productRepository.getById(6);
        entity.setName("new name");
        productRepository.update(entity);
        System.out.println("________________________________");
        System.out.println(productRepository.delete(14));
        productRepository.list().forEach(System.out::println);
        System.out.println("________________________________");
        System.out.println(productRepository.getById(8));
        System.out.println(productRepository.findById(100));
        System.out.println("________________________________");
    }

    public static void main(String[] args){
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
            throw new ShoppingException(e);
        }
    }
}
