package com.imooc.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import javax.annotation.Resource;

/**
 * @Author cuihaiyan
 * @Create_Time 2020-02-12 15:26
 * @Description: 自定义缓存管理器： 用于缓存角色和权限数据（不经常变）
 */

public class RedisCacheManager implements CacheManager {

    @Resource
    private RedisCache redisCache;

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        return redisCache;
    }

}
