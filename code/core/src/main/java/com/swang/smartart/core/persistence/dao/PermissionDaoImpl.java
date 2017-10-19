package com.swang.smartart.core.persistence.dao;

import com.swang.smartart.core.persistence.entity.Permission;
import org.springframework.stereotype.Repository;

/**
 * Created by swang on 2/26/2015.
 */
@Repository("permissionDao")
public class PermissionDaoImpl extends GenericLookupDaoImpl<Permission, Long>
        implements PermissionDao {
}
