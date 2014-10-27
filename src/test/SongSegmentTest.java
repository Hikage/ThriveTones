package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import sax.XMLReader;
import thriveTones.ChordDictionary;
import thriveTones.SongSegment;
import thriveTones.SongSegment.SongPart;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * SongSegmentTest.java
 * Tests the SongSegment class
 */

public class SongSegmentTest {
	private HashMap<SongPart, ChordDictionary> parts_dictionary;
	private static SongSegment song_segment;

	/**
	 * Retrieves the full dictionary and initializes a SongSegment for testing
	 */
	@Before
	public void init(){
		parts_dictionary = new XMLReader().getPartsDictionary();
		try{
			SongPart part = SongPart.chorus;
			if(!parts_dictionary.containsKey(part))
				parts_dictionary.put(part, new ChordDictionary());
			song_segment = new SongSegment(part, 1, "1-4", parts_dictionary.get(part));
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Tests initialization
	 */
	@Test
	public void testInitialization() {
		assertEquals(SongPart.chorus, song_segment.getPart());
		assertEquals(1, song_segment.getChords().size());
	}

    /**
     * Tests an invalid SongPart
     * @throws Exception : on an invalid SongPart
     */
	@Test (expected = Exception.class)
	public void testInvalidPart() throws Exception{
		new SongSegment(null, 1, "1-4", parts_dictionary.get(SongPart.chorus));
	}

	/**
	 * Tests toString()
	 */
	@Test
	public void testToString(){
		assertEquals("15maj/1.0", song_segment.toString());
		assertEquals("C5maj/1.0", song_segment.toString("C", 4));
	}

	/**
	 * Tests equivalent SongSegments
	 */
	@Test
	public void testEquivelantSongSegments(){
		try{
			SongPart part = SongPart.chorus;
			if(!parts_dictionary.containsKey(part))
				parts_dictionary.put(part, new ChordDictionary());
			ChordDictionary dictionary = parts_dictionary.get(part);
			SongSegment song_segment2 = new SongSegment(part, 1, "1-4", dictionary);
			SongSegment song_segment3 = new SongSegment(part, 1, "1-4", dictionary);
			assertEquals(song_segment2, song_segment3);

			SongSegment song_segment4 = new SongSegment(part, 2, "1-4", dictionary);
			assertNotEquals(song_segment2, song_segment4);

			part = SongPart.bridge;
			if(!parts_dictionary.containsKey(part))
				parts_dictionary.put(part, new ChordDictionary());
			SongSegment song_segment5 = new SongSegment(part, 1, "1-4", parts_dictionary.get(part));
			assertNotEquals(song_segment2, song_segment5);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
