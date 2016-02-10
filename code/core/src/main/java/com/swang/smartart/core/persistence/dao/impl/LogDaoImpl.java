package com.swang.smartart.core.persistence.dao.impl;

import com.swang.smartart.core.persistence.dao.LogDao;
import com.swang.smartart.core.persistence.entity.Log;
import com.swang.smartart.core.util.ResourceProperties;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swang on 4/30/2015.
 */
@Repository("logDao")
public class LogDaoImpl extends GenericDaoImpl<Log, Long> implements LogDao {

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * There are two parts of the search to support grails criteria search with DataTables instant
     * search.
     *
     * @param log    contains all criteria for equals, like name equals xx and active equals
     *               true, etc.
     *               it means no criteria on exact equals if t is null.
     * @param search instance wildcard search keyword, like name likes %xx%, etc.
     *               it means no criteria with wildcard search if search is null.
     * @return number of the T matching criteria.
     */
    @Override
    public Long countByCriteria(Log log, String search) {
        return null;
    }

    /**
     * Dynamic search like grails findBy...
     * We create a dynamic criteria, like grails createCriteria() {}.
     * There are two parts of the search to support grails criteria search with DataTables instant
     * search; DataTables dynamic ordering is also supported;
     * To support DataTables pagination we have the start for the offset of the search, and
     * length for the max results we want to return.
     *
     * @param log      contains all criteria for equals, like name equals xx and active equals
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
    public List<Log> findByCriteria(Log log, String search, Integer start, Integer length,
                                    String order, ResourceProperties.JpaOrderDir orderDir) {
        return null;
    }

    /**
     * Test if T is blank for the query.
     *
     * @param log null return false, all required fields are null return false.
     * @return
     */
    @Override
    public Boolean isBlank(Log log) {
        return null;
    }
}
