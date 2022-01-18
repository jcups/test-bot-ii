package ru.jcups.testbotii.model;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.api.telegram.TelegramClient;
import ru.jcups.testbotii.config.BotConfig;
import ru.jcups.testbotii.handlers.CallbackHandler;
import ru.jcups.testbotii.handlers.MessageHandler;
import ru.jcups.testbotii.handlers.command.CurrenciesCommand;
import ru.jcups.testbotii.handlers.command.GifCommand;
import ru.jcups.testbotii.handlers.command.HelpCommand;
import ru.jcups.testbotii.handlers.command.PhotoCommand;
import ru.jcups.testbotii.service.GiphyService;
import ru.jcups.testbotii.service.OpenExchangeRatesService;
import ru.jcups.testbotii.service.UnsplashService;

@Component
public class TelegramBot extends TelegramWebhookBot {

    private final BotConfig botConfig;
    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;
    private final CommandRegistry commandRegistry;

    public TelegramBot(BotConfig botConfig, MessageHandler messageHandler,
                       GiphyService giphyService, UnsplashService unsplashService,
                       OpenExchangeRatesService ratesService, CallbackHandler callbackHandler,
                       TelegramClient telegramClient) {
        this.botConfig = botConfig;
        this.messageHandler = messageHandler;
        this.callbackHandler = callbackHandler;

        if (telegramClient.setWebhook(getBotToken(), getBotPath()).getStatusCode() == HttpStatus.OK)
            System.out.println("successfully set webhookPath");

        this.commandRegistry = new CommandRegistry(true, this::getBotUsername);

        commandRegistry.register(new GifCommand(giphyService));
        commandRegistry.register(new PhotoCommand(unsplashService));
        commandRegistry.register(new CurrenciesCommand(ratesService));
        HelpCommand helpCommand = new HelpCommand();
        commandRegistry.register(helpCommand);

        commandRegistry.registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId().toString());
            commandUnknownMessage.setText("The command '" + message.getText() +
                    "' is not known by this bot. Here comes some help ");
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            callbackHandler.handle(update.getCallbackQuery(), this);
            return null;
        } else if (update.hasMessage()) {
            System.out.println("onWebhookUpdateReceived() with message: " + update.getMessage().getText());
            Message message = update.getMessage();
            if (message.isCommand()) {
                if (!this.commandRegistry.executeCommand(this, message)) {
                    this.processInvalidCommandUpdate(update);
                }
                return null;
            }
        }
        this.processNonCommandUpdate(update);
        return null;
    }

    protected void processInvalidCommandUpdate(Update update) {
        this.processNonCommandUpdate(update);
    }

    private void processNonCommandUpdate(Update update) {
        messageHandler.handle(update.getMessage(), this);
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
