package com.swang.smartart.core.persistence.dao;

import java.io.Serializable;

/**
 * Generic lookup dao for all lookup domains, including *Status and *Type objects.
 * Is the super interface for all Dao of the lookup objects.
 * <p>
 * Created by swang on 2/24/2015.
 */
public interface GenericLookupDao<T extends Serializable, PK> extends GenericDao<T, PK> {

    /**
     * Find object by name.
     *
     * @param name is name of the object.
     * @return generic object specified by name.
     */
    T findByName(String name);

    /**
     * Find object by code.
     *
     * @param code is code of the object.
     * @return generic object specified by code.
     */
    T findByCode(String code);
}
