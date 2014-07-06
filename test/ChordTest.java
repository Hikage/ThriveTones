package test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import markovChords.Chord;


public class ChordTest {

	protected static Chord chord;
	protected static int rnote, roct;
	protected static String rtone;
	
	@Before
	public void chordInit() {
		rnote = new Random().nextInt(11);
		
		int rtonenum = new Random().nextInt(3);
		switch(rtonenum){
		case 0: rtone = "major"; break;
		case 1: rtone = "minor"; break;
		case 2: rtone = "diminished"; break;
		default: rtone = "major";
		}
		
		roct = new Random().nextInt(7) - 3;
		
		System.out.println("Creating " + rtone + " chord: " + rnote + " in octave " + roct);
		chord = new Chord(rnote, rtone, roct);
	}
	
	@Test
	public void testInitialization(){
		assertEquals(rtone, chord.getTonality());
		assertEquals(roct, chord.getOctave());
		assertEquals(rnote, (int)chord.getNotes().get("root"));
		if(rtone.equals("minor") || rtone.equals("diminished"))
			assertEquals(rnote + 3, (int)chord.getNotes().get("third"));
		else
			assertEquals(rnote + 4, (int)chord.getNotes().get("third"));
		if(rtone.equals("diminished"))
			assertEquals(rnote + 6, (int)chord.getNotes().get("fifth"));
		else
			assertEquals(rnote + 7, (int)chord.getNotes().get("fifth"));
	}

}
