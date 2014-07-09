package markovChords;

import java.util.Random;

public class Rhythm {
	double[] rhythm;

	int beats;
	int pulse;


	static int place = 0;
	
	public Rhythm(int length, int b, int p){
		beats = b;
		pulse = p;
		rhythm = new double[length];
		int remainingBeats = beats;
	}
	

}
