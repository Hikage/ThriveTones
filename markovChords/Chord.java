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

	public enum Tonality {MAJOR, MINOR, DIMINISHED, AUGMENTED};
	
	//TODO: bitmap chord representation?
	private HashMap<String, Integer> notes;			//stores actual notes of the chord
	private Tonality tonality;
	private int octave;								//can be + or -; defaults to middle
	
	/**
	 * Constructor method
	 * @param root: integer representation of relative note upon which to build the chord (0 = tonic)
	 * @param tone: tonality of the Chord
	 * @param oct: Chord's octave
	 */
	public Chord(int root, Tonality tone, int oct) throws Exception{
		notes = new HashMap<String, Integer>();
		
		if(root < 0 || root > 11)
			throw new Exception("Invalid note supplied as chord root (0 - 11): " + root);
		notes.put("root", root);
		
		switch(tone){
		case MAJOR: makeMajor(); break;
		case MINOR: makeMinor(); break;
		case DIMINISHED: makeDiminished(); break;
		case AUGMENTED: makeAugmented(); break;
		default: throw new Exception("Invalid tonality: " + tone + " (valid exaples: major, diminished)");
		}
		
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
	 * Adjusts Chord to be of major tonality
	 */
	public void setThirdFifth(int third_offset, int fifth_offset){
		notes.put("third", offset(notes.get("root"), third_offset));
		notes.put("fifth", offset(notes.get("root"), fifth_offset));
	}
	
	/**
	 * Adjusts Chord to be of major tonality
	 */
	public void makeMajor(){
		setThirdFifth(4, 7);
		tonality = Tonality.MAJOR;
	}
	
	/**
	 * Adjusts Chord to be of minor tonality
	 */
	public void makeMinor(){
		setThirdFifth(3, 7);
		tonality = Tonality.MINOR;
	}
	
	/**
	 * Adjusts Chord to be of diminished tonality
	 */
	public void makeDiminished(){
		setThirdFifth(3, 6);
		tonality = Tonality.DIMINISHED;
	}
	
	/**
	 * Adjusts Chord to be of augmented tonality
	 */
	public void makeAugmented(){
		setThirdFifth(4, 8);
		tonality = Tonality.AUGMENTED;
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
	public Tonality getTonality(){
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
