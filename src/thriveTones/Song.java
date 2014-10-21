package thriveTones;

import java.util.LinkedList;
import java.util.ListIterator;

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
	private String key;

	/**
	 * Constructor method
	 * @param ky : key of the new Song
	 * @param seg : segment progression
	 */
	public Song(String ky, LinkedList<SongSegment> seg){
		if(ky.isEmpty()) key = "C";
		else key = ky;
		segments = seg;
	}

	/**
	 * Standardizes a given key
	 * @param ky : key to be standardized
	 * @return : the standardized key string
	 */
	public String standardizeKey(String ky){
		String keys = "ABCDEFG";
		String standard_key = "";
		if(!keys.contains(Character.toString(ky.toUpperCase().charAt(0))))
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
	 * key accessor
	 * @return : Song key
	 */
	public String getKey(){
		return key;
	}

	/**
	 * segments accessor
	 * @return : list of SongSegments associated with song
	 */
	public LinkedList<SongSegment> getSegments(){
		return segments;
	}

	@Override
	public String toString(){
		String playable_segments = "";
		ListIterator<SongSegment> it = segments.listIterator();
		while(it.hasNext())
			playable_segments += it.next().toString() + "\n";
		return playable_segments.trim();		
	}
}
