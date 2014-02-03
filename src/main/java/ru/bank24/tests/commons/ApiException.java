/**
 * 
 */
package ru.bank24.tests.commons;

import org.springframework.core.NestedRuntimeException;

/**
 * Внутреннее nested - исключение.
 * 
 * 
 * @author Alexey Romanchuk
 * @created 02 февр. 2014 г.
 * 
 */

public class ApiException extends NestedRuntimeException {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = -1217407108610092351L;

    /**
     * Конструктор по умолчанию
     * 
     * @param msg
     *            Текст сообщения об ошибке
     * @param cause
     *            Исходное исключение
     */
    public ApiException(String msg, Throwable cause) {

        super(msg, cause);
    }
}
