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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.*;

public class XMLReader extends DefaultHandler {
	
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
	    	System.out.println(nodeValueByAttValue(fields, "song"));
	    }
    }
	
	private static String nodeValueByAttValue(NodeList fields, String attVal){
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
}
