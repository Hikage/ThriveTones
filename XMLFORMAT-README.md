Hooktheory Data Format
=======

Copyright © 2014 Brianna Shade
bshade@pdx.edu
Portland State University

Data structure property of the Hooktheory project
www.hooktheory.com

This program is licensed under the "MIT License"
Please see the file COPYING in the source distribution of this software for license terms.

Hooktheory raw data contains an entry for each song segment, each with a set of attributes to describe aspects of the song and the segment's chord progression.

### Data Fields
##### artist (text)
Song's musical artist  
*Example: "Jimmy Eat World"*

##### song (text)
Title of the song  
*Example: "The Middle"*

##### section (text)
Song segment  
*Examples: "Intro and Verse", "Chorus", "Bridge"*

##### SIF (text)
Search Identifier Full  
Comma-delimited, relative (key-independent) search identifiers representing chord progressions, not including melodies  
Search identifiers are shorthand notation for a chord and its properties and are designed to be easily parsed.  Search identifiers always refer to chord progressions transposed to their relative major mode.  This facilitates analyses to compare chord changes across modes.  A search identifier has the following format:  

**[mode]scale_degree[figured_bass][embellishment][-duration][/applied_target]**
* **mode** (optional): chord-specific, allows for a chord to be borrowed from a parallel mode/key to the song's original key
    Possible values: b (Aeolian/Minor), L(ydian), D(orian), M(ixolydian), Y (Phrygian), C (Locrian); S(n) - scale represented by the scale_degree n  
    To interpret a chord with a mode identifier:  
    * switch the song's key to the parallel mode indicated (with the same tonic)  
    * play the resulting scale_degree chord (see **scale_degree** below)  

    *Examples (key of C maj):*  
    * *b4 -> switch the key to the mode indicated (minor) - c min - and play the resulting iv chord = fm*  
    * *M5 -> switch the key to mixolydian mode, and play the resulting v chord = gm*  
    * *D3 -> switch the key to dorian mode, and play the resulting bIII chord = Eb*  
    
    S(n) first switches the song to the mode indicated by the scale_degree, then plays the scale_degree chord with the new tonality.  The most commonly encountered are S(3) in a minor key to access the relative major key  
    *Examples (key of C maj):*
    * *S(6)1 -> identify the tonality of the scale represented within the parentheses, using the same notes of the original key (aeolian/minor); transpose this scale to have the same tonic as the original (C), then play the resulting scale_degree i chord = cm*
    * *S(2)3 -> identify the tonality of the scale from the 2nd scale_degree (dorian), transpose, then play the resulting bIII chord = Eb*
    * *S(7)1 -> identify the tonality of 7 (locrian), transpose, then play the resulting iº chord = cº*
    
    The lack of an identifier signifies the chord has a tonality natural to the original key  
* **scale_degree**: root of the chord relative to the major key  
    Possible values: 1-7, or "rest"  
    If the value is "rest", the only other valid identifier is duration  
    Degrees are transposed to the relative major chord.  The true degree of the chord can be calculated by subtracting the song mode (see **mode** below - note this is *different* from the chord-specific mode described above) from the scale_degree value, and offsetting back up by 1  
    *Examples (key of C):*
    * *Song mode 6 = minor; scale_degree 6 = i chord = cm*
    * *Song mode 2 = dorian; scale degree 1 = bVII chord = Bb*
    * *Song mode 5 = mixolydian; scale degree 2 = v chord = gm*
* **figured_base** (optional): chord inversion  
    This identifier specifies whether the chord is a triad or a 7 chord, as well as its inversion.  If there are no figured bass identifiers, the chord is a triad in root position (53). Figured bass is notated in accordance with standard classical music theory, as outlined here: http://en.wikipedia.org/wiki/Figured_bass#Figured_bass_notation  
    Possible values: 6 (1st-inversion triad), 64 (2nd-inversion triad), 7 (root-position 7 chord, assumed to be of the tonality that matches the original scale), 65 (1st-inversion 7 chord), 43 (2nd-inversion 7 chord), 42 (3rd-inversion 7 chord)
* **embellishment** (optional): additional or suspended notes  
    Possible values: sus2, sus4, sus42, add9  
    A lack of identifier indicates that the chord is not embellished
* **-duration** (optional): chord's duration in number of beats  
    Defaults to 4 when unspecified  
    When included, preceded by a hyphen ("-")  
    Minimum value: 0.25  
    *Example: 8*
