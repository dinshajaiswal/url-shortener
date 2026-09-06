package com.url_shortener.demo.service;

import com.url_shortener.demo.entity.UrlMapping;
import com.url_shortener.demo.exception.ExpiredUrlException;
import com.url_shortener.demo.exception.InvalidUrlException;
import com.url_shortener.demo.exception.ShortCodeNotFoundException;
import com.url_shortener.demo.utils.Base62Encoder;
import org.springframework.stereotype.Service;
import com.url_shortener.demo.repository.UrlRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;
    private final RedisService redisService;

    public UrlService(UrlRepository urlRepository, Base62Encoder base62Encoder, RedisService redisService){
        this.urlRepository = urlRepository;
        this.base62Encoder = base62Encoder;
        this.redisService = redisService;
    }
    public String createShortUrl(String longUrl, LocalDateTime expiresAt){
        if(!isValidUrl(longUrl)) throw new InvalidUrlException("Invalid URL");

        UrlMapping urlMapping = new UrlMapping(null, longUrl, LocalDateTime.now(), expiresAt);
        urlMapping = urlRepository.save(urlMapping);

        String shortCode = base62Encoder.encode(urlMapping.getId());
        urlMapping.setShortCode(shortCode);
        urlRepository.save(urlMapping);

        return shortCode;
    }

    private String generateShortCode(){
        return UUID.randomUUID().toString().substring(0,6);
    }

    public String getOriginalUrl(String shortCode) {

        String key = "url:" + shortCode;

        // 1. Check Redis
        String cachedUrl = redisService.get(key);

        if (cachedUrl != null) {
            return cachedUrl;
        }

        // 2. Redis MISS → MySQL
        UrlMapping urlMapping =
                urlRepository.findByShortCode(shortCode)
                        .orElseThrow(() ->
                                new ShortCodeNotFoundException(
                                        "Short URL not found"));

        // 3. Check expiration
        LocalDateTime expiresAt = urlMapping.getExpiresAt();

        if (expiresAt != null &&
                expiresAt.isBefore(LocalDateTime.now())) {

            throw new ExpiredUrlException(
                    "Short URL has expired");
        }

        String originalUrl = urlMapping.getOriginalUrl();

        // 4. Calculate Redis TTL
        long ttlSeconds = 3600; // default 1 hour

        if (expiresAt != null) {

            ttlSeconds = Duration.between(
                    LocalDateTime.now(),
                    expiresAt
            ).getSeconds();
        }

        // 5. Cache only if TTL is positive
        if (ttlSeconds > 0) {
            redisService.set(key, originalUrl, ttlSeconds);
        }

        return originalUrl;
    }

    public boolean isValidUrl(String url){
        return url!=null && (url.startsWith("http") || url.startsWith("https"));
    }
}
