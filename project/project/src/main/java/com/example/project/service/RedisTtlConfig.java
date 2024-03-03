package com.example.project.service;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RedisTtlConfig {
    protected long countryCacheTtl;

    protected long cacheToFiveMinutes;
}

