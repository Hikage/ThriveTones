package test;

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
