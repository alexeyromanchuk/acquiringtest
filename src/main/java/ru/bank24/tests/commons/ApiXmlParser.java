/**
 * 
 */
package ru.bank24.tests.commons;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Парсер, который вытаскивает текущий статус API - запроса, а также собирает
 * все элементы раздела "data".
 * 
 * 
 * @author Alexey Romanchuk
 * @created 01 февр. 2014 г.
 * 
 */

public class ApiXmlParser extends DefaultHandler {

    /**
     * Логгер
     */
    private static final Logger logger = LoggerFactory
            .getLogger(ApiXmlParser.class);

    /**
     * Имя текущего элемента
     */
    private String name;

    /**
     * Маркер того, что найден узел data
     */
    private boolean foundData = false;

    /**
     * Маркер того, что найден узел state_info
     */
    private boolean foundState = false;

    /**
     * Значение атрибута state
     */
    private String state;

    /**
     * Сообщение об ошибке (если оно есть)
     */
    private String stateInfo;

    /**
     * Перечень всех элементов, которые были найдены внутри раздела data
     */
    private final Map<String, String> map = new HashMap<String, String>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if ("data".equals(qName)) {
            foundData = false;
        }

        if ("state_info".equals(qName)) {
            foundState = false;
        }

        this.name = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attrs) throws SAXException {

        logger.trace("Element: {}", qName);

        if (!foundData && "data".equals(qName)) {
            foundData = true;
        } else if (foundData) {
            this.name = qName;
        } else {
            this.name = null;
        }

        if ("state_info".equals(qName)) {
            foundState = true;
            state = attrs.getValue("state");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (this.name != null) {
            map.put(name, new String(ch, start, length));
        }

        if (foundState) {
            stateInfo = new String(ch, start, length);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

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
     * @return the map
     */
    public Map<String, String> getMap() {

        return map;
    }

}
