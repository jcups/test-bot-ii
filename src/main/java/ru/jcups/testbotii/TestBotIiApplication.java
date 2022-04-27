package ru.jcups.testbotii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.jcups.testbotii.utils.CF;

import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class TestBotIiApplication {

    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(TestBotIiApplication.class, args);

        String[] strings = CF.get("010", "001");
        System.out.println("strings = " + Arrays.toString(strings));
    }
}
