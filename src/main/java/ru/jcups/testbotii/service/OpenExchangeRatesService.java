package ru.jcups.testbotii.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.api.rates.OpenExchangeRatesClient;
import ru.jcups.testbotii.api.rates.OpenExchangeRatesPojo;
import ru.jcups.testbotii.utils.Messages;

@Service
public class OpenExchangeRatesService {

    private final OpenExchangeRatesClient client;

    @Value("${openexchangerates.key}")
    private String app_id;

    public OpenExchangeRatesService(OpenExchangeRatesClient client) {
        this.client = client;
    }

    public void sendCurrencies(String chatId, String[] args, AbsSender sender) {
        String messageText;
        switch (args.length) {
            case 0:
                messageText = getLatest();
                break;
            case 1:
                messageText = getLatest(args[0]);
                break;
            case 2:
                messageText = getLatest(args[0], args[1]);
                break;
            default:
                messageText = "Не пон?";
                break;
        }
        try {
            sender.execute(new SendMessage(chatId, messageText));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getLatest() {
        OpenExchangeRatesPojo pojo = client.getLatest(app_id);
        return String.format("1 BTC == %.3f USD \n", (1.0f / pojo.getRates().get("BTC"))) +
                String.format("1 USD == %.3f BYN \n", pojo.getRates().get("BYN")) +
                String.format("1 EUR == %.3f BYN \n", (pojo.getRates().get("BYN") / pojo.getRates().get("EUR"))) +
                String.format("100 RUB == %.3f BYN", (pojo.getRates().get("BYN") / pojo.getRates().get("RUB") * 100));
    }

    public String getLatest(String code) {
        OpenExchangeRatesPojo pojo = client.getLatest(app_id);
        Float value = pojo.getRates().get(code);
        if (value == null) {
            return Messages.CURRENCY_NOT_FOUND;
        } else {
            return value.toString();
        }
    }

    public String getLatest(String from, String to) {
        OpenExchangeRatesPojo pojo = client.getLatest(app_id);
        Float valueFrom = pojo.getRates().get(from);
        Float valueTo = pojo.getRates().get(to);
        float currency = valueFrom/valueTo;
        return String.format("%.3f %s == 1 %s", currency, from, to);
    }
}
