package com.hhymyi.analoguesecondkill;

import com.hhymyi.analoguesecondkill.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

@Component
public class Init implements CommandLineRunner {
    Logger logger=LoggerFactory.getLogger(Init.class);

    @Autowired
    private RedisService redisService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("-------------------------------------初始化redis库存--------------------------");
        redisService.set("stock","50");
    }

    @Bean(name="stock")
    public ArrayBlockingQueue getStock() throws InterruptedException {
        int size=50;
        ArrayBlockingQueue<Integer> queue=new ArrayBlockingQueue<Integer>(size);
        for(int i=0;i<size;i++){
            queue.put(1);
        }
        return queue;
    }
}
