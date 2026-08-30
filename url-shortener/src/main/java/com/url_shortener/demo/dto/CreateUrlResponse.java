package com.url_shortener.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CreateUrlResponse {
    private String shortCode;
    private String shortUrl;
    public CreateUrlResponse(String sc, String su){
        this.shortCode = sc;
        this.shortUrl = su;
    }
}
