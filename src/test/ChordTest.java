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
		
		rand_octave = new Random().nextInt(7) - 3;
		
		System.out.println("Creating " + rand_tone + " chord: " + rand_root + " in octave " + rand_octave);
		try	{ chord = new Chord(rand_root, rand_tone, rand_octave, 0, "", 4); }
		catch (Exception e) {
			System.err.println("Oh no!  An error occurred: " + e.getMessage());
		}
	}
	
	@Test
	public void testInitialization(){
		try{
			chord = new Chord(rand_root, rand_tone, rand_octave, 0, "", 4);

			assertEquals(rand_tone, chord.getTonality());
			assertEquals(rand_octave, chord.getOctave());
			assertEquals(rand_root, (int)chord.getRoot());
		}
		catch (Exception e) {
			System.err.println("Oh no!  An error occurred: " + e.getMessage());
		}
	}
	
	@Test
	public void testInitializationEdgeCases(){
		//test upper bound
		Chord upper_chord;
		try {
			upper_chord = new Chord(7, Chord.Tonality.maj, 7, 0, "", 4);
			assertEquals(7, (int)upper_chord.getRoot());
		}
		catch (Exception e) {
			System.err.println("Oh no!  An error occurred: " + e.getMessage());
		}
		
		//test lower bound
		Chord lower_chord;
		try {
			lower_chord = new Chord(1, Chord.Tonality.dim, -7, 0, "", 4);
			assertEquals(1, (int)lower_chord.getRoot());
		}
		catch (Exception e) {
			System.err.println("Oh no!  An error occurred: " + e.getMessage());
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
		chord.changeOctave(original_octave);
		assertEquals(original_octave, chord.getOctave());
		chord.changeOctave(5);
		assertEquals(5, chord.getOctave());
		chord.changeOctave(-3);
		assertEquals(-3, chord.getOctave());
	}
	
	@Test
	public void testEquals(){
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
			System.err.println("Oh no!  An error occurred: " + e.getMessage());
		}
	}

}
