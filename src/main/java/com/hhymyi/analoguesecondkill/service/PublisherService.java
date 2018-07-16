package com.hhymyi.analoguesecondkill.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String sendMessage(String name) {
        try {
            stringRedisTemplate.convertAndSend("TOPIC_USERNAME", name);
            return "消息发送成功了";

        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败了";
        }

    }
}
