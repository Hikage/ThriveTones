package thriveTones;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * SongSegment.java
 * This class represents a song part, containing metadata and a chord progression
 */

public class SongSegment {
	@SuppressWarnings("javadoc")
	public enum SongPart {chorus, verse, bridge, intro, introverse, solo, outro, prechorus, prechoruschorus, verseprechorus};
	private SongPart part;
	private LinkedList<Chord> progression;
	private ChordDictionary chord_dictionary;

	/**
	 * Constructor method
	 * @param pt : song part (chorus, verse, etc)
	 * @param mode : song mode (major, minor, dorian, etc)
	 * @param sif : chords in SIF format
	 * @param dict : Chord dictionary
	 * @throws Exception : throws if an invalid parameter is supplied
	 */
	public SongSegment(SongPart pt, int mode, String sif, ChordDictionary dict) throws Exception{
		if(pt == null)
			throw new IllegalArgumentException("Invalid part value: " + pt);
		if(mode < 0 || mode > 7)
			throw new IllegalArgumentException("Invalid mode value: " + mode);
		if(sif.isEmpty() || sif.equals(""))
			throw new IllegalArgumentException("Invalid SIF value: " + sif);
		if(dict == null)
			throw new IllegalArgumentException("Must supply valid chord dictionary");

        part = pt;
        if(part == null)
            throw new IllegalArgumentException("Invalid song part: " + pt);

		chord_dictionary = dict;
		
		progression = new LinkedList<Chord>();
		
		String[] sif_chords = sif.split(",");
		LinkedList<Chord> sequence = new LinkedList<Chord>();
		for(String sif_chord : sif_chords){
			if(sif_chord.isEmpty()) continue;

			Chord current = new Chord(mode, sif_chord);
			progression.add(current);

			//add to dictionary (only up to the maximum history length)
			if(sequence.size() > chord_dictionary.getMaxHistoryLength())
				sequence.remove();
			chord_dictionary.put(sequence, current);
			sequence.add(current);
		}
	}
	
	/**
	 * Constructor method, meant for bot creation of a new song segment
	 * (as opposed to the read-in constructor above)
	 * @param pt : part to be created
	 * @param dict : chord_dictionary for applicable SongPart
	 * @param seed : starting chord(s) for progression
	 * @param prog_length : length of desired progression
	 * @param hist_length : history length for progression
	 */
	public SongSegment(SongPart pt, ChordDictionary dict, List<Chord> seed, int prog_length, int hist_length){
		part = pt;
		chord_dictionary = dict;

		//allow for random start if no other seed is provided
		if(seed == null){
			Chord start = chord_dictionary.getANextChord(null);
			seed = new LinkedList<Chord>();
			seed.add(start);
		}

		ProgressionGenerator generator = new ProgressionGenerator(chord_dictionary);
		generator.buildProgression(seed, prog_length, hist_length);
		progression = generator.getProgression();
	}

	/**
	 * part accessor
	 * @return : song part
	 */
	public SongPart getPart(){
		return part;
	}
	
	/**
	 * progression accessor
	 * @return : chord progression
	 */
	public LinkedList<Chord> getChords(){
		return progression;
	}

	/**
	 * Converts the SongSegment into a string representation
	 * @return : the string representation of the SongSegment
	 */
	@Override
	public String toString(){
		String playable_chords = "";
		ListIterator<Chord> it = progression.listIterator();
		while(it.hasNext())
			//TODO: cmode
			//TODO: applied targets
			playable_chords += it.next().toString() + " ";
		return playable_chords.trim();
	}

	/**
	 * Converts the SongSegment into a string representation
	 * @param key : song key
	 * @param beats : beats per measure
	 * @return : the string representation of the song
	 */
	public String toString(String key, double beats){
		String playable_chords = "";
		ListIterator<Chord> it = progression.listIterator();
		while(it.hasNext())
			//TODO: cmode
			//TODO: applied targets
			playable_chords += it.next().toString(key, beats) + " ";
		return playable_chords.trim();
	}

	/**
	 * Automatically generated hashCode function for comparisons
	 * @return: a hash value representing the Chord object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((part == null) ? 0 : part.hashCode());
		result = prime * result
				+ ((progression == null) ? 0 : progression.hashCode());
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
		SongSegment other = (SongSegment) obj;
		if (part == null){
			if (other.part != null)
				return false;
		}
		else if (!part.equals(other.part))
			return false;
		if (progression == null){
			if (other.progression != null)
				return false;
		}
		else if (!progression.equals(other.progression))
			return false;
		return true;
	}
}
