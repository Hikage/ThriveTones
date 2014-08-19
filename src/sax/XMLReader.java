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
 * For full format details, see XMLFORMAT-README.md
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
			System.out.println(nodeValueByAttName(fields, "SIF") + " " + nodeValueByAttName(fields, "songKey") + " " + nodeValueByAttName(fields, "mode"));
			System.out.println(song);
			System.out.println();
			//PlaySong(song);
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
	 * Calculates the relative major, based on current key and mode
	 * @param key: song key
	 * @param mode: song mode
	 * @return: the relative major key
	 */
	public static String relativeMajor(String key, int mode){
		String circle = "FCGDAEB";
		int offset;
		switch(mode){
		case 2: offset = -2; break;
		case 3: offset = -4; break;
		case 4: offset = 1; break;
		case 5: offset = -1; break;
		case 6: offset = -3; break;
		case 7: offset = -5; break;
		default: offset = 0;
		}

		String rel_major = "";
		String accidental = "";
		if(key.length() > 1) accidental += key.charAt(1);
		int pos = circle.indexOf(key.toUpperCase().charAt(0));
		int new_pos = pos + offset;
		if(new_pos < 0){
			rel_major += circle.charAt(new_pos + 7);
			if(accidental.equals("#")) return rel_major;
			else return rel_major + accidental + "b";
		}
		if(new_pos >= circle.length()){
			rel_major += circle.charAt(new_pos - 7);
			if(accidental.equals("b")) return rel_major;
			else return rel_major + accidental + "#";
		}
		rel_major += circle.charAt(new_pos);
		return rel_major + accidental;
	}

	/**
	 * Converts the SIF string into Chord objects
	 * @param fields: field nodes of the song to convert
	 * @return: string representation of the song to be played
	 */
	public static String SIFtoChords(NodeList fields) throws Exception{
		String raw_key = extractKey(fields);
		int mode = Integer.parseInt(nodeValueByAttName(fields, "mode"));
		String key = relativeMajor(raw_key, mode);
		double beats = Double.parseDouble(nodeValueByAttName(fields, "beatsInMeasure"));
		String sif = nodeValueByAttName(fields, "SIF");
		String[] sif_chords = sif.split(",");
		Chord[] chords = new Chord[sif_chords.length];
		String playable_chords = "K" + key + "maj ";
		for(int i = 0; i < chords.length; i++){
			if(sif_chords[i].isEmpty()) continue;
			try{
				chords[i] = new Chord(mode, sif_chords[i]);
				playable_chords += chords[i].toString(key.charAt(0) - 'A', beats) + " ";
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
