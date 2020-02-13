package com.imooc.controller;

import com.imooc.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-10 23:53
 * @Description:
 */
@Controller
public class UserController {

    @ResponseBody
    @RequestMapping(value = "/subLogin", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();

        if(!subject.isAuthenticated()){ //当前用户是否已经被认证（登录）
            String username = user.getUsername();
            String password = user.getPassword();

            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            try {
                token.setRememberMe(user.isRememberMe()); //记住我（前台是否勾选复选框）
                subject.login(token); //执行登录
            } catch (AuthenticationException e) {
                return e.getMessage();
            }

            if (subject.hasRole("admin")) {
                return "有admin权限";
            }
            return "无admin权限";
        }

        return "未登录";
    }


    //@RequiresRoles("admin") //要求的角色
    @RequestMapping(value = "/testRole", method = RequestMethod.GET)
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }

    //@RequiresPermissions("xxx") 要求的权限
    //@RequiresRoles("admin1") //要求的角色
    @RequestMapping(value = "/testRole1", method = RequestMethod.GET)
    @ResponseBody
    public String testRole1() {
        return "testRole1 success";
    }


    @RequestMapping(value = "/testPerms", method = RequestMethod.GET)
    @ResponseBody
    public String testPerms() {
        return "testPerms success";
    }

    @RequestMapping(value = "/testPerms1", method = RequestMethod.GET)
    @ResponseBody
    public String testPerms1() {
        return "testPerms1 success";
    }

}
