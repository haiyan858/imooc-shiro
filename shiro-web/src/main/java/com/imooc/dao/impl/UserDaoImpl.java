package com.imooc.dao.impl;

import com.imooc.dao.UserDao;
import com.imooc.vo.User;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-11 11:16
 * @Description:
 */
@Component
public class UserDaoImpl implements UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据用户名查询用户密码（已加密）
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByUserName(String username) {

        String sql = "select username,password from users where username = ? ";

        List<User> list = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        });

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 根据用户名查询角色
     *
     * @param username
     * @return
     */
    @Override
    public List<String> queryRolesByUserName(String username) {

        String sql = "select role_name from user_roles where username = ? ";
        List<String> list = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("role_name");
            }
        });

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return list;
    }

    /**
     * 根据用户名查询权限
     *
     * @param username
     * @return
     */
    @Override
    public List<String> queryPermissionsByUserName(String username) {
        String sql ="select permission from roles_permissions rp inner join user_roles tur on  tur.role_name =  rp.role_name where tur.username = ?";
        List<String> list  = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("permission");
            }
        });
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list;

        /*String sql = "select permission from roles_permissions where role_name = ? ";
        List<String> list = queryRolesByUserName(username);
        List<String> permission = jdbcTemplate.query(sql, new Object[]{list.toArray()}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("permission");
            }
        });
        if (CollectionUtils.isEmpty(permission)) {
            return null;
        }
        return permission;*/
    }
}
