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

This revival first looks at a database of chord progressions found in over 1300 modern pop songs, collected as part of the Hooktheory project (www.hooktheory.com), and makes use of the project's statistics on how likely particular chords are to succeed others.  With this data, new songs are built through a hidden Markov Model by analyzing progressions for choruses, verses, and bridges, taking into account theory guidelines as a heuristic.

_Note: Original code was a collaborative team effort during a 2006 senior capstone independent project course at Colorado State University.  I have since lost the contact information for the other 2-3 individuals and in fact no longer remember their names.  However, I wish to give due credit to their work, to the best of my ability._

### Build Instructions

### Code Plan
[x] Initialize project, flesh out README, and organize coding steps (6/22)  
[x] Research pertinent literature, and collect relevant strategies (6/29)  
[] Obtain pop chords and/or progression statistics (7/2)  
[] Review existing code, refactor/revise as necessary, and refine areas still needing attention (7/6)  
[] Refactor GA to build chord progressions using Markov chains based on statistics (7/20)  
[] Segregate chorus, verse, and bridge data for modular composition (7/27)  
[] Finalize music theory heuristics and develop unit tests (8/3)  
[] Add melody over chord structure (8/10)

### Optional Enhancements / Future Work  
[] Allow users to choose initial key, tempo, rhythm  
[] Refactor composition as a four-part a cappella arrangement
[] Create website for public users to play with song creation  
[] Allow for new songs to be saved/downloaded for the user  

### Development Challenges


### Currently Working On / Next Steps
* Rename project to something clever
* Review old code and clean up!
* Obtain pop chords

### Useful Resources
http://arstechnica.com/science/2009/09/virtual-composer-makes-beautiful-musicand-stirs-controversy/
http://www.fastcodesign.com/1673173/can-computers-write-music-that-has-a-soul
https://www.ece.umd.edu/~blj/papers/OrganizedSound.pdf
http://sourceforge.net/projects/musicalgorithm1/
http://computationalcreativity.net/iccc2012/wp-content/uploads/2012/06/160-Smith.pdf
http://cacm.acm.org/magazines/2011/7/109891-algorithmic-composition/fulltext
http://mzlabs.com/MZLabsJM/page4/page22/page22.html
http://www.hooktheory.com/

### License and Sharing
Full MIT license details available within COPYING
