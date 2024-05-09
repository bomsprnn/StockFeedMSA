package com.example.batchmodule.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean lock(String key) {
        int retry = 0;
        while(retry< 3) {
            if (redisTemplate.opsForValue().setIfAbsent(key, "locked", 10, TimeUnit.SECONDS)) {
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}
