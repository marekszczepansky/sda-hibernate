package pl.sda.dao;

public interface EntityDAO<D> {
    D findById(int id) throws Exception;

    void create(D entity) throws Exception;

    void update(D entity) throws Exception;

    void delete(int id) throws Exception;
}
