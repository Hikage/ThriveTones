package thriveTones;

import java.util.ArrayList;
import java.util.Random;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ChordPairing.java
 * Keeps track of possible Chord pairings for a given Chord, along
 * with their frequency to calculate probabilities
 */

public class ChordPairing {
	private Random random_generator = new Random();
	private ArrayList<Chord> next_chords;
	private ArrayList<Integer> indices;

	/**
	 * Constructor method
	 * @param next: Chord to be added as a possible next chord
	 */
	public ChordPairing(){
		next_chords = new ArrayList<Chord>();
		indices = new ArrayList<Integer>();
	}

	/**
	 * Adds a Chord to the list of possibilities
	 * If the Chord already exists, the index is added to the
	 * indices list without a new Chord being created.  Duplication
	 * is permitted to build an appropriately proportioned roulette
	 * for randomized selection.  If the Chord is new, it is added
	 * to the next_chords list, and the newly created index to the
	 * indices list
	 * @param next: Chord to add to the list
	 */
	public void addChord(Chord next){
		int ind = next_chords.indexOf(next);
		if(ind < 0){
			next_chords.add(next);
			indices.add(next_chords.size() - 1);
		}
		else indices.add(ind);
	}

	/**
	 * Using the roulette structure built during construction, this
	 * needs only select a random index from the indices list and
	 * retrieve the corresponding Chord from the next_chords list
	 * @return: the next chord selected from those available
	 * @throws Exception
	 */
	public Chord getANextChord() throws Exception{
		if(indices.size() < 1)
			throw new Exception("Indices is somehow unpopulated!");
		int rand = random_generator.nextInt(indices.size());
		return next_chords.get(indices.get(rand));
	}

	/**
	 * Accessor for next_chords
	 * @return: list of possible next chords
	 */
	public ArrayList<Chord> getNextChords(){
		return next_chords;
	}

	/**
	 * Accessor for indices
	 * @return: list of indices
	 */
	public ArrayList<Integer> getIndices(){
		return indices;
	}
}
