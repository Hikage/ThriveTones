package markovChords;

/**
 * "Digital Chords" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * Chord.java
 * TODO: details on this class
 */

import java.util.HashMap;

public class Chord {

	//TODO: bitmap chord representation?
	private HashMap<String, Integer> notes;			//stores actual notes of the chord
	private String tonality;						//e.g.: major, minor, diminished, etc.
	private int octave;								//can be + or -; defaults to middle
	
	/**
	 * Constructor method
	 * @param root: integer representation of relative note upon which to build the chord (0 = tonic)
	 * @param tone: tonality of the Chord
	 * @param oct: Chord's octave
	 */
	public Chord(int root, String tone, int oct){
		notes = new HashMap<String, Integer>();
		
		if(root < 0 || root > 10){
			System.err.println("Invalid note supplied as chord root (0 - 10): " + root);
			System.exit(1);
		}
		notes.put("root", root);
		
		int third = offset(root, 4);
		tone = tone.toLowerCase();
		if (tone.equals("minor") || tone.equals("diminished")) third = offset(root, 3);
		else if (tone.equals("major")) third = offset(root, 4);
		else{
			System.err.println("Invalid tonality: " + tone + " (valid exaples: major, diminished)");
			System.exit(1);
		}
		notes.put("third", third);
		
		int fifth = offset(root, 7);
		if(tone.equals("diminished")) fifth = offset(root, 6);
		notes.put("fifth", fifth);
		
		tonality = tone;
		octave = oct;
	}
	
	/**
	 * Helper function for note offsets
	 * @param base: note from which to offset
	 * @param degree: offset value
	 * @return: resulting note
	 */
	private int offset(int base, int degree){
		return (base + degree) % 12;
	}
	
	/**
	 * Adjusts Chord to be of minor tonality
	 */
	public void makeMinor(){
		notes.put("third", notes.get("root") + 3);
		notes.put("fifth", notes.get("root") + 7);
	}
	
	/**
	 * Adjusts Chord to be of diminished tonality
	 */
	public void makeDiminished(){
		notes.put("third", notes.get("root") + 3);
		notes.put("fifth", notes.get("root") + 6);
	}
	
	/**
	 * Adjusts Chord to be of major tonality
	 */
	public void makeMajor(){
		notes.put("third", notes.get("root") + 4);
		notes.put("fifth", notes.get("root") + 7);
	}
	
	/**
	 * Adjust Chord's octave
	 * @param oct: desired octave
	 */
	public void changeOctave(int oct){
		octave = oct;
	}
	
	/**
	 * Retrieves Chord's notes
	 * @return: the notes of the Chord
	 */
	public HashMap<String, Integer> getNotes(){
		return notes;
	}
	
	/**
	 * Retrieves Chord's tonality
	 * @return: the tonality of the Chord
	 */
	public String getTonality(){
		return tonality;
	}
	
	/**
	 * Retrieves Chord's octave
	 * @return: the notes of the octave
	 */
	public int getOctave(){
		return octave;
	}
	
	/**
	 * Converts Chord into a string representation
	 * @return: the string representation of the Chord
	 */
	@Override
	public String toString(){
		String chord = "{";
		for(int note : notes.values())
			chord += note + " ";
		chord += "}";
		return chord;
	}
	
	/**
	 * Automatically generated hashCode function for comparisons
	 * @return: a hash value representing the Chord object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tonality == null) ? 0 : tonality.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + octave;
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
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Chord other = (Chord) obj;
		if (tonality == null && other.tonality != null)
			return false;
		if (!tonality.equals(other.tonality))
			return false;
		if (notes == null && other.notes != null)
			return false;
		if (!notes.equals(other.notes))
			return false;
		if (octave != other.octave)
			return false;
		return true;
	}
}
