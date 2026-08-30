package com.url_shortener.demo.service;

import com.url_shortener.demo.entity.UrlMapping;
import com.url_shortener.demo.exception.InvalidUrlException;
import com.url_shortener.demo.exception.ShortCodeNotFoundException;
import org.springframework.stereotype.Service;
import com.url_shortener.demo.repository.UrlRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    public UrlService(UrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }
    public String createShortUrl(String longUrl){
        if(!isValidUrl(longUrl)) throw new InvalidUrlException("Invalid URL");
        String shortCode ;
        do{
            shortCode = generateShortCode();
        } while(urlRepository.existsByShortCode(shortCode));

        UrlMapping urlMapping = new UrlMapping(shortCode, longUrl, LocalDateTime.now());
        urlRepository.save(urlMapping);
        return shortCode;
    }

    private String generateShortCode(){
        return UUID.randomUUID().toString().substring(0,6);
    }

    public String getOriginalUrl(String shortCode) {
        UrlMapping urlMapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(()-> new ShortCodeNotFoundException("Short URL not found"));

        return urlMapping.getOriginalUrl();
    }

    public boolean isValidUrl(String url){
        return url!=null && (url.startsWith("http") || url.startsWith("https"));
    }
}
