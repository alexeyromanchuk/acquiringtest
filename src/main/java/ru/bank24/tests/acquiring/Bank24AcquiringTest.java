/**
 * 
 */
package ru.bank24.tests.acquiring;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import ru.bank24.tests.commons.ApiResult;
import ru.bank24.tests.commons.ApiTool;
import ru.bank24.tests.commons.Utils;

/**
 * 
 * Description ...
 * 
 * 
 * @author Alexey Romanchuk
 * @created 31 Jan 2014 г.
 * 
 */

public class Bank24AcquiringTest extends Utils {

    /**
     * Логгер
     */
    private static final Logger logger = LoggerFactory
            .getLogger(Bank24AcquiringTest.class);

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

        api = new ApiTool();
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
    }
}
