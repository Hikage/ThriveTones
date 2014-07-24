package markovChords;

/**
 * "Digital Chords" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * Chord.java
 * TODO: details on this class
 */

public class Chord {

	public enum Tonality {maj, min, dim, aug};
	
	//TODO: bitmap chord representation?
	private int root;
	private Tonality tonality;
	private int octave = 5;								//defaults to middle - octave 5
	private int inversion = 0;
	private String embellishment = "";
	private int duration = 4;
	
	/**
	 * Constructor method
	 * @param root: integer representation of relative note upon which to build the chord (0 = tonic)
	 * @param tone: tonality of the Chord
	 * @param oct: Chord's octave
	 */
	public Chord(int rt, Tonality tone, int oct, int inv, String emb, int dur) throws Exception{		
		if(rt-1 < 0 || rt-1 > 7)
			throw new Exception("Invalid root (1-7): " + rt);
		root = rt;
		
		tonality = tone;
		octave = oct;

		changeInversion(inv);

		if(!emb.contains("add") && !emb.contains("sus") && !emb.isEmpty() && emb != null)
			throw new Exception("Invalid embelishment: " + emb);
		embellishment = emb;

		duration = dur;
	}

	/**
	 * Simplified Chord constructor, leaving other qualities default
	 * @param rt: integer representation of relative note upon which to build the chord (1 = tonic)
	 * @param tone: tonality of the Chord
	 * @param dur: Chord's duration
	 */
	public Chord(int rt, Tonality tone, int dur){
		root = rt;
		tonality = tone;
		duration = dur;
	}

	/**
	 * Constructor to take in Hooktheory-format chord
	 * @param mode: key's mode (minor, dorian, major, etc)
	 * @param schord: string representation of the chord
	 * @throws Exception
	 */
	public Chord(int mode, String schord) throws Exception{
		String[] chord_parts = schord.split("-");
		String chord = chord_parts[0];

		char cmode;
		int relative_chord;

		int ptr = 0;
		if(!Character.isDigit(chord.charAt(ptr))){
			cmode = chord.charAt(ptr);
			//TODO: mode
			ptr++;
		}

		relative_chord = chord.charAt(ptr);
		Tonality tone;
		switch(relative_chord){
		case 1: case 4: case 5: tone = Tonality.maj; break;
		case 2: case 3: case 6: tone = Tonality.min; break;
		case 7: tone = Tonality.dim; break;
		default: throw new Exception("Invalid relative chord: " + relative_chord);
		}

		relative_chord -= mode;
		if(relative_chord < 0) relative_chord += 7;
		ptr++;

		int inv = 0;
		if(ptr < chord.length() && Character.isDigit(chord.charAt(ptr))){
			inv = chord.charAt(ptr);
			ptr++;
		}
		if(ptr < chord.length() && Character.isDigit(chord.charAt(ptr))){
			inv = inv * 10 + chord.charAt(ptr);
			ptr++;
		}
		switch(inv){
		case 7: inv = 0; break;
		case 6: case 65: inv = 1; break;
		case 64: case 43: inv = 2; break;
		case 42: inv = 3; break;
		default: throw new Exception("Invalid inversion: " + inv);
		}

		String emb = "";
		if(chord.contains("add")){
			emb = chord.substring(chord.indexOf("add"));
			ptr = chord.length();
		}
		if(chord.contains("sus")){
			emb = chord.substring(chord.indexOf("sus"));
			ptr = chord.length();
		}

		if(ptr < chord.length())
			throw new Exception("Invalid chord! " + chord);

		if(chord_parts.length <= 1) return;

		String[] duration_target = chord_parts[1].split("/");
		int dur = Integer.parseInt(duration_target[0]);
		if(duration_target.length <= 1) return;
		int target = Integer.parseInt(duration_target[1]);
		//TODO: target

		new Chord(relative_chord, tone, 0, inv, emb, dur);
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
	public void makeMajor(){
		tonality = Tonality.maj;
	}

	/**
	 * Adjusts Chord to be of minor tonality
	 */
	public void makeMinor(){
		tonality = Tonality.min;
	}

	/**
	 * Adjusts Chord to be of diminished tonality
	 */
	public void makeDiminished(){
		tonality = Tonality.dim;
	}

	/**
	 * Adjusts Chord to be of augmented tonality
	 */
	public void makeAugmented(){
		tonality = Tonality.aug;
	}

	/**
	 * Adjust Chord's octave
	 * @param oct: desired octave
	 */
	public void changeOctave(int oct){
		octave = oct;
	}

	/**
	 * Adjust Chord's inversion
	 * @param inv: desired inversion
	 */
	public void changeInversion(int inv) throws Exception{
		if (inv < 0 || inv > 3) throw new Exception("Invalid inversion: " + inv);
		inversion = inv;
	}

	/**
	 * Retrieves Chord's root
	 * @return: the root of the Chord
	 */
	public int getRoot(){
		return root;
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
		int octave_offset = octave * 12;
		
		String chord = "";
		chord += root;
		chord += tonality;
		chord += "w+";

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
		result = prime * result + duration;
		result = prime * result
				+ ((embellishment == null) ? 0 : embellishment.hashCode());
		result = prime * result + inversion;
		result = prime * result + octave;
		result = prime * result + root;
		result = prime * result
				+ ((tonality == null) ? 0 : tonality.hashCode());
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
		if (duration != other.duration)
			return false;
		if (embellishment == null) {
			if (other.embellishment != null)
				return false;
		}
		else if (!embellishment.equals(other.embellishment))
			return false;
		if (inversion != other.inversion || octave != other.octave
				|| root != other.root || tonality != other.tonality)
			return false;
		return true;
	}

}
