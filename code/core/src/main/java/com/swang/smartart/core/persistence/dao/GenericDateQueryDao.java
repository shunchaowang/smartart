package com.swang.smartart.core.persistence.dao;

import com.swang.smartart.core.util.ResourceProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by swang on 3/7/2015.
 */
public interface GenericDateQueryDao<T, PK extends Serializable> extends GenericDao<T, PK> {

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * There are two parts of the search to support grails criteria search with DataTables instant
     * search.
     *
     * @param t          contains all criteria for equals, like name equals xx and active equals
     *                   true, etc.
     *                   it means no criteria on exact equals if t is null.
     * @param search     instance wildcard search keyword, like name likes %xx%, etc.
     *                   it means no criteria with wildcard search if search is null.
     * @param rangeStart createdTime date range, needs both start and end.
     * @param rangeEnd   createdTime date range, needs both start and end.
     * @return number of the T matching criteria.
     */
    Long countByCriteria(T t, String search, Date rangeStart, Date rangeEnd);

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * There are two parts of the search to support grails criteria search with DataTables instant
     * search; DataTables dynamic ordering is also supported;
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param t          contains all criteria for equals, like name equals xx and active equals
     *                   true, etc.
     *                   it means no criteria on exact equals if t is null.
     * @param search     instance wildcard search keyword, like name likes %xx%, etc.
     *                   it means no criteria with wildcard search if search is null.
     * @param start      first position of the result.
     * @param length     max record of the result.
     * @param order      order by field.
     * @param orderDir   order direction on the order field.
     * @param rangeStart createdTime date range, needs both start and end.
     * @param rangeEnd   createdTime date range, needs both start and end.
     * @return
     */
    List<T> findByCriteria(T t, String search, Integer start, Integer length,
                           String order, ResourceProperties.JpaOrderDir orderDir,
                           Date rangeStart, Date rangeEnd);

    Boolean isBlank(T t);
}
