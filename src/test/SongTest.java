package test;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * SongTest.java
 * Tests the Song class
 */

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import thriveTones.Song;

public class SongTest {

	private static Song song;

	@Before
	public void init(){
		try{
			song = new Song("Title", "Artist", "Part", "C", 1, "1-4", 4);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testInitialization() {
		assertEquals("Title", song.getName());
		assertEquals("Artist", song.getArtist());
		assertEquals("Part", song.getPart());
		assertEquals("C", song.getKey());
		assertEquals("C", song.getRelMajor());
		assertEquals(1, song.getMode());
		assertEquals(1, song.getChords().size());
		assertEquals(4, song.getBeats(), 0);
	}

	public void ckRelativeMajor(String target, String key, int mode){
		song.changeKey(key, mode);
		assertEquals(key, song.getKey());
		assertEquals(target, song.getRelMajor());
		assertEquals(mode, song.getMode());
	}

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

	@Test
	public void testChangeKey(){
		song.changeKey("C", 1);
		assertEquals("C", song.getKey());
		assertEquals("C", song.getRelMajor());
		assertEquals(1, song.getMode());
		
		song.changeKey("Ab", 6);
		assertEquals("Ab", song.getKey());
		assertEquals("Cb", song.getRelMajor());
		assertEquals(6, song.getMode());
	}
	
	@Test
	public void testToString(){
		assertEquals("KCmaj C5maj/1.0", song.toString());
	}
}
