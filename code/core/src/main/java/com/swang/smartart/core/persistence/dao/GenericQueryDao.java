package com.swang.smartart.core.persistence.dao;

import com.swang.smartart.core.util.ResourceProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Generic Dao Interface for count/query operations.
 * Created by swang on 3/6/2015.
 */
public interface GenericQueryDao<T, PK extends Serializable> extends GenericDao<T, PK> {

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * There are two parts of the search to support grails criteria search with DataTables instant
     * search.
     *
     * @param t      contains all criteria for equals, like name equals xx and active equals
     *               true, etc.
     *               it means no criteria on exact equals if t is null.
     * @param search instance wildcard search keyword, like name likes %xx%, etc.
     *               it means no criteria with wildcard search if search is null.
     * @return number of the T matching criteria.
     */
    Long countByCriteria(T t, String search);

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * There are two parts of the search to support grails criteria search with DataTables instant
     * search; DataTables dynamic ordering is also supported;
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param t        contains all criteria for equals, like name equals xx and active equals
     *                 true, etc.
     *                 it means no criteria on exact equals if t is null.
     * @param search   instance wildcard search keyword, like name likes %xx%, etc.
     *                 it means no criteria with wildcard search if search is null.
     * @param start    first position of the result.
     * @param length   max record of the result.
     * @param order    order by field, default is id.
     * @param orderDir order direction on the order field. default is DESC.
     * @return
     */
    List<T> findByCriteria(T t, String search, Integer start, Integer length,
                           String order, ResourceProperties.JpaOrderDir orderDir);

    /**
     * Test if T is blank for the query.
     *
     * @param t null return false, all required fields are null return false.
     * @return
     */
    Boolean isBlank(T t);
}

