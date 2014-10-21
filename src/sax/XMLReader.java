package sax;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import thriveTones.ChordDictionary;
import thriveTones.SongSegment;
import thriveTones.SongSegment.SongPart;

import java.io.*;
import java.util.HashMap;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * XMLReader.java
 * This class extracts the songs from a specified XML data feed,
 * expecting the Hooktheory format
 * 
 * For full format details, see XMLFORMAT-README.md
 */

public class XMLReader extends DefaultHandler {
	private static HashMap<SongPart, ChordDictionary> parts_dictionary;

	/**
	 * Initializer
	 */
	public XMLReader(){
		parts_dictionary = new HashMap<SongPart, ChordDictionary>();
	}

	/**
	 * Reads in the data based on the filename provided
	 * @param filename : file to be read in
	 * @throws Exception : on invalid file passed in
	 */
	public void readIn(String filename) throws Exception{
        File file = new File(filename);
        if(!file.exists() || file.isDirectory())
        	throw new Exception("File not found! " + filename);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    FileReader reader = new FileReader(file);        
	    Document document = builder.parse(new InputSource(reader));

	    NodeList results = document.getElementsByTagName("resultset");
	    NodeList rows = results.item(0).getChildNodes();
	    int valid_songs = 0;
	    int invalid_songs = 0;
	    int row_number = 35;
	    for(int i = 1; i < rows.getLength(); i++) {
	    	NodeList fields = rows.item(i).getChildNodes();
	    	if(fields.item(1) == null) continue;				//sanity check to avoid empty nodes
			try{
				SongSegment song_segment = SIFtoChords(fields);
				valid_songs++;
				row_number += 11;
			}
			catch(Exception e){
				//System.out.println("\n" + e.getMessage());
				//System.out.println(row_number++ + ".");
				//System.out.println(nodeValueByAttName(fields, "SIF") + " " + nodeValueByAttName(fields, "songKey") + " " + nodeValueByAttName(fields, "mode"));
				invalid_songs++;
			}
		}
		System.out.println("\nValid songs read: " + valid_songs + "\nInvalid songs read: " + invalid_songs + "\n");
	}

	/**
	 * Extracts an attribute value from the XML
	 * @param fields : field nodes amongst which to search for the specified field name
	 * @param att_name : name of the field for which to search
	 * @return : the value of the specified field
	 */
	public String nodeValueByAttName(NodeList fields, String att_name){
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
	 * @param xkey : raw key from XML
	 * @return : standardized key
	 */
	public String XMLKeytoKey(String xkey){
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
	 * @param fields : field nodes amongst which to search for the key
	 * @return : the standardized key value
	 * @throws Exception : on an empty field list
	 */
	public String extractKey(NodeList fields) throws Exception{
		if(fields == null)
			throw new Exception("Supplied fields list is empty");
		String xkey = nodeValueByAttName(fields, "songKey");
		String key = XMLKeytoKey(xkey);
		if(key == null) key = "C";

		return key;
	}

	/**
	 * Converts the SIF string into a Song object
	 * @param fields : field nodes of the song to convert
	 * @return : the newly created SongSegment
	 * @throws Exception : on invalid SongSegment creation
	 */
	public SongSegment SIFtoChords(NodeList fields) throws Exception{
		String title = nodeValueByAttName(fields, "song");
		String artist = nodeValueByAttName(fields, "artist");
		String part = nodeValueByAttName(fields, "section");
		String key = extractKey(fields);
		int mode = Integer.parseInt(nodeValueByAttName(fields, "mode"));
		String sif = nodeValueByAttName(fields, "SIF");
		double beats = Double.parseDouble(nodeValueByAttName(fields, "beatsInMeasure"));

		return new SongSegment(part, mode, sif, beats, parts_dictionary);
	}

	/**
	 * parts_dictionary accessor
	 * @return : the current full parts_dictionary hashmap
	 */
	public HashMap<SongPart, ChordDictionary> getPartsDictionary(){
		return parts_dictionary;
	}

	/**
	 * Part chord_dictionary accessor
	 * @param part : specific dictionary requested
	 * @return : a chord_dictionary for the song part indicated
	 */
	public ChordDictionary getChordDictionary(SongPart part){
		return parts_dictionary.get(part);
	}
}
