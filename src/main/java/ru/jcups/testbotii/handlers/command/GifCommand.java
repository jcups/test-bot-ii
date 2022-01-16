package ru.jcups.testbotii.handlers.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.jcups.testbotii.service.GiphyService;

@Component
public class GifCommand extends BotCommand {

    private final GiphyService service;

    public GifCommand(GiphyService service) {
        super("/gif", "Получить случайную гифку");
        this.service = service;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        arguments = message.getText().split(" ").length > 1 ?
                new String[]{message.getText().split(" ")[1]} : arguments;
        this.execute(absSender, message.getFrom(), message.getChat(), arguments);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (arguments.length == 0) {
            service.sendRandom(String.valueOf(chat.getId()), absSender);
        } else {
            service.sendRandomByTag(String.valueOf(chat.getId()), arguments[0], absSender);
        }
    }
}
