/**
 * 
 */
package ru.bank24.tests.commons;

import java.util.Map;

/**
 * 
 * Description ...
 * 
 * 
 * @author Alexey Romanchuk
 * @created 02 февр. 2014 г.
 * 
 */

public class ApiResult {

    /**
     * Код статуса (processed, failed, terminated, accepted)
     */
    private final String state;

    /**
     * Описание ошибки (для failed)
     */
    private final String stateInfo;

    /**
     * Данные (из раздела data транзакции)
     */
    private final Map<String, String> data;

    /**
     * Конструктор
     * 
     * @param state
     *            Состояние
     * @param stateInfo
     *            Информация о состоянии
     * @param data
     *            Ответные данные
     */
    ApiResult(String state, String stateInfo, Map<String, String> data) {

        this.state = state;
        this.stateInfo = stateInfo;
        this.data = data;
    }

    /**
     * @return the state
     */
    public String getState() {

        return state;
    }

    /**
     * @return the stateInfo
     */
    public String getStateInfo() {

        return stateInfo;
    }

    /**
     * @return the data
     */
    public Map<String, String> getData() {

        return data;
    }
}
