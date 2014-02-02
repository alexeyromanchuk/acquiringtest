package ru.bank24.tests.commons;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Утилиты
 * 
 * 
 * @author Alexey Romanchuk
 * @created 31 янв. 2014 г.
 * 
 */
public class Utils {

    /**
     * Конструктор
     */
    public Utils() {

        /*
         * Empty
         */
    }

    /**
     * Преобразоварние байт в строку в кодировке utf-8 с подавлением исключения
     * 
     * @param bytes
     *            Байты строки
     * @return Строка в кодировке utf-8
     */
    public String utf8(byte[] bytes) {

        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Преобразоварние строки в байты кодировки utf-8
     * 
     * @param s
     *            Строка
     * @return Байты строки в кодировке utf-8
     */
    public byte[] utf8(String s) {

        try {
            return s.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Возвращает текущую дату
     * 
     * @return Дата
     */
    public Date now() {

        return new DateTime().toDate();
    }

    /**
     * Преобразование даты в ISO формат для dateTime в xml - схеме
     * 
     * @param d
     *            Исходная дата
     * @return Строковое представление даты
     */
    public String formatToXML(Date d) {

        DateTimeFormatter f = ISODateTimeFormat.dateTime();
        return f.print(d.getTime());
    }
}
