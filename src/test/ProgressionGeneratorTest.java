package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

import sax.XMLReader;
import thriveTones.Chord;
import thriveTones.Chord.Tonality;
import thriveTones.ChordDictionary;
import thriveTones.ProgressionGenerator;
import thriveTones.SongSegment.SongPart;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ProgressionGeneratorTest.java
 * Tests the ProgressionGenerator class
 */

public class ProgressionGeneratorTest {
	private static ProgressionGenerator generator;
	private static final int prog_length = 12;
	private static final int hist_length = 3;
	private static XMLReader reader;
	private static ChordDictionary chord_dictionary;

	/**
	 * Reads in data for tests
	 */
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

		chord_dictionary = reader.getChordDictionary(SongPart.chorus);
		generator = new ProgressionGenerator(chord_dictionary);
	}

	/**
	 * Tests getting the next Chord for the progression
	 */
	@Test
	public void testGetNextChord() {
		int[] roots = new int[8];
		for(int i = 0; i < 100; i++){
			Chord next = chord_dictionary.getANextChord(null);
			roots[next.getRoot()]++;
		}
		for(int count : roots)
			System.out.println("Root count: " + count + "/" + chord_dictionary.get(new LinkedList<Chord>()).size());
		assertTrue(roots[1] >= 15);
		assertTrue(roots[1] >= roots[5]);
		assertTrue(roots[5] >= roots[2]);
	}

	/**
	 * Tests buildProgression()
	 */
	@Test
	public void testBuildProgression() {
		Chord start = chord_dictionary.getANextChord(null);
		LinkedList<Chord> start_sequence = new LinkedList<Chord>();
		start_sequence.add(start);
		generator.buildProgression(start_sequence, prog_length, hist_length);
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

	/**
	 * Tests a controlled progression generation
	 */
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

		chord_dictionary = reader2.getChordDictionary(SongPart.introverse);
		assertNotNull(chord_dictionary);
		assertFalse(chord_dictionary.isEmpty());
		System.out.println("Dictionary:\n" + chord_dictionary.toString() + "\n");

		generator = new ProgressionGenerator(chord_dictionary);
		Chord start = chord_dictionary.get(new LinkedList<Chord>()).get(0);
		assertEquals(1, start.getRoot());

		ArrayList<Chord> sequence = new ArrayList<Chord>();
		sequence.add(start);
		ArrayList<Chord> available_chords = chord_dictionary.get(sequence);

		boolean same = true;
		for(int i = 1; i < available_chords.size(); i++)
			same = (available_chords.get(i) == available_chords.get(i-1));
		assertFalse(same);

		generator.buildProgression(sequence, 16, 3);
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

	/**
	 * Tests creating controlled song parts
	 */
	@Test
	public void testControlledSongParts(){
		XMLReader reader3 = new XMLReader();
		try {
			reader3.readIn("test2.xml");
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		HashMap<SongPart, ChordDictionary> parts_dictionary = reader3.getPartsDictionary();
		assertNotNull(parts_dictionary);
		assertFalse(parts_dictionary.isEmpty());

		SongPart[] parts = {SongPart.chorus, SongPart.verse, SongPart.bridge, SongPart.outro, SongPart.solo, SongPart.introverse};
		int[] first_roots = {0, 1, 6, 3, 4, 1};
		for(int i = 0; i < parts.length; i++){
			SongPart part = parts[i];
			chord_dictionary = reader3.getChordDictionary(part);
			assertNotNull(chord_dictionary);
			assertFalse(chord_dictionary.isEmpty());

			generator = new ProgressionGenerator(chord_dictionary);
			Chord start = chord_dictionary.get(new LinkedList<Chord>()).get(0);
			assertEquals(first_roots[i], start.getRoot());

			LinkedList<Chord> start_sequence = new LinkedList<Chord>();
			start_sequence.add(start);
			generator.buildProgression(start_sequence, 16, 3);
			LinkedList<Chord> progression = generator.getProgression();
			System.out.print(part.toString() + ": ");
			for(Chord chord : progression)
				System.out.print(chord.toString() + " ");
			System.out.println();
		}
	}

	/**
	 * Tests a progression generation beginning with a sequence
	 */
	@Test
	public void testSequenceStartBuild(){
		XMLReader reader4 = new XMLReader();
		try {
			reader4.readIn("test.xml");
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		chord_dictionary = reader4.getChordDictionary(SongPart.introverse);
		assertNotNull(chord_dictionary);
		assertFalse(chord_dictionary.isEmpty());
		System.out.println("Dictionary:\n" + chord_dictionary.toString() + "\n");

		generator = new ProgressionGenerator(chord_dictionary);

		Chord chord1 = new Chord(6, Tonality.min, 1);
		Chord chord2 = new Chord(2, Tonality.min, 1);
		Chord chord3 = new Chord(1, Tonality.maj, 1);
		LinkedList<Chord> start_sequence = new LinkedList<Chord>();
		start_sequence.add(chord1);
		start_sequence.add(chord2);
		start_sequence.add(chord3);

		ArrayList<Chord> available_chords = chord_dictionary.get(start_sequence);
		assertTrue(available_chords == null);
		available_chords = chord_dictionary.get(start_sequence.subList(2, 3));
		assertFalse(available_chords.isEmpty());

		generator.buildProgression(start_sequence, 16, 3);
		LinkedList<Chord> progression = generator.getProgression();
		assertEquals(6, progression.get(0).getRoot());
		assertEquals(2, progression.get(1).getRoot());
		assertEquals(1, progression.get(2).getRoot());

		System.out.println();
		System.out.println(progression.toString());
	}
}
