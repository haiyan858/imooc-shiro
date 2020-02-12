package com.imooc;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import com.imooc.realm.CustomRealm;

import java.sql.SQLException;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-10 22:17
 * @Description: 自定义Realm
 */
public class CustomRealmAndHashTest {

    @Test
    public void testJdbcRealm() throws SQLException {
//
//        CustomRealm customRealm = new CustomRealm();
//
//        //1 构建 SecurityManager 环境
//        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
//        defaultSecurityManager.setRealm(customRealm);
//
//        //对密码加密
//        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
//        matcher.setHashAlgorithmName("md5");
//        matcher.setHashIterations(1);
//
//        //加盐
//
//        customRealm.setCredentialsMatcher(matcher);
//
//        //2 主体提交 认证请求
//        SecurityUtils.setSecurityManager(defaultSecurityManager);
//        Subject subject = SecurityUtils.getSubject();
//
//        //3
//        UsernamePasswordToken token = new UsernamePasswordToken("Mark", "1234567");
//        subject.login(token);
//
//        System.out.println("isAuthenticated：" + subject.isAuthenticated());
//
////        subject.checkRoles("admin","user");
////
////        subject.checkPermission("user:select");
//
//
    }

}
