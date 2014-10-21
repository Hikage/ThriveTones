package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
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
	@BeforeClass
	public static void init() {
		song = new Song("C", null);
	}

	/**
	 * Tests standardizeKey()
	 */
	@Test
	public void testStandardizeKey(){
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
}
