package thriveTones;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * ChordDictionary.java
 * This class represents a chord dictionary object, storing unique Chords and chord histories up to 4 Chords
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class ChordDictionary extends HashMap<LinkedList<Chord>, ArrayList<Chord>>{
	private Random random_generator;

	public ChordDictionary(){
		random_generator = new Random();
	}

	public void put(LinkedList<Chord> sequence, Chord chord){
		LinkedList<Chord> key = new LinkedList<Chord>();
		if(sequence != null) key.addAll(sequence);					//make local copy to avoid it changing from under us

		ArrayList<Chord> available_chords = this.get(key);
		if(available_chords == null)
			available_chords = new ArrayList<Chord>();
		available_chords.add(chord);								//available chords might change, but put will overwrite anyway

		this.put(key, available_chords);

		//add entire history
		if(!key.isEmpty()){
			LinkedList<Chord> seq = new LinkedList<Chord>();
			if(key.size() > 1) seq.addAll(key.subList(1, key.size()));
			put(seq, chord);
		}
	}

	public Chord getANextChord(List<Chord> sequence) throws Exception{
		if(sequence == null)
			sequence = new LinkedList<Chord>();
		ArrayList<Chord> available_chords = this.get(sequence);
		if(available_chords == null){
			// history had nothing
			assert(sequence.size() > 0);
			// shorten the history by one and try again
			return getANextChord(sequence.subList(1, sequence.size()));
		}
		//pull new random chord from the history choices
		int index = random_generator.nextInt(available_chords.size());
		System.out.println("Index: " + index);
		System.out.println("Chord: " + available_chords.get(index).toString());
		return available_chords.get(index);
	}
}
