/**
 * 
 */
package ru.bank24.tests.commons;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;

/**
 * Сервис, формирующий запросы в банк в соответствии со спецификацией API
 * (http://wiki.bank24.ru/wiki/API-%D0%91%D0%B0%D0%BD%D0%BA ).
 * 
 * 
 * @author Alexey Romanchuk
 * @created 31 янв. 2014 г.
 * 
 */

public class ApiTool extends Utils {

    /**
     * Логгер
     */
    private static final Logger logger = LoggerFactory
            .getLogger(ApiTool.class);

    /**
     * Сервис для http-транспорта
     */
    private final HttpTool http;

    /**
     * Имя пользователя
     */
    private final String userName;

    /**
     * Пароль
     */
    private final String password;

    /**
     * Конструктор сервиса
     * 
     * @param userName
     *            Имя пользователя
     * @param password
     *            Пароль
     */
    public ApiTool(String userName, String password) {

        this.http = new HttpTool("https://ext.bank24.ru");
        this.userName = userName;
        this.password = password;
    }

    /**
     * Шаблон запроса в виде xml.
     */
    private static final String QUERY =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
                    + "<message_v1 xmlns=\"http://www.anr.ru/types\""
                    + " type=\"request\" time=\"%s\"" + " ext_id = \"%s\" >"
                    + "<data trn_code=\"%s\">%s</data>"
                    + "<terminal id=\"testTerminal\">"
                    + "<name>Yekaterinburg</name>" + "</terminal>"
                    + "</message_v1>";

    /**
     * Создание заголовка с данными Basic - авторизации
     * 
     * @return Строка с заголовком
     */
    protected String buildBasicAuth() {

        String s = utf8(Base64.encode(utf8(userName + ":" + password)));
        return "Basic " + s;
    }

    /**
     * Упаковка API - транзакции в http-запрос
     * 
     * @param trnCode
     *            Код транзакции
     * @param data
     *            Данные (содердимое тэга data в виде подстроки xml, например,
     *            "<sum>20.33<sum>" )
     * @return Ответ
     */
    public String queryRaw(String trnCode, String data) {

        String time = formatToXML(now());
        String guid = UUID.randomUUID().toString();

        String rq = String.format(QUERY, time, guid, trnCode, data);
        logger.info("Request:\n{}", rq);

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.TEXT_XML));
        headers.setContentType(MediaType.TEXT_XML);

        headers.add("Authorization", buildBasicAuth());

        ResponseEntity<String> r =
                http.post("/wsexternal/message/process", rq, headers,
                        String.class);

        logger.info("Response status: {}", r.getStatusCode());
        logger.debug("Response headers: {}", r.getHeaders());

        String s = r.getBody();
        logger.info("Response :\n{}", s);

        return s;
    }

    /**
     * Выполнение api - запроса с простым парсингом результата, чтобы удобнее
     * было работать с полученными ответами.
     * 
     * @param trnCode
     *            Код транзакции
     * @param data
     *            Данные транзакции
     * @return Результат выполнения транзакции
     */
    public ApiResult query(String trnCode, String data) {

        String xml = queryRaw(trnCode, data);

        SAXParserFactory f = SAXParserFactory.newInstance();
        InputStream stream = new ByteArrayInputStream(utf8(xml));

        /*
         * TODO: парсер не умеет пока работать с атрибутами элементов в ответах
         */
        ApiXmlParser p = new ApiXmlParser();

        try {
            f.newSAXParser().parse(stream, p);

            logger.debug("state: {} / {}, maps: {}", p.getState(),
                    p.getStateInfo(), p.getMap());
            return new ApiResult(p.getState(), p.getStateInfo(), p.getMap());
        } catch (Exception ex) {
            throw new ApiException("Api exception", ex);
        }
    }
}
