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
			song_segment = new SongSegment("Title", "Artist", "Chorus", "C", 1, "1-4", 4, parts_dictionary);
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
		assertEquals("Title", song_segment.getName());
		assertEquals("Artist", song_segment.getArtist());
		assertEquals(SongPart.chorus, song_segment.getPart());
		assertEquals("C", song_segment.getKey());
		assertEquals("C", song_segment.getRelMajor());
		assertEquals(1, song_segment.getMode());
		assertEquals(1, song_segment.getChords().size());
		assertEquals(4, song_segment.getBeats(), 0);
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
		new SongSegment("Title", "Artist", "random part", "C", 1, "1-4", 4, parts_dictionary);
	}

	/**
	 * Tests changeKey()
	 */
	@Test
	public void testChangeKey(){
		try{
			song_segment.changeKey("C", 1);
			assertEquals("C", song_segment.getKey());
			assertEquals("C", song_segment.getRelMajor());
			assertEquals(1, song_segment.getMode());

			song_segment.changeKey("Ab", 6);
			assertEquals("Ab", song_segment.getKey());
			assertEquals("Cb", song_segment.getRelMajor());
			assertEquals(6, song_segment.getMode());
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Checks for valid relative major calculation
	 * @param target : expected root
	 * @param key : chord key
	 * @param mode : chord mode
	 */
	public void ckRelativeMajor(String target, String key, int mode){
		try{
			song_segment.changeKey(key, mode);
			assertEquals(key, song_segment.getKey());
			assertEquals(target, song_segment.getRelMajor());
			assertEquals(mode, song_segment.getMode());
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the calculation of the relative major
	 * (method called indirectly through changeKey())
	 */
	@Test
	public void testCalculateRelativeMajor(){
		ckRelativeMajor("C", "C", 1);
		ckRelativeMajor("C", "D", 2);
		ckRelativeMajor("C", "E", 3);
		ckRelativeMajor("C", "F", 4);
		ckRelativeMajor("C", "G", 5);
		ckRelativeMajor("C", "A", 6);
		ckRelativeMajor("C", "B", 7);

		ckRelativeMajor("Bb", "C", 2);
		ckRelativeMajor("Ab", "C", 3);
		ckRelativeMajor("G", "C", 4);
		ckRelativeMajor("F", "C", 5);
		ckRelativeMajor("Eb", "C", 6);
		ckRelativeMajor("Db", "C", 7);

		ckRelativeMajor("Db", "Bb", 6);
		ckRelativeMajor("Bbb", "Db", 3);
		ckRelativeMajor("Eb", "F", 2);
		ckRelativeMajor("F#", "B", 4);
		ckRelativeMajor("F##", "B#", 4);
	}

	/**
	 * Tests invalid key change
	 * @throws Exception : on invalid desired key
	 */
	@Test (expected = Exception.class)
	public void testInvalidKeyChange() throws Exception{
		song_segment.changeKey("", 1);
	}

	/**
	 * Tests toString()
	 */
	@Test
	public void testToString(){
		assertEquals("KCmaj C5maj/1.0", song_segment.toString());
	}

	/**
	 * Tests equivalent SongSegments
	 */
	@Test
	public void testEquivelantSongSegments(){
		try{
			SongSegment song_segment2 = new SongSegment("Title", "Artist", "Chorus", "C", 1, "1-4", 4, parts_dictionary);
			SongSegment song_segment3 = new SongSegment("Title", "Artist", "Chorus", "C", 1, "1-4", 4, parts_dictionary);
			assertEquals(song_segment2, song_segment3);

			SongSegment song_segment4 = new SongSegment("Title", "Artist", "Chorus", "G", 1, "1-4", 4, parts_dictionary);
			assertNotEquals(song_segment2, song_segment4);

			SongSegment song_segment5 = new SongSegment("Title", "Artist", "Bridge", "C", 1, "1-4", 4, parts_dictionary);
			assertNotEquals(song_segment2, song_segment5);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
