package com.imooc.session;

import com.imooc.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-11 23:32
 * @Description: session的crud 创建/修改/删除/读取会话
 */
public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisUtil;

    private final String SHIRO_SESSION_PREFIX = "imooc-session:";

    private byte[] getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }

    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key, value);
            jedisUtil.expire(key, 600);
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId); //sessionId和session捆绑，否则空指针异常
        saveSession(session);
        return sessionId;
    }

    /**
     * 根据会话ID获取会话
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("read Session");
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);

        return (Session) SerializationUtils.deserialize(value);
    }

    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     * @param session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    /**
     * 删除会话；当会话过期/会话停止（如用户退出时）会调用
     * @param session
     */
    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        byte[] key = getKey(session.getId().toString());
        jedisUtil.del(key);
    }

    /**
     * 获取存活的session
     * 获取当前所有活跃用户，如果用户量多此方法影响性能
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);

        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)){
            return sessions;
        }
        for (byte[] key:keys){
            byte[] sessionBytes = jedisUtil.get(key);
            Session session = (Session) SerializationUtils.deserialize(sessionBytes);
            sessions.add(session);
        }
        return sessions;
    }
}
