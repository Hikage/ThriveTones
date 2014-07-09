package markovChords;
import java.util.ArrayList;
import java.util.Random;
/*
 * Created on Mar 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class Melody {
	
	//local arrays - tones[] holds the notes of the scale to be used,
	//melody[] holds the final melodic sequence
	private int tones[] = new int[7];
	private ArrayList<Integer> melody = new ArrayList<Integer>();
	
	/**
	 * constructor - takes in user specifications and randomly creates the melody
	 * @param major - tonality
	 * @param length - length of melody (measures)
	 * @param flexibility - how much consecutive notes can vary
	 * @param beats - number of beats per measure
	 */
	public Melody (boolean major, int length, int flexibility, int beats){
		//create the scale
		Limit(major);
		
		//instance variables
		melody.add(tones[0]); //always start the tune on the tonic pitch
		int lastNote = 0;
		boolean lowerOctave = false;
		boolean higherOctave = false;
		Random rand = new Random();
		
		//for loop to fill in each note of the melody
		for(int i = 1; i< length*beats; i++){
		
			lowerOctave = false;
			higherOctave = false;
			int temp;
			
			//get the next note
			do{
				temp = rand.nextInt(flexibility*2 + 1);
				temp -= flexibility;
				temp += lastNote;
			
			
				//change octave if random number is beyond the scale
				if(temp > 6){
					temp -= 7;
					higherOctave = true;
				}			
				if(temp < 0){
					temp += 7;
					lowerOctave = true;
				}
				if(higherOctave)
					ChangeOctave(true);
				if(lowerOctave)
					ChangeOctave(false);
			}while(tones[temp]<20);
				
				//set the next note
				melody.add(tones[temp]);
				lastNote = temp;
		}
		while (lastNote % 7 != 0){
			lowerOctave = false;
			higherOctave = false;
		
			//get the next note
			int temp = rand.nextInt(flexibility*2);
			temp -= flexibility;
			temp += lastNote;
		
			//change octave if random number is beyond the scale
			if(temp > 6){
				temp -= 7;
				higherOctave = true;
			}			
			if(temp < 0){
				temp += 7;
				lowerOctave = true;
			}
			if(higherOctave)
				ChangeOctave(true);
			if(lowerOctave)
				ChangeOctave(false);
		
			//set the next note
			melody.add(tones[temp]);
			lastNote = temp;
		}
		while(melody.size() % beats != 0){
			melody.add(tones[lastNote]);
		}
	}
	
	/**
	 * sets up the scale to be used based on if user wants major or minor
	 * @param m - major or minor
	 */
	public void Limit(boolean m){
		if(m){
			int temp[] = {60, 62, 64, 65, 67, 69, 71};
			tones = temp;
		}		
		else{
			int temp[] = {60, 62, 63, 65, 67, 68, 71};
			tones = temp;
		}
	}
	
	/**
	 * changes the scale up or down an octave
	 * @param up - whether up or down
	 */
	public void ChangeOctave(boolean up){
		if(up){
			for(int i = 0; i<7; i++){
				tones[i] += 12;
			}
		}
		else{
			for(int i = 0; i<7; i++){
				tones[i] -= 12;
			}
		}
	}
	
	/**
	 * @return - returns melody sequence
	 */
	public int[] getMelody(){
		int[] m = new int[melody.size()]; 
		for(int i = 0; i < melody.size(); i++){
			m[i] = melody.get(i);
		}
		return m;
	}
	
	/**
	 * modulates the melody according to established key
	 * @param change - number by which each note must change
	 */
	public void Modulate(int change){
		for(int i = 0; i < melody.size(); i++){
			int t = melody.get(i) + change;
			melody.remove(i);
			melody.add(i, t);
		}
	}
}
