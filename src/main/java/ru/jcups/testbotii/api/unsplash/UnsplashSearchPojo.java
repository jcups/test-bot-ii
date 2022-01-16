package ru.jcups.testbotii.api.unsplash;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnsplashSearchPojo {
    List<UnsplashImagePojo> results;
}
