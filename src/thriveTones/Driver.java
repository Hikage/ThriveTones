package thriveTones;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * Driver.java
 * Main class driver
 */

import java.util.*;

import sax.XMLReader;

public class Driver {
	private static int song_length = 8;
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

		String filename = args[0];

		//Read in the data
		XMLReader reader = new XMLReader();
		try {
			reader.readIn(filename);
		}
		catch (Exception e) {
			System.err.println("Error reading in file: " + e.getMessage());
			e.printStackTrace();
		}

		ChordDictionary dictionary = reader.getChordDictionary();
		ProgressionGenerator generator = new ProgressionGenerator(dictionary);

		//User input to determine song specifics
		/**
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
		**/

		//Get starting chord
		Chord start = null;
		start = dictionary.getANextChord(null);

		//Generate progression
		generator.buildProgression(start, song_length, history);
		LinkedList<Chord> progression = generator.getProgression();

		Song new_hit = new Song("random part", "C", 1, beats, progression);

		//System.out.println(new_hit.toString());
		new_hit.play();

		System.out.println("\nEnd of program.\nThank you for playing!");
		System.exit(0);
	}
}
