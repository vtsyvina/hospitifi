package com.hospitifi.service;

/**
 * Base interface for services that provides CRUD operations
 * @param <E> entity class
 * @param <T> entity id
 */
public interface Service<E, T> {
    E get(T id);

    boolean update(E entity);

    boolean save(E entity);

    boolean delete(T id);
}
