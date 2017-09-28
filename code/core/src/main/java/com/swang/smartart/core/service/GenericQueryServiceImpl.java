package com.swang.smartart.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 * Created by swang on 3/17/2015.
 */
public abstract class GenericQueryServiceImpl<T extends Serializable, PK>
        implements GenericQueryService<T, PK> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Logger logger = LoggerFactory.getLogger(GenericQueryServiceImpl.class);

    /**
     * Find all T from criteria with offset and max records.
     *
     * @param criteriaQuery the criteria contains all query
     * @param start         the offset of the result
     * @param length        the max records of the result
     * @return list of T
     */
    @Override
    public abstract List<T> find(CriteriaQuery<T> criteriaQuery, Integer start, Integer length);

    /**
     * Find all T from criteria with offset.
     *
     * @param criteriaQuery the criteria contains all query
     * @param start         the offset of the result
     * @return list of T
     */
    @Override
    public List<T> find(CriteriaQuery<T> criteriaQuery, Integer start) {
        return find(criteriaQuery, start, null);
    }

    /**
     * Find all T from criteria.
     *
     * @param criteriaQuery the criteria contains all query
     * @return list of T
     */
    @Override
    public List<T> find(CriteriaQuery<T> criteriaQuery) {
        return find(criteriaQuery, null, null);
    }
}
