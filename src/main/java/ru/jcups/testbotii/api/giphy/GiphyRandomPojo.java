package ru.jcups.testbotii.api.giphy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GiphyRandomPojo {
    GifElement data;

    @Data
    public static class GifElement {
        String id;
        String title;
        Map<String, Map<String, String>> images;
    }
}
