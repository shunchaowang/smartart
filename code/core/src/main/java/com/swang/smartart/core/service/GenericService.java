package com.swang.smartart.core.service;

import com.swang.smartart.core.exception.MissingRequiredFieldException;
import com.swang.smartart.core.exception.NoSuchEntityException;
import com.swang.smartart.core.exception.NotUniqueException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by swang on 3/9/2015.
 */
public interface GenericService<T extends Serializable, PK> {

    T create(T t) throws MissingRequiredFieldException, NotUniqueException;

    T get(PK id) throws NoSuchEntityException;

    T update(T t) throws MissingRequiredFieldException, NotUniqueException;

    T delete(PK id) throws NoSuchEntityException;

    List<T> getAll();

    Long countAll();

    /**
     * Test if T is blank for the query.
     *
     * @param t null return false, all required fields are null return false.
     * @return
     */
    Boolean isBlank(T t);
}
