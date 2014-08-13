Hooktheory Data Format
=======

Copyright © 2014 Brianna Shade and the Hooktheory Project  
<bshade@pdx.edu>  
Portland State University

Data structure property of the Hooktheory Project  
http://www.hooktheory.com

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
Search Identifier Full:  
Comma-delimited, relative (key-independent) search identifiers representing chord progressions, not including melodies.

Search identifiers are shorthand notation for a chord and its properties and are designed to be easily parsed.  Search identifiers always refer to chord progressions transposed to their relative major mode.  This facilitates analyses to compare chord changes across modes.  A search identifier has the following format:

> **[mode]scale_degree[figured_bass][embellishment][-duration][/applied_target]**

* **mode** (optional): Chord-specific, allows for a chord to be borrowed from a parallel mode/key to the song's original key.  
  Possible values: b (Aeolian/Minor), L(ydian), D(orian), M(ixolydian), Y (Phrygian), C (Locrian); S(n) - scale represented by the scale_degree n.

  To interpret a chord with a mode identifier:

  * Switch the song's key to the parallel mode indicated (with the same tonic as the song).
  * Play the resulting scale\_degree chord (see **scale_degree** below).

  Examples (key of C maj):

    * *b4 -> Switch the key to the mode indicated (minor) &mdash; Cm &mdash; and play the resulting iv chord = Fm.*
    * *M5 -> Switch the key to mixolydian mode, and play the resulting v chord = Gm.*
    * *D3 -> Switch the key to dorian mode, and play the resulting bIII chord = Eb.*
    
  S(n) first switches the song to the mode indicated by the scale\_degree, then plays the scale\_degree chord with the new tonality.  The most commonly encountered is S(3) in a minor key to access the relative major key.

  Examples (key of C maj):

    * *S(6)1 -> Identify the tonality of the scale represented within the parentheses, using the same notes of the original key (aeolian/minor); transpose this scale to have the same tonic as the original (C), then play the resulting scale\_degree i chord = Cm.*
    * *S(2)3 -> Identify the tonality of the scale from the 2nd scale\_degree (dorian), transpose, then play the resulting bIII chord = Eb.*
    * *S(7)1 -> Identify the tonality of 7 (locrian), transpose, then play the resulting i&deg; chord = C&deg;.*
    
  The lack of an identifier signifies the chord has a tonality natural to the original key.

* **scale\_degree**: root of the chord relative to the major key.

  Possible values: 1-7, or "rest". If the value is "rest", the only other valid identifier is duration.

  Degrees are transposed to the relative major chord.  The true degree of the chord can be calculated by subtracting the song mode (see **mode** below &mdash; note this is *different* from the chord-specific mode described above) from the scale\_degree value, and offsetting back up by 1.

  Examples (key of C):

    * *Song mode 6 = minor; scale\_degree 6 = i chord = Cm*
    * *Song mode 2 = dorian; scale\_degree 1 = bVII chord = Bb*
    * *Song mode 5 = mixolydian; scale\_degree 2 = v chord = Gm*

* **figured_base** (optional): chord inversion

  This identifier specifies whether the chord is a triad or a 7 chord, as well as its inversion.  If there are no figured bass identifiers, the chord is a triad in root position (53). Figured bass is notated in accordance with standard classical music theory, as outlined here: http://en.wikipedia.org/wiki/Figured_bass#Figured_bass_notation

  Possible values: 6 (1st-inversion triad), 64 (2nd-inversion triad), 7 (root-position 7 chord, assumed to be of the tonality that matches the original scale), 65 (1st-inversion 7 chord), 43 (2nd-inversion 7 chord), 42 (3rd-inversion 7 chord)

* **embellishment** (optional): additional or suspended notes

  Possible values: sus2, sus4, sus42, add9.

  A lack of identifier indicates that the chord is not embellished.

* **-duration** (optional): chord's duration in number of beats

  Defaults to 4 when unspecified. When included, preceded by a hyphen ("-"). Minimum value: 0.25.

  *Example: 8*

