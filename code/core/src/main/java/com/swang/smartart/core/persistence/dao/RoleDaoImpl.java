package com.swang.smartart.core.persistence.dao;

import com.swang.smartart.core.persistence.entity.Role;
import org.springframework.stereotype.Repository;

/**
 * Created by swang on 2/26/2015.
 */
@Repository("roleDao")
public class RoleDaoImpl extends GenericLookupDaoImpl<Role, Long>
        implements RoleDao {
}
