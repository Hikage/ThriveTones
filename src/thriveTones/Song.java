package thriveTones;

import java.util.LinkedList;
import java.util.ListIterator;

import org.jfugue.Pattern;
import org.jfugue.Player;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * Song.java
 * TODO details on this class
 */

public class Song {
	private String name;
	private String artist;
	private String part;
	private String key;
	private String rel_major;
	private int mode;
	private LinkedList<Chord> progression;
	private double beats;

	/**
	 * Constructor method
	 * @param nm: song name
	 * @param at: song artist
	 * @param pt: song part (chorus, verse, etc)
	 * @param ky: song key
	 * @param md: song mode (major, minor, dorian, etc)
	 * @param sif: chords in SIF format
	 * @param bim: beats in measure
	 * @throws IllegalArgumentException: throws if an invalid parameter is supplied
	 */
	public Song(String nm, String at, String pt, String ky, int md, String sif, double bim) throws Exception{
		if(nm.isEmpty() || nm.equals(""))
			throw new IllegalArgumentException("Invalid name value: " + nm);
		if(at.isEmpty() || at.equals(""))
			throw new IllegalArgumentException("Invalid artist value: " + at);
		if(pt.isEmpty() || pt.equals(""))
			throw new IllegalArgumentException("Invalid part value: " + pt);
		if(md < 0 || md > 7)
			throw new IllegalArgumentException("Invalid mode value: " + md);
		if(sif.isEmpty() || sif.equals(""))
			throw new IllegalArgumentException("Invalid SIF value: " + sif);
		if(bim < 0.25 || bim > 20)
			throw new IllegalArgumentException("Invalid BiM value: " + bim);
		
		name = nm;
		artist = at;
		part = pt;
		if(ky.isEmpty()) key = "C";
		else key = ky;
		mode = md;
		beats = bim;
		
		progression = new LinkedList<Chord>();
		
		String[] sif_chords = sif.split(",");
		for(String sif_chord : sif_chords){
			if(sif_chord.isEmpty()) continue;
                        Chord chord = new Chord(mode, sif_chord);
                        progression.add(chord);
		}

		calculateRelativeMajor();
	}
	
	/**
	 * Adjusts the song's key
	 * @param ky: new key
	 * @param md: new mode
	 */
	public void changeKey(String ky, int md) throws Exception{
		if(ky.isEmpty())
			throw new IllegalArgumentException("ky is empty!");
		key = ky;
		mode = md;
		calculateRelativeMajor();
	}

	/**
	 * Calculates the relative major, based on song key and mode
	 */
	private void calculateRelativeMajor(){
		String circle = "FCGDAEB";
		int offset;
		switch(mode){
		case 2: offset = -2; break;
		case 3: offset = -4; break;
		case 4: offset = 1; break;
		case 5: offset = -1; break;
		case 6: offset = -3; break;
		case 7: offset = -5; break;
		default: offset = 0;
		}

		rel_major = "";
		String accidental = "";
		if(key.length() > 1) accidental += key.charAt(1);
		int pos = circle.indexOf(key.toUpperCase().charAt(0));
		int new_pos = pos + offset;
		if(new_pos < 0){
			rel_major += circle.charAt(new_pos + 7);
			if(!accidental.equals("#")) rel_major += (accidental + "b");
		}
		else if(new_pos >= circle.length()){
			rel_major += circle.charAt(new_pos - 7);
			if(!accidental.equals("b")) rel_major += (accidental + "#");
		}
		else rel_major += (circle.charAt(new_pos) + accidental);
	}
	
	/**
	 * name accessor
	 * @return: song name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * artist accessor
	 * @return: song artist
	 */
	public String getArtist(){
		return artist;
	}
	
	/**
	 * part accessor
	 * @return: song part
	 */
	public String getPart(){
		return part;
	}
	
	/**
	 * key accessor
	 * @return: song key
	 */
	public String getKey(){
		return key;
	}
	
	/**
	 * relative major accessor
	 * @return: relative major
	 */
	public String getRelMajor(){
		return rel_major;
	}
	
	/**
	 * mode accessor
	 * @return: song mode
	 */
	public int getMode(){
		return mode;
	}
	
	/**
	 * progression accessor
	 * @return: chord progression
	 */
	public LinkedList<Chord> getChords(){
		return progression;
	}
	
	/**
	 * beats accessor
	 * @return: beats in measure
	 */
	public double getBeats(){
		return beats;
	}
	
	/**
	 * Converts the song into a string representation
	 * @return: the string representation of the song
	 */
	@Override
	public String toString(){
		String playable_chords = "K" + rel_major + "maj ";
		ListIterator<Chord> it = progression.listIterator();
		while(it.hasNext())
			//TODO: cmode
			//TODO: applied targets
			playable_chords += it.next().toString(key.charAt(0) - 'A', beats) + " ";
		return playable_chords.trim();
	}

	/**
	 * Plays the song
	 */
	public void play(){
		int TEMPO = 120;
		String INSTRUMENT = "Piano";

		String playable_song = "T" + TEMPO + " I[" + INSTRUMENT + "] " + this.toString();

		System.out.println(playable_song);
		Pattern pattern = new Pattern();
		pattern.setMusicString(playable_song);
		Player player = new Player();
		player.play(pattern);
	}

	/**
	 * Automatically generated hashCode function for comparisons
	 * @return: a hash value representing the Chord object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		long temp;
		temp = Double.doubleToLongBits(beats);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + mode;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((part == null) ? 0 : part.hashCode());
		result = prime * result
				+ ((progression == null) ? 0 : progression.hashCode());
		result = prime * result
				+ ((rel_major == null) ? 0 : rel_major.hashCode());
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
		Song other = (Song) obj;
		if (artist == null)
			if (other.artist != null)
				return false;
		else if (!artist.equals(other.artist))
			return false;
		if (Double.doubleToLongBits(beats) != Double
				.doubleToLongBits(other.beats))
			return false;
		if (key == null)
			if (other.key != null)
				return false;
		else if (!key.equals(other.key))
			return false;
		if (mode != other.mode)
			return false;
		if (name == null)
			if (other.name != null)
				return false;
		else if (!name.equals(other.name))
			return false;
		if (part == null)
			if (other.part != null)
				return false;
		else if (!part.equals(other.part))
			return false;
		if (progression == null)
			if (other.progression != null)
				return false;
		else if (!progression.equals(other.progression))
			return false;
		if (rel_major == null)
			if (other.rel_major != null)
				return false;
		else if (!rel_major.equals(other.rel_major))
			return false;
		return true;
	}
	
}
