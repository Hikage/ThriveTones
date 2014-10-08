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

import sax.XMLReader;
import thriveTones.ChordDictionary;
import thriveTones.Song;
import thriveTones.Song.SongPart;

public class SongTest {
	private ChordDictionary dictionary;
	private static Song song;

	@Before
	public void init(){
		dictionary = new XMLReader().getChordDictionary();
		try{
			song = new Song("Title", "Artist", "Chorus", "C", 1, "1-4", 4, dictionary);
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
		assertEquals(SongPart.chorus, song.getPart());
		assertEquals("C", song.getKey());
		assertEquals("C", song.getRelMajor());
		assertEquals(1, song.getMode());
		assertEquals(1, song.getChords().size());
		assertEquals(4, song.getBeats(), 0);
	}

    @Test
    public void testPartToEnum(){
        assertEquals(SongPart.bridge, song.partToEnum("Bridge"));
        assertEquals(SongPart.chorus, song.partToEnum("Chorus Lead-Out"));
        assertEquals(SongPart.chorus, song.partToEnum("Chorus"));
        assertEquals(SongPart.solo, song.partToEnum("Instrumental"));
        assertEquals(SongPart.introverse, song.partToEnum("Intro and Verse"));
        assertEquals(SongPart.intro, song.partToEnum("Intro"));
        assertEquals(SongPart.outro, song.partToEnum("Outro 1"));
        assertEquals(SongPart.outro, song.partToEnum("Outro 2"));
        assertEquals(SongPart.outro, song.partToEnum("Outro"));
        assertEquals(SongPart.prechoruschorus, song.partToEnum("Pre-Chorus and Chorus"));
        assertEquals(SongPart.prechorus, song.partToEnum("Pre-Chorus"));
        assertEquals(SongPart.outro, song.partToEnum("Pre-Outro"));
        assertEquals(SongPart.solo, song.partToEnum("Solo 1"));
        assertEquals(SongPart.solo, song.partToEnum("Solo 2"));
        assertEquals(SongPart.solo, song.partToEnum("Solo 3"));
        assertEquals(SongPart.solo, song.partToEnum("Solo"));
        assertEquals(SongPart.verseprechorus, song.partToEnum("Verse and Pre-Chorus"));
        assertEquals(SongPart.verse, song.partToEnum("Verse"));
    }

	@Test (expected = Exception.class)
	public void testInvalidPart() throws Exception{
		new Song("Title", "Artist", "random part", "C", 1, "1-4", 4, parts_dictionary);
	}

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

	@Test (expected = Exception.class)
	public void testInvalidKeyChange() throws Exception{
		song.changeKey("", 1);
	}

	@Test
	public void testToString(){
		assertEquals("KCmaj C5maj/1.0", song.toString());
	}

	@Test
	public void testEquivelantSongs(){
		try{
			Song song2 = new Song("Title", "Artist", "Chorus", "C", 1, "1-4", 4, dictionary);
			Song song3 = new Song("Title", "Artist", "Chorus", "C", 1, "1-4", 4, dictionary);
			assertEquals(song2, song3);

			Song song4 = new Song("Title", "Artist", "Chorus", "G", 1, "1-4", 4, dictionary);
			assertNotEquals(song2, song4);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
