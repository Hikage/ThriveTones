package thriveTones;

import java.util.HashMap;
import java.util.LinkedList;
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
	private double beats;
	private HashMap<SongPart, ChordDictionary> parts_dictionary;

	/**
	 * Constructor method
	 * @param pt : song part (chorus, verse, etc)
	 * @param mode : song mode (major, minor, dorian, etc)
	 * @param sif : chords in SIF format
	 * @param bim : beats in measure
	 * @param dict : Chord dictionary
	 * @throws Exception : throws if an invalid parameter is supplied
	 */
	public SongSegment(String pt, int mode, String sif, double bim,
			HashMap<SongPart, ChordDictionary> dict) throws Exception{

		if(pt.isEmpty() || pt.equals(""))
			throw new IllegalArgumentException("Invalid part value: " + pt);
		if(mode < 0 || mode > 7)
			throw new IllegalArgumentException("Invalid mode value: " + mode);
		if(sif.isEmpty() || sif.equals(""))
			throw new IllegalArgumentException("Invalid SIF value: " + sif);
		if(bim < 0.25 || bim > 20)
			throw new IllegalArgumentException("Invalid BiM value: " + bim);

        part = partToEnum(pt);
        if(part == null)
            throw new IllegalArgumentException("Invalid song part: " + pt);

		beats = bim;
		parts_dictionary = dict;
		
		progression = new LinkedList<Chord>();
		
		String[] sif_chords = sif.split(",");
		LinkedList<Chord> sequence = new LinkedList<Chord>();
		for(String sif_chord : sif_chords){
			if(sif_chord.isEmpty()) continue;

			Chord current = new Chord(mode, sif_chord);
			progression.add(current);

			//add to dictionary
			if(!parts_dictionary.containsKey(part))
				parts_dictionary.put(part, new ChordDictionary());
			if(sequence.size() > parts_dictionary.get(part).getMaxHistoryLength())
				sequence.remove();
			parts_dictionary.get(part).put(sequence, current);
			sequence.add(current);
		}
	}
	
	/**
	 * Constructor method, meant for bot creation of a new song
	 * (as opposed to the read-in constructor above)
	 * @param pt : part to be created
	 * @param bim : beats per measure
	 * @param pg : SongSegment chord progression
	 */
	public SongSegment(SongPart pt, double bim, LinkedList<Chord> pg){
		part = pt;
		beats = bim;
		progression = pg;
		parts_dictionary = null;
	}

	/**
	 * Converts a string representation of song part to the standard enum
	 * @param pt : string representation of song part
	 * @return : the corresponding enum value
	 */
	public SongPart partToEnum(String pt){
        pt = pt.toLowerCase();
		if(pt.contains("instrumental") || pt.contains("solo"))
            return SongPart.solo;
        if(pt.contains("bridge"))
            return SongPart.bridge;
        if(pt.contains("outro"))
            return SongPart.outro;
        if(pt.contains("intro")){
            if(pt.contains("verse"))
                return SongPart.introverse;
            else
                return SongPart.intro;
        }
        if(pt.contains("verse")){
            if(pt.contains("pre-chorus"))
                return SongPart.verseprechorus;
            else
                return SongPart.verse;
        }
        if(pt.contains("pre-chorus")){
            if(pt.contains(" chorus"))
                return SongPart.prechoruschorus;
            else
                return SongPart.prechorus;
        }
        if(pt.contains("chorus"))
            return SongPart.chorus;
        return null;
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
	 * beats accessor
	 * @return : beats in measure
	 */
	public double getBeats(){
		return beats;
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
	 * @return : the string representation of the song
	 */
	public String toString(String key){
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
		long temp;
		temp = Double.doubleToLongBits(beats);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (Double.doubleToLongBits(beats) != Double
				.doubleToLongBits(other.beats))
			return false;
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
