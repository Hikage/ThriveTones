package thriveTones;
import java.util.LinkedList;
import java.util.List;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ProgressionGenerator.java
 * Generates a chord progression based on established probabilities
 */

public class ProgressionGenerator {
	private static LinkedList<Chord> progression;
	private ChordDictionary chord_dictionary;

	/**
	 * Constructor method
	 * @param dict : ChordDictionary to be used
	 */
	public ProgressionGenerator(ChordDictionary dict) {
		chord_dictionary = dict;
	}

	/**
	 * Builds a chord progression based on a supplied starting set of Chords
	 * @param start : Chord sequence with which to start the progression
	 * @param prog_length : length of desired progression
	 * @param hist_length : length of the history to use
	 */
	public void buildProgression(List<Chord> start, int prog_length, int hist_length){
		progression = new LinkedList<Chord>();
		progression.addAll(start);

		int max_hist_length = chord_dictionary.getMaxHistoryLength();
		if(hist_length > max_hist_length)
			hist_length = max_hist_length;

		LinkedList<Chord> history = new LinkedList<Chord>();
		if(hist_length >= 1)
			history.addAll(start.subList(Math.max(0, start.size()-hist_length), start.size()));

		for(int i = 1; i < prog_length; i++){
			Chord next = chord_dictionary.getANextChord(history);
			progression.add(next);
			history.add(next);
			if(history.size() > hist_length) history.remove();
		}
	}

	/**
	 * progression accessor
	 * @return : the progression
	 */
	public LinkedList<Chord> getProgression(){
		return progression;
	}
}
