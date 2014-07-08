package test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import markovChords.Chord;


public class ChordTest {

	
	
	protected static Chord chord;
	protected static int rand_note, rand_octave;
	protected static Chord.Tonality rand_tone;
	
	@BeforeClass
	public static void chordInit() {
		rand_note = new Random().nextInt(11);
		
		switch(new Random().nextInt(4)){
		case 0: rand_tone = Chord.Tonality.MAJOR; break;
		case 1: rand_tone = Chord.Tonality.MINOR; break;
		case 2: rand_tone = Chord.Tonality.DIMINISHED; break;
		case 3: rand_tone = Chord.Tonality.AUGMENTED; break;
		default: rand_tone = Chord.Tonality.MAJOR;
		}
		
		rand_octave = new Random().nextInt(7) - 3;
		
		System.out.println("Creating " + rand_tone + " chord: " + rand_note + " in octave " + rand_octave);
		try	{ chord = new Chord(rand_note, rand_tone, rand_octave); }
		catch (Exception e) {
			System.err.println("Oh no!  An error occurred: " + e.getMessage());
		}
	}
	
	@Test
	public void testInitialization(){
		assertEquals(rand_tone, chord.getTonality());
		assertEquals(rand_octave, chord.getOctave());
		assertEquals(rand_note, (int)chord.getNotes().get("root"));
		
		if(rand_tone.equals(Chord.Tonality.MINOR) || rand_tone.equals(Chord.Tonality.DIMINISHED))
			assertEquals((rand_note + 3) % 12, (int)chord.getNotes().get("third"));
		else
			assertEquals((rand_note + 4) % 12, (int)chord.getNotes().get("third"));
		
		if(rand_tone.equals(Chord.Tonality.DIMINISHED))
			assertEquals((rand_note + 6) % 12, (int)chord.getNotes().get("fifth"));
		else if(rand_tone.equals(Chord.Tonality.AUGMENTED))
			assertEquals((rand_note + 8) % 12, (int)chord.getNotes().get("fifth"));
		else
			assertEquals((rand_note + 7) % 12, (int)chord.getNotes().get("fifth"));
	}
	
	@Test
	public void testInitializationEdgeCases(){
		//test upper bound
		Chord upper_chord;
		try {
			upper_chord = new Chord(11, Chord.Tonality.MAJOR, 7);
			assertEquals(11, (int)upper_chord.getNotes().get("root"));
			assertEquals(3, (int)upper_chord.getNotes().get("third"));
			assertEquals(6, (int)upper_chord.getNotes().get("fifth"));
		}
		catch (Exception e) {
			System.err.println("Oh no!  An error occurred: " + e.getMessage());
		}
		
		//test lower bound
		Chord lower_chord;
		try {
			lower_chord = new Chord(0, Chord.Tonality.DIMINISHED, -7);
			assertEquals(0, (int)lower_chord.getNotes().get("root"));
			assertEquals(3, (int)lower_chord.getNotes().get("third"));
			assertEquals(6, (int)lower_chord.getNotes().get("fifth"));
		}
		catch (Exception e) {
			System.err.println("Oh no!  An error occurred: " + e.getMessage());
		}
	}
	
	@Test (expected = Exception.class)
	public void testNegativeChord() throws Exception{
		new Chord(-1, Chord.Tonality.AUGMENTED, 0);
	}
	
	@Test (expected = Exception.class)
	public void testInvalidChord() throws Exception{
		new Chord(12, Chord.Tonality.DIMINISHED, 0);
	}

}
