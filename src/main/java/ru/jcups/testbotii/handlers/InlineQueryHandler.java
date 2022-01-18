package ru.jcups.testbotii.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultGif;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.api.giphy.Image;
import ru.jcups.testbotii.service.GiphyService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InlineQueryHandler {
    private final GiphyService giphyService;

    public InlineQueryHandler(GiphyService giphyService) {
        this.giphyService = giphyService;
    }

    public void handle(InlineQuery inlineQuery, AbsSender sender) {
        String query = inlineQuery.getQuery();
        try {
            List<Image> urls = giphyService.getUrlsByTag(query);
            sender.execute(
                    AnswerInlineQuery.builder()
                            .inlineQueryId(inlineQuery.getId())
                            .results(urls.stream().map(image ->
                                            InlineQueryResultGif.builder()
                                                    .thumbUrl(image.getStillUrl())
                                                    .id(image.getId())
                                                    .gifUrl(image.getOriginalUrl())
                                                    .title(image.getTitle())
                                                    .build())
                                    .collect(Collectors.toList()))
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
