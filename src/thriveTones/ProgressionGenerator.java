package thriveTones;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ProgressionGenerator.java
 * Generates a chord progression based on established probabilities
 */

import java.util.LinkedList;

public class ProgressionGenerator {
	private static LinkedList<Chord> progression;

	/**
	 * Constructor method
	 */
	public ProgressionGenerator() {
		progression = new LinkedList<Chord>();
	}

	/**
	 * Given a starting Chord, provides the next
	 * @param start: Chord with which to start
	 * @return: returns the next Chord
	 */
	public Chord getNextChord(Chord start){
		return start.getNextChords().getANextChord();
	}

	/**
	 * Builds a chord progression based on a supplied starting Chord
	 * @param start: Chord with which to start the progression
	 * @param progLength: length of desired progression
	 */
	public void buildProgression(Chord start, int prog_length){
		progression.add(start);
		for(int i = 1; i < prog_length; i++){
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
