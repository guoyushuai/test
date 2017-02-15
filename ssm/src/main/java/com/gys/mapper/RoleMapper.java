package com.gys.mapper;

import com.gys.pojo.Role;

import java.util.List;

public interface RoleMapper {
    List<Role> findAllRoles();

    Role findRoleById(Integer roleid);

}
