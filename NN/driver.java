3package NN;

import java.io.*;
import java.util.*;

import org.jfugue.*;

public class driver
{
	private static Population pop;
	public static void main(String[] args)
	{	
		pop = new Population();
		Scanner sc = new Scanner(System.in);
		int pick = 0;
		while (pick != 8)
		{
			System.out.println("1. Breed songs by user ratings");
			System.out.println("2. Breed songs by theory rules");
			System.out.println("3. Listen to a song");
			System.out.println("4. Export a song as MIDI");
			System.out.println("5. Change parameters");
			System.out.println("6. Save Population");
			System.out.println("7. Load Population");
			System.out.println("8. Quit");
			pick = sc.nextInt();
			switch (pick)
			{
			case 1: userRated(); break;
			case 2: rulesRated(); break;
			case 3: listen(); break;
			case 4: export(); break;
			case 5: changeParameters(); break;
			case 6: savePopulation(); break;
			case 7: loadPopulation(); break;
			case 8: return;
			}
		}
		
		
		
		
		
/*		System.out.println("Testing the magnitude of a change of starting chords:");
		testStartChordEffectMagnitude();
		System.out.println("\nTesting the effect of a tiny weight change on the song:");
		testWeightEffectMagnitude();
		System.out.println("\nTesting the resemblence of children to their parents:");
		testBreeding();	
*/		
	}
	
	public static void userRated()
	{
		pop.scoreByUser();
	}
	
	public static void rulesRated()
	{
		
	}
	
	public static void listen()
	{
		Scanner sc = new Scanner(System.in);
		int pick = -1;
		while (pick < 0 || pick >= Population.POPULATION)
		{
			System.out.println("Play which song? (0 - " + (Population.POPULATION-1) + ")");
			pick = sc.nextInt();
		}
		pop.playSong(pick);
	}
	
	public static void export()
	{
		Scanner sc = new Scanner(System.in);
		int pick = -1;
		while (pick < 0 || pick >= Population.POPULATION)
		{
			System.out.println("Export which song? (0 - " + (Population.POPULATION-1) + ")");
			pick = sc.nextInt();
		}
		pop.exportSong(pick);
	}
	
	public static void changeParameters()
	{
		Scanner sc = new Scanner(System.in);
		int pick = 0;
		while (pick != 7)
		{
			System.out.println("1. Change Song Length (" + Song.SONG_LENGTH + ")");
			System.out.println("2. Toggle Starting with a C Major Chord (" + Song.startWithCMajor + ")");
			System.out.println("3. Change how many of the highest-scoring songs are preserved (" + Population.preserveBest + ")");
			System.out.println("4. Change Tempo (" + Song.TEMPO + ")");
			System.out.println("5. Change Instrument (" + Song.INSTRUMENT + ")");
			System.out.println("6. Change Population (" + Population.POPULATION + ")");
			System.out.println("7. Back");
			pick = sc.nextInt();
			switch (pick)
			{
			case 1: System.out.println("Change to what? ");
				Song.SONG_LENGTH = sc.nextInt(); break;
			case 2: Song.startWithCMajor = !Song.startWithCMajor; break;
			case 3: System.out.println("Change to what? ");
			Population.preserveBest = sc.nextInt(); break;
			case 4: System.out.println("Change to what? ");
			Song.TEMPO = sc.nextInt(); break;
			case 5: System.out.println("Change to what? ");
			Song.INSTRUMENT = sc.next(); break;
			case 6: System.out.println("Change to what? (WARNING: If you reduce the population, the " +
					"NNets with the lowest scores will be deleted.)");
			int oldPop = Population.POPULATION;
			Population.POPULATION = sc.nextInt();
			Population temp = new Population();
			pop.sortPop();
			for (int i = 0; i < Population.POPULATION && i < oldPop; i++)
				temp.setNet(i, pop.getNet(i));
			pop = temp; break;
			}
		}
	}

