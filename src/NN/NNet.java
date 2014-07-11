package NN;

import java.util.*;
import java.math.*;

//The counter i is generally used for input, j for hidden, and k for output neurons.
public class NNet implements Comparable{
	public final static int INPUTS = Song.VOICES + 1;
	public final static int HIDDEN = 12;
	public final static int OUTPUTS = Song.VOICES;
	public final static int ACTIVATION_MAGNITUDE = 8;
	public final static int ACTIVATION_SPREAD = 20;
	public final static double MUTATION_RATE = 0.005;
	public final static double MINOR_MUTATION_RATE = 0.01;
	public final static double MINOR_MUTATION_CHANGE = 0.025;
	
	private double inputToHidden[][]; //Input-to-hidden weights
	private double hiddenToOutput[][]; //Hidden-to-output weights
	private int fitness; //The nnet's rating  
	                   
	public NNet()
	{
		inputToHidden = new double[INPUTS][HIDDEN];
		hiddenToOutput = new double[HIDDEN][OUTPUTS];
		randomizeWeights();
	}
	
	private void randomizeWeights()
	{
		Random rand = new Random();
		for (int j = 0; j < HIDDEN; j++)
		{
			for (int i = 0; i < INPUTS; i++)
			{
				inputToHidden[i][j] = rand.nextDouble();
				if (rand.nextBoolean())
					inputToHidden[i][j] *= -1;
			}
			for (int k = 0; k < OUTPUTS; k++)
			{
				hiddenToOutput[j][k] = rand.nextDouble();
				if (rand.nextBoolean())
					hiddenToOutput[j][k] *= -1;
			}
		}
	}
	
	//Takes an array of inputs, sends them through the neural net, and returns the output.
	//The input array must have exactly INPUTS elements in it.
	public int[] calculate(int inputChord[])
	{
		int input[] = chordToInputs(inputChord);
		double hiddenValues[] = new double[HIDDEN];
		
		for (int j = 0; j < HIDDEN; j++)
		{
			hiddenValues[j] = 0;
			for (int i = 0; i < INPUTS; i++)
				hiddenValues[j] += input[i] * inputToHidden[i][j];
			hiddenValues[j] = threshold(hiddenValues[j]);
		}
		 
		double outputValues[] = new double[OUTPUTS];
		int endValues[] = new int[OUTPUTS];
		
		for (int k = 0; k < OUTPUTS; k++)
		{
			outputValues[k] = 0;
			for (int j = 0; j < HIDDEN; j++)
				outputValues[k] += hiddenValues[j] * hiddenToOutput[j][k];
			endValues[k] = round(outputValues[k]);
		}
		
		return endValues;
	}
	
	public static int[] chordToInputs(int chord[])
	{
		int inputs[] = new int[INPUTS];
		inputs[0] = chord[0] / 12;
		inputs[1] = chord[0] % 12;
		for (int i = 2; i < INPUTS; i++)
		{
			inputs[i] = chord[i-1] - chord[i-2];
		}
		
		return inputs;
	}
	
	//The function that determines the hidden neurons' outputs based on their inputs. 
	private double threshold(double input)
	{	
		int mag = ACTIVATION_MAGNITUDE;
		int spread = ACTIVATION_SPREAD;
		//Sigmoid function
		return (mag) - (2 * mag)/(1 +Math.exp(input/spread));
		
		
/*		
		//A compromise on the lowest function.
		double temp = input;
 		for (int i = 0; i < 3; i++)
			input = (input * temp) % 7;
		return input;
*/		
/*		//A basic (test) function.
		return input % 10;
*/
		
/*		//This is (input^34 % 67)/4.  I chose this because it gives a great, almost
 		//random scrambling, yet is deterministic.
 		//Problems: It scrambles it too well.  A change of 10^-13 in an input-to-hidden weight
 		//changes the song completely, disallowing children from being like their parents.
		double temp = input;
		for (int i = 0; i < 34; i++)
			input = (input * temp) % 67;
		return input / 4;
*/
	}
	
	private int round(double a)
	{
		if (a - (int)a >= 0.5)
			return (int)a + 1;
		else
			return (int)a;
	}
	
	public void addFitness(int a)
	{
		fitness += a;
	}
	
	public void resetFitness()
	{
		fitness = 0;
	}
	
	public int getFitness()
	{
		return fitness;
	}
	
	//Replaces this NNet with a child of parent1 and parent2.
	public void childOf(NNet parent1, NNet parent2)
	{
		Random rand = new Random();
		for (int j = 0; j < HIDDEN; j++)
		{
			for (int i = 0; i < INPUTS; i++)
			{
				if (rand.nextDouble() < MUTATION_RATE)
				{
					inputToHidden[i][j] = rand.nextDouble();
					if (rand.nextBoolean())
						inputToHidden[i][j] *= -1;
				}
				else if (rand.nextDouble() < MINOR_MUTATION_RATE)
				{
					inputToHidden[i][j] += MINOR_MUTATION_CHANGE;
					if (inputToHidden[i][j] > 1)
						inputToHidden[i][j] = 1;
					else if (inputToHidden[i][j] < -1)
						inputToHidden[i][j] = -1;
				}
				else if (rand.nextBoolean())
					inputToHidden[i][j] = parent1.getInputToHidden(i,j);
				else
					inputToHidden[i][j] = parent2.getInputToHidden(i,j);
				
			}
			for (int k = 0; k < OUTPUTS; k++)
			{
				if (rand.nextDouble() < MUTATION_RATE)
				{
					hiddenToOutput[j][k] = rand.nextDouble();
					if (rand.nextBoolean())
						hiddenToOutput[j][k] *= -1;
				}
				else if (rand.nextDouble() < MINOR_MUTATION_RATE)
				{
					hiddenToOutput[j][k] += MINOR_MUTATION_CHANGE;
					if (hiddenToOutput[j][k] > 1)
						hiddenToOutput[j][k] = 1;
					else if (hiddenToOutput[j][k] < -1)
						hiddenToOutput[j][k] = -1;
				}
				else if (rand.nextBoolean())
					hiddenToOutput[j][k] = parent1.getHiddenToOutput(j,k);
				else
					hiddenToOutput[j][k] = parent2.getHiddenToOutput(j,k);
					
			}
		}
		resetFitness();
	}

	public double getInputToHidden(int i, int j)
	{
		return inputToHidden[i][j];
	}
	
	public double getHiddenToOutput(int j, int k)
	{
		return hiddenToOutput[j][k];
	}
	
	public void testFunction()
	{
		Random rand = new Random();
		Song song = new Song(this);
		System.out.println("Current song: " + song);
		song.playSong();
		inputToHidden[0][0] += 0.01;
		song.generateSong();
		System.out.println("New song:     " + song);
		song.playSong();
		
	}
    
	public String dumpData()
	{
		String str = "";
		for (int j = 0; j < HIDDEN; j++)
		{
			for (int i = 0; i < INPUTS; i++)
			{
				str += inputToHidden[i][j] + " ";
			}
			for (int k = 0; k < OUTPUTS; k++)
			{
				str += hiddenToOutput[j][k] + " ";
			}
		}
		
		return str;
	}
	
	public void setInputToHiddenWeight(int i, int j, double w)
	{
		inputToHidden[i][j] = w;
	}
	
	public void setHiddenToOutputWeight(int j, int k, double w)
	{
		hiddenToOutput[j][k] = w;
	}
	
	public int compareTo(Object o){
	    return  ((NNet)o).getFitness() - this.getFitness();
	}
}
