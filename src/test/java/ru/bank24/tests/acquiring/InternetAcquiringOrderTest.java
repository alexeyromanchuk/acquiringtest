/**
 * 
 */
package ru.bank24.tests.acquiring;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import ru.bank24.tests.commons.ApiResult;
import ru.bank24.tests.commons.ApiTool;
import ru.bank24.tests.commons.Utils;

/**
 * Пример использования API Банка24.Ру для создания нового заказа оплату для
 * интернет-эквайринга. Система регистрирует заказ, присваивая ему уникальный
 * номер (поле OrderNumber) и возвращает список полей, которые нужно вставить в
 * форму оплаты. В дальшейшем submit этой формы приведет к перехода на сайт
 * системы Яндекс.Деньги, где собственно и происходит оплата.
 * 
 * <p>
 * Среди возвращаемых данных есть поле action, которое и содержит url платежной
 * системы Яндекс.Деньги и оно должно быть вставлено в соответствующий атрибут
 * элемента form.
 * 
 * 
 * @author Alexey Romanchuk
 * @created 31 Jan 2014 г.
 * 
 */

public class InternetAcquiringOrderTest extends Utils {

    /**
     * Логгер
     */
    private static final Logger logger = LoggerFactory
            .getLogger(InternetAcquiringOrderTest.class);

    /**
     * Сервис для выполнения запросов к банку
     */
    private ApiTool api;

    /**
     * Инициализация сервисов перед выполнением тестов
     * 
     * @throws Exception
     */
    @Before
    public void setUp() {

        api = new ApiTool("demo", "demo"); // тестовые логи/пароль
    }

    /**
     * Создание тестовой заявки на оплату
     * 
     * @throws ParserConfigurationException
     *             При ошибке парсера
     * @throws IOException
     *             При ошибке ввода/вывода
     * @throws SAXException
     *             ПРи ошибке парсера
     */
    @Test
    public void test() throws SAXException, IOException,
            ParserConfigurationException {

        ApiResult r = api.query("AQ0100", "<sum>123</sum>");

        logger.debug("state: {} / {}, maps: {}", r.getState(),
                r.getStateInfo(), r.getData());
        Assert.assertEquals("processed", r.getState());

        /*
         * Ниже выводим все полученные в ответе поля, которые и нужно вставить в
         * форму (<form>). Среди них:
         * 
         * 1. Поле action из ответа пишем в атрибут action формы
         * 
         * 
         * 2. Поле OrderNumber пишем в hidden input формы, также сохраняем его у
         * себя, поскольку по нему будет полняться вся идентификация заказа.
         * 
         * 
         * 3. Остальные поля также сохраняем в hidden inpur'ах внутри формы
         * 
         * 
         * 4. Поле с суммой, конечно же, может быть простым input'ом.
         */
        logger.info("Form fields: {}", r.getData());
    }
}
