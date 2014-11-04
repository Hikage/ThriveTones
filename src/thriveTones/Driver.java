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
	private static int seg_length = 8;
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

		Song song = new Song("C", 1, "New Hit", "Rockstar Bot", 4);
		song.build(song_sequence, reader.getPartsDictionary(), seg_length, history, false);

		System.out.println("\"" + song.getName() + "\" by " + song.getArtist() + "\n");
		System.out.println(song.toString(true));
		//song.play(tempo);

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
			System.out.println("How many chords would you like per segment?");
			Scanner in = new Scanner(System.in);
			if(in.hasNextInt()){
				seg_length = in.nextInt();
				if(seg_length > 0) break;
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
}
