package ru.jcups.testbotii.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.handlers.command.GifCommand;
import ru.jcups.testbotii.service.CalendarHelper;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@Component
public class CallbackHandler {

    private final GifCommand gifCommand;
    private final CalendarHelper calendarHelper;

    public CallbackHandler(GifCommand gifCommand, CalendarHelper calendarHelper) {
        this.gifCommand = gifCommand;
        this.calendarHelper = calendarHelper;
    }

    public void handle(CallbackQuery query, AbsSender sender) {
        String data = query.getData();
        List<String> list = new java.util.ArrayList<>(List.of(data.split(" ")));
        list.remove(0);
        String[] args = list.toArray(new String[0]);
        System.out.println(Arrays.toString(args));

        if (data.startsWith("gif")) {
            gifCommand.execute(sender, query.getFrom(), query.getMessage().getChat(), args);
        } else if (data.startsWith("date")) {
            calendarHelper(args[0], args[1], args[2], sender, query.getMessage());
        }
    }

    private void calendarHelper(String country, String type, String inputDate, AbsSender sender, Message message) {
        String date = "null";
        try {
            switch (country) {
                case "japan":
                    date = calendarHelper.getJapanDate(type, inputDate);
                    break;
                case "taiwan":
                    date = calendarHelper.getTaiwanDate(type, inputDate);
                    break;
                case "nepal":
                    date = calendarHelper.getNepalDate(type, inputDate);
                    break;
            }

            sender.execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(date).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            try {
                sender.execute(SendMessage.builder()
                        .text("Error parsing date").chatId(message.getChatId().toString()).build());
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }
}
