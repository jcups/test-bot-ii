package ru.jcups.testbotii.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.api.giphy.GiphyClient;
import ru.jcups.testbotii.api.giphy.GiphyRandomPojo;
import ru.jcups.testbotii.api.giphy.GiphySearchPojo;
import ru.jcups.testbotii.api.giphy.Image;
import ru.jcups.testbotii.utils.FUtils;
import ru.jcups.testbotii.utils.Messages;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GiphyService {

    private final GiphyClient client;

    @Value("${giphy.key}")
    private String apiKey;

    public GiphyService(GiphyClient client) {
        this.client = client;
    }

    public List<Image> getUrlsByTag(String tag){
        GiphySearchPojo pojo = client.searchByTag(apiKey, tag);
        return pojo.getData().stream()
                .map(image -> {
                    Map<String, String> original = image.getImages().get("original");
                    return Image.builder()
                            .id(image.getId())
                            .title(image.getTitle())
                            .originalUrl(original.get("url"))
                            .originalSize(Long.parseLong(original.get("size")))
                            .height(Integer.parseInt(original.get("height")))
                            .width(Integer.parseInt(original.get("width")))
                            .stillUrl(image.getImages().get("original_still").get("url"))
                            .stillSize(Long.parseLong(image.getImages().get("original_still").get("size")))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void sendRandom(String chatId, AbsSender sender) {
        GiphyRandomPojo pojo = client.getRandom(apiKey);
        sendGif(chatId, sender, pojo, null);
    }

    public void sendRandomByTag(String chatId, String tag, AbsSender sender) {
        GiphyRandomPojo pojo = client.getRandomByTag(apiKey, tag);
        sendGif(chatId, sender, pojo, tag);
    }

    private void sendGif(String chatId, AbsSender sender, GiphyRandomPojo pojo, String tag) {
        File file = getFileFromPojo(pojo);
        try {
            if (file == null) {
                SendMessage message = new SendMessage(chatId, Messages.ERROR_SENDING_FILE);
                message.setReplyMarkup(tag == null ? getKeyboard() : getKeyboardWithTag(tag));
                sender.execute(message);
            } else {
                SendAnimation animation = new SendAnimation(chatId, new InputFile(file));
                animation.setReplyMarkup(tag == null ? getKeyboard() : getKeyboardWithTag(tag));
                sender.execute(animation);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private File getFileFromPojo(GiphyRandomPojo pojo) {
        Map<String, Map<String, String>> images = pojo.getData().getImages();
        if (images.containsKey("hd") && images.get("hd").containsKey("url"))
            return FUtils.saveFile(images.get("hd").get("url")
                    .replaceAll("media.\\.giphy", "i.giphy"), ".gif");
        else
            return FUtils.saveFile(images.get("original").get("url")
                    .replaceAll("media.\\.giphy", "i.giphy"), ".gif");
    }

    private ReplyKeyboard getKeyboard() {
        return new InlineKeyboardMarkup(List.of(List.of(
                InlineKeyboardButton.builder().text("??????").callbackData("gif").build())));
//        return new ReplyKeyboardMarkup(List.of(new KeyboardRow(List.of(new KeyboardButton("/gif")))));
    }

    private ReplyKeyboard getKeyboardWithTag(String tag) {
        return new InlineKeyboardMarkup(List.of(List.of(
                InlineKeyboardButton.builder().text("?????? '" + tag + '\'').callbackData("gif " + tag).build())));
//        return new ReplyKeyboardMarkup(List.of(new KeyboardRow(List.of(new KeyboardButton("/gif "+tag)))));
    }
}
