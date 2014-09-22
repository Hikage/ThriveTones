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
	private HashMap<LinkedList<Chord>, ArrayList<Chord>> dictionary;
	private Random random_generator;
	private ArrayList<Chord> all_chords;

	public ChordDictionary(){
		dictionary = new HashMap<LinkedList<Chord>, ArrayList<Chord>>();
		random_generator = new Random();
		all_chords = new ArrayList<Chord>();
	}

	public void put(LinkedList<Chord> sequence, Chord chord){
		ArrayList<Chord> available_chords = dictionary.get(sequence);
		if(available_chords == null)
			available_chords = new ArrayList<Chord>();
		available_chords.add(chord);
		dictionary.put(sequence, available_chords);

		//add entire history
		sequence.remove();
		if(sequence.size() > 0)
			put(sequence, chord);
		else							//add the zeroth history (overall chord frequencies)
			all_chords.add(chord);
	}

	public Chord getANextChord(LinkedList<Chord> sequence){
		//pull new random chord if the history doesn't yield anything
		if(sequence == null || sequence.size() < 1){
			int index = random_generator.nextInt(all_chords.size());
			return all_chords.get(0);
		}

		ArrayList<Chord> available_chords = dictionary.get(sequence);
		if(available_chords == null){
			sequence.remove();
			return getANextChord(sequence);
		}
		int index = random_generator.nextInt(available_chords.size());
		return available_chords.get(index);
	}

	public boolean contains(LinkedList<Chord> sequence){
		return dictionary.containsKey(sequence);
	}
}
