package musicTheory;

import java.util.ArrayList;

/**
 * @author Brianna Jarvis
 *
 */
public class Accompaniment {
	//major = 60, 62, 64, 65, 67, 69, 71
	//minor = 60, 62, 63, 65, 67, 68, 71
	
	//establishing different chordal types
	Chords tonic = new Chords(60, 64, 67);
	Chords two = new Chords(62, 65, 69);
	Chords three = new Chords(64, 67, 71);
	Chords subdominant = new Chords(65, 69, 72);
	Chords dominant = new Chords(67, 71, 74);
	Chords six = new Chords(69, 72, 76);
	Chords subtonic = new Chords(71, 74, 77);
	Chords[] accompaniment;				//final chordal progression
	int previous = 0;
	
	/**
	 * Creates chordal accompaniment
	 * @param Mm - tonality
	 * @param m - associated melody
	 * @param n - beats per measure
	 */
	public Accompaniment(boolean Mm, Melody m, int n){
		//adjust for minor tonality
		if(!Mm){
			tonic.ChangeChord(60, 63, 67);
			two.ChangeChord(62, 65, 68);
			three.ChangeChord(63, 67, 71);
			subdominant.ChangeChord(65, 68, 72);
			six.ChangeChord(68, 72, 77);
			subtonic.ChangeChord(70, 74, 77);
		}
	
		//get melody and divide across designated measures
		int[] melody = m.getMelody();
		int newLength = melody.length / n;
		if(melody.length % n != 0) ++newLength;
		
		accompaniment = new Chords[newLength];
		int[] notes = new int[n];
		int q = 0;
		for(int j = 0; j < melody.length; j += n){
			int k = 0;
			for(int i = j; i < j+n; i++){
				if(i >= melody.length){
					notes[k] = notes[k-1];
				}
				else{
					notes[k] = melody[j];
				}
				System.out.println("notes[k] = " + notes[k]);
				k++;
			}
			accompaniment[q] = FindBestFit(notes);
			q++;
		}
	}
	
	/**
	 * Finds the best fit for a chord to match most notes in a measure
	 * @param m - melody sequence
	 * @return - returns the proper chord
	 */
	public Chords FindBestFit(int[] m){
		
		int fit1 = tonic.Compare(m);
		int fit2 = two.Compare(m);	
		int fit3 = three.Compare(m);
		int fit4 = subdominant.Compare(m);
		int fit5 = dominant.Compare(m);
		int fit6 = six.Compare(m);
		int fit7 = subtonic.Compare(m);
		
		int[] fits = {fit1, fit2, fit3, fit4, fit5, fit6, fit7};
		
		int[] topThree = TopThree(fits);
		for(int i = 0; i<3; i++){
			System.out.println("topThree at " + i + " = " + topThree[i]);
		}
		
		int best = Progression(topThree);
		int ch = topThree[best];
		previous = ch; 
		switch(ch){
		case 2: return two;
		case 3: return three;
		case 4: return subdominant;
		case 5: return dominant;
		case 6: return six;
		case 7: return subtonic;
		default: return tonic;
		}
	}
	
	/**
	 * Returns the best three fits for chords to be assessed against progression
	 * @param fits
	 * @return
	 */
	public int[] TopThree(int[] fits){
		int[] list = new int[3];
		int highest;
		int topPlace;
		for(int j = 0; j < 3; j++){
			highest = 0;
			topPlace = 0;
			for(int i = 0; i < fits.length; i++){
				for(int z = 0; z < j; z++){
					if(i == list[z]){
						i++;
					}
				}
				if(i >= fits.length) break;
				
				if(fits[i] > highest){
					highest = fits[i];
					topPlace = i;
				}	
			}
		list[j] = topPlace + 1;
		fits[topPlace] = 0;
		}
		return list;
	}
	
	/**
	 * Finds the best fit out of the top three according to progression rules
	 * @param tops
	 * @return
	 */
	public int Progression(int[] tops){
		System.out.println("previous = " + previous);
		switch(previous){
		//ii -> V, ii -> vii
		case 2:{
			if(tops[0] == 5 || tops[0] == 7) return 0;
			if(tops[1] == 5 || tops[1] == 7) return 1;
			if(tops[2] == 5 || tops[2] == 7) return 2;
			return 0;
		}
		//iii -> vi
		case 3:{
			if(tops[1] == 6) return 1;
			if(tops[2] == 6) return 2;
			return 0;
		}
		//IV -> V, IV -> I, IV -> ii, IV -> vii
		case 4:{
			if(tops[0] == 5 || tops[0] == 1 || tops[0] == 2 || tops[0] == 7) return 0;
			if(tops[1] == 5 || tops[1] == 1 || tops[1] == 2 || tops[1] == 7) return 1;
			if(tops[2] == 5 || tops[2] == 1 || tops[2] == 2 || tops[2] == 7) return 2;
			return 0;
		}
		//V -> I, V -> vi, V -> vii
		case 5:{
			if(tops[0] == 1 || tops[0] == 6 || tops[0] == 7) return 0;
			if(tops[1] == 1 || tops[1] == 6 || tops[1] == 7) return 1;
			if(tops[2] == 1 || tops[2] == 6 || tops[2] == 7) return 2;
			return 0;
		}
		//vi -> IV, vi -> ii
		case 6:{
			if(tops[0] == 4 || tops[0] == 2) return 0;
			if(tops[1] == 4 || tops[1] == 2) return 1;
			if(tops[2] == 4 || tops[2] == 2) return 2;
			return 0;
		}
		//vii -> I
		case 7:{
			if(tops[1] == 1) return 1;
			if(tops[2] == 2) return 2;
			return 0;
		}
		//I -> anything
		default: return 0;
		}
	}	
	
	/**
	 * Adjusts accompaniment to designated key
	 * @param change - number by which to change the notes
	 */
	public void Modulate(int change){
		for(int i = 0; i < accompaniment.length; i++){
			accompaniment[i].ChangeChord(change);
		}
	}
	
	/**
	 * @return - Returns the accompaniment sequence
	 */
	public Chords[] getAccompaniment(){
		return accompaniment;
	}
}
