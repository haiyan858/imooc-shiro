package com.imooc.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-11 23:33
 * @Description: 工具类： jedis 操作redis 的crud
 */

@Component
public class JedisUtil {

    @Resource
    private JedisPool jedisPool;

    /**
     * 获取redis 连接
     * @return
     */
    private Jedis getResouce() {
        return jedisPool.getResource();
    }

    /**
     * 新建key-value
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = getResouce();
        try {
            jedis.set(key, value);
        } finally {
            jedis.close();
        }
    }


    /**
     * 设置key的过期时间
     * @param key
     * @param seconds  秒
     */
    public void expire(byte[] key, int seconds) {
        Jedis jedis = getResouce();
        try {
            jedis.expire(key, seconds);
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取key
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        Jedis jedis = getResouce();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 删除 key
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = getResouce();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取redis中所有key
     * @param shiro_session_prefix 指定的key前缀
     * @return
     */
    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResouce();
        try {
            return jedis.keys((shiro_session_prefix + "*").getBytes());
        } finally {
            jedis.close();
        }
    }


}
