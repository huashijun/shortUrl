package com.huashijun.shorten.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.huashijun.redis.util.RedisUtils;
import com.huashijun.shorten.dto.Shorten;
import com.huashijun.shorten.dto.ShortenDTO;
import com.huashijun.shorten.utils.SixTwoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author huashijun
 */
@Controller
public class IndexController {

    private AtomicLong atomicLong = new AtomicLong(10000);
    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/")
    public String index(HttpServletResponse response){
        Cookie cookie = new Cookie("sessionId", "1234567890");
        response.addCookie(cookie);
        return "index";
    }

    @GetMapping("/{url}")
    public RedirectView redirectToUrl(@PathVariable String url,
                                      HttpServletRequest request) {
        RedirectView redirectView = new RedirectView();
        //短链接
        String shortUrl = "http://localhost/" + url;
        //获取对应的长链接
        Object o= redisUtils.hashGet("shortUrl", shortUrl);
        String longUrl = (String) o;
        redirectView.setUrl(longUrl);
        //获取ip
        String clientIP = ServletUtil.getClientIP(request,  null);
        //存储短链接和ip
        redisUtils.listLeftPush(shortUrl,clientIP);
        //获取cookie
        String cookieValue = "default";
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("sessionId".equals(cookie.getName())){
                    cookieValue = cookie.getValue();
                }
            }
        }
        redisUtils.hashPut("uv",shortUrl,cookieValue);
        return redirectView;
    }

    @GetMapping("/shortUrl/generate")
    @ResponseBody
    public Shorten generate(@RequestParam("longUrl") String longUrl){
        //获取唯一id然后自增
        long l = atomicLong.getAndAdd(1);
        //获取62进制字符串，作为短链接url
        String sixTwoStr = SixTwoUtil.decimalToSixtyTwo(l);
        //生成短链接
        String shortUrl = "http://localhost/" + sixTwoStr;
        //将原长链接和短链接关联存储
        Shorten shorten = new Shorten();
        shorten.setLongUrl(longUrl);
        shorten.setShortUrl(shortUrl);
        redisUtils.hashPut("shortUrl",shortUrl,longUrl);
        return shorten;
    }

    @GetMapping("/shortUrl/analyse")
    @ResponseBody
    public ShortenDTO analyse(@RequestParam("shortUrl") String shortUrl){
        ShortenDTO dto = new ShortenDTO();
        //获取对应的长链接
        Object o= redisUtils.hashGet("shortUrl", shortUrl);
        String longUrl = (String) o;
        dto.setLongUrl(longUrl);
        dto.setShortUrl(shortUrl);
        Long pvNum = redisUtils.listSize(shortUrl);
        dto.setPvNum(pvNum);
        List<Object> ipList = redisUtils.listRange(shortUrl, 0L, -1L);
        HashSet<Object> set = new HashSet<>(ipList);
        ipList.clear();
        ipList.addAll(set);
        dto.setIpNum((long) ipList.size());
        Map<Object, Object> map = redisUtils.hashEntries("uv");
        dto.setUvNum((long)map.size());
        return dto;
    }
}
