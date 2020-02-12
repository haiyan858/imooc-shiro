package com.imooc.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-11 22:41
 * @Description: 多个roles，满足任意一个role即可
 */
public class RolesOrFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest,
                                      ServletResponse servletResponse,
                                      Object o) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);
        String[] roles = (String[]) o;
        if (roles == null || roles.length == 0){
            return true;
        }

        for (String role : roles) {
            if (subject.hasRole(role)){
                return true;
            }
        }

        return false;
    }
}
