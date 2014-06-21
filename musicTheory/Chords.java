package musicTheory;

public class Chords {
	
	int root = 60;
	int third = 60;
	int fifth = 60;
	int octave = 60;
	
	public Chords(int r, int t, int f){
		root = r;
		third = t;
		fifth = f;
		octave = root + 12;
	}
	
	public void ChangeChord(int r, int t, int f){
		root = r;
		third = t;
		fifth = f;
		octave = root + 12;
	}
	
	public void ChangeChord(int change){
		root += change;
		third += change;
		fifth += change;
		octave += change;
	}
	
	public int Compare(int[] m){
		int matches = 0;
		int r = root % 12;
		int t = third % 12;
		int f = fifth % 12;
		
		for(int i = 0; i < m.length; i++){
			if((m[i] % 12) == r){
				++ matches;
				if((m[i] - root) > 12) ChangeOctave(1, true);
				if((m[i] - root) < -12) ChangeOctave(1, false);
			}
			if((m[i] % 12) == t){
				++ matches;
				if((m[i] - third) > 12) ChangeOctave(3, true);
				if((m[i] - third) < -12) ChangeOctave(3, false);
			}
			if((m[i] % 12) == f){
				++matches;
				if((m[i] - fifth) > 12) ChangeOctave(5, true);
				if((m[i] - fifth) < -12) ChangeOctave(5, false);
			}
		}		
		return matches;
	}
	
	public void ChangeOctave(int place, boolean up){
		switch(place){
		case 1:{
			if(up){
				if(octave + 12 > 100);
				else{
					root += 12;
					octave += 12;
				}
			}
			else{
				if(root - 12 < 20);
				else{
					root -= 12;
					octave -=12;
				}
			}
		}
		case 3:{
			if(up){
				if(third + 12 > 100);
				else third += 12;
			}
			else{
				if(third - 12 < 20);
				else third -=12;
			}
		}
		case 5:{
			if(up){
				if(fifth + 12 > 100);
				else fifth += 12;
			}
			else{
				if(fifth - 12 < 20);
				else fifth -= 12;
			}
		}
		}
	}
	
	public int[] GetChord(){
		int[] chord = {root, third, fifth, octave};
		return chord;
	}
}
