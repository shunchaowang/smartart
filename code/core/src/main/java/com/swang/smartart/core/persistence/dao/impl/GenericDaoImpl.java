package com.swang.smartart.core.persistence.dao.impl;

import com.swang.smartart.core.persistence.dao.GenericDao;
import com.swang.smartart.core.util.ResourceProperties;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class GenericDaoImpl<T extends Serializable, PK> implements GenericDao<T, PK> {

    private static final Logger logger = LoggerFactory.getLogger(GenericDaoImpl.class);

    @PersistenceContext
    protected EntityManager entityManager;
    private Class<T> type;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
        logger.debug("Getting generic type parameter " + type.getName());
    }

    @Override
    public T create(T persistentObject) {
        try {
            entityManager.persist(persistentObject);
            return persistentObject;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> getAll() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);
        Root<T> root = query.from(type);
        query.select(root);

        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        logger.debug("getAll query is " + query.toString());
        try {
            return typedQuery.getResultList();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    @Override
    public Long countAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<T> root = query.from(type);
        query.select(builder.count(root));

        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        logger.debug("getAll query is " + query.toString());
        try {
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    @Override
    public T update(T persistentObject) {
        try {
            return entityManager.merge(persistentObject);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(PK id) {
        entityManager.remove(entityManager.getReference(type, id));
    }

    @Override
    public T get(PK id) {
        try {
            return entityManager.find(type, id);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * Counts the record using positional parameters.
     *
     * @param criteria
     * @param args
     */
    @Override
    public Long count(String criteria, List<Object> args) {
        TypedQuery<T> query = entityManager.createQuery(criteria, type);

        // set positional params
        for (int i = 0; i < args.size(); i++) {
            query.setParameter(i, args.get(i));
        }

        logger.debug("Returning positional parameter query string of count " + query.toString());
        return (Long) query.getSingleResult();
    }

    /**
     * Counts the record using name-value-pair parameters.
     *
     * @param criteria
     * @param args
     */
    @Override
    public Long count(String criteria, Map<String, Object> args) {

        TypedQuery<T> query = entityManager.createQuery(criteria, type);
        for (String key : args.keySet()) {
            query.setParameter(key, args.get(key));
        }

        logger.debug("Returning named parameter query string of count " + query.toString());
        return (Long) query.getSingleResult();
    }

    /**
     * Find all records using positional parameters.
     *
     * @param criteria
     * @param args
     */
    @Override
    public List<T> findAll(String criteria, List<Object> args) {

        TypedQuery<T> query = entityManager.createQuery(criteria, type);

        for (int i = 0; i < args.size(); i++) {
            query.setParameter(i, args.get(i));
        }

        logger.debug("Returning positional parameter query string of count " + query.toString());

        return query.getResultList();
    }

    /**
     * Find all records using name-value-pair.
     *
     * @param criteria
     * @param args
     */
    @Override
    public List<T> findAll(String criteria, Map<String, Object> args) {

        TypedQuery<T> query = entityManager.createQuery(criteria, type);

        for (String key : args.keySet()) {
            query.setParameter(key, args.get(key));
        }

        logger.debug("Returning named parameter query string of count " + query.toString());
        return query.getResultList();
    }

    /**
     * Calculate the first result of the query based on page number and size per page.
     */
    private int calculateFirstResult(Integer pageNumber, Integer pageSize) {

        if (pageNumber == null) {
            pageNumber = 1;
        }

        logger.debug("Offset of the query is " + (pageNumber - 1) * pageSize + " with pageNumber " +
                pageNumber +
                " and pageSze " + pageSize);
        return (pageNumber - 1) * pageSize;
    }

    private TypedQuery<T> createPaginatedQuery(String criteria, Integer pageNumber, Integer
            pageSize) {

        // if pageSize is null, set it to default value
        if (pageSize == null) {
            pageSize = ResourceProperties.PAGE_SIZE;
        }

        int firstResult = calculateFirstResult(pageNumber, pageSize);

        TypedQuery<T> query = entityManager.createQuery(criteria, type).
                setFirstResult(firstResult).
                setMaxResults(pageSize);
        logger.debug("Returning paginated query " + query.toString());
        return query;
    }

    /**
     * Find all records using positional parameters with pagination.
     *
     * @param criteria
     * @param args
     */
    @Override
    public List<T> findAll(String criteria, List<Object> args, Integer pageNumber, Integer
            pageSize) {

        TypedQuery<T> query = createPaginatedQuery(criteria, pageNumber, pageSize);
        for (int i = 0; i < args.size(); i++) {
            query.setParameter(i, args.get(i));
        }

        logger.debug("Returning paginated positional parameter query for all " + query.toString());
        return query.getResultList();
    }

    /**
     * Find all records using name-value-pair with pagination.
     *
     * @param criteria
     * @param args
     */
    @Override
    public List<T> findAll(String criteria, Map<String, Object> args, Integer pageNumber, Integer
            pageSize) {

        TypedQuery<T> query = createPaginatedQuery(criteria, pageNumber, pageSize);

        for (String key : args.keySet()) {
            query.setParameter(key, args.get(key));
        }

        logger.debug("Returning paginated named parameter query for all " + query.toString());
        return query.getResultList();
    }

    /**
     * Find all records with pagination without parameters.
     *
     * @param criteria
     * @param pageNumber
     * @param pageSize
     */
    @Override
    public List<T> findAll(String criteria, Integer pageNumber, Integer pageSize) {

        TypedQuery<T> query = createPaginatedQuery(criteria, pageNumber, pageSize);

        logger.debug("Returning paginated string query for all " + query.toString());

        return query.getResultList();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    /**
     * Count by jpa criteria query.
     *
     * @param criteriaQuery the criteria contains all query
     * @return the number of the query
     */
    @Override
    public Long count(CriteriaQuery<Long> criteriaQuery) {
        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * Count by jqa query language.
     *
     * @param jpql query string, like SELECT u.username FROM User AS u.
     * @return number of result
     */
    @Override
    public Long count(String jpql) {
        TypedQuery<Long> typedQuery = entityManager.createQuery(jpql, Long.class);
        try {
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * Find all T from criteria with offset and max records.
     *
     * @param criteriaQuery the criteria contains all query
     * @param start         the offset of the result
     * @param length        the max records of the result
     * @return list of T
     */
    @Override
    public List<T> find(CriteriaQuery<T> criteriaQuery, Integer start, Integer length) {
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        logger.debug("findByCriteria query is " + typedQuery.unwrap(Query.class).getQueryString());
        if (start != null) {
            typedQuery.setFirstResult(start);
        }
        if (length != null) {
            typedQuery.setMaxResults(length);
        }

        try {
            return typedQuery.getResultList();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * Count by jqa query language.
     *
     * @param jpql query string, like SELECT u.username FROM User AS u.
     * @return list of result
     */
    @Override
    public List<T> find(String jpql) {
        TypedQuery<T> typedQuery = entityManager.createQuery(jpql, type);
        try {
            return typedQuery.getResultList();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * Find all T from criteria with offset.
     *
     * @param criteriaQuery the criteria contains all query
     * @param start         the offset of the result
     * @return list of T
     */
    @Override
    public List<T> find(CriteriaQuery<T> criteriaQuery, Integer start) {
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        if (start != null) {
            typedQuery.setFirstResult(start);
        }
        try {
            return typedQuery.getResultList();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * Find all T from criteria.
     *
     * @param criteriaQuery the criteria contains all query
     * @return list of T
     */
    @Override
    public List<T> find(CriteriaQuery<T> criteriaQuery) {
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        try {
            return typedQuery.getResultList();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * Query for one single object
     *
     * @param criteriaQuery
     * @return Single T
     */
    @Override
    public T findSingle(CriteriaQuery<T> criteriaQuery) {
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * Query for one single object
     *
     * @param jpql
     * @return Single T
     */
    @Override
    public T findSingle(String jpql) {
        TypedQuery<T> typedQuery = entityManager.createQuery(jpql, type);
        try {
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }
}
