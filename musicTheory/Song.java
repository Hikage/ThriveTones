package musicTheory;

/*
 * Created on Mar 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class Song {
	
	Melody tune;
	Accompaniment accom;		//chords
	
	/**
	 * Constructor; establishes the melodic tune and chordal accompaniment
	 * @param Mm - major or minor
	 * @param l - length
	 * @param f - flexibility
	 * @param notes - number of notes per measure
	 * @param key1 - letter of key
	 * @param key2 - sharp or flat
	 */
	public Song(boolean Mm, int l, int f, int notes, char key1, char key2){
		tune = new Melody(Mm, l, f, notes);
		accom = new Accompaniment(Mm, tune, notes);	
		Modulate(key1, key2);
	}
	
	/**
	 * Modulates song from base of C to specified key
	 * @param key - letter of key
	 * @param acci - accidental (sharp or flat)
	 */
	public void Modulate(char key, char acci){
		System.out.println("key = " + key + acci);
		int change = 0;
		switch(key){
		case 'a': change = -3; break;
		case 'b': change = -1; break;
		case 'd': change = 2; break;
		case 'e': change = 4; break;
		case 'f': change = 5; break;
		case 'g': change = -5; break;
		default: change = 0;
		}
		
		switch(acci){
		case '#': change++; break;
		case 'b': change--; break;
		}
		
		if(change != 0){
			System.out.println("Changing keys");
			tune.Modulate(change);
			accom.Modulate(change);
		}
	}
	
	//returns the melody without accompaniment
	public int[] justMelody(){
		return tune.getMelody();
	}
	
	//returns chords
	public int[][] getChords(){
		int accomLength = accom.getAccompaniment().length;
		int[][]chords = new int[accomLength][4];
		for(int i = 0; i < accomLength; i++){
			for(int j = 0; j < 4; j++)
				chords[i][j] = accom.getAccompaniment()[i].GetChord()[j];
		}
		return chords;
	}
}
