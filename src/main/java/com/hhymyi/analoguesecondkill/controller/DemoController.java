package com.hhymyi.analoguesecondkill.controller;

import com.hhymyi.analoguesecondkill.service.PersonService;
import com.hhymyi.analoguesecondkill.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {


    @Autowired
    private RedisService redisService ;
    @Autowired
    private PersonService personService;



    @RequestMapping(value = "/set",method = RequestMethod.GET)
    public String set(String key,String value){
        return redisService.set(key,value)?"success":"faild";
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String get(String key){
        return redisService.get(key).toString();
    }

    @RequestMapping("/setnx")
    public String setnx(String key,String value){
        return redisService.setnx(key,value);
    }

    @RequestMapping("/getPersonById")
    public Object  getPersonById(Long id){
        return personService.getPersonById(id);
    }

    @RequestMapping("/secontKill")
    public String secontKill(Long id){
        return personService.secontKill(id);
    }

}