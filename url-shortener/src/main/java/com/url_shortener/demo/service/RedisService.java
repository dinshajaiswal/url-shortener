package com.url_shortener.demo.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {
    private final RedisTemplate<String,String> redisTemplate;
    public RedisService(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    public void set(String key, String value, long seconds){
        redisTemplate.opsForValue().set(key,value, Duration.ofSeconds(seconds));
    }
    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }
    public void delete(String key){
        redisTemplate.delete(key);
    }
    public boolean testConnection() {

        set("test-key", "hello-redis", 60);

        String value = get("test-key");

        return "hello-redis".equals(value);
    }
}
