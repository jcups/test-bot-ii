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
import ru.jcups.testbotii.handlers.InlineQueryHandler;
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
    private final InlineQueryHandler inlineQueryHandler;
    private final CommandRegistry commandRegistry;

    public TelegramBot(BotConfig botConfig, MessageHandler messageHandler,
                       GiphyService giphyService, UnsplashService unsplashService,
                       OpenExchangeRatesService ratesService, CallbackHandler callbackHandler,
                       TelegramClient telegramClient, InlineQueryHandler inlineQueryHandler) {
        this.botConfig = botConfig;
        this.messageHandler = messageHandler;
        this.callbackHandler = callbackHandler;
        this.inlineQueryHandler = inlineQueryHandler;

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
        try {
            if (update.hasCallbackQuery()) {
                System.out.println("onWebhookUpdateReceived() with callback: " + update.getCallbackQuery().getData() +
                        "\nfrom: " + update.getCallbackQuery().getFrom().getUserName());
                callbackHandler.handle(update.getCallbackQuery(), this);
                return null;
            } else if (update.hasMessage()) {
                System.out.println("onWebhookUpdateReceived() with message: " + update.getMessage().getText() +
                        "\nfrom: " + update.getMessage().getChat().getUserName());
                Message message = update.getMessage();
                if (message.isCommand()) {
                    if (!this.commandRegistry.executeCommand(this, message)) {
                        this.processInvalidCommandUpdate(update);
                    }
                    return null;
                }
            } else if (update.hasInlineQuery()) {
                System.out.println("onWebhookUpdateReceived() with inlineQuery: " + update.getInlineQuery().getQuery() +
                        "\nfrom: " + update.getInlineQuery().getFrom().getUserName());
                inlineQueryHandler.handle(update.getInlineQuery(), this);
                return null;
            }
            this.processNonCommandUpdate(update);
            return null;
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
            return null;
        }
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
