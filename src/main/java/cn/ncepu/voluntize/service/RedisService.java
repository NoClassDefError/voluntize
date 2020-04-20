package cn.ncepu.voluntize.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    public Integer get(String key) {
//        System.out.println(redisTemplate);
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, int timeout) {
        redisTemplate.opsForValue().set(key, 1);
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public void incur(String key, int timeout) {
        Integer i = redisTemplate.opsForValue().get(key);
        if (i != null) {
            i++;
            redisTemplate.opsForValue().set(key, i);
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }
}
