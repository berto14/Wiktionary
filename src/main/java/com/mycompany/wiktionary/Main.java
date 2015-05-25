package com.mycompany.wiktionary;

import java.io.IOException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main {

    private static final String xmlFilePath = "enwiktionary-20141004-pages-articles.xml";
    
    public static void main(String argv[]) throws IOException {

        try {
            //Create a object of the SAXParserFactory class to handle the huge XML file
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            saxParser.parse(xmlFilePath, new Handler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
