package test;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ProgressionGeneratorTest.java
 * Tests the ProgressionGenerator class
 */

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

import sax.XMLReader;
import thriveTones.Chord;
import thriveTones.ChordDictionary;
import thriveTones.ProgressionGenerator;

public class ProgressionGeneratorTest {
	private static ProgressionGenerator generator;
	private static final int prog_length = 12;
	private static final int hist_length = 3;
	private static XMLReader reader;
	private static ChordDictionary chord_dictionary;

	@BeforeClass
	public static void init(){
		reader = new XMLReader();
		try {
			reader.readIn("Hooktheory-Data.xml");
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		chord_dictionary = reader.getChordDictionary();
		generator = new ProgressionGenerator(chord_dictionary);
	}

	@Test
	public void testGetNextChord() {
		int[] roots = new int[8];
		for(int i = 0; i < 100; i++){
			Chord next = chord_dictionary.getANextChord(null);
			roots[next.getRoot()]++;
		}
		for(int count : roots)
			System.out.println("Root count: " + count + "/" + chord_dictionary.get(new LinkedList<Chord>()).size());
		assertTrue(roots[1] > 18);
		assertTrue(roots[1] > roots[5]);
		assertTrue(roots[5] >= roots[2]);
	}

	@Test
	public void testBuildProgression() {
		Chord start = chord_dictionary.getANextChord(null);
		generator.buildProgression(start, prog_length, hist_length);
		LinkedList<Chord> progression = generator.getProgression();
		assertEquals(prog_length, progression.size());
		assertEquals(start, progression.get(0));

		System.out.println();
		System.out.println("Chords length: " + chord_dictionary.get(new LinkedList<Chord>()).size());
		for(Chord chord : generator.getProgression())
			System.out.print(chord.toString() + " ");

		boolean same = true;
		for(int i = 1; i < progression.size(); i++){
			same = progression.get(i-1).equals(progression.get(i));
			if(!same) break;
		}
		assertFalse(same);
	}

	@Test
	public void testControlledGeneration(){
		XMLReader reader2 = new XMLReader();
		try {
			reader2.readIn("test.xml");
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		chord_dictionary = reader2.getChordDictionary();
		System.out.println("Dictionary:\n" + chord_dictionary.toString() + "\n");
		generator = new ProgressionGenerator(chord_dictionary);
		Chord start = reader2.getChordDictionary().get(new LinkedList<Chord>()).get(0);
		assertEquals(1, start.getRoot());

		ArrayList<Chord> sequence = new ArrayList<Chord>();
		sequence.add(start);
		ArrayList<Chord> available_chords = chord_dictionary.get(sequence);

		boolean same = true;
		for(int i = 1; i < available_chords.size(); i++)
			same = (available_chords.get(i) == available_chords.get(i-1));
		assertFalse(same);

		generator.buildProgression(start, 16, 3);
		LinkedList<Chord> progression = generator.getProgression();
		assertEquals(1, progression.get(0).getRoot());
		System.out.println();
		System.out.print(progression.get(0).getRoot() + " ");

		same = true;
		for(int i = 1; i < progression.size(); i++){
			same = (progression.get(i) == progression.get(i-1));
			int prev_chord = progression.get(i-1).getRoot();
			int curr_chord = progression.get(i).getRoot();
			System.out.print(curr_chord + " ");

			switch(prev_chord){
			case 1:	assertTrue(curr_chord == 1 || curr_chord == 5); break;
			case 4: assertEquals(1, curr_chord); break;
			case 5: assertEquals(4, curr_chord); break;
			default: fail("Invalid chord: " + curr_chord);
			}
		}
		assertFalse(same);
	}
}
