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

import thriveTones.Chord;
import thriveTones.Chord.Tonality;
import thriveTones.ProgressionGenerator;

public class ProgressionGeneratorTest {
	private static ProgressionGenerator generator;

	@BeforeClass
	public static void init(){
		generator = new ProgressionGenerator("Hooktheory-Data.xml");
	}

	@Test
	public void testBuildProgression() {
		generator.buildProgression(new Chord(1, Tonality.maj, 4), 4);
		assertEquals(4, generator.getProgression().size());
		assertEquals(new Chord(1, Tonality.maj, 4), generator.getProgression().get(0));
		assertEquals(new Chord(1, Tonality.min, 4), generator.getProgression().get(1));
	}

	@Test
	public void testGetNextChord() {
		Chord next = generator.getNextChord(new Chord(1, Tonality.maj, 4));
		assertEquals(new Chord(1, Tonality.min, 4), next);
	}
}
