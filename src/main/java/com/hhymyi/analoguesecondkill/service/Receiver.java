package com.hhymyi.analoguesecondkill.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhymyi.analoguesecondkill.entity.PersonLog;
import com.hhymyi.analoguesecondkill.repository.PersonLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class Receiver implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PersonLogRepository personLogRepository;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer<String> valueSerializer = stringRedisTemplate.getStringSerializer();
        String deserialize = valueSerializer.deserialize(message.getBody());
        logger.info("收到的mq消息" + deserialize);
        ObjectMapper mapper = new ObjectMapper();
        try {
            PersonLog personLog = mapper.readValue(deserialize, PersonLog.class);
            personLogRepository.save(personLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
