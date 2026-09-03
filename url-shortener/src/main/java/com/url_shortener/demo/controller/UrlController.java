package com.url_shortener.demo.controller;

import com.url_shortener.demo.dto.CreateUrlRequest;
import com.url_shortener.demo.dto.CreateUrlResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.url_shortener.demo.service.UrlService;

@RestController
public class UrlController {
    private final UrlService urlService;
    public UrlController(UrlService urlService){
        this.urlService = urlService;
    }
    @PostMapping("/api/v1/urls")
    public ResponseEntity<CreateUrlResponse> createShortUrl(@RequestBody CreateUrlRequest request){
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
