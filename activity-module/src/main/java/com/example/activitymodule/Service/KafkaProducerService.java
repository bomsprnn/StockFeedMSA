package com.example.activitymodule.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.network.Send;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service

@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {
    private static final String TOPIC = "newsfeed";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String msg) {
        System.out.println(String.format("Produce message : %s", msg));
        kafkaTemplate.send(TOPIC, msg);

    }

}
