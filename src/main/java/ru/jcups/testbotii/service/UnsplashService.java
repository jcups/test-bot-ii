package ru.jcups.testbotii.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.api.unsplash.UnsplashClient;
import ru.jcups.testbotii.api.unsplash.UnsplashImagePojo;
import ru.jcups.testbotii.api.unsplash.UnsplashSearchPojo;
import ru.jcups.testbotii.utils.FUtils;
import ru.jcups.testbotii.utils.Messages;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnsplashService {

    private final UnsplashClient client;

    @Value("${unsplash.access_key}")
    private String accessKey;

    public UnsplashService(UnsplashClient client) {
        this.client = client;
    }

    public void getRandom(String chatId, AbsSender sender) {
        UnsplashImagePojo pojo = client.getRandom(accessKey);
        File file = FUtils.saveFile(pojo.getUrls().get("full"), ".jpg");
        try {
            if (file == null) {
                sender.execute(new SendMessage(chatId, Messages.ERROR_SENDING_FILE));
            } else {
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(file));
                if (pojo.getDescription() != null)
                    sendPhoto.setCaption(pojo.getDescription());
                sender.execute(sendPhoto);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
            sendRegularPhoto(chatId, sender, FUtils.saveFile(pojo.getUrls().get("regular"), ".jpg"));
        }
    }

    private void sendRegularPhoto(String chatId, AbsSender sender, File regular) {
        try {
            sender.execute(new SendPhoto(chatId, new InputFile(regular)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void searchByTag(String chatId, String tag, AbsSender sender) {
        UnsplashSearchPojo pojo = client.searchByKeyword(accessKey, tag);
        List<InputMedia> files = new LinkedList<>();
        for (UnsplashImagePojo image : pojo.getResults().stream().limit(10).collect(Collectors.toList())) {
            File saved = FUtils.saveFile(image.getUrls().get("regular"), ".jpg");
            InputMediaPhoto photo = new InputMediaPhoto();
            assert saved != null;
            photo.setMedia(saved, saved.getName());
            photo.setCaption(image.getDescription());
            files.add(photo);
        }
        try {
            if (files.isEmpty())
                sender.execute(new SendMessage(chatId, Messages.ERROR_SENDING_FILE));
            else
                sender.execute(new SendMediaGroup(chatId, files));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
