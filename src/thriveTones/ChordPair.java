package thriveTones;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ChordPair.java
 * This class represents a chord pair object, consisting of an order-dependent first and second chord pair
 */

public class ChordPair {
	private Chord first;
	private Chord second;
	private ChordPairing next_chords;

	/**
	 * Constructor
	 * @param f: first Chord of the pair
	 * @param s: second Chord of the pair
	 */
	public ChordPair(Chord f, Chord s){
		first = f;
		second = s;
		next_chords = new ChordPairing();
	}

	/**
	 * Add a linked Chord for use in progressions
	 * @param next: a Chord that succeeds the current Chord
	 */
	public void addNextChord(Chord next){
		next_chords.addChord(next);
	}

	/**
	 * Accessor
	 * @return: the first Chord of the pair
	 */
	public Chord first(){
		return first;
	}

	/**
	 * Accessor
	 * @return: the second Chord of the pair
	 */
	public Chord second(){
		return second;
	}

	/**
	 * Retrieves ChordPair's possible next Chords
	 * @return: the list of next possible Chords
	 */
	public ChordPairing getNextChords(){
		return next_chords;
	}

	/**
	 * Automatically generated hashCode function for comparisons
	 * @return: a hash value representing the ChordPair object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	/**
	 * Automatically generated equals function for comparisons
	 * @return: returns true iff obj = this
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChordPair other = (ChordPair) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

}
