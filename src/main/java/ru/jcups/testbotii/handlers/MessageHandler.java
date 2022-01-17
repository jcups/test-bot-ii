package ru.jcups.testbotii.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.utils.CF;
import ru.jcups.testbotii.utils.Messages;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class MessageHandler {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");

    public void handle(Message message, AbsSender sender) {
        String msg = message.getText().trim();

        if (msg.matches("\\d{3}\\d{3}") || msg.matches("\\d{3}.\\d{3}")) {
            departments(message, sender, msg);
        } else if (msg.matches("\\d{2,4} \\d{2} \\d{2}")) {
            calendars(message, sender, msg);
        }
    }

    private void calendars(Message message, AbsSender sender, String msg) {
        try {
            sender.execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Выберите календарь:")
                    .replyMarkup(getCalendarMarkup(msg.replaceAll(" ", "-")))
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboard getCalendarMarkup(String msg) {
        return new InlineKeyboardMarkup(List.of(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Japan 1").callbackData("date japan 1 " + msg).build(),
                        InlineKeyboardButton.builder()
                                .text("Japan 2").callbackData("date japan 2 " + msg).build(),
                        InlineKeyboardButton.builder()
                                .text("Japan 3").callbackData("date japan 3 " + msg).build()),
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Taiwan 1").callbackData("date taiwan 1 " + msg).build(),
                        InlineKeyboardButton.builder()
                                .text("Taiwan 2").callbackData("date taiwan 2 " + msg).build())
        ));
    }

    private void departments(Message message, AbsSender sender, String msg) {
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
