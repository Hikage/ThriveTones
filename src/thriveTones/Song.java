package thriveTones;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import thriveTones.SongSegment.SongPart;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * Song.java
 * This class represents a song, comprising of a series of SongSegments
 */

public class Song {
	private HashMap<SongPart, SongSegment> segments = new HashMap<SongPart, SongSegment>();
	private LinkedList<SongSegment> song = new LinkedList<SongSegment>();
	private String name;
	private String artist;
	private int mode;
	private String key;
	private String rel_major;
	private double beats;

	/**
	 * Constructor method
	 * @param ky : key of the new Song
	 * @param md : Song mode
	 * @param nm : Song name
	 * @param at : Song artist
	 * @param bim : beats in measure
	 */
	public Song(String ky, int md, String nm, String at, double bim){
		if(ky == null)
			throw new IllegalArgumentException("Key cannot be null");
		key = standardizeKey(ky);
		if(md < 0 || md > 7)
			throw new IllegalArgumentException("Invalid mode value: " + md);
		mode = md;
		if(nm.isEmpty() || nm.equals(""))
			throw new IllegalArgumentException("Invalid name value: " + nm);
		if(at.isEmpty() || at.equals(""))
			throw new IllegalArgumentException("Invalid artist value: " + at);
		name = nm;
		artist = at;
		if(bim < 0.25 || bim > 20)
			throw new IllegalArgumentException("Invalid BiM value: " + bim);
		beats = bim;

		calculateRelativeMajor();
	}

	/**
	 * Standardizes a given key
	 * @param ky : key to be standardized
	 * @return : the standardized key string
	 */
	public String standardizeKey(String ky){
		String keys = "ABCDEFG";
		String standard_key = "";
		if(ky.isEmpty() || !keys.contains(Character.toString(ky.toUpperCase().charAt(0))))
			standard_key = "C";
		else{
			standard_key += ky.toUpperCase().charAt(0);

			String accidental = ky.substring(1).toLowerCase();
			for(int i = 0; i < accidental.length(); i++){
				char symbol = accidental.charAt(i);
				char acc;
				if(symbol == 'f' || symbol == 'b') acc = 'b';
				else if(symbol == 's' || symbol == '#') acc = '#';
				else break;

				if(i == 0)
					standard_key += acc;
				else if(acc == standard_key.charAt(standard_key.length()-1))
					standard_key += acc;
				else break;
			}
		}

		return standard_key;
	}

	/**
	 * Adjusts the song's key
	 * @param ky : new key
	 * @param md : new mode
	 * @throws Exception : if the provided key is invalid
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
	 * @param song_sequence : sequence of parts to be built
	 * @param parts_dictionary : dictionary for song building
	 * @param seg_length : length of each segment
	 * @param history : length of desired history
	 * @param debug : optional debug mode
	 */
	public void build(SongPart[] song_sequence,	HashMap<SongPart, ChordDictionary> parts_dictionary,
			int seg_length, int history, boolean debug){
		song = new LinkedList<SongSegment>();

		for(int i = 0; i < song_sequence.length; i++){
			//TODO: allow for multiple verses, etc
			SongPart part = song_sequence[i];
			List<Chord> history_seed = new LinkedList<Chord>();
			if(i != 0){
				List<Chord> previous_segment = song.get(i-1).getChords();
				history_seed = previous_segment.subList(Math.max(0, previous_segment.size() - history),	previous_segment.size());
			}

			SongSegment song_segment = segments.get(part);
			if(song_segment == null){
				song_segment = new SongSegment(part, parts_dictionary.get(part), history_seed, seg_length, history, debug);
				segments.put(part, song_segment);
			}
			song.add(song_segment);
		}
	}

	/**
	 * key accessor
	 * @return : Song key
	 */
	public String getKey(){
		return key;
	}

	/**
	 * relative major accessor
	 * @return : relative major
	 */
	public String getRelMajor(){
		return rel_major;
	}

	/**
	 * mode accessor
	 * @return : Song mode
	 */
	public int getMode(){
		return mode;
	}

	/**
	 * segments accessor
	 * @return : list of SongSegments associated with song
	 */
	public LinkedList<SongSegment> getSong(){
		return song;
	}

	/**
	 * name accessor
	 * @return : song name
	 */
	public String getName(){
		return name;
	}

	/**
	 * artist accessor
	 * @return : song artist
	 */
	public String getArtist(){
		return artist;
	}

	/**
	 * beats accessor
	 * @return : beats in measure
	 */
	public double getBeats(){
		return beats;
	}

	/**
	 * Returns the string representation of the Song
	 * @return : the string representation of the Song
	 */
	@Override
	public String toString(){
		return this.toString(false);
	}

	/**
	 * Returns the string representation of the Song
	 * @param label : whether SongSegment labels should be printed
	 * @return : the string representation of the Song
	 */
	public String toString(boolean label){
		String playable_segments = "K" + rel_major + "maj\n";
		ListIterator<SongSegment> it = song.listIterator();
		while(it.hasNext())
			playable_segments += it.next().toString(key, beats, label) + "\n";
		return playable_segments.trim();		
	}

	/**
	 * Tests if dependency is present
	 * @param className : dependency under test
	 * @return : if the dependency is installed
	 */
	public static boolean isPresent(String className) {
	    try {
	        Class.forName(className);
	        return true;
	    } catch (Throwable ex) {
	        // Class or one of its dependencies is not present...
	        return false;
	    }
	}

	/**
	 * Plays the Song
	 * @param tempo : desired tempo
	 */
	public void play(int tempo){
		if (isPresent("org.jfugue.Player") && isPresent("org.jfugue.Pattern")) {
			String INSTRUMENT = "Piano";

			String playable_song = "T" + tempo + " I[" + INSTRUMENT + "] " + this.toString();

			System.out.println(playable_song);
			org.jfugue.Pattern pattern = new org.jfugue.Pattern();
			pattern.setMusicString(playable_song);
			org.jfugue.Player player = new org.jfugue.Player();
			player.play(pattern);

			//Gives user option to save song as midi; loops in case cancels exit
			boolean exit = false;
			do{
				System.out.println("\nWould you like to save this song? (y or n)");
				Scanner in = new Scanner(System.in);
				String save = in.nextLine();
				if (save.equalsIgnoreCase("y")){
					exit = true;
					export(player, pattern);
				}
				else{
					System.out.println("Data will be lost. Are you sure?");
					in = new Scanner(System.in);
					save = in.nextLine();
					if(save.equalsIgnoreCase("y")) exit = true;
				}
			}while(!exit);
		}
		else{
			System.err.println("\nCannot play song; JFugue is not installed (see build instructions in the README)");
			System.out.println(this.toString(true));
		}
	}

	/**
	 * Saves generated song to a midi file for later playback
	 * @param player : Player object
	 * @param pattern : Pattern object
	 */
	public void export(org.jfugue.Player player, org.jfugue.Pattern pattern){
		Scanner sc = new Scanner(System.in);
		System.out.println("Type a name for the song");
		String songName = sc.next() + ".mid";
		pattern.setMusicString(this.toString());
		File outFile = new File(songName);
		try {
			player.saveMidi(pattern, outFile);
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
