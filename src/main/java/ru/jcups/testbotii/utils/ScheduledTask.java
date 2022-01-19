package ru.jcups.testbotii.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jcups.testbotii.model.TelegramBot;
import ru.jcups.testbotii.service.OpenExchangeRatesService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ScheduledTask {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM y HH:mm:ss");

    private final OpenExchangeRatesService ratesService;
    private final TelegramBot bot;

    public ScheduledTask(OpenExchangeRatesService ratesService, TelegramBot bot) {
        this.ratesService = ratesService;
        this.bot = bot;
    }

    @Scheduled(fixedRate = 1800000)
    public void reportCurrentTime() {
        ratesService.sendCurrencies("311199801", new String[]{}, bot);
        System.out.println("Send rates to Master: "+sdf.format(new Date()));
    }

    @Scheduled(cron = "0 15 18 * * *")
    public void reportStatistic() {
        try {
            bot.execute(new SendMessage("311199801", StatisticUtils.getStatistic()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
