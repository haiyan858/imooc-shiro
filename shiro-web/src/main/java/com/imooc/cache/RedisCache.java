package com.imooc.cache;

import com.imooc.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-12 15:29
 * @Description: cache 实例
 */

@Component
public class RedisCache<K, V> implements Cache<K, V>{

    private final String CACHE_PREFIX = "imooc-cache:";

    @Resource
    private JedisUtil jedisUtil;

    private byte[] getKey(K k){
        if (k instanceof String){
            return (CACHE_PREFIX+k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    /*private byte[] getValue(V v){
        return SerializationUtils.serialize(v);
    }*/

    @Override
    public V get(K k) throws CacheException {
        byte[] value = jedisUtil.get(getKey(k));
        if (value != null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key,value);
        jedisUtil.expire(key,600);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        jedisUtil.del(key);
        if (value != null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {
        //不要重写此方法。
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        Set<byte[]> keys = jedisUtil.keys(CACHE_PREFIX);
        Set set = new HashSet<>();
        if (keys != null){
            for (byte[] key:keys){
                set.add(SerializationUtils.deserialize(key));
            }
        }
        return set;
    }

    @Override
    public Collection<V> values() {

        return null;
    }
}
