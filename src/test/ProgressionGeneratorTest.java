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

import org.junit.BeforeClass;
import org.junit.Test;

import sax.XMLReader;
import thriveTones.Chord;
import thriveTones.Chord.Tonality;
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

		generator = new ProgressionGenerator();
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
		Chord next = generator.getNextChord(reader.getUniqueChords().get(0));
		//assertEquals(new Chord(1, Tonality.min, 4), next);
	}
}
