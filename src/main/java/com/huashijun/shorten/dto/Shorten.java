package com.huashijun.shorten.dto;

import java.io.Serializable;

public class Shorten implements Serializable {

    private String longUrl;

    private String shortUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}