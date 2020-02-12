package com.imooc.realm;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-10 21:37
 * @Description:
 */
public class CustomRealm extends AuthorizingRealm {
    Map<String, String> userMap = new HashMap<>(16);

    {
        //userMap.put("Mark", "1234567");
        //userMap.put("Mark", "fcea920f7412b5da7be0cf42b8c93759");
        userMap.put("Mark", "d40fdd323f5322ff34a41f026f35cf20"); // 1234567+盐
        super.setName("customeRealm");
    }

    //
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
        Set<String> sets = new HashSet<>();
        sets.add("user:select");
        sets.add("user:add");
        return sets;
    }

    //模拟数据库或者缓存取数：角色
    private Set<String> getRolesByUserName(String username) {
        Set<String> sets = new HashSet<>();
        sets.add("admin");
        sets.add("user");
        return sets;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1 从主体传过来的认证信息中，获取用户名
        String username = (String) authenticationToken.getPrincipal();

        //2 通过用户名到数据中获取凭证
        String password = getPasswordByUserName(username);
        if (password == null) {
            return null;
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo("Mark", password, "customRealm");
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Mark"));
        return simpleAuthenticationInfo;
    }

    //模拟数据库或者缓存取数
    private String getPasswordByUserName(String username) {
        return userMap.get(username);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("1234567","Mark");
        System.out.println(md5Hash);
    }
}
