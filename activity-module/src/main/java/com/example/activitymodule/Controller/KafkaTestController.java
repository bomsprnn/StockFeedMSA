package com.example.activitymodule.Controller;

import com.example.activitymodule.Service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity/kafka")
public class KafkaTestController {

    private final KafkaProducerService producerService;


    @PostMapping("/publish")
    public String sendMessageToKafkaTopic(@RequestParam("message") String message) {

        producerService.sendMessage(message);
        return "okok";
    }
}