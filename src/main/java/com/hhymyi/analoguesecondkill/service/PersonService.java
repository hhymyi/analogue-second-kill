package com.hhymyi.analoguesecondkill.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhymyi.analoguesecondkill.entity.Person;
import com.hhymyi.analoguesecondkill.entity.PersonLog;
import com.hhymyi.analoguesecondkill.repository.PersonLogRepository;
import com.hhymyi.analoguesecondkill.repository.PersonRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

@Service
@EnableAutoConfiguration
public class PersonService {
    private Logger logger = LoggerFactory.getLogger(PersonService.class);


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ArrayBlockingQueue<Integer> stock;

    @Autowired
    private PersonLogRepository personLogRepository;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public Person getPersonById(Long id) {
        return personRepository.findById(id).get();
    }


    /**
     * redis
     * @param id
     * @return
     */
    public String secontKill(Long id) {
        PersonLog pl = new PersonLog();
        String stockStr = redisService.get("stock").toString();
        Integer stock = Integer.parseInt(stockStr);
        Long listSize = redisService.lPush("killList", id.toString());
        if (stock >= listSize) {
            pl.setNo(id.intValue());
            pl.setKillResult(true);
            pl.setKillInfo("success");
            personLogRepository.save(pl);
            return "success";
        } else {
            pl.setKillResult(false);
            pl.setKillInfo("sold out");
            personLogRepository.save(pl);
            return "sold out";
        }
    }

//    public String secontKill(Long id) {
//        logger.info("kill begin:" + id);
//        PersonLog pl = new PersonLog();
//        String keyNmae = "lock";
//        while (true) {
//            String setnx = redisService.setnx(keyNmae, "lock");
//            logger.info("killing" + id);
//            if ("success".equals(setnx)) {
//                try {
//                    String stockStr = redisService.get("stock").toString();
//                    Integer stock = Integer.parseInt(stockStr);
//                    if (stock > 0) {
//                        redisService.set("stock", stock - 1);
//                        pl.setNo(id.intValue());
//                        pl.setKillResult(true);
//                        pl.setKillInfo("success");
//                        ObjectMapper mapper = new ObjectMapper();
//                        String jsongStr = mapper.writeValueAsString(pl);
//                        kafkaTemplate.send("secondKill", "secondKill", jsongStr);
//                        return "success";
//                    } else {
//                        pl.setKillResult(false);
//                        pl.setKillInfo("sold out");
//                        ObjectMapper mapper = new ObjectMapper();
//                        String jsongStr = mapper.writeValueAsString(pl);
//                        kafkaTemplate.send("secondKill", "secondKill", jsongStr);
//                        return "sold out";
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    logger.info("kill release" + id);
//                    logger.info("------------------------------------------------------------------");
//                    redisService.remove(keyNmae);
//                }
//            }
//        }
//    }

    @KafkaListener(topics = "secondKill")
    public void listenKillTopic(ConsumerRecord<?, ?> cr) {
//        logger.info("{} - {} : {}", cr.topic(), cr.key(), cr.value());
        ObjectMapper mapper = new ObjectMapper();
        try {
            PersonLog personLog = mapper.readValue(cr.value().toString(), PersonLog.class);
            personLogRepository.save(personLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * redis setnx锁 发布订阅
     * @param id
     * @return
     */
//    public String secontKill(Long id) {
//        logger.info("kill begin:" + id);
//        PersonLog pl = new PersonLog();
//        String keyNmae = "lock";
//        while (true) {
//            String setnx = redisService.setnx(keyNmae, "lock");
//            logger.info("killing" + id);
//            if ("success".equals(setnx)) {
//                try {
//                    String stockStr = redisService.get("stock").toString();
//                    Integer stock = Integer.parseInt(stockStr);
//                    if (stock > 0) {
//                        redisService.set("stock", stock - 1);
//                        pl.setNo(id.intValue());
//                        pl.setKillResult(true);
//                        pl.setKillInfo("success");
//                        ObjectMapper mapper = new ObjectMapper();
//                        String jsongStr = mapper.writeValueAsString(pl);
//                        redisService.lPush("kill result",jsongStr);
////                        publisherService.sendMessage(jsongStr);
//                        return "success";
//                    } else {
//                        pl.setKillResult(false);
//                        pl.setKillInfo("sold out");
//                        ObjectMapper mapper = new ObjectMapper();
//                        String jsongStr = mapper.writeValueAsString(pl);
//                        redisService.lPush("kill result",jsongStr);
////                        publisherService.sendMessage(jsongStr);
//                        return "sold out";
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    logger.info("kill release" + id);
//                    logger.info("------------------------------------------------------------------");
//                    redisService.remove(keyNmae);
//                }
//            }
//        }
//    }


    /**
     * 阻塞队列
     * @param id
     * @return
     */
//    public String secontKill(Long id) {
//        PersonLog pl = new PersonLog();
//        if (stock.poll() != null) {
//            pl.setNo(id.intValue());
//            pl.setKillResult(true);
//            pl.setKillInfo("success");
//            personLogRepository.save(pl);
//            return "success";
//        } else {
//            pl.setKillResult(false);
//            pl.setKillInfo("sold out");
//            personLogRepository.save(pl);
//            return "sold out";
//        }
//    }


    /**
     * 数据库 行锁
     * @param id
     * @return
     */
//    @Transactional(value="transactionManager", rollbackFor = Exception.class)
//    public String secontKill(Long id){
//        Person p=personRepository.getPersionByIdForUpdate(1l);
//        PersonLog pl=new PersonLog();
//        pl.setNo(id.intValue());
//        if(p.getAge()<100){
//            p.setAge(p.getAge()+1);
//            personRepository.save(p);
//            pl.setKillResult(true);
//            pl.setKillInfo("success");
//            personLogRepository.save(pl);
//            return "success";
//        }else{
//            pl.setKillResult(false);
//            pl.setKillInfo("sold out");
//            personLogRepository.save(pl);
//            return "sold out";
//        }
//    }


    /**
     * 数据库 乐观锁
     * 200并发   18个未成功
     * @param id
     * @return
     */
//    public String secontKill(Long id){
//        Person p=personRepository.findById(1l).get();
//        PersonLog pl=new PersonLog();
//        pl.setNo(id.intValue());
//        if(p.getAge()<100){
//            p.setAge(p.getAge()+1);
//            try{
//                personRepository.save(p);
//            }catch(Exception e){
//                e.printStackTrace();
//                pl.setKillResult(false);
//                pl.setKillInfo("update by another thread");
//                personLogRepository.save(pl);
//                return "update by another thread";
//            }
//            pl.setKillResult(true);
//            pl.setKillInfo("success");
//            personLogRepository.save(pl);
//            return "success";
//        }else{
//            pl.setKillResult(false);
//            pl.setKillInfo("sold out");
//            personLogRepository.save(pl);
//            return "sold out";
//        }
//    }

    /**
     * 常规
     * 200并发   100个超卖46个
     * @param id
     * @return
     */
//    public String secontKill(Long id){
//        Person p=personRepository.findById(1l).get();
//        PersonLog pl=new PersonLog();
//        pl.setNo(id.intValue());
//        if(p.getAge()<100){
//            p.setAge(p.getAge()+1);
//            personRepository.save(p);
//            pl.setKillResult(true);
//            personLogRepository.save(pl);
//            return "success";
//        }else{
//            pl.setKillResult(false);
//            personLogRepository.save(pl);
//            return "sold out";
//        }
//    }

}
