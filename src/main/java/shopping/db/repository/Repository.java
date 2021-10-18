package shopping.db.repository;

import shopping.db.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface Repository<T extends Entity> {

    void save(T entity);

    List<T> list();

    default T getById(int id) {
        return  findById(id).orElse(null);
    }

    Optional<T> findById(int id);


    boolean delete(int id);

    default boolean delete(T entity){
        return delete(entity.getId());
    }
}
