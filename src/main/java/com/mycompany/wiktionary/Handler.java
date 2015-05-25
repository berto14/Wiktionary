package com.mycompany.wiktionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Handler extends DefaultHandler {

    /*This booleans are used to keep with which tag we are working (title or text)*/
    boolean tagFnoun = false;
    boolean tagInformation = false;

    StringBuilder content = new StringBuilder();
    long start;
    int articleCounter;
    
    String checking;
    
    boolean yes = false;
    
    BufferedWriter out = null;
   
    public void file(StringBuilder inf) throws IOException {
        try {   
            out.write(inf.toString());
            out.write("\n"); 
        } finally {   
        } 
    }
    
    public void file(String inf) throws IOException {
        try {   
            out.write(inf);
            out.write("\n"); 
        } finally {   
        } 
    }

    /*With this method we open the document with we are gonna work*/
    @Override
    public void startDocument() throws SAXException {
        super.startDocument(); //To change body of generated methods, choose Tools | Templates.
        try {   
            out = new BufferedWriter(new FileWriter("information.txt"/*, true*/));
        } catch (IOException ex) {
            new SAXException("Cannot open file ´information.txt´", ex);
        }
    }

    
    /*With this method we close the document with we are gonna work*/
    @Override
    public void endDocument() throws SAXException {
        super.endDocument(); //To change body of generated methods, choose Tools | Templates.
            if (out != null) {
            try {  
                out.close();
            } catch (IOException ex) {
                new SAXException("Cannot close the document", ex);
            }
            }   
    }
    
    /*In this case we use it to detect when a tag is opened*/
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {       
        try {
            file("");
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*We must take into account the cases which are of our interest.
        In this case the title (word) and the text (information about thw word)*/
        try {
            if (qName.equalsIgnoreCase("title")) {
                file("<" + qName + ">");
                tagFnoun = true;
                tagInformation = false;
                start = System.nanoTime();
                articleCounter++;
            }

            if (qName.equalsIgnoreCase("text")) {
                file("<" + qName + ">");
                tagInformation = true;
                tagFnoun = false;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*We take the content of the targets we identified in the previous method*/
    public void characters(final char ch[], final int start, final int length) throws SAXException {
            
        if (tagFnoun) {
            content.append(ch, start, length);
            tagFnoun = false;
        } else if (tagInformation) {  
            checking = new String(ch,start, length);

            if (checking.contains("==English==")) {
                yes = true ;
            } 
            if (yes == true) {
                content.append(checking);
            }
           
        } 
    }

    /*We detect when a tag is closed and this way to know when the reading of a tag is finished*/
    public void endElement(String uri, String localName, String qName) throws SAXException {
       
        try {
            if (qName.equals("title")) {
                file(content);
                file("</" + qName + "> \n");
            } else if (qName.equals("text")) {
                file(content);
                tagInformation = false;
                yes = false;
                file("</" + qName + "> \n\n");
                System.out.println(String.format("Parse article %d, took: %.3f sec", articleCounter, (System.nanoTime()-start)/1000000.0));
            }
            
            content.setLength(0);
            
        } catch (IOException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}



