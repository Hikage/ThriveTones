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
	private ChordDictionary chord_dictionary;

	/**
	 * Constructor method
	 */
	public ProgressionGenerator(ChordDictionary dict) {
		chord_dictionary = dict;
	}

	/**
	 * Builds a chord progression based on a supplied starting Chord
	 * @param start: Chord with which to start the progression
	 * @param prog_length: length of desired progression
	 * @param hist_length: length of the history to use
	 */
	public void buildProgression(Chord start, int prog_length, int hist_length){
		progression = new LinkedList<Chord>();
		progression.add(start);

		LinkedList<Chord> history = new LinkedList<Chord>();
		if(hist_length >= 1)
			history.add(start);

		for(int i = 1; i < prog_length; i++){
			try {
				System.out.print("History: ");
				for(Chord h : history)
					System.out.print(h.toString() + " ");
				System.out.println();
				Chord next = chord_dictionary.getANextChord(history);
				progression.add(next);
				history.add(next);
				if(history.size() > hist_length) history.remove();
			}
			catch (Exception e) {
				System.err.println("Something went wrong with building the progression: " + e.getMessage());
				e.printStackTrace();
				System.exit(0);
			}
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
