package ru.jcups.testbotii.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class StatisticUtils {
    public static final Map<String, Long> statistic = new TreeMap<>();

    public static void ctrl(Update update, AbsSender sender) {

        String username = update.hasMessage() ?
                update.getMessage().getFrom().getUserName() : update.hasCallbackQuery() ?
                update.getCallbackQuery().getFrom().getUserName() : update.hasInlineQuery() ?
                update.getInlineQuery().getFrom().getUserName() : null;
        if (username == null) return;
        if (statistic.containsKey(username)) {
            statistic.put(username, statistic.get(username) + 1);
        } else {
            statistic.put(username, 1L);
            try {
                sender.execute(new SendMessage("311199801", "New user: " + username));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getStatistic() {
        return statistic.isEmpty() ? "Statistic is empty" : statistic.entrySet().stream()
                .map(user -> String.format("%s messaged %d counts;\n", user.getKey(), user.getValue()))
                .collect(Collectors.joining());
    }
}
