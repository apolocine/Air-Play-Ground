package org.amia.playground.dao;

import java.util.List;

public interface CRUDRepository<T> {
    T create(T t);
    T read(int id);
    List<T> readAll();
    T update(T t);
    boolean delete(int id);
}
