package TheoryBased;
import NN.NNet;
import NN.Song;

public class scorrer {
	private int key_shift = 0;
										//       1   2   3 4   5   6   7  
										//       0   2   4 5   7   9   11
	public int [][] basic_flat_chords = {	{1,0,0,0,1,0,0,1,0,0,0,0},
											{0,0,1,0,0,1,0,0,0,1,0,0},
											{0,0,0,0,1,0,0,1,0,0,0,1},
											{1,0,0,0,0,1,0,0,0,1,0,0},
											{0,0,1,0,0,0,0,1,0,0,0,1},
											{1,0,0,0,1,0,0,0,0,1,0,0},
											{0,0,1,0,0,1,0,0,0,0,0,1},};

        public final static int VOICES = 4;

	public scorrer(){}
	public scorrer(int key_shift_i){
		key_shift = key_shift_i;
	}
	
	
	
	public int score_NNet(NNet net){
		int score = 0;

		int[][] basic_chords = {//60,62,64,65,67,69,71, 72,74,76,77,79,81,83
		    {60, 64, 67, 72},
                    {62, 65, 69, 74},
                    {64, 67, 71, 76},
                    {65, 69, 72, 77},
                    {67, 71, 74, 79},
                    {69, 72, 76, 81},
		    {71, 74, 77, 83}};

		for (int r = 0; r < basic_chords.length ; r++){
		        int[] current_chord = basic_chords[r];
			
                        /*for (int c = 0; c < current_chord.length ; c++){

			    System.out.print( current_chord[c]  + "\t");
			}
			System.out.println();
			for (int c = 0; c < current_chord.length ; c++){
			    System.out.print( current_chord[c]%12  + "\t");
			}
			System.out.println();*/

			int[] next_chord = getNextChord(net, current_chord);

			/*for (int c = 0; c < next_chord.length ; c++){
				System.out.print( next_chord[c]  + "\t");
			}
			System.out.println();
			for (int c = 0; c < next_chord.length ; c++){
				System.out.print( next_chord[c]%12  + "\t");
			}
			System.out.println();*/
			
			int this_chords_score = score_chord(next_chord);
		        //System.out.println(this_chords_score);
                        score += this_chords_score;
		}
		return score;
	}
	private int[] getNextChord(NNet net, int[] currentChord)
	{
		int netInputs[] = new int[VOICES];

		netInputs[0] = currentChord[0] % 12;
		//Find the offset of the notes relative to the bass note
		for (int i = 1; i < VOICES; i++)
			netInputs[i] = currentChord[i] - currentChord[i-1];
		int netOutputs[] = net.calculate(netInputs);
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
		return currentChord;
	}
	public int score_chord(int[] chord){
		int [] results = compare_chord(chord);

		for (int c = 0; c < chord.length ; c++){
			chord[c] -= key_shift;
		}
		int min = results[0];
		for (int c = 0; c < results.length ; c++){
			if (results[c] < min)
				min = results[c];
		}
		return 7 - min;
	}
	
	public int[] compare_chord(int[] chord){
		int[] result = new int[basic_flat_chords.length];
			
		int[] flat_chord = flatten_chord(chord);
				
		for (int r = 0; r < basic_flat_chords.length ; r++){ // row
			int[] chord_comparison = new int[12]; 
			for (int c = 0; c < basic_flat_chords[r].length ; c++){ // column
				chord_comparison[c] = basic_flat_chords[r][c] ^ flat_chord[c];
			}
			result[r] = sum(chord_comparison);
		}
		return result;
	}
	
	public int[] flatten_chord(int[] chord){
		int[] flat_chord = new int[12];
	
		for (int c = 0; c < chord.length ; c++){ 
			flat_chord[chord[c] % 12] = 1;
		}
		return flat_chord;
	}
	private int sum(int[] array){
		int sum_r = 0;
		for (int c = 0; c < array.length ; c++){
			sum_r += array[c];
		}
		return sum_r;
	}
	private int invert(int inta){
		if (inta == 0) return 1; else return 0;
	}
	
}
