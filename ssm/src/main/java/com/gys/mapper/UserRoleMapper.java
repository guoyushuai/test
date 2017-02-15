package com.gys.mapper;

import com.gys.pojo.UserRole;

public interface UserRoleMapper {
    /*void saveNewUserRole(Integer userid, Integer roleid);*/
    void saveNewUserRole(UserRole userRole);

    void delRoleByUserId(Integer userid);

}
