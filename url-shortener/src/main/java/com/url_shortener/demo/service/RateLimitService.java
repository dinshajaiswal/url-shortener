package com.url_shortener.demo.service;

import org.springframework.stereotype.Service;

@Service
public class RateLimitService {
    private final RedisService redisService;

    public RateLimitService(RedisService redisService){
        this.redisService = redisService;
    }

    public boolean isAllowed(String clientIP){
        String key = "rate_limit:" + clientIP;

        long count = redisService.increment(key);

        if(count==1) {
            redisService.expire(key, 60);
        }
        return count<=10;
    }
}
