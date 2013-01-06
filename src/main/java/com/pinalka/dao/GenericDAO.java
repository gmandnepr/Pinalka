package com.pinalka.dao;

import java.util.List;

/**
 * Generic dao that provides common operations for all entities
 *
 * @param <T> exact type of the entity
 * @author gman
 */
public interface GenericDAO<T> {

    /**
     * Is the default size for a single page of data
     */
    int DEFAULT_PAGE_SIZE = 100;

    /**
     * Finds record by id
     *
     * @param id of the record
     * @return the record
     */
    T getById(Long id);

    /**
     * Finds all records
     *
     * @return all records
     */
    List<T> getList();

    /**
     * Find all records on the given page with default size
     *
     * @param page to find
     * @return records on the given page
     */
    List<T> getList(int page);

    /**
     * Find all records on the given page with given size
     *
     * @param page to find
     * @param pageSize page size
     * @return records on the given page with given size
     */
    List<T> getList(int page, int pageSize);

    /**
     * Obtain number of all records in the database
     *
     * @return number of the records
     */
    long count();

    /**
     * Persists new record
     *
     * @param object to add to the database
     */
    void create(T object);

    /**
     * Updates record in the database
     *
     * @param object to update in the database
     */
    void update(T object);

    /**
     * Deletes record from the database
     *
     * @param object to remove from the database
     */
    void delete(T object);

    /**
     * Remove record by its id
     *
     * @param id is the id of the record to remove
     */
    void deleteById(Long id);
}
