package test;
import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import thriveTones.Chord;
import thriveTones.Chord.Tonality;
import thriveTones.ChordDictionary;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ChordDictionaryTest.java
 * Tests the ChordDictionary class
 */

public class ChordDictionaryTest {
	private static ChordDictionary chord_dictionary;
	private static final boolean debug = false;

	/**
	 * Ensures a new ChordDictionary object is established
	 */
	@BeforeClass
	public static void chordDictionaryInit(){
		chord_dictionary = new ChordDictionary();
	}

	/**
	 * Clears out any existing ChordDictionary object
	 */
	@Before
	public void resetDictionary(){
		chord_dictionary.clear();
	}

	/**
	 * Tests initialization
	 */
	@Test
	public void testInit() {
		assertFalse(chord_dictionary == null);
	}

	/**
	 * Tests single-chord creation
	 */
	@Test
	public void testPutSingleChord(){
		Chord chord = new Chord(1, Tonality.maj, 4);
		chord_dictionary.put(null, chord);
		assertEquals(chord, chord_dictionary.getANextChord(null, debug));
	}

	/**
	 * Tests expected progression probabilities
	 */
	@Test
	public void testChordProbabilities(){
		Chord chord1 = new Chord(1, Tonality.maj, 4);
		Chord chord2 = new Chord(5, Tonality.maj, 4);
		chord_dictionary.put(null, chord1);
		chord_dictionary.put(null, chord1);
		assertEquals(chord1, chord_dictionary.getANextChord(null, debug));
		assertEquals(2, chord_dictionary.get(new LinkedList<Chord>()).size());

		chord_dictionary.put(null, chord2);

		int frequency = 0;
		for(int i = 0; i < 100; i++){
			Chord next = chord_dictionary.getANextChord(null, debug);
			if(next.getRoot() == 1) frequency++;
		}
		System.out.println("Chord frequency: " + frequency);
		assertTrue(frequency > 55);
	}

	/**
	 * Tests simple sequence
	 */
	@Test
	public void testSingleChordSequence(){
		Chord chord1 = new Chord(1, Tonality.maj, 4);
		Chord chord2 = new Chord(4, Tonality.maj, 4);
		Chord chord3 = new Chord(5, Tonality.maj, 4);

		chord_dictionary.put(null, chord1);

		LinkedList<Chord> sequence = new LinkedList<Chord>();
		sequence.add(chord1);
		chord_dictionary.put(sequence, chord2);
		chord_dictionary.put(sequence, chord2);
		chord_dictionary.put(sequence, chord3);
		chord_dictionary.put(sequence, chord3);

		assertEquals(4, chord_dictionary.get(sequence).size());
		assertEquals(2, chord_dictionary.size());

		int frequency = 0;
		for(int i = 0; i < 100; i++){
			Chord next = chord_dictionary.getANextChord(sequence, debug);
			if(next.getRoot() == 4) frequency++;
		}
		System.out.println("50/50 sequence frequency: " + frequency);
		assertTrue(frequency >= 40 && frequency <= 60);
	}

	/**
	 * Tests more complicated chord progression probabilities
	 */
	@Test
	public void testSequenceChordProbabilities(){
		Chord chord1 = new Chord(1, Tonality.maj, 4);
		Chord chord2 = new Chord(4, Tonality.maj, 4);
		Chord chord3 = new Chord(5, Tonality.maj, 4);

		chord_dictionary.put(null, chord1);

		LinkedList<Chord> sequence1 = new LinkedList<Chord>();
		sequence1.add(chord1);
		chord_dictionary.put(sequence1, chord2);
		sequence1.add(chord2);
		chord_dictionary.put(sequence1, chord3);

		LinkedList<Chord> sequence2 = new LinkedList<Chord>();
		sequence2.add(chord1);
		chord_dictionary.put(sequence2, chord2);
		sequence2.add(chord2);
		chord_dictionary.put(sequence2, chord1);
		chord_dictionary.put(sequence2, chord1);

		assertEquals(3, chord_dictionary.get(sequence1).size());
		assertEquals(4, chord_dictionary.size());
		assertEquals(chord_dictionary.get(sequence1), chord_dictionary.get(sequence2));

		int frequency = 0;
		for(int i = 0; i < 100; i++){
			Chord next = chord_dictionary.getANextChord(sequence1, debug);
			if(next.getRoot() == 1) frequency++;
		}
		System.out.println("Sequence frequency: " + frequency);
		assertTrue(frequency > 55);
	}

	/**
	 * Tests overloaded put()
	 */
	@Test
	public void testPutNextChord(){
		Chord chord1 = new Chord(1, Tonality.maj, 4);
		chord_dictionary.put(null, chord1);					//in practice, this will be added before it becomes part of a sequence

		LinkedList<Chord> sequence = new LinkedList<Chord>();
		sequence.add(chord1);

		Chord chord2 = new Chord(4, Tonality.maj, 4);

		chord_dictionary.put(sequence, chord2);
		assertTrue(chord_dictionary.containsKey(sequence));
		assertEquals(2, chord_dictionary.get(new LinkedList<Chord>()).size());
		assertEquals(chord1, chord_dictionary.get(new LinkedList<Chord>()).get(0));
		assertEquals(chord2, chord_dictionary.get(new LinkedList<Chord>()).get(1));

		assertEquals(chord2, chord_dictionary.get(sequence).get(0));
		assertEquals(chord2, chord_dictionary.getANextChord(sequence, debug));
	}

