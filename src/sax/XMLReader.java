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

import markovChords.Song;

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
			Song song = SIFtoChords(fields);
			System.out.println(nodeValueByAttName(fields, "SIF") + " " + nodeValueByAttName(fields, "songKey") + " " + nodeValueByAttName(fields, "mode"));
			System.out.println(song.toString());
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
	 * Converts the SIF string into a Song object
	 * @param fields: field nodes of the song to convert
	 */
	public static Song SIFtoChords(NodeList fields) throws Exception{
		String title = nodeValueByAttName(fields, "song");
		String artist = nodeValueByAttName(fields, "artist");
		String part = nodeValueByAttName(fields, "section");
		String key = extractKey(fields);
		int mode = Integer.parseInt(nodeValueByAttName(fields, "mode"));
		String sif = nodeValueByAttName(fields, "SIF");
		double beats = Double.parseDouble(nodeValueByAttName(fields, "beatsInMeasure"));
		
		return new Song(title, artist, part, key, mode, sif, beats);
	}
}
