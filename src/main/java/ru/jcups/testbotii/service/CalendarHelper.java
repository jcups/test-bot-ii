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

		switch (type) {
			case "reiwa":
				calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 2018); //Reiwa +2018 令和
				break;
			case "heisei":
				calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1988); //Heisei +1988 平成
				break;
			case "showa":
				calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1925); //Showa +1925 昭和
				break;
		}

		return sdf.format(calendar.getTime());
	}

	public String getTaiwanDate(String date) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(date));
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1911);
		return sdf.format(calendar.getTime());
	}

	public String getThailandDate(String date) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(date));
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 543);
		return sdf.format(calendar.getTime());
	}

	public String[] getRussianExpirationDates(String birthDate) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(birthDate));

		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 20);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 90);
		String firstIssueExpirationDate = sdf.format(calendar.getTime());

		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 25);
		String secondIssueExpirationDate = sdf.format(calendar.getTime());

		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 54);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 90);
		String lastExpirationDate = sdf.format(calendar.getTime());

		return new String[]{firstIssueExpirationDate, secondIssueExpirationDate, lastExpirationDate};
	}
}
