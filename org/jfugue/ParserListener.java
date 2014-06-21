package org.jfugue;

/**
 * Classes that implement ParserListener and add themselves as listeners
 * to a <code>Parser</code> object will receive events when
 * the <code>Parser</code> inteprets tokens from a Music String.
 * @see Parser
 *
 *@author David Koelle
 *@version 2.0
 */
public interface ParserListener
{
    /**
     * Called when the parser encounters a voice event.
     * @param voice the event that has been parsed
     */
    public void voiceEvent(Voice voice);

    /**
     * Called when the parser encounters a tempo event.
     * @param tempo the event that has been parsed
     */
    public void tempoEvent(Tempo tempo);

    /**
     * Called when the parser encounters a instrument event.
     * @param instrument the event that has been parsed
     */
    public void instrumentEvent(Instrument instrument);

    /**
     * Called when the parser encounters a controller event.
     * @param controller the event that has been parsed
     */
    public void controllerEvent(Controller controller);

    /**
     * Called when the parser encounters an initial note event.
     * @param note the event that has been parsed
     */
    public void noteEvent(Note note);

    /**
     * Called when the parser encounters a sequential note event.
     * @param note the event that has been parsed
     */
    public void sequentialNoteEvent(Note note);

    /**
     * Called when the parser encounters a parallel note event.
     * @param note the event that has been parsed
     */
    public void parallelNoteEvent(Note note);
}
