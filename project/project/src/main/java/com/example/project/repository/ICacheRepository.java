package com.example.project.repository;


public interface ICacheRepository {

    void createCacheEntity(String key, String value);
    void createCountryCacheEntity(String key, String value);
    String getCacheEntity(String key);
    void updateCountryCacheEntity(String key, String value);
    Boolean isKeyExist(String key);
    void removeKey(String key);

}
