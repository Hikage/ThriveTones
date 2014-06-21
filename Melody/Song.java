package Melody;
import java.util.Scanner;

import org.jfugue.*;
public class Song {
	public static int SONG_LENGTH = 12;
	public final static int MELODY_LENGTH = NNet.INPUTS/2;
	public static int TEMPO = 90;
	public static String INSTRUMENT = "Piano";
	
	private String song;
	private int currentMelody[];
	private NNet net;
	private Player player;
	private Pattern pattern;
	
	public Song()
	{
		player = new Player();
		pattern = new Pattern();
	}
	
	public Song(NNet n)
	{
		player = new Player();
		pattern = new Pattern();
		net = n;
		currentMelody = startMelody();
		generateSong();
	}
	
	public void generateSong()
	{
		currentMelody = startMelody();
		song = "T" + TEMPO + " I[" + INSTRUMENT + "] ";
		for (int i = 0; i < SONG_LENGTH; i++)
		{
			getNextNote();
			song += "[" + currentMelody[0] + "]";
			switch(currentMelody[MELODY_LENGTH])
			{
			case 1: song += "s "; break;
			case 2: song += "i "; break;
			case 3: song += "i. "; break;
			case 4: song += "q "; break;
			case 5: song += "qs "; break;
			case 6: song += "q. "; break;
			case 7: song += "qi. "; break;
			case 8: song += "h "; break;
			}
		}
	}
	
	public void generateStringStart()
	{
		song = "T" + TEMPO + " I[" + INSTRUMENT + "] ";
	}
	
	public void generateNext(int[] startMelody)
	{
		currentMelody = startMelody;
		getNextNote();
		song += "[" + currentMelody[0] + "]";
		switch(currentMelody[MELODY_LENGTH])
		{
		case 1: song += "s "; break;
		case 2: song += "i "; break;
		case 3: song += "i. "; break;
		case 4: song += "q "; break;
		case 5: song += "qs "; break;
		case 6: song += "q. "; break;
		case 7: song += "qi. "; break;
		case 8: song += "h "; break;
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
	private void getNextNote()
	{
		int[] results = net.calculate(currentMelody);
		for (int i = NNet.INPUTS-1; i > 0; i--)
		{
			currentMelody[i] = currentMelody[i-1];
		}
		currentMelody[0] = results[0] + currentMelody[1];
		currentMelody[MELODY_LENGTH] = results[1] % 8;
		if (currentMelody[MELODY_LENGTH] < 0)
			currentMelody[MELODY_LENGTH] *= -1;
		currentMelody[MELODY_LENGTH] += 1;
		
		//Don't allow the highest or lowest octave
		if(currentMelody[0] < 12)
			currentMelody[0] = 12;
		else if(currentMelody[0] > 115)
			currentMelody[0] = 115;
	}
	
	//A placeholder for a method that returns the starting chord of a song.
	public static int[] startMelody()
	{
		int a[] = new int[NNet.INPUTS];
		for (int i = 0; i < MELODY_LENGTH; i++)
		{
			a[i] = (60-MELODY_LENGTH/2) + i;
		}
		for (int j = MELODY_LENGTH; j < NNet.INPUTS; j++)
		{
			a[j] = 4;
		}
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
