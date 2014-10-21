package thriveTones;

import java.util.*;

import sax.XMLReader;
import thriveTones.SongSegment.SongPart;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * Driver.java
 * Main class driver
 */

public class Driver {
	private static XMLReader reader;
	private static int song_length = 8;
	private static int tempo = 120;
	private static int beats = 4;
	private static int history = 3;

	/**
	 * Main Class
	 * @param args
	 */
	public static void main (String args[]){
		if(args.length != 1){
			System.err.println("Usage: java thriveTones.Driver data_file_to_read_in.xml");
			System.exit(0);
		}

		readInData(args[0]);
		//getUserInputs();

		//TODO: generate sequence automatically
		SongPart[] song_sequence = {SongPart.intro, SongPart.verse, SongPart.verse, SongPart.chorus,
				SongPart.verse, SongPart.prechorus, SongPart.chorus, SongPart.bridge, SongPart.solo,
				SongPart.chorus, SongPart.chorus, SongPart.outro};

		LinkedList<SongSegment> segments = new LinkedList<SongSegment>();

		for(SongPart part : song_sequence)
			segments.add(buildSongSegment(part));

		Song song = new Song("C", 1, segments, "New Hit", "Rockstar Bot", 4);

		System.out.println("\"" + song.getName() + "\" by " + song.getArtist() + "\n");
		//System.out.println(song.toString());
		song.play(tempo);

		System.out.println("\nEnd of program.\nThank you for playing!");
		System.exit(0);
	}

	/**
	 * Reads in the data from the provided filename
	 * @param filename : file containing data to be read in
	 */
	public static void readInData(String filename){
		reader = new XMLReader();
		try {
			reader.readIn(filename);
		}
		catch (Exception e) {
			System.err.println("Error reading in file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Prompts user for inputs pertaining to song length, etc
	 */
	public static void getUserInputs(){
		do{
			System.out.println("How many chords would you like?");
			Scanner in = new Scanner(System.in);
			if(in.hasNextInt()){
				song_length = in.nextInt();
				if(song_length > 0) break;
			}
		}while(true);

		do{
			System.out.println("How many beats per measure?");
			Scanner in = new Scanner(System.in);
			if (in.hasNextInt()){
				beats = in.nextInt();
				if(beats > 0) break;
			}
		}while(true);
	}

	/**
	 * Builds the specified song part
	 * @param part : song part to be built
	 * @param play : whether or not to play the given SongSegment
	 * @return : constructed SongSegment
	 */
	public static SongSegment buildSongSegment(SongPart part){
		ChordDictionary chord_dictionary = reader.getChordDictionary(part);
		ProgressionGenerator generator = new ProgressionGenerator(chord_dictionary);

		//Get starting chord
		Chord start = null;
		start = chord_dictionary.getANextChord(null);

		//Generate progression and create new song
		generator.buildProgression(start, song_length, history);
		return new SongSegment(part, generator.getProgression());
	}
}
