package com.url_shortener.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UrlRepository {
    private final Map<String, String> urlMap = new ConcurrentHashMap<>();
    public void save(String shortUrl, String longUrl){
        urlMap.put(shortUrl, longUrl);
    }
    public String getOriUrl(String shortUrl){
        return urlMap.get(shortUrl);
    }
    public Boolean isExist(String shortUrl){
        return urlMap.containsKey(shortUrl);
    }
}
