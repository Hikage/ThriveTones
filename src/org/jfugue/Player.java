package org.jfugue;

import java.io.File;

import javax.sound.midi.*;

/**
 * Prepares a pattern to be turned into music by the Renderer.  This class
 * also handles saving the sequence derived from a pattern as a MIDI file.
 *
 *@see Renderer
 *@see Pattern
 *@author David Koelle
 *@version 2.0
 */
public class Player
{
    private Sequencer sequencer;

    /**
     * Instantiates a new Player object.
     */
    public Player()
    {
        try {
            // Get default sequencer.
            sequencer = MidiSystem.getSequencer();
            if (sequencer == null) {
                // Error -- sequencer device is not supported.
                // Inform user and return...
                System.out.println("Error: Sequencer device is not supported");
            } else {
                // Acquire resources and make operational.
                sequencer.open();
           }

        } catch (Exception e) {
        }
    }

    /**
     * Plays a pattern by setting up a Renderer and feeding the pattern to it.
     * @param pattern the pattern to play
     * @see Renderer
     */
    public void play(Pattern pattern)
    {
        Renderer renderer = new Renderer();
        final Sequence sequence = renderer.render(pattern);

        // Play the sequence
        try {
            sequencer.setSequence(sequence);
        } catch (Exception e)
        {
            System.out.println("Exception while playing music");
            e.printStackTrace();
        }

        Player.this.sequencer.addMetaEventListener(
            new MetaEventListener() {
                public void meta(MetaMessage event) {
                    if (event.getType() == 47) {
                        Player.this.sequencer.stop();
                    }
                }
            }
        );

        Player.this.sequencer.start();
        try {
            Thread.sleep(sequence.getMicrosecondLength() / 1000);
        } catch (Exception e)
        {    }
    }

    /**
     * Saves the MIDI data from a pattern into a file.
     * @param pattern the pattern to save
     * @filename the name of the file to save the pattern to.  Should include file extension, such as .mid
     */
    public void save(Pattern pattern, String filename)
    {
        Renderer renderer = new Renderer();
        Sequence sequence = renderer.render(pattern);

        int[] writers = MidiSystem.getMidiFileTypes(sequence);
        if (writers.length == 0) return;

        try {
            MidiSystem.write(sequence,writers[0],new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the sequencer containing the MIDI data from a pattern that has been parsed.
     * @return the Sequence from the pattern that was recently parsed
     */
    public Sequencer getSequencer()
    {
        return this.sequencer;
    }
}


