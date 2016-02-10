package com.swang.smartart.core.service.impl;

import com.swang.smartart.core.service.GenericQueryService;
import com.swang.smartart.core.util.ResourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by swang on 3/17/2015.
 */
public abstract class GenericQueryServiceImpl<T extends Serializable, PK>
        implements GenericQueryService<T, PK> {

    private Logger logger = LoggerFactory.getLogger(GenericQueryServiceImpl.class);

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * There are two parts of the search to support grails criteria search with DataTables instant
     * search.
     * This class is abstract which needs to be implemented by subclasses.
     *
     * @param t      contains all criteria for equals, like name equals xx and active equals
     *               true, etc.
     *               it means no criteria on exact equals if t is null.
     * @param search instance wildcard search keyword, like name likes %xx%, etc.
     *               it means no criteria with wildcard search if search is null.
     * @return number of the T matching criteria.
     */
    @Override
    public abstract Long countByCriteria(T t, String search);

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     *
     * @param t contains all criteria for equals, like name equals xx and active equals
     *          true, etc.
     *          it means no criteria on exact equals if t is null.
     * @return number of the T matching criteria.
     */
    @Override
    public Long countByCriteria(T t) {

        return countByCriteria(t, null);
    }

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * <p>
     * it means no criteria on exact equals if t is null.
     *
     * @param search instance wildcard search keyword, like name likes %xx%, etc.
     *               it means no criteria with wildcard search if search is null.
     * @return number of the T matching criteria.
     */
    @Override
    public Long countByCriteria(String search) {

        return countByCriteria(null, search);
    }

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * There are two parts of the search to support grails criteria search with DataTables instant
     * search; DataTables dynamic ordering is also supported;
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     * This class is abstract which needs to be implemented by subclasses.
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
    @Override
    public abstract List<T> findByCriteria(T t, String search, Integer start, Integer length,
                                           String order, ResourceProperties.JpaOrderDir orderDir);

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * DataTables dynamic ordering is also supported.
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param t        contains all criteria for equals, like name equals xx and active equals
     *                 true, etc.
     *                 it means no criteria on exact equals if t is null.
     * @param start    first position of the result.
     * @param length   max record of the result.
     * @param order    order by field, default is id.
     * @param orderDir order direction on the order field. default is DESC.
     * @return
     */
    @Override
    public List<T> findByCriteria(T t, Integer start, Integer length, String order,
                                  ResourceProperties.JpaOrderDir orderDir) {

        return findByCriteria(t, null, start, length, order, orderDir);
    }

    /**
     * Dynamic search like grails findBy... without ordering.
     * We create a dynamic criteria, like grails createCriteria() {}.
     * There are two parts of the search to support grails criteria search with DataTables instant
     * search; DataTables dynamic ordering is also supported;
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param t      contains all criteria for equals, like name equals xx and active equals
     *               true, etc.
     *               it means no criteria on exact equals if t is null.
     * @param search instance wildcard search keyword, like name likes %xx%, etc.
     *               it means no criteria with wildcard search if search is null.
     * @param start  first position of the result.
     * @param length max record of the result.
     * @return
     */
    @Override
    public List<T> findByCriteria(T t, String search, Integer start, Integer length) {

        return findByCriteria(t, null, start, length, null, null);
    }

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * DataTables dynamic ordering is also supported.
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param search   instance wildcard search keyword, like name likes %xx%, etc.
     *                 it means no criteria with wildcard search if search is null.
     * @param start    first position of the result.
     * @param length   max record of the result.
     * @param order    order by field, default is id.
     * @param orderDir order direction on the order field. default is DESC.
     * @return
     */
    @Override
    public List<T> findByCriteria(String search, Integer start, Integer length, String order,
                                  ResourceProperties.JpaOrderDir orderDir) {

        return findByCriteria(null, search, start, length, order, orderDir);
    }

    /**
     * Dynamic search like grails findBy... without pagination.
     * We create a dynamic criteria, like grails createCriteria() {}.
     * DataTables dynamic ordering is also supported.
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param search   instance wildcard search keyword, like name likes %xx%, etc.
     *                 it means no criteria with wildcard search if search is null.
     * @param order    order by field, default is id.
     * @param orderDir order direction on the order field. default is DESC.
     * @return
     */
    @Override
    public List<T> findByCriteria(String search, String order, ResourceProperties.JpaOrderDir
            orderDir) {

        return findByCriteria(null, search, null, null, order, orderDir);
    }


    /**
     * Dynamic search like grails findBy... without pagination.
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
     * @param order    order by field, default is id.
     * @param orderDir order direction on the order field. default is DESC.
     * @return
     */
    @Override
    public List<T> findByCriteria(T t, String search, String order,
                                  ResourceProperties.JpaOrderDir orderDir) {

        logger.info("FindByCriteria without pagination.");
        return findByCriteria(t, search, null, null, order, orderDir);
    }

    /**
     * Dynamic search like grails findBy... without ordering.
     * We create a dynamic criteria, like grails createCriteria() {}.
     * DataTables dynamic ordering is also supported.
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param t      contains all criteria for equals, like name equals xx and active equals
     *               true, etc.
     *               it means no criteria on exact equals if t is null.
     * @param start  first position of the result.
     * @param length max record of the result.
     * @return
     */
    @Override
    public List<T> findByCriteria(T t, Integer start, Integer length) {

        logger.info("FindByCriteria without ordering.");
        return findByCriteria(t, null, start, length, null, null);
    }

    /**
     * Dynamic search like grails findBy... without pagination.
     * We create a dynamic criteria, like grails createCriteria() {}.
     * DataTables dynamic ordering is also supported.
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param t        contains all criteria for equals, like name equals xx and active equals
     *                 true, etc.
     *                 it means no criteria on exact equals if t is null.
     * @param order    order by field, default is id.
     * @param orderDir order direction on the order field. default is DESC.
     * @return
     */
    @Override
    public List<T> findByCriteria(T t, String order, ResourceProperties.JpaOrderDir orderDir) {

        return findByCriteria(t, null, null, null, order, orderDir);
    }

    /**
     * Dynamic search like grails findBy... without ordering.
     * We create a dynamic criteria, like grails createCriteria() {}.
     * DataTables dynamic ordering is also supported.
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param search instance wildcard search keyword, like name likes %xx%, etc.
     *               it means no criteria with wildcard search if search is null.
     * @param start  first position of the result.
     * @param length max record of the result.
     * @return
     */
    @Override
    public List<T> findByCriteria(String search, Integer start, Integer length) {

        return findByCriteria(null, search, start, length, null, null);
    }

    @Override
    public List<T> findByCriteria(T t) {
        return findByCriteria(t, null, null, null, null);
    }
}
