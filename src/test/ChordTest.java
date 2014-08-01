package test;

/**
 * "Digital Chords" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ChordTest.java
 * Tests the Chord class
 */

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import markovChords.Chord;


public class ChordTest {

	protected static Chord chord;
	protected static int rand_root, rand_octave;
	protected static Chord.Tonality rand_tone;

	@BeforeClass
	public static void chordInit() {
		rand_root = new Random().nextInt(7) + 1;

		switch(new Random().nextInt(4)){
		case 0: rand_tone = Chord.Tonality.maj; break;
		case 1: rand_tone = Chord.Tonality.min; break;
		case 2: rand_tone = Chord.Tonality.dim; break;
		case 3: rand_tone = Chord.Tonality.aug; break;
		default: rand_tone = Chord.Tonality.maj;
		}

		rand_octave = new Random().nextInt(10);

		//necessary for tests requiring an established chord
		try{
			System.out.println("Creating " + rand_tone + " chord: " + rand_root + " in octave " + rand_octave);
			chord = new Chord(rand_root, rand_tone, rand_octave, 0, "", 4);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testFullInitialization(){
		try{
			System.out.println("Creating " + rand_tone + " chord: " + rand_root + " in octave " + rand_octave);
			chord = new Chord(rand_root, rand_tone, rand_octave, 0, "", 4);

			assertEquals(rand_root, (int)chord.getRoot());
			assertEquals(rand_tone, chord.getTonality());
			assertEquals(rand_octave, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(4, chord.getDuration());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleInitializtion(){
		try{
			System.out.println("Creating simple " + rand_tone + " chord: " + rand_root);
			chord = new Chord(rand_root, rand_tone, 8);

			assertEquals(rand_root, (int)chord.getRoot());
			assertEquals(rand_tone, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(8, chord.getDuration());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testUpperBoundChord(){
		try {
			chord = new Chord(7, Chord.Tonality.aug, 10, 3, "sus42", 8);
			assertEquals(7, (int)chord.getRoot());
			assertEquals(Chord.Tonality.aug, chord.getTonality());
			assertEquals(10, chord.getOctave());
			assertEquals(3, chord.getInversion());
			assertEquals(8, chord.getDuration());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testLowerBoundChord(){
		try {
			chord = new Chord(1, Chord.Tonality.dim, 0, 0, "", 1);
			assertEquals(1, (int)chord.getRoot());
			assertEquals(Chord.Tonality.dim, chord.getTonality());
			assertEquals(0, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(1, chord.getDuration());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test (expected = Exception.class)
	public void testEmptySIFChord() throws Exception{
		new Chord(1, "");
	}

	@Test (expected = Exception.class)
	public void testEmptyTargetSIFChord() throws Exception{
		new Chord(1, "1/");
	}

	@Test (expected = Exception.class)
	public void testInvalidTargetSIFChord() throws Exception{
		new Chord(1, "1/V");
	}

	@Test (expected = Exception.class)
	public void testLongTargetSIFChord() throws Exception{
		new Chord(1, "1/12");
	}

	@Test (expected = Exception.class)
	public void testEmptyDurationSIFChord() throws Exception{
		new Chord(1, "1-");
	}

	@Test (expected = Exception.class)
	public void testInvalidDurationSIFChord() throws Exception{
		new Chord(1, "1-V");
	}

	@Test (expected = Exception.class)
	public void testLongDurationSIFChord() throws Exception{
		new Chord(1, "1-120");
	}

	@Test
	public void testSimpleMajorSIFChord(){
		try{
			chord = new Chord(1, "1-4");
			assertEquals(1, (int)chord.getRoot());
			assertEquals(Chord.Tonality.maj, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(4, chord.getDuration());

			chord = new Chord(1, "3-1");
			assertEquals(3, (int)chord.getRoot());
			assertEquals(Chord.Tonality.min, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(1, chord.getDuration());

			chord = new Chord(1, "7-8");
			assertEquals(7, (int)chord.getRoot());
			assertEquals(Chord.Tonality.dim, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(8, chord.getDuration());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleMinorSIFChord(){
		try{
			chord = new Chord(6, "6-4");			//i chord
			assertEquals(1, (int)chord.getRoot());
			assertEquals(Chord.Tonality.min, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(4, chord.getDuration());

			chord = new Chord(6, "1-1");			//III chord
			assertEquals(3, (int)chord.getRoot());
			assertEquals(Chord.Tonality.maj, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(1, chord.getDuration());

			chord = new Chord(6, "7-8");			//ii* chord
			assertEquals(2, (int)chord.getRoot());
			assertEquals(Chord.Tonality.dim, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(8, chord.getDuration());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSIFInvertedChord(){
		try{
			chord = new Chord(2, "27-4");			//i7 chord
			assertEquals(1, (int)chord.getRoot());
			assertEquals(Chord.Tonality.min, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals(4, chord.getDuration());

			chord = new Chord(3, "764-1");			//v* chord
			assertEquals(5, (int)chord.getRoot());
			assertEquals(Chord.Tonality.dim, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(2, chord.getInversion());
			assertEquals(1, chord.getDuration());

			chord = new Chord(4, "742-8");			//iv*7 chord
			assertEquals(4, (int)chord.getRoot());
			assertEquals(Chord.Tonality.dim, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(3, chord.getInversion());
			assertEquals(8, chord.getDuration());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSIFEmbellishedChord(){
		try{
			chord = new Chord(5, "56sus2-4");		//I chord
			assertEquals(1, (int)chord.getRoot());
			assertEquals(Chord.Tonality.maj, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(1, chord.getInversion());
			assertEquals("sus2", chord.getEmbellishment());
			assertEquals(4, chord.getDuration());

			chord = new Chord(6, "37add9-1");		//v79 chord
			assertEquals(5, (int)chord.getRoot());
			assertEquals(Chord.Tonality.min, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(0, chord.getInversion());
			assertEquals("add9", chord.getEmbellishment());
			assertEquals(1, chord.getDuration());

			chord = new Chord(7, "364sus42-8");		//iv chord
			assertEquals(4, (int)chord.getRoot());
			assertEquals(Chord.Tonality.min, chord.getTonality());
			assertEquals(5, chord.getOctave());
			assertEquals(2, chord.getInversion());
			assertEquals("sus42", chord.getEmbellishment());
			assertEquals(8, chord.getDuration());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testRestSIFChord(){
		try{
			chord = new Chord(1, "rest");
			assertEquals(0, chord.getRoot());
			assertEquals(4, chord.getDuration());

			chord = new Chord(6, "REST-10");
			assertEquals(0, chord.getRoot());
			assertEquals(10, chord.getDuration());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test (expected = Exception.class)
	public void testTargetRestSIFChord() throws Exception{
		new Chord(1, "rest/5");
	}

	@Test (expected = Exception.class)
	public void testDurationTargetRestSIFChord() throws Exception{
		new Chord(1, "rest-4/5");
	}

	@Test (expected = Exception.class)
	public void testExtraInfoRestSIFChord() throws Exception{
		new Chord(1, "rest42-4");
	}

	@Test
	public void testInversions(){
		try{
			chord = new Chord(1, "1");
			String chord_string = chord.toString();
			assertFalse(chord_string.contains("^"));
			assertFalse(chord_string.contains("7"));

			chord = new Chord(1, "57");
			chord_string = chord.toString();
			assertFalse(chord_string.contains("^"));
			assertTrue(chord_string.contains("7"));

			chord = new Chord(1, "36");
			chord_string = chord.toString();
			assertTrue(chord_string.contains("^"));
			assertFalse(chord_string.contains("7"));

			chord = new Chord(1, "664");
			chord_string = chord.toString();
			assertTrue(chord_string.contains("^^"));
			assertFalse(chord_string.contains("7"));

			chord = new Chord(1, "142");
			chord_string = chord.toString();
			assertTrue(chord_string.contains("^^^"));
			assertTrue(chord_string.contains("7"));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test (expected = Exception.class)
	public void testNegativeChord() throws Exception{
		new Chord(-1, Chord.Tonality.aug, 0, 0, "", 4);
	}

	@Test (expected = Exception.class)
	public void testInvalidChord() throws Exception{
		new Chord(12, Chord.Tonality.dim, 0, 0, "", 4);
	}

	@Test
	public void testMakeMajor(){
		int original_octave = chord.getOctave();
		chord.makeMajor();
		assertEquals(original_octave, chord.getOctave());
		assertEquals(Chord.Tonality.maj, chord.getTonality());
	}

	@Test
	public void testMakeMinor(){
		int original_octave = chord.getOctave();
		chord.makeMinor();
		assertEquals(original_octave, chord.getOctave());
		assertEquals(Chord.Tonality.min, chord.getTonality());
	}

	@Test
	public void testMakeDiminished(){
		int original_octave = chord.getOctave();
		chord.makeDiminished();
		assertEquals(original_octave, chord.getOctave());
		assertEquals(Chord.Tonality.dim, chord.getTonality());
	}

	@Test
	public void testMakeAugmented(){
		int original_octave = chord.getOctave();
		chord.makeAugmented();
		assertEquals(original_octave, chord.getOctave());
		assertEquals(Chord.Tonality.aug, chord.getTonality());
	}

	@Test
	public void testChangeOctave(){
		int original_octave = chord.getOctave();
		try{
			chord.changeOctave(original_octave);
			assertEquals(original_octave, chord.getOctave());
			chord.changeOctave(5);
			assertEquals(5, chord.getOctave());
			chord.changeOctave(0);
			assertEquals(0, chord.getOctave());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testToString(){
		try{
			//TODO: Adjust for applied targets and suspension
			chord = new Chord(1, "542sus4-3/5");
			assertEquals("55maj7^^^w", chord.toString());
			assertEquals("E5maj7^^^w", chord.toString(0));
			assertEquals("F5maj7^^^w", chord.toString(1));
			assertEquals("G5maj7^^^w", chord.toString(2));
			assertEquals("A5maj7^^^w", chord.toString(3));
			assertEquals("B5maj7^^^w", chord.toString(4));
			assertEquals("C5maj7^^^w", chord.toString(5));
			assertEquals("D5maj7^^^w", chord.toString(6));
			assertEquals("E5maj7^^^w", chord.toString(7));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testEquivalentChords(){
		try{
			chord = new Chord(rand_root, rand_tone, rand_octave, 0, "", 4);
			Chord chord2 = new Chord(rand_root, rand_tone, rand_octave, 0, "", 4);
			assertTrue(chord.equals(chord2));
			assertTrue(chord2.equals(chord));

			chord2.changeOctave(rand_octave + 1);
			assertFalse(chord.equals(chord2));
			assertFalse(chord2.equals(chord));

			chord2.changeOctave(rand_octave);
			assertTrue(chord.equals(chord2));
			assertTrue(chord2.equals(chord));

			chord.makeDiminished();
			chord2.makeDiminished();
			assertTrue(chord.equals(chord2));
			assertTrue(chord2.equals(chord));

			chord.makeMinor();
			assertFalse(chord.equals(chord2));
			assertFalse(chord2.equals(chord));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
