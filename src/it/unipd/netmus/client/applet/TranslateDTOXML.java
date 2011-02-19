package it.unipd.netmus.client.applet;

import java.util.ArrayList;
import java.util.List;

import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

public class TranslateDTOXML {
	
	//node signatures
	private final static String ROOT_NAME = "Library";
	private final static String SONG_NAME = "Song";
	private final static String ALBUMTITLE_NAME = "AlbumTitle";
	private final static String AUTHORCOMPOSER_NAME = "AuthorComposer";
	private final static String LEADARTIST_NAME = "LeadArtist";
	private final static String SONGGENRE_NAME = "SongGenre";
	private final static String SONGTITLE_NAME = "SongTitle";
	private final static String TRACKNUMBER_NAME = "TrackNumber";
	private final static String YEAR_NAME = "Year";
	//name of the attribute file:
	private final static String FILE_NAME = "File";
	
	private Document document;
	private Element root;
	
	
	public TranslateDTOXML() {
		reset();
	};
	
	//used to start with a fresh DOM tree, it must be called to use DTOtoXML.
	public void reset(){
		document = XMLParser.createDocument();
		root = document.createElement(ROOT_NAME);
		document.appendChild(root);
	};
	
	//add a single DTO to XML
	public void DTOtoXML(SongDTO brano){
		
		//create the new song node
		Element song = document.createElement(SONG_NAME);
		//append the filename
		Element fileName = document.createElement(FILE_NAME);
		fileName.appendChild(document.createCDATASection(brano.getFile()));
		song.appendChild(fileName);
		//fill the new node with data.
		Element albumTitle = document.createElement(ALBUMTITLE_NAME);
		albumTitle.appendChild(document.createCDATASection(brano.getAlbum()));
		song.appendChild(albumTitle);
		
		Element authorComposer = document.createElement(AUTHORCOMPOSER_NAME);
		authorComposer.appendChild(document.createCDATASection(brano.getComposer()));
		song.appendChild(authorComposer);
		
		Element leadArtist = document.createElement(LEADARTIST_NAME);
		leadArtist.appendChild(document.createTextNode(brano.getArtist()));
		leadArtist.appendChild(document.createCDATASection(brano.getArtist()));
		song.appendChild(leadArtist);
		
		Element songGenre = document.createElement(SONGGENRE_NAME);
		songGenre.appendChild(document.createCDATASection(brano.getGenre()));
		song.appendChild(songGenre);
		
		Element songTitle = document.createElement(SONGTITLE_NAME);
		songTitle.appendChild(document.createCDATASection(brano.getTitle()));
		song.appendChild(songTitle);
		
		Element trackNumber = document.createElement(TRACKNUMBER_NAME);
		trackNumber.appendChild(document.createCDATASection(brano.getTrackNumber()));
		song.appendChild(trackNumber);
		
		Element year = document.createElement(YEAR_NAME);
		year.appendChild(document.createCDATASection(brano.getYear()));
		song.appendChild(year);
		
		//add the new node to the tree
		root.appendChild(song);	
	}
	
	//generate the XML code
	public String generateXML(){
		return document.toString();
	}
	
	private SongDTO generateDTO(Element node){
		SongDTO song = new SongDTO();
		while (node.hasChildNodes()){
			Element child = (Element) node.getFirstChild();
			String name = child.getNodeName();
			if (name.equals(FILE_NAME)) song.setFile(child.getFirstChild().getNodeValue());
			else if (name.equals(ALBUMTITLE_NAME)) song.setAlbum(child.getFirstChild().getNodeValue());
			else if (name.equals(AUTHORCOMPOSER_NAME)) song.setComposer(child.getFirstChild().getNodeValue());
			else if (name.equals(LEADARTIST_NAME)) song.setArtist(child.getFirstChild().getNodeValue());
			else if (name.equals(SONGGENRE_NAME)) song.setGenre(child.getFirstChild().getNodeValue());
			else if (name.equals(SONGTITLE_NAME)) song.setTitle(child.getFirstChild().getNodeValue());
			else if (name.equals(TRACKNUMBER_NAME)) song.setTrackNumber(child.getFirstChild().getNodeValue());
			else if (name.equals(YEAR_NAME)) song.setYear(child.getFirstChild().getNodeValue());
			node.removeChild(child);
		}
		return song;
	}
	
	public List<SongDTO> XMLToDTO(String xml){
		
		List<SongDTO> list=null;
		try {
			//create the DOM tree
			document = XMLParser.parse(xml);
			root = (Element) document.getFirstChild();
			//create the list
			list = new ArrayList<SongDTO>();
			//start parsing
			while (root.hasChildNodes()){
				//for every child, parse it and att it to the list
				list.add(generateDTO((Element)root.getFirstChild()));
				root.removeChild(root.getFirstChild());
			}
			
		} catch (DOMParseException e){}
		
		return list;
	}
}
