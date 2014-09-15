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
import thriveTones.ProgressionGenerator;

public class ProgressionGeneratorTest {
	private static ProgressionGenerator generator;
	private static final int prog_length = 12;
	private static XMLReader reader;

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

		generator = new ProgressionGenerator(reader);
	}

	@Test
	public void testBuildProgression() {
		Chord start = reader.getUniqueChords().get(0);
		generator.buildProgression(start, prog_length);
		assertEquals(prog_length, generator.getProgression().size());
		assertEquals(start, generator.getProgression().get(0));
		//assertEquals(new Chord(1, Tonality.min, 4), generator.getProgression().get(1));

		System.out.println();
		for(Chord chord : generator.getProgression())
			System.out.print(chord.toString() + " ");
	}

	@Test
	public void testGetNextChord() {
		try {
			Chord next = generator.getNextChord(null, reader.getUniqueChords().get(0));
			//assertEquals(new Chord(1, Tonality.min, 4), next);
		}
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
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

		generator = new ProgressionGenerator(reader2);
		Chord start = reader.getUniqueChords().get(0);
		assertEquals(1, start.getRoot());

		generator.buildProgression(start, 16);
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
