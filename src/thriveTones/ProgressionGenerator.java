package thriveTones;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ProgressionGenerator.java
 * Generates a chord progression based on established probabilities
 */

import static org.junit.Assert.fail;

import java.util.LinkedList;

import sax.XMLReader;
import thriveTones.Chord.Tonality;

public class ProgressionGenerator {
	private static LinkedList<Chord> progression;

	/**
	 * Constructor method
	 * @param filename: file from which to read the data
	 */
	public ProgressionGenerator(String filename) {
		progression = new LinkedList<Chord>();
		Chord start = new Chord(1, Tonality.maj, 4);
		XMLReader reader = new XMLReader();
		try {
			reader.readIn(filename);
			//calculate progression probabilities
			//buildProgression(start, 4);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Given a starting Chord, provides the next
	 * @param start: Chord with which to start
	 * @return: returns the next Chord
	 */
	public Chord getNextChord(Chord start){
		return new Chord(1, Tonality.min, 4);
	}

	/**
	 * Builds a chord progression based on a supplied starting Chord
	 * @param start: Chord with which to start the progression
	 * @param progLength: length of desired progression
	 */
	public void buildProgression(Chord start, int progLength){
		progression.add(start);
		for(int i = 1; i < progLength; i++){
			Chord next = getNextChord(start);
			progression.add(next);
			start = next;
		}
	}

	/**
	 * progression accessor
	 * @return: the progression
	 */
	public LinkedList<Chord> getProgression(){
		return progression;
	}
}
