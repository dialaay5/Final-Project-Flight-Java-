package com.example.project.repository;

import com.example.project.service.RedisDetailsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPooled;

@Repository
public class CacheRepositoryImpl implements ICacheRepository {

    @Autowired
    JedisPooled jedisPool;

    @Autowired
    private RedisDetailsConfig redisDetailsConfig;

    @Override
    public void createCountryCacheEntity(String key, String value) {
        jedisPool.setex(key, redisDetailsConfig.getTtl().getCountryCacheTtl(), value);
        System.out.println("key name:" + key);
    }
    @Override
    public void createCacheEntity(String key, String value) {
        jedisPool.setex(key, redisDetailsConfig.getTtl().getCacheToFiveMinutes(), value);
        System.out.println("key name:" + key);
    }

    @Override
    public String getCacheEntity(String key) {
        if (isKeyExist(key)){
            return jedisPool.get(key);
        }
        return null;
    }

    @Override
    public void updateCountryCacheEntity(String key, String value) {
        if (isKeyExist(key)){
            jedisPool.setex(key, redisDetailsConfig.getTtl().getCountryCacheTtl(), value);
        }
    }
    @Override
    public Boolean isKeyExist(String key) {
        return jedisPool.exists(key);
    }

    @Override
    public void removeKey(String key) {
        if (isKeyExist(key)) {
            jedisPool.del(key);
            System.out.println("key deleted");
        }
    }

}