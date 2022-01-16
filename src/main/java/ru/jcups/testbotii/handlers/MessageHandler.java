package ru.jcups.testbotii.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.utils.CF;
import ru.jcups.testbotii.utils.Messages;

@Component
public class MessageHandler {

    public void handle(Message message, AbsSender sender) {
        String msg = message.getText().trim();

        System.out.println(msg);
        switch (msg.length()) {
            case 6:
                if (msg.matches("\\d{6}")) {
                    String code = msg.substring(0, 3) + "-" + msg.substring(3);
                    sendDepartments(message, sender, code);
                }
                break;
            case 7:
                if (msg.matches("\\d{3}\\D\\d{3}")) {
                    String code = msg.substring(0, 3) + "-" + msg.substring(4);
                    sendDepartments(message, sender, code);
                }
                break;
            default:
                try {
                    sender.execute(new SendMessage(message.getChatId().toString(), Messages.INCORRECT_INPUT));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void sendDepartments(Message message, AbsSender sender, String code) {
        System.out.println(code);
        String[] departments = CF.get(code);
        try {
            if (departments != null) {
                for (String department : departments)
                    sender.execute(new SendMessage(message.getChatId().toString(), department));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
