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
	private static ChordDictionary dictionary;

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

		dictionary = reader.getChordDictionary();
		generator = new ProgressionGenerator(dictionary);
	}

	@Test
	public void testGetNextChord() {
		int frequency = 0;
		for(int i = 0; i < 100; i++){
			Chord next = dictionary.getANextChord(null);
			if(next.getRoot() == 1) frequency++;
		}
		assertTrue(frequency > 70);
	}

	@Test
	public void testBuildProgression() {
		Chord start = dictionary.getANextChord(null);
		generator.buildProgression(start, prog_length, hist_length);
		assertEquals(prog_length, generator.getProgression().size());
		assertEquals(start, generator.getProgression().get(0));
		//assertEquals(new Chord(1, Tonality.min, 4), generator.getProgression().get(1));

		System.out.println();
		for(Chord chord : generator.getProgression())
			System.out.print(chord.toString() + " ");
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

		generator = new ProgressionGenerator(reader2.getChordDictionary());
		Chord start = reader.getChordDictionary().getAllChords().get(0);
		assertEquals(1, start.getRoot());

		generator.buildProgression(start, 16, 2);
		LinkedList<Chord> progression = generator.getProgression();
		assertEquals(1, progression.get(0).getRoot());
		System.out.print(progression.get(0).getRoot() + " ");

		for(int i = 1; i < progression.size(); i++){
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
	}
}
