package org.jfugue;

/**
 * This class represents a segment of music.  By representing segments of music
 * as patterns, JFugue gives users the opportunity to play around with pieces
 * of music in new and interesting ways.  Patterns may be added together, transformed,
 * or otherwise manipulated to expand the possibilities of creative music.
 *
 * @author David Koelle
 * @version 2.0
 */
public class Pattern
{
    private String musicString;

    /**
     * Instantiates a new pattern
     */
    public Pattern()
    {
        this.musicString = new String();
    }

    /**
     * Instantiates a new pattern using the given music string
     * @param s the music string
     */
    public Pattern(String s)
    {
        this.musicString = new String(s);
    }

    /**
     * Sets the music string kept by this pattern.
     * @param s the music string
     */
    public void setMusicString(String s)
    {
        this.musicString = s;
    }

    /**
     * Returns the music string kept in this pattern
     * @return the music string
     */
    public String getMusicString()
    {
        return this.musicString;
    }

    /**
     * Adds an additional pattern to the end of this pattern.
     * @param pattern the pattern to add
     */
    public void add(Pattern pattern)
    {
        this.musicString = this.musicString + " " + pattern.getMusicString();
    }

    /**
     * Adds a music string to the end of this pattern.
     * @param musicString the music string to add
     */
    public void add(String musicString)
    {
        this.musicString = this.musicString + " " + musicString;
    }

    /**
     * Adds an individual element to the pattern.  This takes into
     * account the possibility that the element may be a sequential or
     * parallel note, in which case no space is placed before it.
     * @param element the element to add
     */
    public void addElement(JFugueElement element)
    {
        String elementMusicString = element.musicString();

        // Don't automatically add a space if this is a continuing note event
        if ((elementMusicString.charAt(0) == '+') ||
            (elementMusicString.charAt(0) == '_')) {
            this.musicString = this.musicString + elementMusicString;
        } else {
            this.musicString = this.musicString + " " + elementMusicString;
        }
    }
}
