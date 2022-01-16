package ru.jcups.testbotii.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.api.giphy.GiphyClient;
import ru.jcups.testbotii.api.giphy.GiphyRandomPojo;
import ru.jcups.testbotii.utils.FUtils;
import ru.jcups.testbotii.utils.Messages;

import java.io.File;
import java.util.Map;

@Service
public class GiphyService {

    private final GiphyClient client;

    @Value("${giphy.key}")
    private String apiKey;

    public GiphyService(GiphyClient client) {
        this.client = client;
    }

    public void sendRandom(String chatId, AbsSender sender) {
        GiphyRandomPojo pojo = client.getRandom(apiKey);
        sendGif(chatId, sender, pojo);
    }

    public void sendRandomByTag(String chatId, String tag, AbsSender sender) {
        GiphyRandomPojo pojo = client.searchByTag(apiKey, tag);
        sendGif(chatId, sender, pojo);
    }

    private void sendGif(String chatId, AbsSender sender, GiphyRandomPojo pojo) {
        Map<String, Map<String, String>> images = pojo.getData().getImages();
        File file;
        if (images.containsKey("hd") && images.get("hd").containsKey("url"))
            file = FUtils.saveFile(images.get("hd").get("url")
                    .replaceAll("media.\\.giphy", "i.giphy"), ".gif");
        else
            file = FUtils.saveFile(images.get("original").get("url")
                    .replaceAll("media.\\.giphy", "i.giphy"), ".gif");
        try {
            if (file == null)
                sender.execute(new SendMessage(chatId, Messages.ERROR_SENDING_FILE));
            else
                sender.execute(new SendAnimation(chatId, new InputFile(file)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
