package com.url_shortener.demo.service;

import com.url_shortener.demo.exception.InvalidUrlException;
import com.url_shortener.demo.exception.ShortCodeNotFoundException;
import org.springframework.stereotype.Service;
import com.url_shortener.demo.repository.UrlRepository;

import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    public UrlService(UrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }
    public String createShortUrl(String longUrl){
        if(!isValidUrl(longUrl)) throw new InvalidUrlException("Invalid URL");
        String shortUrl ;
        do{
            shortUrl = generateShortCode();
        } while(urlRepository.isExist(shortUrl));

        urlRepository.save(shortUrl, longUrl);
        return shortUrl;
    }
    private String generateShortCode(){
        return UUID.randomUUID().toString().substring(0,6);
    }

    public String getOriginalUrl(String shortCode) {
        String original = urlRepository.getOriUrl(shortCode);
        if(original == null){
            throw new ShortCodeNotFoundException(
                    "Short URL not found"
            );
        }
        return original;
    }

    public boolean isValidUrl(String url){
        return url!=null && (url.startsWith("http") || url.startsWith("https"));
    }
}
