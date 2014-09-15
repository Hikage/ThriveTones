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

	/**
	 * Main Class
	 * @param args
	 */
	public static void main (String args[]){
		String filename = args[0];

		//Read in the data
		XMLReader reader = new XMLReader();
		try {
			reader.readIn(filename);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		ProgressionGenerator generator = new ProgressionGenerator(reader);

		//Get starting chord
		//TODO: use stats to determine best starting chord
		int index = new Random().nextInt(reader.getUniqueChords().size());
		Chord start = reader.getUniqueChords().get(index);

		//User input to determine song specifics
		int song_length = 8;
		/**
		do{
			System.out.println("How many chords would you like?");
			Scanner in = new Scanner(System.in);
			if(in.hasNextInt()){
				song_length = in.nextInt();
				if(song_length > 0) break;
			}
		}while(true);
		**/

		int beats = 4;
		/**
		do{
			System.out.println("How many beats per measure?");
			Scanner in = new Scanner(System.in);
			if (in.hasNextInt()){
				beats = in.nextInt();
				if(beats > 0) break;
			}
		}while(true);
		**/

		//Generate progression
		generator.buildProgression(start, song_length);
		LinkedList<Chord> progression = generator.getProgression();

		Song new_hit = new Song("random part", "C", 1, beats, progression);

		//System.out.println(new_hit.toString());
		new_hit.play();

		System.out.println("\nEnd of program.\nThank you for playing!");
		System.exit(0);
	}
}
