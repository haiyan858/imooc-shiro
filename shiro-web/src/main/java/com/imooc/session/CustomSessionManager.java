package com.imooc.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-12 00:29
 * @Description: 重写SessionManager
 * 因为访问一个页面时，重复读取redis中的session很多次，对redis会有压力
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    //重写读取session的这一部分，不从redis读取了，从request中读取
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId =  getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if (request != null && sessionId != null){
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null){
                return session;
            }
        }

        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null){
            request.setAttribute(sessionId.toString(),session);
        }

        return session;
    }
}
