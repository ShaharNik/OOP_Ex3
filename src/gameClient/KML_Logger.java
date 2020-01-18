package gameClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
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
	private static int i;
	LocalDateTime localDateTime;
	
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
	}
	public static void main(String[] args) 
	{
		KML_Logger myKml = new KML_Logger();
		myKml.createKMLforScenario(23);

	}
	
	private void initFruits()
	{
		Iterator<String> f_iter = game.getFruits().iterator();
		while (f_iter.hasNext()) 
		{
			String json = f_iter.next();
			MyFruit f = new MyFruit();
			f.initFromJson(json);
			this._fruits.add(f);
		}
	}
	private void initRobots()
	{
		// init robots
		List<String> Robots = game.getRobots(); // Why Game don't give ROBOTS?
		for (int i = 0; i < Robots.size(); i++) 
		{
			Robot b = new Robot();
			b.botFromJSON(Robots.get(i));
			this._bots.add(b);
		}
	}
	/**
	 * This function write all the game details in a new kml.
	 * @param g Game that we want to write his details in the kml.
	 * @return boolean if we succeed to create the kml or not.
	 */
	public boolean createKMLforScenario(int scenario)
	{
		game = Game_Server.getServer(scenario);
		initFruits();
		initRobots();

		 // -----========== KML Start String and Styles =======------------
		kmlBuilder.append( "<<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<kml xmlns=\"http://www.opengis.net/kml/2.2\">"
				+ "\n<Document>"
				+ "<Style id=\"red\">\r\n" + 
				"<IconStyle><Icon><href>http://maps.google.com/mapfiles/ms/icons/red-dot.png</href></Icon></IconStyle>\r\n" + 
				"</Style><Style id=\"Robot\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/pal3/icon49.png</href></Icon></IconStyle>\r\n" + 
				"</Style><Style id=\"Fruit\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/pal5/icon35.png</href></Icon></IconStyle></Style>");

		//  -------======== Add Fruits and Robots Placemarks ==========------------
		try
		{
			FileWriter fw = new FileWriter("data\\"+i++ +"myGameKmlScenario" +scenario+".kml");
			BufferedWriter bw = new BufferedWriter(fw);
			for (MyFruit fr : this._fruits) 
			{
				addPlacemark("Fruit", fr.getPos().y(), fr.getPos().x(), fr.getPos().z());
			}
			for(Robot ro : this._bots)
			{
				addPlacemark("Robot", ro.getPos().y(), ro.getPos().x(), ro.getPos().z());
			}
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
	
    private void addPlacemark(String style_id, double y_location, double x_location, double z_location)
    {
    	/*
   <Placemark>
      <TimeStamp>
        <when>2007-01-14T21:05:02Z</when>
      </TimeStamp>
      <styleUrl>#paddle-a</styleUrl>
      <Point>
        <coordinates>-122.536226,37.86047,0</coordinates>
      </Point>
    </Placemark>
    
    y = nlat
    x = nlon
    	 */
    	
    	
        //Local date time instance
        localDateTime = LocalDateTime.now();
        //Get formatted String
        String whenPlacemarkString = FORMATTER.format(localDateTime); // "yyyy/MM/dd HH:mm:ss"
        kmlBuilder.append(
                " \n   <Placemark>\r\n" +
                        "      <TimeStamp>\r\n" +
                        "        <when>" + whenPlacemarkString+ "</when>\r\n" +
                        "      </TimeStamp>\r\n" +
                        "      <styleUrl>#" + style_id + "</styleUrl>\r\n" +
                        "      <Point>\r\n" + // location format:  -122.536226,37.86047,0
                        "        <coordinates>" + y_location + "," + x_location + "," + z_location + "</coordinates>\r\n" +
                        "      </Point>\r\n" +
                        "    </Placemark>\r\n"
        );

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