* **/applied_target**: indicates the chord is an applied (borrowed) chord (secondary dominant (V/n), secondary subdominant (IV/n), or secondary leading-tone (viiº/n) of a scale_degree n, excluding 7), most commonly followed by the scale_degree to which it is applied for secondary dominants and leading tones, and by the associated secondary dominant for secondary subdominants  
    The applied target consists of two parts: the first must be either a 4, 5, or a 7, and the second is the scale_degree specifying the type of applied chord  
    When included, preceded by a forward slash ("/")  
    *Examples (key of C major):*
    * *5/5 (a "V of V" chord) = a V chord of the major key of G = D (most commonly followed by a G chord)*
    * *7/2 (a "viiº of ii" chord) = a viiº chord of the major key of D = cº (most likely to resolve to a dm chord)*
    * *4/6 (a "IV of vi" chord) = a IV chord of the major key of A = E (most likely to be succeeded by a 5/6 = F#, followed by am)*

Examples:
* 1-4,5-2,6-2,4-4 = I for 4 beats, V for 2 beats, vi for 2 beats, IV for 4 beats  
* 5 = V for 4 beats  
* 16-3 = I in 1st inversion for 3 beats  
* 542-4/2 = V7/ii in 3rd inversion for 4 beats
* b665 = VI7 in first inversion borrowed from the Minor mode for 4 beats  
* M5sus4 = v sus4 of the borrowed Mixolydian mode for 4 beats  
* S(6)37 (assuming a song mode of 1) = III7 chord borrowed from the key of the relative vi key (minor) for 4 beats

##### beatsInMeasure (integer)  
The number of beats in each measure  
This does not take into account variable-sized measures, nor does it differentiate between time signatures such as 6 8 versus 6 4, as it does not specify the note unit value.  However, this is not imperative, as all timing proportions are retained; only the beats per minute could be skewed by a misinterpretation  
*Example: 4*

##### songKey (text, max length: 2)
The song's key  
Represented by a capital letter from A to G, followed by an optional sharp or flat.  This accidental can be represented with #, s, b, or f.  
*Example: "Eb"*

##### bpm (integer)
Beats Per Minute  
Denotes the tempo of the song.  
*Example: 146*

##### mode (integer enumeration 1-7)
Tonic relation to the major scale.  For example, 1 represents a major key, while 6 represents minor, 2 represents Dorian, etc  
Numerical representation of modern musical mode of the song, as outlined here: http://en.wikipedia.org/wiki/Mode_(music)#Modern  
*Example: 1*

### Site example
One example from Hooktheory's TheoryTab project is as follows.  This is how it appears on the site (http://www.hooktheory.com/theorytab/view/lady-gaga/poker-face):  
![Hooktheory Example Screenshot](https://github.com/Hikage/MIDI_AI/blob/master/HooktheorySongExample.jpg "Hooktheory Example Screenshot")  

and this is how it is represented within the datafeed:

    <row>  
        <field name="artist"> Lady Gaga</field>
        <field name="song">Poker Face</field>
        <field name="section">Chorus</field>
        <field name="SIF">,6-1,6-1,6-1,6-1,4-1,4-1,4-1,4-1,1-1,1-1,1-1,1-1,5-1,5-1,5-1,5-1,</field>
        <field name="beatsInMeasure">4</field>
        <field name="songKey">G#</field>
        <field name="bpm">118</field>
        <field name="mode">6</field>
    </row>

The key is set at G#, with the mode 6 (aeolian - minor).  All chords of the SIF are in terms of the key's relative major key (in this case, B major).  Each chord's scale degree is that in relation to new root.  A "6" chord, then, would be the vi chord of B major = g# minor.  All chord tonalities retain their values relative to the major key (i.e.: I, ii, iii, IV, V, vi, viiº), unless specified otherwise with a chord mode (borrowed) or applied target.  

The chords translate as follows:

6-1: i for 1 beat  
6-1: i for 1 beat  
6-1: i for 1 beat  
6-1: i for 1 beat  
4-1: VI for 1 beat  
4-1: VI for 1 beat  
4-1: VI for 1 beat  
4-1: VI for 1 beat  
1-1: III for 1 beat  
1-1: III for 1 beat  
1-1: III for 1 beat  
1-1: III for 1 beat  
5-1: VII for 1 beat  
5-1: VII for 1 beat  
5-1: VII for 1 beat  
5-1: VII for 1 beat  

Another, more interesting example:  
"Skyfall" - Adele (chorus)  
(http://www.hooktheory.com/theorytab/view/adele/skyfall#chorus)  
![Hooktheory Complex Example Screenshot] (https://github.com/Hikage/MIDI_AI/blob/master/HooktheoryComplexSongExample.jpg "Hooktheory Complex Example Screenshot")  

and this is the data representation:

    <row>  
        <field name="artist">Adele</field>
        <field name="song">Skyfall</field>
        <field name="section">Chorus</field>
        <field name="SIF">,rest,6-3,M642-1,4-3,442-1,2-3,66-1,364sus4-2,S(3)3-2,6-3,M642-1,4-3,442-1,S(3)2-3,66-1,364sus4-2,S(3)3-2,6-2,4-2,L27-2,4-2,6-2,4-2,L27-2,4-2,</field>
        <field name="beatsInMeasure">4</field>
        <field name="songKey">C</field>
        <field name="bpm">76</field>
        <field name="mode">6</field>
    </row>
    
The chords translate as follows (key of c minor):  

rest for 4 beats  
6-3: i (cm) for 3 beats  
M642-1: i7 (cm7) in 3rd inversion for 1 beat  
4-3: VI (A) for 3 beats  
442-1: VI7 (A7) in 3rd inversion for 1 beat  
2-3: iv (fm) for 3 beats  
66-1: i (cm) in 1st inversion for 1 beat  
364sus4-2: v (gm) in 2nd inversion, with a sustained 4, for 2 beats  
S(3)3-2: V (G) for 2 beats  
6-3: i (cm) for 3 beats  
M642-1: i7 (cm7) in 3rd inversion for 1 beat  
4-3: VI (A) for 3 beats  
442-1: VI7 (A7) in 3rd inversion for 1 beat  
S(3)2-3: IV (F) for 3 beats  
66-1: i (cm) in 1st inversion for 1 beat  
364sus4-2: v (gm) in 2nd inversion, with a sustained 4, for 2 beats  
S(3)3-2: V (G) for 2 beats  
6-2: i (cm) for 2 beats  
4-2: VI (A) for 2 beats  
L27-2: IV7 (F7) for two beats  
4-2: VI (A) for 2 beats  
6-2: i (cm) for 2 beats  
4-2: VI (A) for 2 beats  
L27-2: IV7 (F7) for two beats  
4-2: VI (A) for 2 beats
