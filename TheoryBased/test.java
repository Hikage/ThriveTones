package TheoryBased;
import NN.NNet;
import NN.Song;
import org.jfugue.*;
//import NN.Population;
import java.util.Random;

//hack test example 
public class test {
		
	
	public static void main(String[] args){
		
		scorrer scoreri = new scorrer(); 
		/*
		int[][] test_chords = { {60, 64, 67, 72},
								{62, 65, 69, 74}};

		for (int r = 0; r < test_chords.length ; r++){
			System.out.println(scoreri.score_chord( test_chords[r]  ));
			for (int c = 0; c < test_chords[r].length ; c++){
				System.out.print( test_chords[r][c] + "\t");
			}
			System.out.println();
			for (int c = 0; c < test_chords[r].length ; c++){
				System.out.print( test_chords[r][c]%12  + "\t");
			}
			System.out.println();
			System.out.println();
		}	*/	
		
		/*
		Random rand = new Random();
		
		int[] rand_test_chord = {0,0,0,0};
		//int max = 0;
		for (int r = 0; r < 100000 ; r++){
			for (int c = 0; c < rand_test_chord.length ; c++){
				rand_test_chord[c] = rand.nextInt(128);
			}
			
			int score = scoreri.score_chord( rand_test_chord );
			//if (score > max) max = score;

			if (score == 7){
				System.out.println(score);			
				for (int c = 0; c < rand_test_chord.length ; c++){
					System.out.print( rand_test_chord[c] + "\t");
				}
				System.out.println();
				for (int c = 0; c < rand_test_chord.length ; c++){
					System.out.print( rand_test_chord[c]%12  + "\t");
				}
				System.out.println();
				System.out.println();
			}
		}
		//System.out.println();
		//System.out.println(max);
		/**/
		
		
		/*
		NNet a = new NNet();
		System.out.println(scoreri.score_NNet(a));
      		/**/
		

		/*
		Population pop = new Population();
		
                for (int r = 0; r < 10 ; r++){		   
		    pop.scoreByRules();
		    System.out.println();
		}

		/**/


		
                RPopulation rpop = new RPopulation();

                for (int r = 0; r < 10000 ; r++){
                    rpop.breedByRules();
		    if (r % 1000 == 0){
			rpop.print_fitnesses();
			System.out.println();
		    }
                }

		
                rpop.print_fitnesses();

		NNet top = rpop.get_net(0);
		Song top_song_gen = new Song(top);
		int[] start_chord = {60, 64, 67, 72};
		top_song_gen.generateSong(start_chord);
		
		System.out.println(top_song_gen.getSong());
		//top_song_gen.playSong();
		
		Pattern song = new Pattern(top_song_gen.getSong());
		Player player = new Player();
		player.play(song);

		//rpop.print_weights();


		

		/**/

		//System.out.println(" " + (1^1) + " " + (1^0) + " " + (0^1) + " " + (0^0));
		
	}
	
	
	
}
