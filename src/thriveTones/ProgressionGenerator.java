package thriveTones;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ProgressionGenerator.java
 * Generates a chord progression based on established probabilities
 */

import java.util.ArrayList;
import java.util.LinkedList;

import sax.XMLReader;

public class ProgressionGenerator {
	private static LinkedList<Chord> progression;
	private ChordDictionary dictionary;

	/**
	 * Constructor method
	 */
	public ProgressionGenerator(ChordDictionary dict) {
		progression = new LinkedList<Chord>();
		dictionary = dict;
	}

	/**
	 * Builds a chord progression based on a supplied starting Chord
	 * @param current: Chord with which to start the progression
	 * @param progLength: length of desired progression
	 */
	public void buildProgression(Chord current, int prog_length, int hist_length){
		progression.add(current);
		LinkedList<Chord> history = new LinkedList<Chord>();

		for(int i = Math.max(0, progression.size() - hist_length); i < progression.size(); i++)
			history.add(progression.get(i));

		for(int i = 1; i < prog_length; i++)
			progression.add(dictionary.getANextChord(history));
	}

	/**
	 * progression accessor
	 * @return: the progression
	 */
	public LinkedList<Chord> getProgression(){
		return progression;
	}
}
