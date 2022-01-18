package ru.jcups.testbotii.api.giphy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "giphy", url = "api.giphy.com/v1")
public interface GiphyClient {

    @GetMapping("/gifs/random?api_key={api_key}")
    GiphyRandomPojo getRandom(@PathVariable String api_key);

    @GetMapping("/gifs/random?api_key={api_key}&tag={tag}")
    GiphyRandomPojo getRandomByTag(@PathVariable String api_key, @PathVariable String tag);

    @GetMapping("/gifs/search?api_key={api_key}&q={q}&limit=50")
    GiphySearchPojo searchByTag(@PathVariable String api_key, @PathVariable String q);

}
