package ru.jcups.testbotii.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.yaml")
public class BotConfig {

    @Value("${telegram_bot.username}")
    String botUsername;

    @Value("${telegram_bot.token}")
    String botToken;

    @Value("${telegram_bot.webhook_path}")
    String botWebhookPath;
}
