package shopping;


import shopping.db.entity.OrderEntity;
import shopping.db.entity.ProductEntity;
import shopping.db.repository.OrderRepository;
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
    private final OrderRepository orderRepository;

    public Main() {
        Properties databaseProperties = loadDatabaseProperties();
        try {
            connection = DriverManager.getConnection(DB_URL + DB_SCHEMA,databaseProperties);
        } catch (SQLException e) {
            throw new ShoppingException(e);
        }
        this.orderRepository = new OrderRepository(connection);
        this.productRepository = new ProductRepository(connection);
    }

    public void runProducts(){
        ProductEntity newEntity = new ProductEntity();
        newEntity.setName("bla bla");
        newEntity.setPrice(9.99);
        productRepository.save(newEntity);
        System.out.println("________________________________");
        ProductEntity entity = productRepository.getById(6);
        entity.setName("new name");
        productRepository.save(entity);
        System.out.println("________________________________");
        System.out.println(productRepository.delete(14));
        productRepository.list().forEach(System.out::println);
        System.out.println("________________________________");
        System.out.println(productRepository.getById(8));
        System.out.println(productRepository.findById(100));
        System.out.println("________________________________");
    }

    public void runOrders() {
        System.out.println("==================================");
        System.out.println("==================================");
        OrderEntity entity = new OrderEntity();
        orderRepository.save(entity);
        orderRepository.list().forEach(System.out::println);
    }

    public static void main(String[] args){
        try(Main m = new Main()){
            m.runProducts();
            m.runOrders();
        }
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
