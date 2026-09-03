package com.url_shortener.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateUrlRequest {
    private String url;
    private LocalDateTime expiresAt;
}
