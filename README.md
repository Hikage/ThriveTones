ThriveTones
=======

_Computers are the new musicians!  Using theory heuristics, compose through a crowd-source-driven genetic algorithm._

Copyright © 2014 Brianna Shade  
bshade@pdx.edu  
Portland State University
  
This program is licensed under the "MIT License"  
Please see the file COPYING in the source distribution of this software for license terms.

### About
This is a revived school project from 2006 whereby simple melodies were created from a genetic algorithm (using human input as each iteration's selection method).  As part of the initial project, I contributed an alternative approach, instead randomly creating melodies within the confines of music theory guidelines.

This revival first looks at a sampling from a database of chord progressions found in over 1300 modern pop songs, collected as part of the Hooktheory project (www.hooktheory.com), and makes use of the project's statistics on how likely particular chords are to succeed others.  With this data, new songs are built through a Markov model by analyzing progressions for choruses, verses, and bridges, taking into account theory guidelines as a heuristic.

_Note: Original code was a collaborative team effort during a 2006 senior capstone independent project course at Colorado State University.  I have since lost the contact information for the other 2-3 individuals and in fact no longer remember their names.  However, I wish to give due credit to their work, to the best of my ability.  Fortunately, the new approach is no longer making use of a GA, and the Markov model implementation is almost entirely an individual effort._

### Build Instructions
* Compile all .java code files in src/ThriveTones and src/sax.  Additionally, you may wish to compile the files found in src/test if you wish to perform any unit tests.
* Make sure the input data file is located within the classpath directory.  Currently, the program looks for this file in src/..
* Execute the Driver.java file, providing the data file name as the only argument

Example usage: java src/thriveTones.Driver Hooktheory-Data.xml

### Code Plan
[x] Initialize project, flesh out README, and organize coding steps (6/22)  
[x] Research pertinent literature, and collect relevant strategies (6/29)  
[x] Obtain pop chords and/or progression statistics (7/2)  
[x] Build XML reader to process Hooktheory data (7/13)    
[x] Complete data read-in to generate stats (8/24)  
[x] Refactor GA to build chord progressions using Markov chains based on statistics (8/31)  
[] Segregate chorus, verse, and bridge data for modular composition (10/13)  

### Optional Enhancements / Future Work  
[] Add melody over chord structure  
[] Allow users to choose initial key, tempo, rhythm  
[] Refactor composition as a four-part a cappella arrangement  
[] Refactor code to make use of the Hooktheory API instead of a static data dump  
[] Create website for public users to play with song creation  
[] Allow for new songs to be saved/downloaded for the user  

### Currently Working On / Next Steps
* Modularize song pieces for more coherent construction
* Support for chord modes and targets

### Development Challenges
#### How to best represent chords:  
These were originally represented literally, as integer frequencies.  This, of course, was impractical, so I strove to represent them more abstractly, to be independent of key.  I initially thought to simply represent them by their Roman numeral, but this quickly proved insufficient, as it did not account for inversions, added 7ths, or even anything so simple as distinguishing between major and minor.  Therefore, I was left with maintaining each's note structure.  I ultimately opted for a hashmapping of notes numbered 0 to 11 to allow for the greatest flexibility (0 being the tonic).  Additionally, values for an octave and for the chord's tonality (merely for convenience) were stored as part of the object.  A root was independently specified, permitting any necessary chord to be built.  This was still flawed, however, as inversions were beyond this representation, and chords built upon higher numerals (e.g.: 11) potentially had their third and/or fifth wrapped to a lower numeral.

It was later discovered that JFugue can accept an arbitrary chord in the format of letter\[octave\]\[tonality\]\[duration\].  Accidentals are automatically inserted depending on the specified key.  For example, "KEbmaj E5majw" would play an Eb major triad - the tonic in the key of Eb major - for a whole note (4 beats).  This greatly simplified the chord representation, necessitating only the root, and a few other elements if they deviate from the key (tonality, octave, etc).  Inversions are also supported, as are some sustained and additional notes.  Modes and borrowed chords still prove challenging, but these have been shelved for the time being to allow for further advancement with the construction portion.

Chords could also possibly be represented with a bitmap.  At this time, I have not yet explored this option, but it would provide a lighter-weight (if less legible) implementation.

#### Hooktheory data:
Reading in the Hooktheory data proved more difficult than initially thought.  For one, all representations aren't consistent (for example, b and f both denote "flat").  Not all songs have an associated key, and some contain unsupported chords (like 11 chords).  The representation also wasn't immediately intuitive, delaying progress.  However, this forced a greater understanding of the data, prompting a write-up on the structure for future developers.  This may be greatly due to the fact that the data itself is crowd-sourced, making it challenging to maintain consistency.

#### Chord progression statistics:
In order to build progression statistics, a dynamic list of encountered chords must be maintained and visible across all classes of this project.  I experimented with various implementations, including having a dedicated class, having the set of chords live in XMLReader, and having them exist as an extention of the Chord instances.  Ultimately, I opted for storing the unique list of chords in XMLReader, as they're read in (being careful to not create a new chord if one already exists).

Chord pairings were an attribute of Chord (each chord would then have a list of subsequent chords that could follow), and this object maintained both the set of available chords, as well as a bag of indices, representing each's frequency.  For example, if 3 of 4 V chords are followed by a I chord and 1 of 4 is followed by another V, then the set would contain {I, V} and the bag would contain {0, 0, 0, 1}.  This built a roulette incrementally, making probabilities easy; selecting a random index, a I chord would be selected 75% of the time.

Unfortunately, the set of unique chords had to be passed between the classes.  Further, this implementation did not allow for variable histories that would add flexibility to the generation of chord progressions.

Therefore, this was refactored to a ChordDictionary, represented as its own class object extending a hashmap.  With each new chord read in from the data file, a key/value pair is inserted into the dictionary.  The value is the newly created Chord object, and the key is the history Chord(s), the length of which is set at the beginning of execution.  Entries are made for all history lengths, up to the maximum.  As an example, for the chord progression 1 5 4 1 and with a maximum history length of 3, when reading in the final 1 chord, the following entries are made into the dictionary:  
{[1 5 4], 1}  
{[5 4], 1}  
{[4], 1}  
{[], 1}  

This allows for any length history to be used, or none at all.  If a given history is not a key in the dictionary, it is truncated and searched for again.  Ultimately, if no history is found, a random next chord is selected, based on overall probabilities.

#### JFugue:
Some complicated chords are far more difficult to represent in JFugue.  Therefore, some of the more intricate chords will need to be represented with literal intervals instead of the condensed representation of Cmaj, etc.  This creates a need for some more obsure representation and consideration for unusual edge cases.  For now, this has been disregarded for the sake of simplicity, but it will need to be addressed eventually.

### Useful Resources
http://arstechnica.com/science/2009/09/virtual-composer-makes-beautiful-musicand-stirs-controversy/  
http://www.fastcodesign.com/1673173/can-computers-write-music-that-has-a-soul  
https://www.ece.umd.edu/~blj/papers/OrganizedSound.pdf  
http://sourceforge.net/projects/musicalgorithm1/  
http://computationalcreativity.net/iccc2012/wp-content/uploads/2012/06/160-Smith.pdf  
http://cacm.acm.org/magazines/2011/7/109891-algorithmic-composition/fulltext  
http://mzlabs.com/MZLabsJM/page4/page22/page22.html  
http://www.jfugue.org/jfugue-chapter2.pdf  
http://www.hooktheory.com/

### License and Sharing
Full MIT license details available within COPYING
