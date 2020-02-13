package com.imooc.shiro.realm;

import com.imooc.dao.UserDao;
import com.imooc.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-10 21:37
 * @Description:
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;
    {
        super.setName("customRealm");
    }

    /**
     * 用于授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();

        Set<String> roles = getRolesByUserName(username);
        Set<String> permissions = getPermissionsByUserName(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(getRolesByUserName(username));

        return simpleAuthorizationInfo;
    }

    //模拟数据库或者缓存取数：权限
    private Set<String> getPermissionsByUserName(String username) {
        /*Set<String> sets = new HashSet<>();
        sets.add("user:select");
        sets.add("user:add");
        return sets;*/

        List<String> list = userDao.queryPermissionsByUserName(username);
//        System.out.println(list.get(0));
//        System.out.println(list.get(1));
        return new HashSet<>(list);
    }

    //模拟数据库或者缓存取数：角色
    private Set<String> getRolesByUserName(String username) {
        /*Set<String> sets = new HashSet<>();
        sets.add("admin");
        sets.add("user");
        return sets;*/

        List<String> list = userDao.queryRolesByUserName(username);

        Set<String> sets = new HashSet<>(list);

        return sets;
    }

    /**
     * 用于认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1 从主体传过来的认证信息中，获取用户名
        //这个是controller 传过来的， 从表单获取的 - > subject.login(token); //执行登录
        String username = (String) token.getPrincipal(); // 此处两个token是同一个

        //2 通过用户名到数据中获取凭证
        String password = getPasswordByUserName(username);
        if (password == null) {
            return null;
        }

        //SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo("Mark", password, "customRealm");
        //simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Mark"));

        //realName 表示 the realm from where the principal and credentials were acquired.
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));
        return simpleAuthenticationInfo;
    }

    //模拟数据库或者缓存取数
    private String getPasswordByUserName(String username) {
        User user = userDao.getUserByUserName(username);
        if (user == null) {
            return null;
        }
        return user.getPassword();
        //return userMap.get(username);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("1234567", "Mark");
        System.out.println(md5Hash); //d40fdd323f5322ff34a41f026f35cf20
    }
}
