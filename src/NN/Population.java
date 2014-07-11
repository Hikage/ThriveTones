package NN;
import java.util.*;

import TheoryBased.scorrer;
public class Population
{
	public static int preserveBest = 1;
	public static int POPULATION = 6;
	
	private NNet pop[];
	private Song song;
	
	public Population()
	{
		pop = new NNet[POPULATION];
		
		for (int i = 0; i < POPULATION; i++){
		    pop[i] = new NNet();
		}
		song = new Song(pop[0]);
	}
	
	public void resetFitnesses()
	{
		for (int i = 0; i < POPULATION; i ++)
			pop[i].resetFitness();
	}
	
	public NNet getNet(int i)
	{
		return pop[i];
	}
	
	public void setNet(int i, NNet n)
	{
		pop[i] = n;
	}
	
	public void playSong(int i)
	{
		song.changeNet(pop[i]);
		song.generateSong();
		System.out.println(song);
		song.playSong();
	}
	
	public void exportSong(int i)
	{
		song.changeNet(pop[i]);
		song.generateSong();
		song.exportSong();
	}
	
	public void scoreByUser()
	{
		resetFitnesses();
		song.changeNet(pop[0]);
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < POPULATION; i++)
		{
			song.changeNet(pop[i]);
			song.generateSong();
			int rating = -1;
			while (rating == -1)
			{
				System.out.println("Rate the following song from 0 to 100: (-1 to replay)");
				System.out.println(song);
				song.playSong();
				
				try {
					rating = sc.nextInt();
				}
				catch (InputMismatchException ex) { }
			}
			System.out.println("You rated the song " + rating + ".");
			pop[i].addFitness(rating);
		}
		breedAll();
		
	}
	
	public void scoreByRules()
	{
		resetFitnesses();
		//Song song = new Song(pop[0]);
		scorrer scorreri = new scorrer();

		System.out.println();
		for (int i = 0; i < POPULATION; i++)
		{
		        int rating = scorreri.score_NNet(pop[i]) * 2 ;//(0-49)*2
			pop[i].addFitness(rating);
			System.out.println(rating + "\t");
		}
		breedAll();
		
		
	}
	
	//Make sure all elements of pop[] have been scored.
	public void breedAll()
	{
		Arrays.sort(pop);
		//Start at 1 to keep the user's favorite song.
		NNet temp[] = new NNet[POPULATION];
		for (int i = 0; i < preserveBest; i++)
			temp[i] = pop[i];
		for (int i = preserveBest; i < POPULATION; i++)
		{
			temp[i] = new NNet();
			NNet selection1 = select_random_weighted_net();
			NNet selection2 = select_random_weighted_net();
			while(selection1 == selection2)
				selection2 = select_random_weighted_net();
			temp[i].childOf(selection1, selection2);
		}
		pop = temp;
	}
	
	public void sortPop()
	{
		Arrays.sort(pop);
	}
	
	private NNet select_random_weighted_net()
	{
		Random rand = new Random();
		//Normalize fitnesses in the population.  For example:
		//Say fitnesses are {5,10,15}.  selections would be {16.7, (33.3+16.7), (50+33.3+16.7}
		//or {16.7, 50, 100}.  This makes a random number from 1 to 100 pick each with the
		//correct probabilities.
		double selections[] = new double[POPULATION];
		int sum = 0;
		//Find the total fitness
		for (int i = 0; i < POPULATION; i++)
		    sum += pop[i].getFitness();
		selections[0] = (double)pop[0].getFitness()/sum;
		for (int i = 1; i < POPULATION; i++)
			selections[i] = (double)pop[i].getFitness()/sum + selections[i-1];

		double target = rand.nextDouble();
		int selected_net = 0;
		while(target > selections[selected_net])
			selected_net++;
		
		return pop[selected_net];

	}
}

