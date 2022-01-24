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

    private final float[] previousRates = new float[]{0f, 0f, 0f, 0f};

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
            sender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(messageText)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getLatest() {
        OpenExchangeRatesPojo pojo = client.getLatest(app_id);
        float BTC = pojo.getRates().get("BTC");
        float BYN = pojo.getRates().get("BYN");
        float EUR = pojo.getRates().get("EUR");
        float RUB = pojo.getRates().get("RUB");

        float BTC_USD = 1.0f / BTC;
        float BYN_EUR = BYN / EUR;
        float BYN_RUB = BYN / RUB * 100;

        String result;
        if (previousRates[0] == 0f) {
            result = String.format("1 BTC == %.3f USD \n", (1.0f / BTC)) +
                    String.format("1 USD == %.3f BYN \n", BYN) +
                    String.format("1 EUR == %.3f BYN \n", (BYN / EUR)) +
                    String.format("100 RUB == %.3f BYN", (BYN / RUB * 100));
        } else {
            char upArrow = 8593;
            char downArrow = 8595;
            result = String.format("1 BTC == %.3f USD %c \n", BTC_USD,
                    (BTC_USD == previousRates[0] ? ' ' :
                            BTC_USD > previousRates[0] ? upArrow : downArrow)) +
                    String.format("1 USD == %.3f BYN %c \n", BYN,
                            (BYN == previousRates[1] ? ' ' :
                                    BYN > previousRates[1] ? upArrow : downArrow)) +
                    String.format("1 EUR == %.3f BYN %c \n", BYN_EUR,
                            (BYN_EUR == previousRates[2] ? ' ' :
                                    BYN_EUR > previousRates[2] ? upArrow : downArrow)) +
                    String.format("100 RUB == %.3f BYN %c ", BYN_RUB,
                            (BYN_RUB == previousRates[3] ? ' ' :
                                    BYN_RUB > previousRates[3] ? upArrow : downArrow));
        }
        previousRates[0] = BTC_USD;
        previousRates[1] = BYN;
        previousRates[2] = BYN_EUR;
        previousRates[3] = BYN_RUB;
        return result;
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
        float currency = valueFrom / valueTo;
        return String.format("%.3f %s == 1 %s", currency, from, to);
    }
}
