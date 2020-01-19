package gameClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import MyDataStructure.MyFruit;
import MyDataStructure.Robot;
import MyDataStructure.graph;
import MyDataStructure.myDGraph;
import MyDataStructure.node_data;
import Server.Game_Server;
import Server.game_service;

public class KML_Logger
{
	/**
	 * This class convert Game to kml.
	 * @author Shahar and Or
	 */



	ArrayList<MyFruit> _fruits;
	ArrayList<Robot> _bots;
	graph g;
	game_service game;
	StringBuilder kmlBuilder;
	DateTimeFormatter FORMATTER;
	private int i;
	LocalDateTime localDateTime;
	FileWriter fw;
	BufferedWriter bw;
	
	public KML_Logger() // needs to be singeltone
	{
		_fruits = new ArrayList<MyFruit>();
		_bots = new ArrayList<Robot>();
		g = null;
		game = null;
		kmlBuilder = new StringBuilder();;
		FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		i=0;
		localDateTime = null;
		try 
		{
			this.fw = new FileWriter("data\\"+ i++ +"myGameKmlScenario.kml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.bw = new BufferedWriter(fw);
	}
	public static void main(String[] args) 
	{
		KML_Logger myKml = new KML_Logger();
		//myKml.createKMLforScenario();

	}
	
	void initFruits()
	{
		Iterator<String> f_iter = game.getFruits().iterator();
		while (f_iter.hasNext()) 
		{
			String json = f_iter.next();
			MyFruit f = new MyFruit();
			f.initFromJson(json);
			this._fruits.add(f);
			if (f.getType() == 1)
				this.addPlacemark("Apple", f.getPos().y(), f.getPos().x(), f.getPos().z());
			else // banana
				this.addPlacemark("Banana", f.getPos().y(), f.getPos().x(), f.getPos().z());
		}
	}
	void initRobots()
	{
		// init robots
		List<String> Robots = game.getRobots(); // Why Game don't give ROBOTS?
		for (int i = 0; i < Robots.size(); i++) 
		{
			Robot b = new Robot();
			b.botFromJSON(Robots.get(i));
			this._bots.add(b);
			this.addPlacemark("Robot", b.getPos().y(), b.getPos().x(), b.getPos().z());
		}
	}
	/**
	 * This function write all the game details in a new kml.
	 * @param g Game that we want to write his details in the kml.
	 * @return boolean if we succeed to create the kml or not.
	 */
	public void createKMLforScenario()
	{
		 // -----========== KML Start String and Styles =======------------
		kmlBuilder.append( "<<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<kml xmlns=\"http://www.opengis.net/kml/2.2\">"
				+ "\n<Document>"
				+ "<Style id=\"red\">\r\n" + 
				"<IconStyle><Icon><href>http://maps.google.com/mapfiles/ms/icons/red-dot.png</href></Icon></IconStyle>\r\n" + 
				"</Style><Style id=\"Robot\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/pal3/icon49.png</href></Icon></IconStyle>\r\n" + 
				"</Style><Style id=\"Banana\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/pal3/icon40.png</href></Icon></IconStyle>\r\n" +
				"</Style><Style id=\"Apple\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/pal5/icon35.png</href></Icon></IconStyle></Style>");

		//  -------======== Add Fruits and Robots Placemarks ==========------------
		initRobots();
		initFruits();
	}

    private void addPlacemark(String style_id, double y_location, double x_location, double z_location)
    {	
		String startTime  = java.time.LocalDate.now()+"T"+java.time.LocalTime.now();
		LocalTime spanEndsTime = LocalTime.now();
		//spanEndsTime = spanEndsTime.plusSeconds(2);
		spanEndsTime= spanEndsTime.plusNanos(100*1000000);
		String endTime = java.time.LocalDate.now()+"T"+spanEndsTime;
    	switch (style_id)
    	{
    		case "Robot":
    		{
    	        //Local date time instance
    	        localDateTime = LocalDateTime.now();
    	        //Get formatted String
    	        String whenPlacemarkString = FORMATTER.format(localDateTime); // "yyyy/MM/dd HH:mm:ss"
    	        kmlBuilder.append(
    	    			"<Placemark>\n\r" +
    							"<styleUrl>#"+"Robot"+"</styleUrl>\n"+
    	    					"<Point>\n\r" +
    								"<coordinates>" + y_location + "," + x_location + "," + z_location + "</coordinates>\n" +
    							"</Point>\n" +
    							"<TimeSpan>\r"+
    								"<begin>"+ startTime +"</begin>\n"+
    								"<end>"+ endTime +"</end>\n"+
    							"</TimeSpan>\n"+
    							"</Placemark\n>"
    	        		);
    		}
    		break;
    		case "Banana":
    		{
    	        kmlBuilder.append(
    	    			"<Placemark>\n\r" +
    							"<styleUrl>#"+"Banana"+"</styleUrl>\n"+
    	    					"<Point>\n\r" +
    								"<coordinates>" + y_location + "," + x_location + "," + z_location + "</coordinates>\n" +
    							"</Point>\n" +
    							"<TimeSpan>\n\r"+
    								"<begin>"+ startTime +"</begin>\n"+
    								"<end>"+ endTime +"</end>\n"+
    							"</TimeSpan>\n"+
    							"</Placemark\n>"
    	        		);
    		}
    		case "Apple":
    		{
    	        kmlBuilder.append(
    	    			"<Placemark>\n\r" +
    							"<styleUrl>#"+"Apple"+"</styleUrl>\n"+
    	    					"<Point>\n\r" +
    								"<coordinates>" + y_location + "," + x_location + "," + z_location + "</coordinates>\n" +
    							"</Point>\n" +
    							"<TimeSpan>\n\r"+
    								"<begin>"+ startTime +"</begin>\n"+
    								"<end>"+ endTime +"</end>\n"+
    							"</TimeSpan>\n"+
    							"</Placemark\n>"
    	        		);
    		}
    		break;
    		
    	}


    }
    boolean FinishAndClose()
    {
		try
		{
			// ---====== KML End Tags ====-----
			kmlBuilder.append("\n</Document></kml>>");
			// ----===== Fix some string mistakes if exist ====--------
			String kml = kmlBuilder.toString().replaceAll("</Placemark>, <Placemark>", "</Placemark><Placemark>").replaceAll("</Placemark>, ", "</Placemark>").replaceAll(", <Placemark>", "<Placemark>");
			kml = kml.substring(1, kml.length()-1);
			
			// Write the KML string to the .kml file we opened in //data
			bw.write(kml);
			bw.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
    }
	public void SetGraph(graph other_g)
	{
		this.g = other_g;
	}
	public void setGame(game_service game)
	{
		this.game = game;
	}
    
}