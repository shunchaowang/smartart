package com.swang.smartart.core.service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 * Created by swang on 3/10/2015.
 */
public interface GenericQueryService<T extends Serializable, PK>
        extends GenericService<T, PK> {

    /**
     * Call generic dao to retrieve CriteriaBuilder from EntityManage, which will be
     * used to formulate CriteriaQuery and Criteria including where, orderBy, etc...
     *
     * @return CriteriaBuilder to be used by the controller for domain T
     */
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

}
