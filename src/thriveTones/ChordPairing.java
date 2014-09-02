package thriveTones;

import java.util.ArrayList;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ChordPairing.java
 * Keeps track of possible chord pairings, along with their frequency
 * to calculate probabilities
 */

public class ChordPairing {
	private ArrayList<Chord> next_chords;
	private ArrayList<Integer> indices;
	
	public ChordPairing(Chord next){
		next_chords = new ArrayList<Chord>();
		indices = new ArrayList<Integer>();
		addChord(next);
	}
	
	public void addChord(Chord next){
		int ind = next_chords.indexOf(next);
		if(ind < 0){
			next_chords.add(next);
			indices.add(next_chords.size() - 1);
		}
		else indices.add(ind);
	}
	
	public ArrayList<Chord> getNextChords(){
		return next_chords;
	}
	
	public ArrayList<Integer> getIndices(){
		return indices;
	}
}
