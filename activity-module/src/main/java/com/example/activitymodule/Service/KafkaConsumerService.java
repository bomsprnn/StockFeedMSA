package com.example.activitymodule.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
        @KafkaListener(topics = "newsfeed", groupId = "newsfeed-group")
        public void listenGroupFoo(String message) throws Exception {

            System.out.println("Received Message in group newsfeed " + message);
        }
}
