package sax;

/**
 * "Digital Chords" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * XMLReader.java
 * TODO: details on this class
 */

import javax.xml.parsers.*;

import markovChords.Chord;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.*;
import java.util.HashMap;

public class XMLReader extends DefaultHandler {

	private static final HashMap<String, Integer> keys = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
	{
		put("C", 0);
		put("C#", 1); put("Db", 1);
		put("D", 2);
		put("D#", 3); put("Eb", 3);
		put("E", 4);
		put("F", 5);
		put("F#", 6); put("Gb", 6);
		put("G", 7);
		put("G#", 8); put("Ab", 8);
		put("A", 9);
		put("A#", 10); put("Bb", 10);
		put("B", 11);
	}};

	private static void usage(){
		System.err.println("Usage: java XMLReader filename.xml");
		System.exit(1);
	}

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
	    	System.out.println(SIFtoChords(fields));
	    }
    }

	public static String nodeValueByAttValue(NodeList fields, String attVal){
		String node_value = "";
		for(int j = 0; j < fields.getLength(); j++){
    		Node node = fields.item(j);
    		if(node.getNodeName().equals("field") && node.getAttributes().getNamedItem("name").getNodeValue().equals(attVal)){
    			node_value = node.getTextContent().trim();
    			break;
    		}    		
    	}

		return node_value;
	}

	public static String XMLKeytoKey(String xkey){
		if(xkey.length() < 1 || xkey.length() > 2
				|| xkey.toUpperCase().charAt(0) < 'A' || xkey.toUpperCase().charAt(0) > 'G'){
			return null;
		}
		if(xkey.length() == 1) return xkey;

		String key = "";
		key += xkey.charAt(0);

		switch(xkey.toLowerCase().charAt(1)){
		case 'b':
		case 'f': key += 'b'; break;
		case '#':
		case 's': key += '#'; break;
		default: return null;
		}

		return key;
	}

	public static String extractKey(NodeList fields){
		String xkey = nodeValueByAttValue(fields, "songKey");
		String key = XMLKeytoKey(xkey);
		if(key == null){
			System.err.println("Invalid key: " + xkey);
			System.exit(1);
		}

		return key;
	}

	public static String SIFtoChords(NodeList fields){
		String key = extractKey(fields);
		int mode = Integer.parseInt(nodeValueByAttValue(fields, "mode"));
		String sif = nodeValueByAttValue(fields, "SIF");
		String[] sif_chords = sif.split(",");
		Chord[] chords = new Chord[sif_chords.length];
		String playable_chords = "K" + key + "maj ";
		for(int i = 0; i < chords.length; i++){
			if(sif_chords[i].isEmpty()) continue;
			try{
				chords[i] = new Chord(mode, sif_chords[i]);
				playable_chords += chords[i].toString() + " ";
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return playable_chords;
	}
}
