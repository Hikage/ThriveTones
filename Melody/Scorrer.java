package Melody;

import java.util.*;

public class Scorrer
{
	public static int[][][] melodyTests = {
		//C Major scale
		{{60, 62, 64, 65, 67, 69, 71, 72},
			{2, -1, -12, -5, 7, 4}},
		//C Minor scale
		{{60, 62, 63, 65, 67, 68, 70, 72},
			{2, -2, -12, -5, 7, 3}},
		//C Harmonic Minor Scale
		{{60, 62, 63, 65, 67, 68, 71, 72},
			{2, -2, -12, -5, 7, 3}},
		//C major arpeggio
		{{60, 64, 67, 64, 60, 64, 67, 64},
			{-4, 8, -2}},
		//C minor arpeggio
		{{60, 63, 67, 63, 60, 63, 67, 63},
			{-3, 9, -1}},
		//C major arpeggio
		{{48, 52, 55, 60, 64, 67, 72, 76},
			{-4, 3}},
		//C minor arpeggio
		{{48, 51, 55, 60, 63, 67, 72, 75},
			{-3, 4}},
		//Chromatic scale
		{{60, 61, 62, 63, 64, 65, 66, 67},
			{1, -1, 5, -7}},
		//Twinkle-twinkle
		{{60, 60, 67, 67, 69, 69, 67, 65},
			{0, -1, -3, 2}},
		//Little lamb
		{{64, 62, 60, 62, 64, 64, 64, 62},
			{0, -2, 5, 2}},
		//Fur Elise
		{{64, 63, 64, 63, 64, 59, 62, 60},
			{-3, 1}},
		//Happy Birthday
		{{55, 55, 57, 55, 60, 59, 55, 55},
			{2, -3, 5}},
		//In the Hall of the Mountain King
		{{57, 59, 60, 62, 64, 60, 64, 63},
			{-4, 1}},

	};
	
	public static int[][][] rhythmTests = {
		{{4, 4, 4, 4, 4, 4, 4, 4},
			{4,8}},
		{{2, 2, 2, 2, 2, 2, 2, 2},
			{2, 4}},
		{{4, 4, 2, 2, 2, 2, 4, 4},
			{2, 4}},
		{{8, 4, 4, 8, 4, 4, 8, 4},
			{4,8}},
		{{4, 2, 4, 4, 4, 8, 4, 2},
			{4}},
		{{4, 1, 1, 1, 1, 2, 2, 2},
			{2, 4, 8}},
	};
	
	
	public static void score(NNet net)
	{
		Random rand = new Random();
		net.resetFitness();
		for (int i = 0; i < melodyTests.length; i++)
		{
			int rhythm = rand.nextInt(rhythmTests.length);
			int test[] = new int[NNet.INPUTS];
			for (int j = 0; j < NNet.INPUTS/2; j++)
				test[j] = melodyTests[i][0][j];
			for (int j = NNet.INPUTS/2; j < NNet.INPUTS; j++)
				test[j] = rhythmTests[rhythm][0][j-NNet.INPUTS/2];
			
			int results[] = net.calculate(test);
			
			for (int j = 0; j < melodyTests[i][1].length; j++)
			{
				if (melodyTests[i][1][j] == results[0])
				{
					net.addFitness(10);
					break;
				}
				else
				{
					net.addFitness(1);
					break;
				}
			}
			for (int j = 0; j < rhythmTests[rhythm][1].length; j++)
			{
				if (rhythmTests[rhythm][1][j] == results[1])
				{
					net.addFitness(5);
					break;
				}
			}
			
			if (results[0] > 12 || results[0] < -12)
				net.addFitness(-2);
		}

	}

}
