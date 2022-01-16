package ru.jcups.testbotii.comands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class StartCommand extends BotCommand {
    public StartCommand() {
        super("start", "With this command you can start the Bot");
    }
}
