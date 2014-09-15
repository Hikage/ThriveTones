package test;

/**
 * "ThriveTones" Song Generator
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
import thriveTones.Chord;
import thriveTones.Chord.Tonality;
import thriveTones.ChordPair;

public class XMLReaderTest {

	private static NodeList rows;
	private static XMLReader reader;
	private static String file = "Hooktheory-Data.xml";
	
	@BeforeClass
	public static void XMLReaderInit() {
		reader = new XMLReader();
		
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    	    
		    FileReader freader = new FileReader(file);
		    Document document = builder.parse(new InputSource(freader));
		    
		    NodeList results = document.getElementsByTagName("resultset");
		    rows = results.item(0).getChildNodes();
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testReadIn(){
		try {
			reader.readIn(file);
		}
		catch (Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}

		assertEquals(229, reader.getUniqueChords().size());
		assertEquals(17, rows.item(1).getChildNodes().getLength());
		assertEquals("Jimmy Eat World", rows.item(1).getChildNodes().item(1).getTextContent().trim());
	}

	@Test
	public void testNodeValueByAttName(){
		NodeList fields = rows.item(1).getChildNodes();
		assertEquals("Jimmy Eat World", reader.nodeValueByAttName(fields, "artist"));
		assertEquals("The Middle", reader.nodeValueByAttName(fields, "song"));
		assertEquals("Intro and Verse", reader.nodeValueByAttName(fields, "section"));
		assertEquals(",1-8,5-8,4-8,1-8,1-8,5-8,4-8,1-8,", reader.nodeValueByAttName(fields, "SIF"));
		assertEquals("4", reader.nodeValueByAttName(fields, "beatsInMeasure"));
		assertEquals("Eb", reader.nodeValueByAttName(fields, "songKey"));
		assertEquals("146", reader.nodeValueByAttName(fields, "bpm"));
		assertEquals("1", reader.nodeValueByAttName(fields, "mode"));
		assertEquals("", reader.nodeValueByAttName(fields, "badFieldName"));
	}

	@Test
	public void testValidXMLKeytoKey(){
		assertEquals("A", reader.XMLKeytoKey("A"));

		assertEquals("Bb", reader.XMLKeytoKey("Bb"));
		assertEquals("Cb", reader.XMLKeytoKey("CB"));
		assertEquals("Db", reader.XMLKeytoKey("Df"));
		assertEquals("Eb", reader.XMLKeytoKey("EF"));

		assertEquals("F#", reader.XMLKeytoKey("F#"));
		assertEquals("G#", reader.XMLKeytoKey("Gs"));
		assertEquals("A#", reader.XMLKeytoKey("AS"));
		assertEquals("B#", reader.XMLKeytoKey("bS"));
	}

	@Test
	public void testInvalidXMLKeytoKey(){
		assertNull(reader.XMLKeytoKey("H"));
		assertNull(reader.XMLKeytoKey("cy"));
		assertNull(reader.XMLKeytoKey(""));
		assertNull(reader.XMLKeytoKey("14"));
		assertNull(reader.XMLKeytoKey("A*"));
		assertNull(reader.XMLKeytoKey("ABC"));
		assertNull(reader.XMLKeytoKey("sus"));
	}

	@Test
	public void testExtractKey(){
		NodeList fields = rows.item(1).getChildNodes();
		try{
			assertEquals("Eb", reader.extractKey(fields));
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test (expected = Exception.class)
	public void testNullExtractKey() throws Exception{
		reader.extractKey(null);
	}

	@Test
	public void testSIFtoChords(){
		NodeList fields = rows.item(1).getChildNodes();
		try{
			assertEquals("KEbmaj E5maj/2.0 B5maj/2.0 A5maj/2.0 E5maj/2.0 E5maj/2.0 B5maj/2.0 A5maj/2.0 E5maj/2.0",
					reader.SIFtoChords(fields).toString());
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test (expected = Exception.class)
	public void testNullSIFtoChords() throws Exception{
		reader.SIFtoChords(null);
	}

	@Test
	public void testGetChord(){
		reader = new XMLReader();
		assertEquals(0, reader.getUniqueChords().size());

		Chord chord = new Chord(1, Tonality.maj, 4);
		Chord stored_chord = reader.getChord(chord);
		assertEquals(1, reader.getUniqueChords().size());
		assertEquals(chord, stored_chord);
		assertEquals(chord, reader.getUniqueChords().get(0));

		//doesn't re-add the same Chord
		reader.getChord(chord);
		assertEquals(1, reader.getUniqueChords().size());

		//shouldn't re-add the same Chord, even if it's a different object
		Chord chord2 = new Chord(1, Tonality.maj, 4);
		stored_chord = reader.getChord(chord2);
		assertEquals(1, reader.getUniqueChords().size());
		assertEquals(chord, stored_chord);
		assertEquals(chord2, stored_chord);
	}

	@Test
	public void testGetChordPair(){
		reader = new XMLReader();
		assertEquals(0, reader.getUniqueChordPairs().size());

		Chord chord1 = new Chord(5, Tonality.maj, 4);
		Chord chord2 = new Chord(1, Tonality.maj, 4);
		ChordPair chord_pair = new ChordPair(chord1, chord2);
		ChordPair stored_chord_pair = reader.getChordPair(chord1, chord2);
		assertEquals(1, reader.getUniqueChordPairs().size());
		assertEquals(chord_pair, stored_chord_pair);
		assertEquals(chord_pair, reader.getUniqueChordPairs().get(0));

		//doesn't re-add the same ChordPair
		reader.getChordPair(chord1, chord2);
		assertEquals(1, reader.getUniqueChordPairs().size());

		//shouldn't re-add the same ChordPair, even if it's a different object
		Chord chord3 = new Chord(1, Tonality.maj, 4);
		ChordPair chord_pair2 = new ChordPair(chord1, chord3);
		stored_chord_pair = reader.getChordPair(chord1, chord3);
		assertEquals(1, reader.getUniqueChordPairs().size());
		assertEquals(chord_pair, stored_chord_pair);
		assertEquals(chord_pair2, stored_chord_pair);
	}
}
