package test;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ChordPairing.java
 * Keeps track of possible chord pairings, along with their frequency
 * to calculate probabilities
 */

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import thriveTones.Chord;
import thriveTones.Chord.Tonality;
import thriveTones.ChordPairing;

public class ChordPairingTest {
	private static ChordPairing pairing;
	private static Chord chord1;
	private static Chord chord2;

	@BeforeClass
	public static void init(){
		chord1 = new Chord(1, Tonality.maj, 4);
		chord2 = new Chord(5, Tonality.min, 4);
	}

	@Before
	public void pairingInit(){
		pairing = new ChordPairing(chord1);
	}

	@Test
	public void testAddSameChord() {
		pairing.addChord(chord1);
		pairing.addChord(chord1);
		ArrayList<Integer> indices = pairing.getIndices();
		ArrayList<Chord> next_chords = pairing.getNextChords();
		assertEquals(3, indices.size());
		for(int index : indices)
			assertEquals(0, index);
		assertEquals(1, next_chords.size());
		assertEquals(1, next_chords.get(0).getRoot());
	}

	@Test
	public void testAddDifferentChord() {
		pairing.addChord(chord2);
		pairing.addChord(chord1);
		ArrayList<Integer> indices = pairing.getIndices();
		ArrayList<Chord> next_chords = pairing.getNextChords();
		assertEquals(3, indices.size());
		assertEquals(0, (int)indices.get(0));
		assertEquals(1, (int)indices.get(1));
		assertEquals(0, (int)indices.get(2));
		assertEquals(2, next_chords.size());
		assertEquals(1, next_chords.get(0).getRoot());
		assertEquals(5, next_chords.get(1).getRoot());
	}

}
