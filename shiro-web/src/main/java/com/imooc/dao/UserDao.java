package com.imooc.dao;

import com.imooc.vo.User;

import java.util.List;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-11 11:16
 * @Description:
 */
public interface UserDao {
    User getUserByUserName(String username);

    List<String> queryRolesByUserName(String username);

    List<String> queryPermissionsByUserName(String username);
}
