package Melody;
import java.util.*;

import TheoryBased.scorrer;
public class Population
{
	public static int preserveBest = 3;
	public static int POPULATION = 10;
	
	private NNet pop[];
	private Song song;
	
	public Population()
	{
		System.out.println("Generating Population");
		pop = new NNet[POPULATION];
		
		for (int i = 0; i < POPULATION; i++){
		    pop[i] = new NNet();
		}
		song = new Song();
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
		
		System.out.println("1. Breed the Population");
		System.out.println("2. Back");
		int pick = sc.nextInt();
		if (pick == 1)
			breedAll();
		
	}
	
	public void scoreByRules()
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("How many iterations?");
		int pick = sc.nextInt();
		for (int k = 0; k < pick; k++)
		{
			System.out.println("Iteration " + k + ". . .");
			resetFitnesses();
			for (int i = 0; i < POPULATION; i++)
			{
				Scorrer.score(pop[i]);
				if (k == pick - 1)
					System.out.println("Number " + i + " scored: " + pop[i].getFitness());
				if (pop[i].getFitness() < 1)
				{
					pop[i].resetFitness();
					pop[i].addFitness(1);
				}
			}
			breedAll();
		}
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
	
	public NNet select_random_weighted_net()
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

