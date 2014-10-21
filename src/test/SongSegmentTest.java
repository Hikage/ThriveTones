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
			song_segment = new SongSegment("Chorus", 1, "1-4", parts_dictionary);
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
	 * Tests partToEnum()
	 */
    @Test
    public void testPartToEnum(){
        assertEquals(SongPart.bridge, song_segment.partToEnum("Bridge"));
        assertEquals(SongPart.chorus, song_segment.partToEnum("Chorus Lead-Out"));
        assertEquals(SongPart.chorus, song_segment.partToEnum("Chorus"));
        assertEquals(SongPart.solo, song_segment.partToEnum("Instrumental"));
        assertEquals(SongPart.introverse, song_segment.partToEnum("Intro and Verse"));
        assertEquals(SongPart.intro, song_segment.partToEnum("Intro"));
        assertEquals(SongPart.outro, song_segment.partToEnum("Outro 1"));
        assertEquals(SongPart.outro, song_segment.partToEnum("Outro 2"));
        assertEquals(SongPart.outro, song_segment.partToEnum("Outro"));
        assertEquals(SongPart.prechoruschorus, song_segment.partToEnum("Pre-Chorus and Chorus"));
        assertEquals(SongPart.prechorus, song_segment.partToEnum("Pre-Chorus"));
        assertEquals(SongPart.outro, song_segment.partToEnum("Pre-Outro"));
        assertEquals(SongPart.solo, song_segment.partToEnum("Solo 1"));
        assertEquals(SongPart.solo, song_segment.partToEnum("Solo 2"));
        assertEquals(SongPart.solo, song_segment.partToEnum("Solo 3"));
        assertEquals(SongPart.solo, song_segment.partToEnum("Solo"));
        assertEquals(SongPart.verseprechorus, song_segment.partToEnum("Verse and Pre-Chorus"));
        assertEquals(SongPart.verse, song_segment.partToEnum("Verse"));
    }

    /**
     * Tests an invalid SongPart
     * @throws Exception : on an invalid SongPart
     */
	@Test (expected = Exception.class)
	public void testInvalidPart() throws Exception{
		new SongSegment("random part", 1, "1-4", parts_dictionary);
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
			SongSegment song_segment2 = new SongSegment("Chorus", 1, "1-4", parts_dictionary);
			SongSegment song_segment3 = new SongSegment("Chorus", 1, "1-4", parts_dictionary);
			assertEquals(song_segment2, song_segment3);

			SongSegment song_segment4 = new SongSegment("Chorus", 2, "1-4", parts_dictionary);
			assertNotEquals(song_segment2, song_segment4);

			SongSegment song_segment5 = new SongSegment("Bridge", 1, "1-4", parts_dictionary);
			assertNotEquals(song_segment2, song_segment5);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
