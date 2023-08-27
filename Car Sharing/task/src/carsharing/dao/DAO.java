package carsharing.dao;

import java.util.List;

public interface DAO<T> {

    public List<T> findAll();
    public int create(); // Creates table
    public int insert(T t); // Inserts new record into table
    public int updateById(int id, T t);

    public int deleteById(int id);
}
