package com.sahikran.rss;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sahikran.exception.RssReaderException;
import com.sahikran.model.Item;
import com.sahikran.model.RSSItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RssItemIterator implements Iterator<RSSItem>, Closeable {

    private static final Logger log = LoggerFactory.getLogger(RssItemIterator.class);

    private static final List<String> supportedRssElementTypes = Arrays.asList("title", "link", "description", "guid", "pubDate", "dc:creator", "channel", "item", "dc:source", "creator");

    private final XMLStreamReader xmlStreamReader;
    private final InputStream inputStream;
    private final StringBuilder textBuilder;
    private RSSItem nextItem;
    private String elementName = null;
    private Map<String, Object> rssItemDataMap;
    private boolean isItemRecord = false;

    private RssItemIterator(XMLStreamReader xmlStreamReader, InputStream inputStream, StringBuilder textBuilder){
        this.xmlStreamReader = xmlStreamReader;
        this.inputStream = inputStream;
        this.textBuilder = textBuilder;
        nextItem = null;
    }

    @Override
    public boolean hasNext() {
        if(nextItem == null){
            nextItem = next();
        }
        return nextItem != null;
    }

    @Override
    public RSSItem next() {
        if(nextItem != null){
            var next = nextItem;
            nextItem = null;
            return next;
        }
        // if nextItem is null then 
        
        try {
            while(xmlStreamReader.hasNext()){
                var type = xmlStreamReader.next();
                if(type == XMLStreamConstants.CHARACTERS || type == XMLStreamConstants.CDATA){
                    parseCharacters();
                } else if (type == XMLStreamConstants.START_ELEMENT) {
                    parseStart();
                } else if (type == XMLStreamConstants.END_ELEMENT) {
                    boolean isItemParsed = parseEnd();
                    // break the loop if last tag </rss> has been read
                    if(elementName.equalsIgnoreCase("rss")){
                        break;
                    }
                    if(isItemParsed){
                        isItemRecord = false;
                        return new RSSItem.Builder()
                            .setRssDataItem(
                                RssDataObjectFactory.fromPropertyMap(Item.class, rssItemDataMap)
                            )
                            .build();
                    }
                }
            }
        } catch (XMLStreamException e) {
            log.error("has next on xml readers has thrown an exception", e);
        }
        
        try {
            xmlStreamReader.close();
            inputStream.close();
        } catch (XMLStreamException | IOException e) {
            log.error("Unable to close either xmlStreamReader or inputStream", e);
        }
        return null;
    }

    private void parseCharacters(){
        if(!xmlStreamReader.hasText()){
            return;
        }
        var text = xmlStreamReader.getText();
        if(text.trim().isEmpty()){
            return;
        }
        textBuilder.append(text);
    }

    private void parseStart(){
        elementName = xmlStreamReader.getLocalName();
        // only parse the inclusion list of nodes
        if(!supportedRssElementTypes.contains(elementName)){
            return;
        }
        textBuilder.setLength(0);
        if(elementName.equalsIgnoreCase("item")){
            isItemRecord = true;
            rssItemDataMap = new HashMap<>();
        }
    }

    private boolean parseEnd(){
        elementName = xmlStreamReader.getLocalName();
        // only parse the inclusion list of nodes
        if(!supportedRssElementTypes.contains(elementName)){
            return false;
        }
        String text = textBuilder.toString().trim();
        if(isItemRecord){
            elementName = stripInvalidPrefixes(elementName);
            insertInToItemDataMap(text);
        }
        textBuilder.setLength(0);
        return elementName.equals("item");
    }

    /**
     * To handle cases where multiple values to be added for the same key such as 'creators'
     * There can be multiple creators for an item. so we need to initialize an array and
     * add multiple creators
     * @param text
     */
    private void insertInToItemDataMap(String text){
        List<String> tempList = null;
        if(elementName.equals("creator")){
            Object value = rssItemDataMap.get(elementName);
            if(value instanceof ArrayList){
                tempList = ((ArrayList<String>)value);
            } else {
                tempList = new ArrayList<>();
                tempList.add((String)value);
            }
            tempList.add(text);
            rssItemDataMap.put(elementName, tempList);
        } else {
            rssItemDataMap.put(elementName, text);
        }

        rssItemDataMap.put(elementName, text);
    }

    /**
     * Strip any prefixes such as dc: to dc:title or dc:creator
     * @param elementName
     * @return stripped elementName
     */
    private String stripInvalidPrefixes(String elementName){
        if(elementName.startsWith("dc:")){
            return elementName.substring(3);
        }
        return elementName;
    }

    @Override
    public void close() throws IOException {
        if(xmlStreamReader != null){
            try {
                xmlStreamReader.close();
            } catch (XMLStreamException e) {
                log.error("unable to close xmlStreamReader object ", e);
            }
        }
        if(inputStream != null){
            inputStream.close();
        }
    }

    public static class Builder {

        public Builder(){
        }

        public RssItemIterator build(InputStream inputStream) throws RssReaderException{
            // creates an instance of XmlReader from input stream instance
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            // disable XML external entity (XXE) processing
            xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            try {
                return new RssItemIterator(xmlInputFactory.createXMLStreamReader(inputStream), 
                            inputStream, new StringBuilder());
            } catch (XMLStreamException e) {
                throw new RssReaderException("unable to instantiate RssItemIterator because of invalid inputStream provided", e);
            }
        }
    }
    
}
