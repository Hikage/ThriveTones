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
	private XMLReader xreader;

	/**
	 * Constructor method
	 */
	public ProgressionGenerator(XMLReader xr) {
		progression = new LinkedList<Chord>();
		xreader = xr;
	}

	/**
	 * Given a starting Chord, provides the next
	 * @param start: Chord with which to start
	 * @return: returns the next Chord
	 * @throws Exception
	 */
	public Chord getNextChord(Chord previous, Chord start) throws Exception{
		if(previous == null)
			return start.getChordPairing().getANextChord();
		else{
			ChordPair pair = new ChordPair(previous, start);
			ArrayList<ChordPair> unique_chord_pairs = xreader.getUniqueChordPairs();
			if(!unique_chord_pairs.contains(pair))
				throw new Exception("Pair should already exist within the unique list");
			pair = unique_chord_pairs.get(unique_chord_pairs.indexOf(pair));
			if(pair.getNextChords() == null)
				throw new Exception("Pair should already have an established next_chords list");
			else
				return pair.getNextChords().getANextChord();
		}
	}

	/**
	 * Builds a chord progression based on a supplied starting Chord
	 * @param current: Chord with which to start the progression
	 * @param progLength: length of desired progression
	 */
	public void buildProgression(Chord current, int prog_length){
		progression.add(current);

		Chord previous = null;
		for(int i = 1; i < prog_length; i++){
			Chord next;
			try {
				next = getNextChord(previous, current);
				progression.add(next);
				previous = current;
				current = next;
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
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
