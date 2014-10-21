package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import thriveTones.Song;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * SongTest.java
 * Tests the Song class
 */

public class SongTest {
	private static Song song;

	/**
	 * Initializes a Song object prior to tests
	 */
	@Before
	public void init() {
		song = new Song("C", 1, null, "AI Creation", "Music Bot", 4);
	}

	/**
	 * Tests initialization
	 */
	@Test
	public void testInitialization(){
		assertEquals("C", song.getKey());
		assertEquals("C", song.getRelMajor());
		assertEquals(1, song.getMode());
		assertEquals("AI Creation", song.getName());
		assertEquals("Music Bot", song.getArtist());
		assertEquals(4, song.getBeats(), 0);
	}

	/**
	 * Tests invalid initialization
	 */
	@Test (expected = Exception.class)
	public void testInvalidInitialization(){
		new Song(null, 1, null, "Title", "Artist", 4);
	}

	/**
	 * Tests standardizeKey()
	 */
	@Test
	public void testStandardizeKey(){
		assertEquals("C", song.standardizeKey(""));
		assertEquals("C", song.standardizeKey("C"));
		assertEquals("G", song.standardizeKey("G"));
		assertEquals("F", song.standardizeKey("f"));
		assertEquals("Ab", song.standardizeKey("Ab"));
		assertEquals("Bb", song.standardizeKey("bB"));
		assertEquals("Db", song.standardizeKey("df"));
		assertEquals("C#", song.standardizeKey("Cs"));
		assertEquals("Abb", song.standardizeKey("Aff"));
		assertEquals("Cbbb", song.standardizeKey("cbfb"));
		assertEquals("C", song.standardizeKey("x"));
		assertEquals("C", song.standardizeKey("ci"));
		assertEquals("G#####", song.standardizeKey("g#####"));
		assertEquals("C", song.standardizeKey("v#"));
	}

	/**
	 * Tests changeKey()
	 */
	@Test
	public void testChangeKey(){
		try{
			song.changeKey("C", 1);
			assertEquals("C", song.getKey());
			assertEquals("C", song.getRelMajor());
			assertEquals(1, song.getMode());

			song.changeKey("Ab", 6);
			assertEquals("Ab", song.getKey());
			assertEquals("Cb", song.getRelMajor());
			assertEquals(6, song.getMode());
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Tests invalid key change
	 * @throws Exception : on invalid desired key
	 */
	@Test (expected = Exception.class)
	public void testInvalidKeyChange() throws Exception{
		song.changeKey("", 1);
	}

	/**
	 * Checks for valid relative major calculation
	 * @param target : expected root
	 * @param key : chord key
	 * @param mode : chord mode
	 */
	public void ckRelativeMajor(String target, String key, int mode){
		try{
			song.changeKey(key, mode);
			assertEquals(key, song.getKey());
			assertEquals(target, song.getRelMajor());
			assertEquals(mode, song.getMode());
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
}
