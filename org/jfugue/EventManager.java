package org.jfugue;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Places musical data into the MIDI sequence.
 *
 *@author David Koelle
 *@version 2.0
 */
public class EventManager
{
    private byte currentTrack = 0;
    private final int CHANNELS = 16;
    private long time[] = new long[CHANNELS];
    private Sequence sequence;
    private Track track[] = new Track[CHANNELS];

    public EventManager()
    {
        try {
            sequence = new Sequence(Sequence.PPQ,120);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i=0; i < CHANNELS; i++) {
            time[i] = 0;
            track[i] = sequence.createTrack();
        }
        currentTrack = 0;
    }

    /**
     * Sets the current track, or channel, to which new events will be added.
     * @param track the track to select
     */
    public void setCurrentTrack(byte track)
    {
        currentTrack = track;
    }

    /**
     * Advances the timer for the current track by the specified duration,
     * which is specified in Pulses Per Quarter (PPQ)
     * @param duration the duration to increase the track timer
     */
    public void advanceTrackTimer(long duration)
    {
        time[currentTrack] += duration;
    }

    /**
     * Sets the timer for the current track by the given time,
     * which is specified in Pulses Per Quarter (PPQ)
     * @param newTime the time at which to set the track timer
     */
    public void setTrackTimer(long newTime)
    {
        time[currentTrack] = newTime;
    }

    /**
     * Returns the timer for the current track.
     * @return the timer value for the current track, specified in Pulses Per Quarter (PPQ)
     */
    public long getTrackTimer()
    {
        return time[currentTrack];
    }

    /**
     * Adds a MIDI event to the current track.  If the command passed to
     * addEvent is a ShortMessage.NOTE_ON command, then this method will
     * automatically add a ShortMessage.NOTE_OFF command for the note,
     * using the duration parameter to space the NOTE_OFF command properly.
     *
     * @param command the MIDI command represented by this message
     * @param data1 the first data byte
     * @param data2 the second data byte
     * @param duration for Note events, the duration of the note
     */
    public void addEvent(int command, int data1, int data2, long duration)
    {
        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(command,currentTrack,data1,data2);
            MidiEvent event = new MidiEvent(message,time[currentTrack]);
            track[currentTrack].add(event);

            advanceTrackTimer(duration);

            if (command == ShortMessage.NOTE_ON) {
                ShortMessage message2 = new ShortMessage();
                message2.setMessage(ShortMessage.NOTE_OFF,currentTrack,data1,data2);
                MidiEvent event2 = new MidiEvent(message2,time[currentTrack]);
                track[currentTrack].add(event2);
            }
        } catch (Exception e)
        {
            System.out.println("JFugue addEvent Exception");
        }
    }

    /**
     * Adds a ShortMessage.NOTE_ON event to the current track, using attack and
     * decay velocity values.  This method will
     * automatically add a ShortMessage.NOTE_OFF command for the note,
     * using the duration parameter to space the NOTE_OFF command properly.
     *
     * @param command the NOTE_ON command.  If another command is given, this
     * method will call addEvent(command, data1, data2, duration)
     * @param data1 the first data byte, which contains the note value
     * @param data2 the second data byte for the NOTE_ON event, which contains the attack velocity
     * @param data3 the second data byte for the NOTE_OFF event, which contains the decay velocity
     * @param duration the duration of the note
     */
    public void addEvent(int command, int data1, int data2, int data3, long duration)
    {
        if (command != ShortMessage.NOTE_ON) {
            addEvent(command, data1, data2, duration);
            return;
        }

        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(command,currentTrack,data1,data2);
            MidiEvent event = new MidiEvent(message,time[currentTrack]);
            track[currentTrack].add(event);

            advanceTrackTimer(duration);

            ShortMessage message2 = new ShortMessage();
            message2.setMessage(ShortMessage.NOTE_OFF,currentTrack,data1,data3);
            MidiEvent event2 = new MidiEvent(message2,time[currentTrack]);
            track[currentTrack].add(event2);
        } catch (Exception e)
        {
            System.out.println("JFugue addEvent Exception");
        }
    }

    /**
     * Returns the current sequence, which is a collection of tracks.
     * If your goal is to add events to the sequence, you don't want to use this method to
     * get the sequence; instead, use the addEvent methods to add your events.
     * @return the current sequence
     */
    public Sequence getSequence()
    {
        return sequence;
    }
}