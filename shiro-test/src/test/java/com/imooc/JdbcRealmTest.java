package com.imooc;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-10 21:05
 * @Description: jdbcRealm 测试
 */
public class JdbcRealmTest {


    DruidDataSource datasource = new DruidDataSource();

    {
        datasource.setUrl("jdbc:mysql://localhost:3306/test");
        datasource.setUsername("root");
        datasource.setPassword("123456");
    }

    @Test
    public void testJdbcRealm() throws SQLException {

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(datasource);
        jdbcRealm.setPermissionsLookupEnabled(true); //设置为true =》去查询权限数据

        String sql = "select password from test_user where username=? ";
        jdbcRealm.setAuthenticationQuery(sql);

        /*DruidPooledConnection conn = datasource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        String username = "Mark";
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        String[] result = new String[]{};
        for(boolean foundResult = false; rs.next(); foundResult = true) {
            if (foundResult) {
                throw new AuthenticationException("More than one user row found for user [" + username + "]. Usernames must be unique.");
            }
            result[0] = rs.getString(1);
            result[1] = rs.getString(2);
        }*/


        //1 构建 SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //2 主体提交 认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //3
        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");
        subject.login(token);

        /*System.out.println("isAuthenticated：" + subject.isAuthenticated());

        subject.checkRoles("admin","user");

        subject.checkPermission("user:select");*/

    }
}