	/**
	 * Tests overloaded put() with given sequence
	 */
	@Test
	public void testPutNextChordSequence(){
		Chord chord1 = new Chord(1, Tonality.maj, 4);
		chord_dictionary.put(null, chord1);

		LinkedList<Chord> sequence = new LinkedList<Chord>();
		sequence.add(chord1);

		Chord chord2 = new Chord(4, Tonality.maj, 4);
		chord_dictionary.put(sequence, chord2);
		assertTrue(chord_dictionary.containsKey(sequence));

		sequence.add(chord2);

		Chord chord3 = new Chord(5, Tonality.maj, 4);

		chord_dictionary.put(sequence, chord3);
		assertTrue(chord_dictionary.containsKey(sequence));
		assertEquals(3, chord_dictionary.get(new LinkedList<Chord>()).size());
		assertEquals(chord1, chord_dictionary.get(new LinkedList<Chord>()).get(0));
		assertEquals(chord2, chord_dictionary.get(new LinkedList<Chord>()).get(1));
		assertEquals(chord3, chord_dictionary.get(new LinkedList<Chord>()).get(2));
		assertEquals(chord3, chord_dictionary.getANextChord(sequence, debug));

		sequence.remove();
		assertTrue(chord_dictionary.containsKey(sequence));
		assertEquals(chord3, chord_dictionary.getANextChord(sequence, debug));
	}

	/**
	 * Tests getANextChord()
	 */
	@Test
	public void testGetANextChord(){
		Chord chord1 = new Chord(1, Tonality.maj, 4);
		Chord chord2 = new Chord(4, Tonality.maj, 4);
		Chord chord3 = new Chord(5, Tonality.maj, 4);
		LinkedList<Chord> sequence = new LinkedList<Chord>();

		chord_dictionary.put(null, chord1);

		sequence.add(chord1);
		chord_dictionary.put(sequence, chord2);

		sequence.add(chord2);
		chord_dictionary.put(sequence, chord3);

		sequence.add(chord3);

		assertNotNull(chord_dictionary.getANextChord(sequence, debug));
		sequence.remove(sequence.size()-1);
		assertEquals(chord3, chord_dictionary.getANextChord(sequence, debug));
		sequence.remove(sequence.size()-1);
		assertEquals(chord2, chord_dictionary.getANextChord(sequence, debug));
		sequence.remove(sequence.size()-1);
		assertNotNull(chord_dictionary.getANextChord(sequence, debug));
	}

	/**
	 * Tests behavior with non-present key
	 */
	@Test
	public void testKeyNotPresent(){
		Chord chord1 = new Chord(1, Tonality.maj, 4);
		Chord chord2 = new Chord(4, Tonality.maj, 4);
		Chord chord3 = new Chord(5, Tonality.maj, 4);
		LinkedList<Chord> sequence1 = new LinkedList<Chord>();

		chord_dictionary.put(null, chord1);
		sequence1.add(chord1);
		chord_dictionary.put(sequence1, chord2);
		sequence1.add(chord2);
		chord_dictionary.put(sequence1, chord3);
		sequence1.add(chord3);

		LinkedList<Chord> sequence2 = new LinkedList<Chord>();
		sequence2.add(chord3);
		sequence2.add(chord2);
		sequence2.add(chord1);

		LinkedList<Chord> sequence3 = new LinkedList<Chord>();
		sequence3.add(chord3);

		assertEquals(chord2, chord_dictionary.getANextChord(sequence2, debug));

		int[] roots = new int[8];
		for(int i = 0; i < 100; i++){
			Chord next;
			next = chord_dictionary.getANextChord(null, debug);
			roots[next.getRoot()]++;
		}
		assertTrue(roots[1] > 20);
		assertTrue(roots[4] > 20);
		assertTrue(roots[5] > 20);
	}

	/**
	 * Tests repetitive chords
	 */
	@Test
	public void testRepetitiveChords(){
		Chord chord = new Chord(1, Tonality.maj, 4);
		LinkedList<Chord> sequence = new LinkedList<Chord>();

		chord_dictionary.put(null, chord);				//first instance
		sequence.add(chord);
		chord_dictionary.put(sequence, chord);			//second instance
		sequence.add(chord);
		chord_dictionary.put(sequence, chord);			//third instance
		sequence.add(chord);
		int pre_dict_size = chord_dictionary.size();
		chord_dictionary.put(sequence, chord);			//fourth instance - shouldn't be added!

		assertEquals(pre_dict_size, chord_dictionary.size());

		Chord chord2 = new Chord(5, Tonality.maj, 4);
		chord_dictionary.put(sequence, chord2);			//perfectly legal
		assertEquals(pre_dict_size + 1, chord_dictionary.size());
	}
}
