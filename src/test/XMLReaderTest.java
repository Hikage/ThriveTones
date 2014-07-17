package test;

/**
 * "Digital Chords" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * XMLReaderTest.java
 * TODO details on this class
 */

import static org.junit.Assert.*;

import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import sax.XMLReader;

/**
 * 
 */
public class XMLReaderTest {

	protected static NodeList rows;
	
	@BeforeClass
	public static void XMLReaderInit() {
		String file = "test.xml";
		
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    	    
		    FileReader reader = new FileReader(file);        
		    Document document = builder.parse(new InputSource(reader));
		    
		    NodeList results = document.getElementsByTagName("resultset");
		    rows = results.item(0).getChildNodes();
		}
		catch(Exception e){
			System.err.println("Oh no!  An error occurred: " + e.getMessage());
		}
	}
	
	@Test
	public void testInitialization() {		
		assertEquals(3, rows.getLength());
		assertEquals(17, rows.item(1).getChildNodes().getLength());
		assertEquals("Jimmy Eat World", rows.item(1).getChildNodes().item(1).getTextContent().trim());
	}

	@Test
	public void testNodeValueByAttValue(){
		NodeList fields = rows.item(1).getChildNodes();
		assertEquals("Jimmy Eat World", XMLReader.nodeValueByAttValue(fields, "artist"));
		assertEquals("The Middle", XMLReader.nodeValueByAttValue(fields, "song"));
		assertEquals("Intro and Verse", XMLReader.nodeValueByAttValue(fields, "section"));
		assertEquals(",1-8,5-8,4-8,1-8,1-8,5-8,4-8,1-8,", XMLReader.nodeValueByAttValue(fields, "SIF"));
		assertEquals("4", XMLReader.nodeValueByAttValue(fields, "beatsInMeasure"));
		assertEquals("Eb", XMLReader.nodeValueByAttValue(fields, "songKey"));
		assertEquals("146", XMLReader.nodeValueByAttValue(fields, "bpm"));
		assertEquals("1", XMLReader.nodeValueByAttValue(fields, "mode"));
		assertEquals("", XMLReader.nodeValueByAttValue(fields, "badFieldName"));
	}
	
}
