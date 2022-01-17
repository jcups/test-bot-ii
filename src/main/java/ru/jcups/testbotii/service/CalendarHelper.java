package ru.jcups.testbotii.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class CalendarHelper {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public String getJapanDate(String type, String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        switch (type){
            case "1":
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1911);
                break;
            case "2":
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1985);
                break;
            case "3":
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+2000);
                break;
        }
        return sdf.format(calendar.getTime());
    }

    public String getTaiwanDate(String type, String date) {
        return date;
    }

    public String getNepalDate(String type, String date) {
        return date;
    }
}
