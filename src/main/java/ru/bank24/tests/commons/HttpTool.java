/**
 * 
 */
package ru.bank24.tests.commons;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Штука, которая позволяет отправлять http-запросы до веб-сервисов Банка24.Ру.
 * 
 * 
 * @author Alexey Romanchuk
 * @created 29 янв. 2014 г.
 * 
 */

public class HttpTool extends Utils {

    /**
     * Логгер
     */
    private static final Logger logger = LoggerFactory
            .getLogger(HttpTool.class);

    /**
     * Имя хоста, где располагается http-сервис
     */
    private final String hostName;

    /**
     * REST - клиент от Spring
     */
    private final RestTemplate client;

    /**
     * Конструктор, который инициализирует транспорт
     * 
     * @param hostName
     *            Имя хоста
     */
    public HttpTool(String hostName) {

        this.hostName = hostName;
        client = new RestTemplate();

        client.setRequestFactory(new SimpleClientHttpRequestFactory() {

            /**
             * {@inheritDoc}
             */
            @Override
            protected void prepareConnection(HttpURLConnection connection,
                    String httpMethod) throws IOException {

                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
            }
        });

        client.setErrorHandler(new DefaultResponseErrorHandler() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void handleError(ClientHttpResponse r) throws IOException {

                String s = utf8(FileCopyUtils.copyToByteArray(r.getBody()));
                logger.error("Error: {}", s);

                super.handleError(r);
            }
        });
    }

    /**
     * Генерация полного URL
     * 
     * @param path
     *            Относительный путь до URL
     * @return Полный URL
     */
    public String getUrl(String path) {

        String p = path;

        if (p.startsWith("https:")) {
            return p;
        }
        if (!p.startsWith("/")) {
            p = "/" + p;
        }
        return hostName + p;
    }

    /**
     * Выполнение POST - запроса
     * 
     * @param path
     *            URL относительно хоста
     * @param body
     *            Тело запроса
     * @param headers
     *            Заголовки
     * @param clazz
     *            Класс содержимого
     * @return Ответ сервера
     * 
     * @param <S>
     *            Тип содержимого
     */
    public <S> ResponseEntity<S> post(String path, S body,
            MultiValueMap<String, String> headers, Class<S> clazz) {

        return client.exchange(getUrl(path), HttpMethod.POST,
                new HttpEntity<S>(body, headers), clazz);
    }
}
