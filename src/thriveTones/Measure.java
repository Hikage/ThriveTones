package thriveTones;

import java.util.ArrayList;
import java.util.Random;

public class Measure {
	ArrayList oneMeasure = new ArrayList();
	Rhythm r;
	int remainingBeats = 0;
	Random rand = new Random(8);
	double[] rhythmChoices = {6, 4, 3, 2, 1.5, 1, .5, .25};
	
	public Measure(Melody m, int beats, int pulse){
		
	}
	
	public double getNoteValue(){
		int next;
		if(remainingBeats == 0) return -1;
		do{
			next = rand.nextInt();
		}while(next > remainingBeats);
		remainingBeats -= rhythmChoices[next];
		oneMeasure.add(rhythmChoices[next]);
		return rhythmChoices[next];
	}
}