	public static void savePopulation()
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Type a save file number.");
		int pick = sc.nextInt();
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter("save" + pick));
	        for (int i = 0; i < Population.POPULATION; i++)
	        {
	        	out.write(pop.getNet(i).dumpData());
	        }
	        out.close();
	    } catch (IOException e) {
	    }
		
	}
	
	public static void loadPopulation()
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Type a save file number.");
		int pick = sc.nextInt();
		Scanner file;
		try{
			file = new Scanner(new File("save" + pick));
		}
		catch (FileNotFoundException ex){
			System.out.println("File not found.");
			return;
		}
		try {
			for (int p = 0; p < Population.POPULATION; p++)
			{
				if (!file.hasNext())
				{
					System.out.println("Save file had only " + p + " NNets out of " + Population.POPULATION +
							" in the population.  The rest are randomly generated.");
					break;
				}
				for (int j = 0; j < NNet.HIDDEN; j++)
				{
					for (int i = 0; i < NNet.INPUTS; i++)
					{
						pop.getNet(p).setInputToHiddenWeight(i,j,file.nextDouble());
					}
					for (int k = 0; k < NNet.OUTPUTS; k++)
					{
						pop.getNet(p).setHiddenToOutputWeight(j,k,file.nextDouble());
					}
				}
			}
		}
		catch (InputMismatchException e) {
			System.out.println("Dimensions are wrong.  The save file must have the " +
					"same number of input/hidden/output nodes.");
		}
		
	}
	
	public static void savePopulation(Population pop)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Type a save file number.");
		int pick = sc.nextInt();
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter("save" + pick));
	        for (int i = 0; i < Population.POPULATION; i++)
	        {
	        	out.write(pop.getNet(i).dumpData());
	        }
	        out.close();
	    } catch (IOException e) {
	    }
		
	}
	
	public static void loadPopulation(Population pop)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Type a save file number.");
		int pick = sc.nextInt();
		Scanner file;
		try{
			file = new Scanner(new File("save" + pick));
		}
		catch (FileNotFoundException ex){
			System.out.println("File not found.");
			return;
		}
		try {
			for (int p = 0; p < Population.POPULATION; p++)
			{
				for (int j = 0; j < NNet.HIDDEN; j++)
				{
					for (int i = 0; i < NNet.INPUTS; i++)
					{
						pop.getNet(p).setInputToHiddenWeight(i,j,file.nextDouble());
					}
					for (int k = 0; k < NNet.OUTPUTS; k++)
					{
						pop.getNet(p).setHiddenToOutputWeight(j,k,file.nextDouble());
					}
				}
			}
		}
		catch (InputMismatchException e) {
			System.out.println("Dimensions are wrong.  The save file must have the " +
					" same population and same number of input/hidden/output nodes.");
		}
		
	}
	
	//Tests the difference between songs when a slightly different starting chord is specified.
	//The 2nd song should be fairly distinguishable from the first, and the 3rd should be the
	//same as the first.
	public static void testStartChordEffectMagnitude()
	{
		NNet testNet = new NNet();
		Song testSong = new Song(testNet);
		int a[] = {50,55,60,65};
		testSong.generateSong(a);
		System.out.println(testSong);
		testSong.playSong();
		int b[] = {50,55,60,64};
		testSong.generateSong(b);
		System.out.println(testSong);
		testSong.playSong();
		int c[] = {50,55,60,65};
		testSong.generateSong(c);
		System.out.println(testSong);
		testSong.playSong();
	}
	
	//Tests how different a song is produced by a tiny change of an inputToHidden weight.
	//The change should be noticable, but not a completely different song.
	public static void testWeightEffectMagnitude()
	{
		NNet a = new NNet();
		a.testFunction();
	}

	//Tests whether children of two songs bear any resemblence to their parents.
	//The first two songs played are random parents, and the next two are their children.
	public static void testBreeding()
	{
		NNet a = new NNet();
		NNet b = new NNet();
		Song s1 = new Song(a);
		Song s2 = new Song(b);
		
		System.out.println("Parent 1: " + s1);
		s1.playSong();
		System.out.println("Parent 2: " + s2);
		s2.playSong();
		
		NNet c = new NNet();
		NNet d = new NNet();
		
		
		for (int i = 0; i < 3; i++)
		{
			c.childOf(a,b);
			d.childOf(a,b);
			a.childOf(c,d);
			b.childOf(c,d);
		}
		
		Song c1 = new Song(a);
		Song c2 = new Song(b);
		
		System.out.println("Child 1: " + c1);
		c1.playSong();
		System.out.println("Child 2: " + c2);
		c2.playSong();
	}
}
