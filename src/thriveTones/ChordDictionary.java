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
import java.util.Random;

@SuppressWarnings("serial")
public class ChordDictionary extends HashMap<LinkedList<Chord>, ArrayList<Chord>>{
	private Random random_generator;

	public ChordDictionary(){
		random_generator = new Random();
	}

	public void put(LinkedList<Chord> sequence, Chord chord){
		if(sequence == null)
			sequence = new LinkedList<Chord>();

		ArrayList<Chord> available_chords = this.get(sequence);
		if(available_chords == null)
			available_chords = new ArrayList<Chord>();
		available_chords.add(chord);

		this.put(sequence, available_chords);

		//add entire history
		if(!sequence.isEmpty()){
			LinkedList<Chord> seq = new LinkedList<Chord>();
			if(sequence.size() > 0) seq.addAll(sequence.subList(1, sequence.size()));
			put(seq, chord);
		}
	}

	public Chord getANextChord(LinkedList<Chord> sequence) throws Exception{
		if(sequence == null)
			sequence = new LinkedList<Chord>();

		ArrayList<Chord> available_chords = this.get(sequence);
		if(available_chords == null){
			if(sequence.isEmpty())
				throw new Exception("Sequence is empty and has no next chords!");
			LinkedList<Chord> seq = new LinkedList<Chord>();
			seq.addAll(sequence.subList(1, sequence.size()));
			return getANextChord(seq);
		}
		//pull new random chord if the history doesn't yield anything
		int index = random_generator.nextInt(available_chords.size());
		return available_chords.get(index);
	}
}
