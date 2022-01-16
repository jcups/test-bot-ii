package ru.jcups.testbotii.api.giphy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GiphySearchPojo {
    List<Image> data;

    @Data
    public static class Image {
        String id;
        String title;
        Map<String, Map<String, String>> images;
    }
}
