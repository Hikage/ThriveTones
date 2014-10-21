package thriveTones;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

import org.jfugue.Pattern;
import org.jfugue.Player;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * Song.java
 * This class represents a song, comprising of a series of SongSegments
 */

public class Song {
	private LinkedList<SongSegment> segments = new LinkedList<SongSegment>();
	private String name;
	private String artist;
	private int mode;
	private String key;
	private String rel_major;

	/**
	 * Constructor method
	 * @param ky : key of the new Song
	 * @param md : Song mode
	 * @param seg : segment progression
	 * @param nm : Song name
	 * @param at : Song artist
	 */
	public Song(String ky, int md, LinkedList<SongSegment> seg, String nm, String at){
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
		segments = seg;

		name = nm;
		artist = at;

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
	public LinkedList<SongSegment> getSegments(){
		return segments;
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

	@Override
	public String toString(){
		String playable_segments = "K" + rel_major + "maj ";
		ListIterator<SongSegment> it = segments.listIterator();
		while(it.hasNext())
			playable_segments += it.next().toString(key) + "\n";
		return playable_segments.trim();		
	}

	/**
	 * Plays the Song
	 * @param tempo : desired tempo
	 */
	public void play(int tempo){
		String INSTRUMENT = "Piano";

		String playable_song = "T" + tempo + " I[" + INSTRUMENT + "] " + this.toString();

		System.out.println(playable_song);
		Pattern pattern = new Pattern();
		pattern.setMusicString(playable_song);
		Player player = new Player();
		player.play(pattern);

		/**
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
		**/
	}

	/**
	 * Saves generated song to a midi file for later playback
	 * @param player : Player object
	 * @param pattern : Pattern object
	 */
	public void export(Player player, Pattern pattern){
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
