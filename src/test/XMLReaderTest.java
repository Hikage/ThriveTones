package test;

/**
 * "Digital Chords" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * XMLReaderTest.java
 * Tests the XMLReader class
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


public class XMLReaderTest {

	protected static NodeList rows;
	
	@BeforeClass
	public static void XMLReaderInit() {
		String file = "Hooktheory-Data.xml";
		
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    	    
		    FileReader reader = new FileReader(file);        
		    Document document = builder.parse(new InputSource(reader));
		    
		    NodeList results = document.getElementsByTagName("resultset");
		    rows = results.item(0).getChildNodes();
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInitialization() {		
		//assertEquals(3, rows.getLength());
		assertEquals(17, rows.item(1).getChildNodes().getLength());
		assertEquals("Jimmy Eat World", rows.item(1).getChildNodes().item(1).getTextContent().trim());
	}

	@Test
	public void testNodeValueByAttName(){
		NodeList fields = rows.item(1).getChildNodes();
		assertEquals("Jimmy Eat World", XMLReader.nodeValueByAttName(fields, "artist"));
		assertEquals("The Middle", XMLReader.nodeValueByAttName(fields, "song"));
		assertEquals("Intro and Verse", XMLReader.nodeValueByAttName(fields, "section"));
		assertEquals(",1-8,5-8,4-8,1-8,1-8,5-8,4-8,1-8,", XMLReader.nodeValueByAttName(fields, "SIF"));
		assertEquals("4", XMLReader.nodeValueByAttName(fields, "beatsInMeasure"));
		assertEquals("Eb", XMLReader.nodeValueByAttName(fields, "songKey"));
		assertEquals("146", XMLReader.nodeValueByAttName(fields, "bpm"));
		assertEquals("1", XMLReader.nodeValueByAttName(fields, "mode"));
		assertEquals("", XMLReader.nodeValueByAttName(fields, "badFieldName"));
	}

	@Test
	public void testValidXMLKeytoKey(){
		assertEquals("A", XMLReader.XMLKeytoKey("A"));

		assertEquals("Bb", XMLReader.XMLKeytoKey("Bb"));
		assertEquals("Cb", XMLReader.XMLKeytoKey("CB"));
		assertEquals("Db", XMLReader.XMLKeytoKey("Df"));
		assertEquals("Eb", XMLReader.XMLKeytoKey("EF"));

		assertEquals("F#", XMLReader.XMLKeytoKey("F#"));
		assertEquals("G#", XMLReader.XMLKeytoKey("Gs"));
		assertEquals("A#", XMLReader.XMLKeytoKey("AS"));
		assertEquals("B#", XMLReader.XMLKeytoKey("bS"));
	}

	@Test
	public void testInvalidXMLKeytoKey(){
		assertNull(XMLReader.XMLKeytoKey("H"));
		assertNull(XMLReader.XMLKeytoKey("cy"));
		assertNull(XMLReader.XMLKeytoKey(""));
		assertNull(XMLReader.XMLKeytoKey("14"));
		assertNull(XMLReader.XMLKeytoKey("A*"));
		assertNull(XMLReader.XMLKeytoKey("ABC"));
		assertNull(XMLReader.XMLKeytoKey("sus"));
	}

	@Test
	public void testExtractKey(){
		NodeList fields = rows.item(1).getChildNodes();
		try{
			assertEquals("Eb", XMLReader.extractKey(fields));
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test (expected = Exception.class)
	public void testNullExtractKey() throws Exception{
		XMLReader.extractKey(null);
	}

	@Test
	public void testSIFtoChords(){
		NodeList fields = rows.item(1).getChildNodes();
		try{
			assertEquals("KEbmaj E5maj/2.0 B5maj/2.0 A5maj/2.0 E5maj/2.0 E5maj/2.0 B5maj/2.0 A5maj/2.0 E5maj/2.0",
					XMLReader.SIFtoChords(fields).toString());
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test (expected = Exception.class)
	public void testNullSIFtoChords() throws Exception{
		XMLReader.SIFtoChords(null);
	}

}
