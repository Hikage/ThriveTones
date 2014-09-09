package thriveTones;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * Driver.java
 * Main class driver
 */

import static org.junit.Assert.fail;

import java.util.*;

import org.jfugue.*;

import sax.XMLReader;

public class Driver {
	private static String song;
	private static Player player;
	private static Pattern pattern;

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
			e.printStackTrace();
			fail(e.getMessage());
		}

		ProgressionGenerator generator = new ProgressionGenerator();

		//Get random starting chord
		int index = new Random().nextInt(reader.getUniqueChords().size());
		Chord start = reader.getUniqueChords().get(index);

		//User input to determine song specifics
		int song_length = 8;
		/**
		do{
			System.out.println("How many chords would you like?");
			Scanner in = new Scanner(System.in);
			if (in.hasNextInt()){
				song_length = in.nextInt();
				if(song_length > 0)
					break;
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
				if(beats > 0)
					break;
			}
		}while(true);
		**/

		//Generate progression
		generator.buildProgression(start, song_length);
		LinkedList<Chord> progression = generator.getProgression();

		String chords = "";
		for(Chord chord : progression)
			chords += " " + chord.toString('C', beats);

		//Play our creation
		player = new Player();
		pattern = new Pattern();

		try{
			PlaySong(chords);
		}catch (Throwable e){
			System.out.println("error with play: suspect someone else has hold of the sound card");
		}

		/**
		//Gives user option to save song as midi; loops in case cancels exit
		boolean exit = false;
		do{
			System.out.println("Would you like to save this song? (y or n)");
			Scanner in = new Scanner(System.in);
			String save = in.nextLine();
			if (save.equalsIgnoreCase("y")){
				exit = true;
				try{
					exportSong(song);
				}catch (Throwable e){
					System.out.println("error: suspect someone else has hold of the sound card");
				}
			}
			else{
				System.out.println("Data will be lost. Are you sure?");
				in = new Scanner(System.in);
				save = in.nextLine();
				if(save.equalsIgnoreCase("y")) exit = true;
			}
		}while(exit == false);
		**/

		System.out.println("\nEnd of program.\nThank you for playing!");
	}

	/**
	 * Plays song
	 * @param progression: chords to be played
	 */
	public static void PlaySong(String progression){
		int TEMPO = 120;
		String INSTRUMENT = "Piano";
		
		song = "T" + TEMPO + " I[" + INSTRUMENT + "]" + progression;
		
		System.out.println(song);
		pattern.setMusicString(song);
		player.play(pattern);
	}

	/**
	 * Saves generated song to a midi file for later playback
	 * @param song - final notes played
	 */
	public static void exportSong(String song)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Type a name for the song");
		String songName = sc.next();
		pattern.setMusicString(song);
		player.save(pattern, songName + ".mid");
	}
}
