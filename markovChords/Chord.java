package markovChords;

import java.util.HashMap;

public class Chord {

	private HashMap<String, Integer> notes;
	private String modifier;
	private int octave;								//can be + or -; defaults to middle
	
	public Chord(int relchord, String mod, int oct){
		int root = (relchord - 1) % 7;
		notes.put("root", root);
		
		int third = root + 4;
		switch (mod){
		case "major": third = root + 4; break;
		case "minor":
		case "diminished": third = root + 3; break;
		default: System.err.println("Invalid modifier: " + mod + " (valid exaples: major, diminished)"); System.exit(1);
		}
		notes.put("third", third);
		
		int fifth = root + 7;
		if(mod.equals("diminished")) fifth = root + 6;
		notes.put("fifth", fifth);
		
		modifier = mod;
		octave = oct;
	}
	
	public void makeMinor(){
		notes.put("third", notes.get("root") + 3);
		notes.put("fifth", notes.get("root") + 7);
	}
	
	public void makeDiminished(){
		notes.put("third", notes.get("root") + 3);
		notes.put("fifth", notes.get("root") + 6);
	}
	
	public void makeMajor(){
		notes.put("third", notes.get("root") + 4);
		notes.put("fifth", notes.get("root") + 7);
	}
	
	public void changeOctave(int oct){
		octave = oct;
	}
	
	public HashMap<String, Integer> getNotes(){
		return notes;
	}
	
	@Override
	public String toString(){
		String chord = "{";
		for(int note : notes.values())
			chord += note + " ";
		chord += "}";
		return chord;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((modifier == null) ? 0 : modifier.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + octave;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Chord other = (Chord) obj;
		if (modifier == null && other.modifier != null)
			return false;
		if (!modifier.equals(other.modifier))
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
