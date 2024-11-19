package com.rocketseat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UrlData {
    private String originalUrl;
    private long timeExpiration;
}