* **/applied_target** (optional): indicates the chord is an applied (borrowed) chord

  The applied chord can be the secondary dominant (V/n), secondary subdominant (IV/n), or secondary leading-tone (vii&deg;/n) of a scale\_degree n, excluding 7). The applied chord is most commonly followed by the scale\_degree to which it is applied for secondary dominants and leading tones, and by the associated secondary dominant for secondary subdominants.

  The applied target consists of two parts: the first must be either a 4, 5, or a 7, and the second is the scale\_degree specifying the type of applied chord.

  When included, the applied chord is preceded by a forward slash ("/").

  Examples (key of C major):

  * *5/5 (a "V of V" chord) = A V chord of the major key of G = D (most commonly followed by a G chord).*
  * *7/2 (a "vii&deg; of ii" chord) = A vii&deg; chord of the major key of D = C&deg; (most likely to resolve to a Dm chord).*
  * *4/6 (a "IV of vi" chord) = A IV chord of the major key of A = E (most likely to be succeeded by a 5/6 = F#, followed by Am).*

Some general SIF examples:

* 1-4,5-2,6-2,4-4 = I for 4 beats, V for 2 beats, vi for 2 beats, IV for 4 beats
* 5 = V for 4 beats
* 16-3 = I in 1st inversion for 3 beats
* 542-4/2 = V7/ii in 3rd inversion for 4 beats
* b665 = VI7 in first inversion borrowed from the Minor mode for 4 beats
* M5sus4 = v sus4 of the borrowed Mixolydian mode for 4 beats
* S(6)37 (assuming a song mode of 1) = III7 chord borrowed from the key of the relative vi key (minor) for 4 beats

##### beatsInMeasure (integer)

The number of beats in each measure.

This does not take into account variable-sized measures, nor does it differentiate between time signatures such as 6 8 versus 6 4, as it does not specify the note unit value.  However, this is not imperative, as all timing proportions are retained; only the beats-per-minute could be skewed by a misinterpretation.

*Example: 4*

##### songKey (text, max length: 2)

The song's key.

Represented by a capital letter from A to G, followed by an optional sharp or flat.  This accidental can be represented with #, s, b, or f.

*Example: "Eb"*

##### bpm (integer)

Beats per minute.

Denotes the tempo of the song.

*Example: 146*

##### mode (integer enumeration 1-7)

Tonic relation to the major scale.

For example, 1 represents a major key, while 6 represents minor, 2 represents Dorian, etc.

The mode is the numerical representation of modern musical mode of the song, as outlined here: http://en.wikipedia.org/wiki/Mode_(music)#Modern

*Example: 1*

### Full XML Examples

This section contains some XML examples from the Hooktheory TheoryTab project database, along with their rendering by the Hooktheory site.

##### Poker Face

Chorus of Poker Face by Stefani Germanotta and Nadir Khayat,
as performed by Lady Gaga
(http://www.hooktheory.com/theorytab/view/lady-gaga/poker-face).

Screenshot from Hooktheory:

![Hooktheory Example Screenshot](https://github.com/Hikage/MIDI_AI/blob/master/HooktheoryPokerFace.jpg "Hooktheory Poker Face Screenshot")

XML from Hooktheory database:

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

The song key is given as G#. The song mode is given as 6 (aeolian/minor), indicating that the song is performed in G#m. The scale degree of each chord in the notation is given in terms of the relative major key. B major is the relative major of G# minor. Thus, in the SIF for this song a notation of 1 indicates a B major chord, and a notation of 6 indicates a G# minor chord.

It should be emphasized that all chord tonalities retain their values relative to the major key (i.e.: I, ii, iii, IV, V, vi, vii&deg;), unless specified otherwise with a chord mode (borrowed) or applied target.

The chords translate as follows:

> 6-1: i (G#m) for 1 beat  
> 6-1: i (G#m) for 1 beat  
> 6-1: i (G#m) for 1 beat  
> 6-1: i (G#m) for 1 beat  
> 4-1: VI (E) for 1 beat  
> 4-1: VI (E) for 1 beat  
> 4-1: VI (E) for 1 beat  
> 4-1: VI (E) for 1 beat  
> 1-1: III (B) for 1 beat  
> 1-1: III (B) for 1 beat  
> 1-1: III (B) for 1 beat  
> 1-1: III (B) for 1 beat  
> 5-1: VII (F#) for 1 beat  
> 5-1: VII (F#) for 1 beat  
> 5-1: VII (F#) for 1 beat  
> 5-1: VII (F#) for 1 beat  

##### Skyfall

Chorus of Skyfall by Adele Adkins and Paul Epworth,
as performed by Adele
(http://www.hooktheory.com/theorytab/view/adele/skyfall#chorus).

Screenshot from Hooktheory:

![Hooktheory Example Screenshot] (https://github.com/Hikage/MIDI_AI/blob/master/HooktheorySkyfall.jpg "Hooktheory Skyfall Screenshot")

XML from Hooktheory database:

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
    
As with the Poker Face example, the SIF is notated in the relative major (Eb major) of C minor.  This example includes a few more interesting elements, in particular the S(3) notation used to grab the G chord.

> rest for 4 beats  
> 6-3: i (Cm) for 3 beats  
> M642-1: i7 (Cm7) in 3rd inversion for 1 beat  
> 4-3: VI (A) for 3 beats  
> 442-1: VI7 (A7) in 3rd inversion for 1 beat  
> 2-3: iv (Fm) for 3 beats  
> 66-1: i (Cm) in 1st inversion for 1 beat  
> 364sus4-2: v (Gm) in 2nd inversion, with a sustained 4, for 2 beats  
> S(3)3-2: V (G) for 2 beats  
> 6-3: i (Cm) for 3 beats  
> M642-1: i7 (Cm7) in 3rd inversion for 1 beat  
> 4-3: VI (A) for 3 beats  
> 442-1: VI7 (A7) in 3rd inversion for 1 beat  
> S(3)2-3: IV (F) for 3 beats  
> 66-1: i (Cm) in 1st inversion for 1 beat  
> 364sus4-2: v (Gm) in 2nd inversion, with a sustained 4, for 2 beats  
> S(3)3-2: V (G) for 2 beats  
> 6-2: i (Cm) for 2 beats  
> 4-2: VI (A) for 2 beats  
> L27-2: IV7 (F7) for two beats  
> 4-2: VI (A) for 2 beats  
> 6-2: i (Cm) for 2 beats  
> 4-2: VI (A) for 2 beats  
> L27-2: IV7 (F7) for two beats  
> 4-2: VI (A) for 2 beats
