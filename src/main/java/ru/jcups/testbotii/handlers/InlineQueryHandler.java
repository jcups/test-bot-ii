package ru.jcups.testbotii.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Random;

@Component
public class InlineQueryHandler {

    public void handle(InlineQuery inlineQuery, AbsSender sender) {
        String query = inlineQuery.getQuery();
        try {
            sendAnswer(inlineQuery, sender, query);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendAnswer(InlineQuery inlineQuery, AbsSender sender, String query) throws TelegramApiException {
        sender.execute(
                AnswerInlineQuery.builder()
                        .inlineQueryId(inlineQuery.getId())
                        .cacheTime(1)
                        .result(getArticle(inlineQuery, query))
                        .build()
        );
    }

    private InlineQueryResultArticle getArticle(InlineQuery inlineQuery, String query) {
        return InlineQueryResultArticle.builder()
                .id(inlineQuery.getId())
                .title("Какой " + (query == null || query.trim().isEmpty() ?
                        "ты " : query.trim() + " ") + "Путин?")
                .description("Отправить твою Путиность в этот чат")
                .inputMessageContent(InputTextMessageContent.builder()
                        .messageText(String.format("%s на %d%% Путин!", query == null || query.trim().isEmpty() ?
                                "@" + inlineQuery.getFrom().getUserName() : query.trim(), new Random().nextInt(100)))
                        .parseMode("html")
                        .disableWebPagePreview(true)
                        .build())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Проверить свою Путиность!")
                                .switchInlineQuery("")
                                .build()))
                        .build())
                .thumbUrl("https://ih1.redbubble.net/image.816660411.2958/flat,750x,075,f-pad,750x1000,f8f8f8.u2.jpg")
                .build();
    }

}
