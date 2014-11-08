package thriveTones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import thriveTones.SongSegment.SongPart;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * OutlineGenerator.java
 * This class generates song outlines based on a grammar of choruses, verses, etc.
 */

public class OutlineGenerator {
	private HashMap<Character, String[]> grammar = new HashMap<Character, String[]>();
	private ArrayList<SongPart> outline = new ArrayList<SongPart>();
	private Random random_generator = new Random();

	/**
	 * Constructor
	 */
	public OutlineGenerator(){
		/**
		 * possible parts: chorus, verse, bridge, intro, introverse, solo, outro, prechorus, prechoruschorus, verseprechorus
		 * i, v, p, c, b, s, o
		 * I -> iB | B
		 * B -> vM | vvM | M
		 * M -> Cv | CvM | CS
		 * C -> c | pc | cc | pcc
		 * S -> sE | bE | E
		 * E -> cE | o
		 */

		//build grammar
		String[] I = {"iB", "B"};
		grammar.put('I', I);
		String[] B = {"vM", "vvM", "M"};
		grammar.put('B', B);
		String[] M = {"CvM", "CvCS"};
		grammar.put('M', M);
		String[] C = {"c", "pc", "cc", "pcc"};
		grammar.put('C', C);
		String[] S = {"sCE", "bCE", "CE"};
		grammar.put('S', S);
		String[] E = {"cE", "o"};
		grammar.put('E', E);
	}

	/**
	 * Expands the given String into a set of terminals
	 * @param symbols : String to be expanded
	 * @return : the expanded string
	 * @throws Exception : if an invalid argument is supplied
	 */
	public String expandGrammar(String symbols) throws Exception{
		String expanded = "";
		for(int i = 0; i < symbols.length(); i++){
			char symbol = symbols.charAt(i);
			if(Character.isLowerCase(symbol)) expanded += symbol;
			else{
				String[] options = grammar.get(symbol);
				if(options == null)
					throw new IllegalArgumentException("Invalid argument: " + symbols);
				int index = random_generator.nextInt(options.length);
				expanded += expandGrammar(options[index]);
			}
		}
		return expanded;
	}

	/**
	 * Builds and returns the song outline
	 * @return : the generated song outline
	 */
	public ArrayList<SongPart> buildOutline(){
		return outline;
	}

	/**
	 * grammar accessor
	 * @return : the song structure grammar
	 */
	public HashMap<Character, String[]> getGrammar(){
		return grammar;
	}
}
