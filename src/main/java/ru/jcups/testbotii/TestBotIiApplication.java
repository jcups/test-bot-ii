package ru.jcups.testbotii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.jcups.testbotii.handlers.CommandsHandler;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class TestBotIiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestBotIiApplication.class, args);

        CommandsHandler commandsHandler = context.getBean(CommandsHandler.class);
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(commandsHandler);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
