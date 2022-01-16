package ru.jcups.testbotii.model;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.config.BotConfig;
import ru.jcups.testbotii.handlers.MessageHandler;

@Component
public class TelegramBot extends TelegramWebhookBot {

    private final BotConfig botConfig;
    private final MessageHandler messageHandler;

    public TelegramBot(BotConfig botConfig, MessageHandler messageHandler) {
        this.botConfig = botConfig;
        this.messageHandler = messageHandler;
//        try {
//            this.setWebhook(new SetWebhook(getBotToken()+getBotToken()));
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println("onWebhookUpdateReceived() with message: "+update.getMessage().getText());
        messageHandler.handle(update.getMessage(), this);
        try {
            execute(new SendMessage(update.getMessage().getChatId().toString(), update.getMessage().getText()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public String getBotPath() {
        return botConfig.getBotWebhookPath();
    }
}
