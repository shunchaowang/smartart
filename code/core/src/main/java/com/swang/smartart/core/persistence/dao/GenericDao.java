package com.swang.smartart.core.persistence.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericDao<T extends Serializable, PK> {

    T create(T persistentObject);

    T get(PK id);

    T update(T persistentObject);

    void delete(PK id);

    Long countAll();

    List<T> getAll();

    /**
     * Counts the record using positional parameters.
     */
    Long count(String criteria, List<Object> args);

    /**
     * Counts the record using name-value-pair parameters.
     */
    Long count(String criteria, Map<String, Object> args);

    /**
     * Find all records using positional parameters.
     */
    List<T> findAll(String criteria, List<Object> args);

    /**
     * Find all records using name-value-pair.
     */
    List<T> findAll(String criteria, Map<String, Object> args);

    /**
     * Find all records using positional parameters with pagination.
     */
    List<T> findAll(String criteria, List<Object> args, Integer pageNumber, Integer pageSize);

    /**
     * Find all records using name-value-pair with pagination.
     */
    List<T> findAll(String criteria, Map<String, Object> args, Integer pageNumber, Integer
            pageSize);

    /**
     * Find all records with pagination without parameters.
     */
    List<T> findAll(String criteria, Integer pageNumber, Integer pageSize);

    CriteriaBuilder getCriteriaBuilder();

    /**
     * Count by jpa criteria query.
     *
     * @param criteriaQuery the criteria contains all query
     * @return the number of the query
     */
    Long count(CriteriaQuery<Long> criteriaQuery);

    /**
     * Count by jqa query language.
     *
     * @param jpql query string, like SELECT u.username FROM User AS u.
     * @return number of result
     */
    Long count(String jpql);

    /**
     * Find all T from criteria with offset and max records.
     *
     * @param criteriaQuery the criteria contains all query
     * @param start         the offset of the result
     * @param length        the max records of the result
     * @return list of T
     */
    List<T> find(CriteriaQuery<T> criteriaQuery, Integer start, Integer length);

    /**
     * Find all T from criteria with offset.
     *
     * @param criteriaQuery the criteria contains all query
     * @param start         the offset of the result
     * @return list of T
     */
    List<T> find(CriteriaQuery<T> criteriaQuery, Integer start);

    /**
     * Find all T from criteria.
     *
     * @param criteriaQuery the criteria contains all query
     * @return list of T
     */
    List<T> find(CriteriaQuery<T> criteriaQuery);

    /**
     * Count by jqa query language.
     *
     * @param jpql query string, like SELECT u.username FROM User AS u.
     * @return list of result
     */
    List<T> find(String jpql);

    /**
     * Query for one single object
     *
     * @return Single T
     */
    T findSingle(CriteriaQuery<T> criteriaQuery);

    /**
     * Query for one single object
     *
     * @return Single T
     */
    T findSingle(String jpql);
}
