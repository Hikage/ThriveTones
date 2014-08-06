package markovChords;

/**
 * "Digital Chords" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * Chord.java
 * This class represents a chord object, having specified attributes like root, duration, and tonality
 */

public class Chord {

	public enum Tonality {maj, min, dim, aug};
	
	//TODO: bitmap chord representation?
	private int root;									//relative note upon which to build the chord (1-7, 1 = tonic)
	private Tonality tonality;
	private int octave = 5;								//defaults to middle - octave 5
	private int inversion = 0;
	private String embellishment = "";
	private int duration = 4;
	private boolean seven = false;
	
	/**
	 * Constructor method
	 * @param root: integer representation of Chord's root
	 * @param tone: tonality of the Chord
	 * @param oct: Chord's octave
	 * @param inv: Chord's inversion
	 * @param emb: Chord's embellishment
	 * @param dur: Chord's duration
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
		if(schord.length() < 1)
			throw new Exception("Empty chord supplied!");

		//Parse incoming string
		String[] chord_parts = schord.toLowerCase().split("/");
		int applied_target = 0;

		if(schord.contains("/")){		//has an applied target
			if(chord_parts.length != 2
					|| !Character.isDigit(chord_parts[1].charAt(0))	|| chord_parts[1].length() != 1)
				throw new Exception("Invalid applied target: " + schord);
			else
				applied_target = Integer.parseInt(chord_parts[1]);
		}

		if(schord.contains("-")){		//has duration
			chord_parts = chord_parts[0].split("-");
			if(chord_parts.length != 2 || !Character.isDigit(chord_parts[1].charAt(0))
					|| chord_parts[1].length() > 2 || chord_parts[1].length() < 1)
				throw new Exception("Invalid duration: " + schord);
			else
				duration = Integer.parseInt(chord_parts[1]);
		}

		String chord = chord_parts[0];

		if(chord.length() < 1)
			throw new Exception("Empty chord detected!");

		if(chord.contains("rest")){
			if(!chord.equals("rest"))
				throw new Exception("Invalid rest chord: " + schord);
			if(applied_target > 0)
				throw new Exception("Rests cannot have applied targets: " + schord);

			root = 0;
			tonality = Tonality.maj;		//just for the sake of not having a null attribute
			return;
		}

		int ptr = 0;

		//Chord's mode (minor, major, dorian, etc)
		char cmode;
		if(!Character.isDigit(chord.charAt(ptr))){
			cmode = chord.charAt(ptr);
			//TODO: mode
			ptr++;
		}

		//Chord numeral relative to key (I, ii, V, etc)
		int relative_chord = Character.getNumericValue(chord.charAt(ptr));

		switch(relative_chord){
		case 1: case 4: case 5: tonality = Tonality.maj; break;
		case 2: case 3: case 6: tonality = Tonality.min; break;
		case 7: tonality = Tonality.dim; break;
		default: throw new Exception("Invalid relative chord: " + relative_chord);
		}

		//Translate chord based on mode; major (1) has no offset, while minor (6) should have the iv chord translate to i
		relative_chord -= mode;
		if(relative_chord < 0) relative_chord += 7;
		root = relative_chord + 1;
		ptr++;

		//Extract and translate inversion
		int inv = 0;
		if(ptr < chord.length() && Character.isDigit(chord.charAt(ptr))){
			inv = Character.getNumericValue(chord.charAt(ptr));
			ptr++;
		}
		if(ptr < chord.length() && Character.isDigit(chord.charAt(ptr))){
			inv = inv * 10 + Character.getNumericValue(chord.charAt(ptr));
			ptr++;
		}
		switch(inv){
		case 7: seven = true;
		case 0: changeInversion(0); break;
		case 65: seven = true;
		case 6: changeInversion(1); break;
		case 43: seven = true;
		case 64: changeInversion(2); break;
		case 42: seven = true; changeInversion(3); break;
		default: throw new Exception("Invalid inversion: " + inv);
		}

		//Parse any embellishments
		String emb = "";
		if(chord.contains("add")){
			emb = chord.substring(chord.indexOf("add"));
			ptr = chord.length();
		}
		if(chord.contains("sus")){
			emb = chord.substring(chord.indexOf("sus"));
			ptr = chord.length();
		}
		//TODO: reenable support for sus42 if ever encountered
		if(!emb.equals("sus2") && !emb.equals("sus4") && !emb.equals("add9") && !emb.isEmpty())
			throw new Exception("Invalid embellishment: " + emb);
		embellishment = emb;

		if(ptr < chord.length())
			throw new Exception("Invalid chord! " + chord);

		if(chord_parts.length <= 1) return;

		//TODO: target
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
	public void changeOctave(int oct) throws Exception{
		if(oct < 0 || oct > 10)
			throw new Exception("Invalid octave (0-10): " + oct);
		octave = oct;
	}

	/**
	 * Adjust Chord's inversion
	 * @param inv: desired inversion
	 */
	public void changeInversion(int inv) throws Exception{
		if (inv < 0 || inv > 3)
			throw new Exception("Invalid inversion: " + inv);
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
	 * @return: the Chord's octave
	 */
	public int getOctave(){
		return octave;
	}
	
	/**
	 * Retrieves Chord's inversion
	 * @return: the Chord's inversion
	 */
	public int getInversion(){
		return inversion;
	}
	
	/**
	 * Retrieves Chord's embellishment
	 * @return: the Chord's embellishment
	 */
	public String getEmbellishment(){
		return embellishment;
	}

	/**
	 * Retrieves Chord's duration
	 * @return: the Chord's duration
	 */
	public int getDuration(){
		return duration;
	}

	/**
	 * Converts Chord into a key-independent string representation
	 * @return: the JFugue string representation of the Chord
	 */
	@Override
	public String toString(){
		return toString(-1);
	}

	/**
	 * Converts Chord into a string representation, taking key into account
	 * @return: the JFugue string representation of the Chord
	 */
	public String toString(int key){
		if(root == 0) return "R";	//rest

		String chord = "";
		if(key < 0) chord += root;
		else chord += (char)(int)('A' + ((key + root - 1) % 7));
		chord += octave;						//no need to specify JFugue's default 3, but doesn't hurt
		if(!embellishment.contains("sus"))		//tonality no longer means anything if the 3rd is dropped for sus
			chord += tonality;
		if(seven) chord += "7";
		for(int i = 0; i<inversion; i++) chord += "^";
		chord += embellishment;
		//TODO: implement duration
		chord += "w";

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
