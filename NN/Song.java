package NN;
import java.util.Scanner;

import org.jfugue.*;
public class Song {
	public static int SONG_LENGTH = 6;
	public static boolean startWithCMajor = false;
	public final static int VOICES = 4;
	public static int TEMPO = 120;
	public static String INSTRUMENT = "Piano";
	
	private String song;
	private int currentChord[];
	private NNet net;
	private Player player;
	private Pattern pattern;
	
	public Song(NNet n)
	{
		player = new Player();
		pattern = new Pattern();
		net = n;
		currentChord = startChord();
		generateSong();
	}
	
	public void generateSong()
	{
		currentChord = startChord();
		song = "T" + TEMPO + " I[" + INSTRUMENT + "] ";
		for (int i = 0; i < SONG_LENGTH; i++)
		{
			if (!startWithCMajor)
				getNextChord();
			for (int j = 0; j < VOICES; j++)
			{
				song += "[" + currentChord[j] + "]";
				if (j < VOICES-1)
					song += "+";
			}
			song += " ";
			if (startWithCMajor)
				getNextChord();
		}
		
	}
	
	public void generateSong(int startingChord[])
	{
		currentChord = startingChord;
		song = "T" + TEMPO + " I[Piano] ";
		for (int i = 0; i < SONG_LENGTH; i++)
		{
			for (int j = 0; j < VOICES; j++)
			{
				song += "[" + currentChord[j] + "]";
				if (j < VOICES-1)
					song += "+";
			}
			song += " ";
			getNextChord();
		}
	}

        public String getSong(){
	    return song;
	}
	
	public void playSong()
	{
		pattern.setMusicString(song);
		player.play(pattern);
	}
	
	public void exportSong()
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Type a name for the song");
		String songName = sc.next();
		pattern.setMusicString(song);
		player.save(pattern, songName + ".mid");
	}
	
	//Puts currentChord through the NNet and sets it to the output.
	private void getNextChord()
	{
		int netOutputs[] = net.calculate(currentChord);
		currentChord[0] = currentChord[0] + netOutputs[0];
		for (int i = 1; i < VOICES; i++)
		{
			currentChord[i] = currentChord[i-1] + netOutputs[i];
		}
		for (int i = 0; i < VOICES; i++)
		{
			//Don't allow the highest or lowest octave
			if(currentChord[i] < 12)
				currentChord[i] = 12;
			if(currentChord[i] > 115)
				currentChord[i] = 115;
		}
	}
	
	//A placeholder for a method that returns the starting chord of a song.
	private int[] startChord()
	{
		int a[] = {60, 64, 67, 72};
		return a;
	}
	
	public String toString()
	{
		return song;
	}
	
	public void changeNet(NNet a)
	{
		net = a;
	}
}
