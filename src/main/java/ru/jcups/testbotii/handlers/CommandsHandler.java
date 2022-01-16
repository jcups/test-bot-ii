package ru.jcups.testbotii.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.config.BotConfig;
import ru.jcups.testbotii.handlers.command.CurrenciesCommand;
import ru.jcups.testbotii.handlers.command.GifCommand;
import ru.jcups.testbotii.handlers.command.HelpCommand;
import ru.jcups.testbotii.handlers.command.PhotoCommand;
import ru.jcups.testbotii.service.GiphyService;
import ru.jcups.testbotii.service.OpenExchangeRatesService;
import ru.jcups.testbotii.service.UnsplashService;

@Component
public class CommandsHandler extends TelegramLongPollingCommandBot {

    private final BotConfig botConfig;
    private final MessageHandler messageHandler;

    public CommandsHandler(BotConfig botConfig,
                           GiphyService giphyService,
                           UnsplashService unsplashService,
                           OpenExchangeRatesService ratesService, MessageHandler messageHandler) {
        this.botConfig = botConfig;
        this.messageHandler = messageHandler;

        register(new GifCommand(giphyService));
        register(new PhotoCommand(unsplashService));
        register(new CurrenciesCommand(ratesService));
        HelpCommand helpCommand = new HelpCommand();
        register(helpCommand);

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId().toString());
            commandUnknownMessage.setText("The command '" + message.getText() +
                    "' is not known by this bot. Here comes some help ");
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        });
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        messageHandler.handle(update.getMessage(), this);
//        if (update.hasMessage()) {
//            Message message = update.getMessage();
//
//            if (message.hasText()) {
//                SendMessage echoMessage = new SendMessage();
//                echoMessage.setChatId(message.getChatId().toString());
//                echoMessage.setText("Hey heres your message:\n" + message.getText());
//
//                try {
//                    execute(echoMessage);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}
