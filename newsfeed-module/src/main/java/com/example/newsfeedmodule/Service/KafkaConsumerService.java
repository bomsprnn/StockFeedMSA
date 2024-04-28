package com.example.newsfeedmodule.Service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    //@KafkaListener(topics = "newsfeed", groupId = "newsfeed")
    public void listenGroupFoo(String message) {

        System.out.println("Received Message in group newsfeed " + message);
    }
}
