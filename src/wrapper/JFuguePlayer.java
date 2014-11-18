package wrapper;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.jfugue.Player;
import org.jfugue.Pattern;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * PlayerWrapper.java
 * This is a wrapper to the optional JFugue Player class
 */

public class JFuguePlayer{
    /**
     * Plays the Song
     * @param tempo : desired tempo
     * @param song : string representation of song to be played
     */
    public void play(int tempo, String song){
        String INSTRUMENT = "Piano";

        String playable_song = "T" + tempo + " I[" + INSTRUMENT + "] " + song;

        System.out.println(playable_song);
        Pattern pattern = new Pattern();
        pattern.setMusicString(playable_song);
        Player player = new Player();
        player.play(pattern);

        //Gives user option to save song as midi; loops in case cancels exit
        boolean exit = false;
        do{
            System.out.println("\nWould you like to save this song? (y or n)");
            Scanner in = new Scanner(System.in);
            String save = in.nextLine();
            if (save.equalsIgnoreCase("y")){
                exit = true;
                export(player, pattern);
            }
            else{
                System.out.println("Data will be lost. Are you sure?");
                in = new Scanner(System.in);
                save = in.nextLine();
                if(save.equalsIgnoreCase("y")) exit = true;
            }
        }while(!exit);
    }

   /**
     * Saves generated song to a midi file for later playback
     * @param player : Player object
     * @param pattern : Pattern object
     */
    public void export(Player player, Pattern pattern){
        Scanner sc = new Scanner(System.in);
        System.out.println("Type a name for the song");
        String songName = sc.next() + ".mid";
        pattern.setMusicString(this.toString());
        File outFile = new File(songName);
        try {
            player.saveMidi(pattern, outFile);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
