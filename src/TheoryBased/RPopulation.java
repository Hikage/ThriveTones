package TheoryBased;
import java.util.*;
//import TheoryBased.scorrer;
import NN.*;
public class RPopulation
{
	public final static int POPULATION = 20;
	
	private NNet pop[];

        private Random rand = new Random();
        private scorrer scorreri = new scorrer();


	public RPopulation()
	{
		pop = new NNet[POPULATION];
		
                for (int i = 0; i < POPULATION; i++){
		    pop[i] = new NNet();
		}
	}
	
        public NNet get_net(int which_net){
	    return pop[which_net];
	}

	public void resetFitnesses()
	{
		for (int i = 0; i < POPULATION; i ++)
			pop[i].resetFitness();
	}
	
    /*public void scoreByUser()
	{
		resetFitnesses();
		Song song = new Song(pop[0]);
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < POPULATION; i++)
		{
			song.changeNet(pop[i]);
			song.generateSong();
			System.out.println("Rate the following song from 1 to 100:");
			System.out.println(song);
			song.playSong();
			int rating = sc.nextInt();
			System.out.println("You rated the song " + rating + ".");
			pop[i].addFitness(rating);
		}
		breedAll();
		
		}*/
	
        public void scoreAllByRules(){
	    for (int i = 0; i < POPULATION; i++)
                {
		    scoreByRules(pop[i]);
                }
	   
	}

        public void scoreByRules(NNet net)
	{

  	        net.resetFitness();		//resetFitnesses();
		//Song song = new Song(pop[0]);
		
		
		int rating = scorreri.score_NNet(net) * 2 + 2 ;//(0-49)*2
		net.addFitness(rating);
		//System.out.println(rating + "\t");
		
		
		
		
	}
	
	//Make sure all elements of pop[] have been scored.
	public void breedByRules()
	{
  	        scoreAllByRules();
 	        
		Arrays.sort(pop);//sort by fitnesses
    		/*for (int i = 0; i < POPULATION; i++){
		    System.out.println(pop[i].getFitness());
		}*/
		


		//chose 2 to breed, random selection weighted by fitness

		NNet selection1 = select_random_weighted_net();
		NNet selection2 = select_random_weighted_net();
		while(selection1 == selection2){
			selection2 = select_random_weighted_net();
		}
		
		
		
		//breed a new NNet from the 2 parents selected
		NNet new_net = new NNet();
		new_net.childOf(selection1, selection2);
		scoreByRules(new_net);		
		//System.out.println();
		//System.out.print(new_net.getFitness() + "\t");
		
		//see if it is good enough to add
		if (new_net.getFitness() > pop[POPULATION-1].getFitness()){
		    	pop[POPULATION-1] = new_net;
			//System.out.println("added");
    		} else {
		        //System.out.println("rejected");
		}
                //sort(pop);//dont really need to
		

/*		
		int max = -1;
		int max2 = -1;
		NNet first = pop[0];
		NNet second;
		for (int i = 0; i < POPULATION; i++)
		{
			if (pop[i].getFitness() > max)
			{
				if (first.getFitness() > max2)
				{
					second = first;
					max2 = second.getFitness();
				}
				first = pop[i];
				max = first.getFitness();
			}
			else if (pop[i].getFitness() > max2)
			{
				second = pop[i];
				max2 = second.getFitness();
			}
		}
		
		
*/
	}

        
        public void print_fitnesses(){
	    for (int i = 0; i < POPULATION; i++){
		System.out.println(pop[i].getFitness());
	    }
	    
	    System.out.println();
	}

	private NNet select_random_weighted_net(){
	    
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
			selected_net ++;
		
		return pop[selected_net];

	}
}

