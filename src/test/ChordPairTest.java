package test;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ChordPairTest.java
 * Tests the ChordPair class
 */

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import thriveTones.Chord;
import thriveTones.Chord.Tonality;
import thriveTones.ChordPair;

public class ChordPairTest {
	private static Chord first;
	private static Chord second;
	private static ChordPair pair;

	@BeforeClass
	public static void chordPairInit() {
		first = new Chord(5, Tonality.maj, 4);
		second = new Chord(1, Tonality.maj, 4);
		pair = new ChordPair(first, second);
	}

	@Test
	public void testInitialization(){
		assertEquals(first, pair.first());
		assertEquals(second, pair.second());
		assertTrue(pair.getChordPairing().getNextChords().isEmpty());
	}

	@Test
	public void testAddNextChord(){
		assertEquals(0, pair.getChordPairing().getNextChords().size());
		Chord third = new Chord(4, Tonality.maj, 4);
		pair.addNextChord(third);
		assertEquals(1, pair.getChordPairing().getNextChords().size());
		assertEquals(third, pair.getChordPairing().getNextChords().get(0));
	}

	@Test
	public void testEquivalentChordPairs(){
		ChordPair pair2 = new ChordPair(first, second);
		assertEquals(pair2, pair);

		Chord new_first = new Chord(5, Tonality.maj, 4);
		Chord new_second = new Chord(1, Tonality.maj, 4);
		ChordPair pair3 = new ChordPair(new_first, new_second);
		assertEquals(pair3, pair);

		ChordPair pair4 = new ChordPair(first, first);
		assertNotEquals(pair4, pair);
	}

}
