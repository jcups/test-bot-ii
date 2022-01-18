package ru.jcups.testbotii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class TestBotIiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestBotIiApplication.class, args);

    }

}
