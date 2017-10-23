package com.hospitifi.repository;

/**
 * Base interface for repositories that provides CRUD operations
 * @param <E> entity class
 * @param <T> entity id
 */
public interface Repository<E, T> {
    E get(T id);

    boolean update(E entity);

    boolean save(E entity);

    boolean delete(T id);
}
