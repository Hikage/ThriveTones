package org.jfugue;

import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;

/**
 * This class takes a pattern, and turns it into wonderful music.
 *
 * <p>
 * Playing music is thing you can do to render a pattern.  You could
 * create your own rendrer that draws sheet music based on a pattern.
 * Or, you could create a graphical light show based on the musical
 * notes in the pattern.
 * </p>
 *
 *@author David Koelle
 *@version 2.0
 */
public class Renderer implements ParserListener
{
    EventManager eventManager;
    Parser parser;
    long initialNoteTime = 0;

    /**
     * Instantiates a Renderer
     */
    public Renderer()
    {
        int resolution = 500;
        this.eventManager = new EventManager();
        this.parser = new Parser();
        this.parser.addParserListener(this);
    }

    /**
     * Starts rendering a pattern by sending it to the parser and listening
     * for parser events to be fired when tokens from the pattern are interpreted.
     * @param pattern the pattern to render
     */
    public Sequence render(Pattern pattern)
    {
        // Send the pattern to the parser.  The renderer will receive musicEvents,
        // which will put the events into the tracks
        try {
            parser.parse(pattern);
        } catch (Exception e)
        {
            System.out.println("Exception while parsing music");
            e.printStackTrace();
        }
        return this.eventManager.getSequence();
    }

    // ParserListener methods
    ////////////////////////////

    public void voiceEvent(Voice voice)
    {
        this.eventManager.setCurrentTrack(voice.getVoice());
    }

    public void tempoEvent(Tempo tempo)
    {
        this.parser.setTempo(tempo.getTempo());
    }

    public void instrumentEvent(Instrument instrument)
    {
        this.eventManager.addEvent(ShortMessage.PROGRAM_CHANGE,instrument.getInstrument(),0,0);
    }

    public void controllerEvent(Controller controller)
    {
        this.eventManager.addEvent(ShortMessage.CONTROL_CHANGE,controller.getIndex(),controller.getValue(),0);
    }

    public void noteEvent(Note note)
    {
        // Remember the current track time, so we can flip back to it
        // if there are other notes to play in parallel
        this.initialNoteTime = this.eventManager.getTrackTimer();
        long duration = note.getDuration();

        // Add messages to the track
        if (note.isRest()) {
            this.eventManager.advanceTrackTimer(note.getDuration());
        } else {
            initialNoteTime = eventManager.getTrackTimer();
            byte attackVelocity = note.getAttackVelocity();
            byte decayVelocity = note.getDecayVelocity();
            this.eventManager.addEvent(ShortMessage.NOTE_ON,note.getValue(),attackVelocity,decayVelocity,duration);
        }
    }

    public void sequentialNoteEvent(Note note)
    {
        long duration = note.getDuration();
        if (note.isRest()) {
            this.eventManager.advanceTrackTimer(note.getDuration());
        } else {
            byte attackVelocity = note.getAttackVelocity();
            byte decayVelocity = note.getDecayVelocity();
            this.eventManager.addEvent(ShortMessage.NOTE_ON,note.getValue(),attackVelocity,decayVelocity,duration);
        }
    }

    public void parallelNoteEvent(Note note)
    {
        long duration = note.getDuration();
        this.eventManager.setTrackTimer(this.initialNoteTime);
        if (note.isRest()) {
            this.eventManager.advanceTrackTimer(note.getDuration());
        } else {
            byte attackVelocity = note.getAttackVelocity();
            byte decayVelocity = note.getDecayVelocity();
            this.eventManager.addEvent(ShortMessage.NOTE_ON,note.getValue(),attackVelocity,decayVelocity,duration);
        }
    }
}
