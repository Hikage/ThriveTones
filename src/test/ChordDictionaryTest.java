package test;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ChordDictionaryTest.java
 * Tests the ChordDictionary class
 */

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import thriveTones.Chord;
import thriveTones.Chord.Tonality;
import thriveTones.ChordDictionary;

public class ChordDictionaryTest {
	private static ChordDictionary dictionary;

	@BeforeClass
	public static void chordDictionaryInit(){
		dictionary = new ChordDictionary();
	}

	@Before
	public void resetDictionary(){
		dictionary.clear();
	}

	@Test
	public void testInit() {
		assertFalse(dictionary == null);
		assertFalse(dictionary.getAllChords() == null);
	}

	@Test
	public void testEmptyGet(){
		assertTrue(dictionary.getAllChords().isEmpty());
		assertNull(dictionary.getANextChord(null));
	}

	@Test
	public void testPutSingleChord(){
		Chord chord = new Chord(1, Tonality.maj, 4);
		dictionary.put(null, chord);
		assertEquals(chord, dictionary.getAllChords().get(0));
		assertEquals(chord, dictionary.getANextChord(null));
	}

	@Test
	public void testPutNextChord(){
		
	}

	@Test
	public void testPutNextChordSequence(){
		
	}

	@Test
	public void testPutVariableHistory(){
		
	}

}
