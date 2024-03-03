package com.example.project.service;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

//@ConstructorBinding
@ConfigurationProperties("redis")
public class RedisDetailsConfig {
    @Getter
    protected String host;
    @Getter
    protected int port;
    @Getter
    protected RedisTtlConfig ttl;

    public RedisDetailsConfig(String host, int port, RedisTtlConfig ttl) {
        this.host = host;
        this.port = port;
        this.ttl = ttl;
    }
}
