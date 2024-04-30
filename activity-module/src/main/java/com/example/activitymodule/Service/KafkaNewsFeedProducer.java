package com.example.activitymodule.Service;

import com.example.activitymodule.Dto.NewsFeedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

@Slf4j
@RequiredArgsConstructor
public class KafkaNewsFeedProducer {
    private static final String TOPIC = "create-newsfeed";
    private static final String TOPIC2 = "delete-newsfeed";
    private final KafkaTemplate<String, NewsFeedDto> kafkaTemplate;

    public void sendCreateNewsFeedMessage(NewsFeedDto newsFeedDto) {
        System.out.println(String.format("Produce message") );
        kafkaTemplate.send(TOPIC, newsFeedDto);
    }

    public void sendDeleteNewsFeedMessage(NewsFeedDto newsFeedDto) {
        System.out.println(String.format("Produce message") );
        kafkaTemplate.send(TOPIC2, newsFeedDto);
    }

}