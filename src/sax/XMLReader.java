package sax;

/**
 * "Digital Chords" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * XMLReader.java
 * This class extracts the songs from a specified XML data feed,
 * expecting the Hooktheory format
 * 
 * 	Fields:
 * 		artist 
 *		song
 *		section (e.g.: chorus, verse, intro, etc)
 *		SIF (chord progression)
 *		beatsInMeasure
 *		songKey
 *		bpm
 *		mode (i.e.: Ionian, Dorian, etc.  http://en.wikipedia.org/wiki/Mode_(music)#Modern; 1 is major, 6 is minor)
 *
 *	SIF Detail:  each chord has the format [mode]scale_degree[figured_bass][embellishment][-duration][/applied_target]
 *		mode (allows for chords to be borrowed from other keys)
 *			b - Minor, L(ydian), D(orian), M(ixolydian), Y - Phrygian, C - Locrian, S(n) - mode of the scale_degree n
 *		scale_degree (integer representation of Roman numerals, e.g.: I, ii, iii, IV, etc; 1-7, or "rest")
 *		figured_base (chord inversion: 6 (1st triad), 64 (2nd triad), 7 (root 7 chord), 65 (1st 7), 43 (2nd 7), 42 (3rd 7)
 *		embellishment (sus2, sus4, sus42, add9)
 *		duration (number of beats; default: 4)
 *		applied_target (secondary dominant/subdominant/leading-tone; e.g.: V/IV ("5 of 4"))
 *
 *	Chords are notated as their relative major mode.  Therefore, a i (1) chord in a minor mode (6) is written as 6
 */

import javax.xml.parsers.*;

import markovChords.Chord;

import org.jfugue.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.*;

public class XMLReader extends DefaultHandler {

	/**
	 * Usage function
	 */
	private static void usage(){
		System.err.println("Usage: java XMLReader filename.xml");
		System.exit(1);
	}

	/**
	 * Main class
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		if(args.length < 1 || args[0] == null) usage();

        String filename = args[0];
        File file = new File(filename);
        if(!file.exists() || file.isDirectory()) throw new Exception("File not found! " + filename);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    FileReader reader = new FileReader(file);        
	    Document document = builder.parse(new InputSource(reader));

	    NodeList results = document.getElementsByTagName("resultset");
	    NodeList rows = results.item(0).getChildNodes();
	    for(int i = 1; i < rows.getLength(); i++) {
	    	NodeList fields = rows.item(i).getChildNodes();
	    	if(fields.item(1) == null) continue;				//sanity check to avoid empty nodes
			String song = SIFtoChords(fields);
			System.out.println(song);
			PlaySong(song);
	    }
    }

	/**
	 * Extracts an attribute value from the XML
	 * @param fields: field nodes amongst which to search for the specified field name
	 * @param att_name: name of the field for which to search
	 * @return: the value of the specified field
	 */
	public static String nodeValueByAttName(NodeList fields, String att_name){
		String node_value = "";
		for(int j = 0; j < fields.getLength(); j++){
    		Node node = fields.item(j);
    		if(node.getNodeName().equals("field")
    				&& node.getAttributes().getNamedItem("name").getNodeValue().equals(att_name)){
    			node_value = node.getTextContent().trim();
    			break;
    		}    		
    	}

		return node_value;
	}

	/**
	 * Converts varying keys from the raw XML into a standard key notation
	 * @param xkey: raw key from XML
	 * @return: standardized key
	 */
	public static String XMLKeytoKey(String xkey){
		if(xkey.length() < 1 || xkey.length() > 2
				|| xkey.toUpperCase().charAt(0) < 'A' || xkey.toUpperCase().charAt(0) > 'G'){
			return null;
		}
		if(xkey.length() == 1) return xkey.toUpperCase();

		String key = "";
		key += xkey.toUpperCase().charAt(0);

		switch(xkey.toLowerCase().charAt(1)){
		case 'b':
		case 'f': key += 'b'; break;
		case '#':
		case 's': key += '#'; break;
		default: return null;
		}

		return key;
	}

	/**
	 * Extracts the key from the XML file
	 * @param fields: field nodes amongst which to search for the key
	 * @return: the standardized key value
	 */
	public static String extractKey(NodeList fields) throws Exception{
		if(fields == null)
			throw new Exception("Supplied fields list is empty");
		String xkey = nodeValueByAttName(fields, "songKey");
		String key = XMLKeytoKey(xkey);
		if(key == null)
			throw new Exception("Invalid key: " + xkey);

		return key;
	}

	/**
	 * Converts the SIF string into Chord objects
	 * @param fields: field nodes of the song to convert
	 * @return: string representation of the song to be played
	 */
	public static String SIFtoChords(NodeList fields) throws Exception{
		String key = extractKey(fields);
		int mode = Integer.parseInt(nodeValueByAttName(fields, "mode"));
		String sif = nodeValueByAttName(fields, "SIF");
		String[] sif_chords = sif.split(",");
		Chord[] chords = new Chord[sif_chords.length];
		String playable_chords = "K" + key + "maj ";
		for(int i = 0; i < chords.length; i++){
			if(sif_chords[i].isEmpty()) continue;
			try{
				chords[i] = new Chord(mode, sif_chords[i]);
				playable_chords += chords[i].toString(key.charAt(0) - 'A') + " ";
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return playable_chords.trim();
	}

	/**
	 * Plays the specified song
	 * @param song: song to be played
	 */
	public static void PlaySong(String song){
		int TEMPO = 120;
		String INSTRUMENT = "Piano";

		String playable_song = "T" + TEMPO + " I[" + INSTRUMENT + "] " + song;

		System.out.println(playable_song);
		Pattern pattern = new Pattern();
		pattern.setMusicString(playable_song);
		Player player = new Player();
		player.play(pattern);
	}
}
