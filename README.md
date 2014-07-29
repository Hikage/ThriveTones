MIDI_AI
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

### Code Plan
[x] Initialize project, flesh out README, and organize coding steps (6/22)  
[x] Research pertinent literature, and collect relevant strategies (6/29)  
[x] Obtain pop chords and/or progression statistics (7/2)  
[x] Build XML reader to process Hooktheory data (7/13)    
[] Refactor GA to build chord progressions using Markov chains based on statistics (8/3)  
[] Review existing code, refactor/revise as necessary, and refine areas still needing attention (ongoing)  
[] Segregate chorus, verse, and bridge data for modular composition (8/10)  
[] Finalize music theory heuristics and develop unit tests (8/17)  
[] Add melody over chord structure (8/21)

### Optional Enhancements / Future Work  
[] Allow users to choose initial key, tempo, rhythm  
[] Refactor composition as a four-part a cappella arrangement  
[] Refactor code to make use of the Hooktheory API instead of a static data dump  
[] Create website for public users to play with song creation  
[] Allow for new songs to be saved/downloaded for the user  

### Currently Working On / Next Steps
* Rename project to something clever
* Continue to refactor old code and clean up!
* Hooktheory data write-up
* Support for inversions, modes, and targets

### Development Challenges
#### How to best represent chords:  
These were originally represented literally, as integer frequencies.  This, of course, was impractical, so I strove to represent them more abstractly, to be independent of key.  I initially thought to simply represent them by their Roman numeral, but this quickly proved insufficient, as it did not account for inversions, added 7ths, or even anything so simple as distinguishing between major and minor.  Therefore, I was left with maintaining each's note structure.  I ultimately opted for a hashmapping of notes numbered 0 to 11 to allow for the greatest flexibility (0 being the tonic).  Additionally, values for an octave and for the chord's tonality (merely for convenience) were stored as part of the object.  A root was independently specified, permitting any necessary chord to be built.  This was still flawed, however, as inversions were beyond this representation, and chords built upon higher numerals (e.g.: 11) potentially had their third and/or fifth wrapped to a lower numeral.

It was later discovered that JFugue can accept an arbitrary chord in the format of letter[octave][tonality][duration].  Accidentals are automatically inserted depending on the specified key.  For example, "KEbmaj E5majw" would play an Eb major triad - the tonic in the key of Eb major - for a whole note (4 beats).  This greatly simplified the chord representation, necessitating only the root, and a few other elements if they deviate from the key (tonality, octave, etc).

Chords could also possibly be represented with a bitmap.  At this time, I have not yet explored this option, but it would provide a lighter-weight (if less legible) implementation.

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
