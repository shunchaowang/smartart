package com.swang.smartart.core.persistence.dao;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericDao<T, PK extends Serializable> {

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

    /**
     * Count record number using JPA criteria builder.
     *
     * @param typedQuery the criteria with query root, where clause
     * @return count of the result
     */
    Long countAllByCriteria(TypedQuery<Long> typedQuery);

    /**
     * Find all records using JPA criteria builder.
     *
     * @param typedQuery the criteria with query root, where clause and order by
     * @return List of the result objects
     */
    List<T> findAllByCriteria(TypedQuery<T> typedQuery);

    CriteriaBuilder getCriteriaBuilder();

    TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);

    TypedQuery<Long> createCountQuery(CriteriaQuery<Long> criteriaQuery);

}
