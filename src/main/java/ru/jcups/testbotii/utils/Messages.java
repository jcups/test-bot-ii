package ru.jcups.testbotii.utils;

public class Messages {
    public static final String HELP = "Здравстуй товарищ, этот бот может делать следующее: \n\n" +
            "\\gif - Получить рандомную гифку; \n" +
            "\\gif [tag] - Получить гифку по тэгу; \n" +
            "\\photo - Получить рандомную фотку; \n" +
            "\\photo [tag] - Получить фотку по тэгу; \n" +
            "\\currencies - Получить курс всех валют относительно USD; \n" +
            "\\currencies [BYN] - Получить курс валюты относительно USD; \n" +
            "\\currencies [RUB] [EUR] - Получить курс валюты относильно другой; \n\n" +
            "Так же я помогу тебе быстрее найти подразделение РФ по коду, принимается в нескольких формата: \n" +
            " - 000000 - Просто 6 цифр; \n" +
            " - 010-001 - На месте '-' может быть любой другой НЕчисловой символ; \n\n" +
            "С предложениями по совершенствованию бота обращаться сюда - @jcups.";

    public static final String CURRENCY_NOT_FOUND = "Ошибка, валюта не найдена";

    public static final String INCORRECT_INPUT = "А? Чего?\nНе понял тебя..";

    public static final String ERROR_SENDING_FILE = "Произошла ошибка при отправке файла";

}
