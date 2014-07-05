package test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import markovChords.Chord;


public class ChordTest {

	protected static Chord chord;
	
	@Before
	public void chordInit() {
		int rchord = new Random().nextInt(7) + 1;
		
		int rtonenum = new Random().nextInt(3);
		String rtone;
		switch(rtonenum){
		case 0: rtone = "major"; break;
		case 1: rtone = "minor"; break;
		case 2: rtone = "diminished"; break;
		default: rtone = "major";
		}
		
		int roct = new Random().nextInt(7) - 3;
		
		System.out.println("Creating " + rtone + " chord: " + rchord + " in octave " + roct);
		chord = new Chord(rchord, rtone, roct);
		
		assertEquals(rtone, chord.getTonality());
		assertEquals(roct, chord.getOctave());
	}

}
