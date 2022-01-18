package ru.jcups.testbotii.api.giphy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Image {
    String id;
    String title;
    String originalUrl;
    String stillUrl;
    long originalSize;
    long stillSize;
    int height;
    int width;
}

