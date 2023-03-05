package com.huashijun.shorten.dto;

import java.io.Serializable;

public class ShortenDTO implements Serializable {

    private String longUrl;

    private String shortUrl;

    private Long pvNum;

    private Long uvNum;

    private Long ipNum;

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

    public Long getPvNum() {
        return pvNum;
    }

    public void setPvNum(Long pvNum) {
        this.pvNum = pvNum;
    }

    public Long getUvNum() {
        return uvNum;
    }

    public void setUvNum(Long uvNum) {
        this.uvNum = uvNum;
    }

    public Long getIpNum() {
        return ipNum;
    }

    public void setIpNum(Long ipNum) {
        this.ipNum = ipNum;
    }
}