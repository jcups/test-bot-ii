package ru.jcups.testbotii.handlers.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.jcups.testbotii.service.OpenExchangeRatesService;

@Component
public class CurrenciesCommand extends BotCommand {
    private final OpenExchangeRatesService service;

    public CurrenciesCommand(OpenExchangeRatesService service) {
        super("/currencies", "Получить курсы валют");
        this.service = service;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String[] args = message.getText().split(" ");
        arguments = args.length >= 2 ?
                (args.length >= 3 ?
                        new String[]{args[1], args[2]} :
                        new String[]{message.getText().split(" ")[1]})
                : arguments;
        this.execute(absSender, message.getFrom(), message.getChat(), arguments);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        service.sendCurrencies(chat.getId().toString(), arguments, absSender);
    }
}
