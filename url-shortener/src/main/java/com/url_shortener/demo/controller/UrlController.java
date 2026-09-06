package com.url_shortener.demo.controller;

import com.url_shortener.demo.dto.CreateUrlRequest;
import com.url_shortener.demo.dto.CreateUrlResponse;
import com.url_shortener.demo.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.url_shortener.demo.service.UrlService;

@RestController
public class UrlController {
    private final UrlService urlService;
    private final RateLimitService rateLimitService;

    public UrlController(UrlService urlService, RateLimitService rateLimitService){
        this.urlService = urlService;
        this.rateLimitService = rateLimitService;
    }
    @PostMapping("/api/v1/urls")
    public ResponseEntity<CreateUrlResponse> createShortUrl(@RequestBody CreateUrlRequest request, HttpServletRequest httpRequest){
        String clientIP = httpRequest.getRemoteAddr();

        if(!rateLimitService.isAllowed(clientIP)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        String shortCode = urlService.createShortUrl(request.getUrl(), request.getExpiresAt());
        String shortUrl = "http://localhost:8080/" + shortCode;
        CreateUrlResponse response = new CreateUrlResponse(shortCode, shortUrl);
        return ResponseEntity.ok(response);
    }
    @GetMapping("{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String originalUrl = urlService.getOriginalUrl(shortCode);
        if(originalUrl == null) return ResponseEntity.notFound().build();
        return ResponseEntity.status(302).header("Location", originalUrl).build();
    }
}
