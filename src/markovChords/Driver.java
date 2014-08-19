package markovChords;

import java.util.*;
import org.jfugue.*;

/**
 * @author Brianna Jarvis
 * March 27, 2006
 */
public class Driver {
	//class variables
	private static String song;
	private static Player player;
	private static Pattern pattern;
	private static int SONG_LENGTH;
	static FinalSong complete;

	private static final int C = 60, Cs = 61, Db = 61, D = 62, Ds = 63, Eb = 63, E = 64,
			F = 65, Fs = 66, Gb = 66, G = 67, Gs = 68, Ab = 68, A = 69, As = 70, Bb = 70, B = 71;
	
	/**
	 * Main Class
	 * @param args
	 */
	public static void main (String args[]){
		/**
		 * Chords are represented by Roman numeral (key-independent), then translated
		 * Start with first chord, based on probabilities for beginning chords (default I)
		 * Depending on previous chord, pick another (repeat until desired length)
		 * Resolve to concluding chord, based on probabilities (default I)
		 */

		boolean major = true;		//default to major tonality
		boolean valid = false;		//not yet a valid song
		int length = 0;				//no length, flexibility, time, or key
		int flex = 0;
		int time1 = 0;
		int time2 = 0;
		char key1 = 'c';
		char key2 = ' ';
		
		//determine tonality; loop until valid input
		while(!valid){
			System.out.println("Major or minor?");
			Scanner in = new Scanner(System.in);
			String Mm = in.nextLine().toString();
		
			if (Mm.equalsIgnoreCase("minor")){
				major = false;
				valid = true;
			}
			
			if(Mm.equalsIgnoreCase("major")){
				valid = true;
			}
		}
		
		//determine length; loop until valid number
		valid = false;
		while(!valid){
			System.out.println("Approximately how many measures long would you like the melody to be?");
			Scanner in = new Scanner(System.in);
			if (in.hasNextInt()){
				length = in.nextInt();
				if(length > 0)
					valid = true;
			}
		}
		
		//determine flexibility; loop until valid number
		valid = false;
		while (!valid){
			System.out.println("How flexible would you like the melody to be? (1 - 5)");
			Scanner in = new Scanner(System.in);
			if (in.hasNextInt()){
				flex = in.nextInt();
				if((flex > 0) && (flex < 6))
					valid = true;
			}
		}
		
		//determine time signature; loop until valid input
		valid = false;
		while(!valid){
			System.out.println("What time signature would you like this melody to have?" +
					"(ie: 4 4)");
			Scanner in = new Scanner(System.in);
			if(in.hasNextInt()){			//number of beats per measure
				time1 = in.nextInt();
				if (time1 > 0 && time1 < 10)
					valid = true;
			}
			if(!in.hasNextInt()) valid = false;
			else{
				time2 = in.nextInt();		//type of beat measured
				if(time2 != 1 && time2 != 2 && time2 != 4 && time2 != 8 && time2 != 16)
					valid = false;
			}
		}

		//tune key; loop until valid input
		/*valid = false;
		while(!valid){
			System.out.println("What key would you like the song to be in?" +
					"(ie: a#)");
			Scanner in = new Scanner(System.in);
			String line = in.nextLine().toLowerCase();
			if(!line.equals(null) && !(line.charAt(0) > 'g')){
				key1 = line.charAt(0);
				if(!(line.length() < 2)) key2 = line.charAt(1);
				System.out.println("Key = " + key1 + key2);
				valid = true;
			}
		}*/
		
		complete = new FinalSong(major, length, flex, time1, key1, key2);
		
		player = new Player();
		pattern = new Pattern();

		try{
			PlayMelody(time1);
		}catch (Throwable e){
			System.out.println("error with play: suspect someone else has hold of the sound card");
		}
		
		//gives user option to save song as midi; loops in case cancels exit
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
		
		System.out.println("End of program.\nThank you for playing!");
	}
	
	/**
	 * Composes melody, Calls PlaySong
	 * @param beats - number of beats per measure
	 */
	public static void PlayMelody(int beats){
		int length = complete.justMelody().length;
		int accomLength = complete.getChords().length;
		int songArr[][] = new int[length][4];
		for(int k = 0; k < accomLength; k++){
			for(int i = (k*beats); i< (k*beats)+beats; i++){
				songArr[i][0] = complete.justMelody()[i];
				//TODO: fix this
				//songArr[i][j] = complete.getChords()[k];
				System.out.println("final chord = " + songArr[i][0] + ", " + 
						songArr[i][1] + ", " + songArr[i][2] + ", " + songArr[i][3]);
			}
		}	
		
		/*int songArr[][] = new int[length][1];
		for(int i = 0; i < length; i++){
			songArr[i][0] = complete.justMelody()[i];
		}*/
		PlaySong(complete.justMelody(), complete.getChords(), beats);
	}
	
	/**
	 * Plays song
	 * @param melody - series of melodic notes
	 * @param chords - takes in associated chords
	 * @param beats - number of beats per measure
	 */
	public static void PlaySong(int melody[], Chord chords[], int beats){
		int TEMPO = 120;
		String INSTRUMENT = "Piano";
		
		//String song;
		Chord currentChord;
		/*Player player;
		Pattern pattern;*/
		int songLength = chords.length;
		int melodyLength = melody.length;
		
		song = "T" + TEMPO + " I[" + INSTRUMENT + "] ";
		for (int i = 0; i < songLength; i++)
		{
			currentChord = chords[i];
			song += currentChord.toString();
			
			for (int k = i*beats; k < i*beats + beats; k++){
				song += "[" + melody[k] + "]q";
				if(k < (i*beats + beats -1)) song += "_";
			}
			song += " ";
		}
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