package uk.ac.ucl.cs.robotsimulator.simulation;

import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.vecmath.Point2d;

import uk.ac.ucl.cs.robotsimulator.simulation.activitybot.ActivityBot;

/**
 * This class constructs the scenario using a JSON file. It is separated out for
 * "convenience".
 * 
 * @author ucacsjj
 *
 */

public class JsonScenarioGenerator extends ScenarioGenerator
{
	// The logger
	private final static Logger _logger = Logger.getLogger(JsonScenarioGenerator.class.getName());

	// The title
	private String _title;
	
	private Arena _arena;
	private LinkedList<Robot> _robots;
	private LinkedList<Wall> _walls;
	private LinkedList<Label> _labels;
	
	public JsonScenarioGenerator()
	{
	}
	
	@Override
	public Arena getArena()
	{
		return _arena;
	}

	@Override
	public LinkedList<Robot> getRobots()
	{
		return _robots;
	}

	@Override
	public LinkedList<Wall> getWalls()
	{
		return _walls;
	}

	public void loadSceneryFile(File sceneryFile)
	{
		try
		{
			final JsonReader reader = Json.createReader(new FileReader(sceneryFile));
			final JsonStructure jsonStructure = reader.read();
			parse(jsonStructure);
		}
		catch (final Exception e)
		{
			_logger.severe("An exception was thrown while trying to parse the map file "
					+ sceneryFile.getName() + "; the message is\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	// This method parses the JSON file. Lots of error checking is done. Surely it shouldn't be this verbose though, shoudl it?
	protected void parse(JsonStructure sceneContent)
	{
		_arena = new Arena(500, 1600);
		_robots = new LinkedList<Robot>();
		_walls = new LinkedList<Wall>();
		_labels = new LinkedList<Label>();

		// Top-level should be an object
		if (sceneContent.getValueType() != JsonValue.ValueType.OBJECT)
		{
			_logger.severe("The file does not start with an object");
			return;
		}

		final JsonObject topLevelSceneObject = (JsonObject) sceneContent;
		
		// Now get the robot simulator
		final JsonObject robotSimulatorObject = topLevelSceneObject.getJsonObject("robotsimulator");
		
		if (robotSimulatorObject == null)
		{
			_logger.severe("Cannot find a top-level JSON object named robot simulator");
			return;			
		}
		
		// Get the name (optional)
		JsonValue titleObject = robotSimulatorObject.get("title");
		if (titleObject.getValueType() != JsonValue.ValueType.STRING)
		{
			_logger.severe("Title needs to be a string type");
			return;
		}
		_title = ((JsonString) titleObject).getString();
		
		// Get the arena - should be an array
		JsonValue arenaArrayObject = robotSimulatorObject.get("arenaSize");
		if (arenaArrayObject.getValueType() != JsonValue.ValueType.ARRAY)
		{
			_logger.severe("arenaSize needs to be an array type");
			return;
		}
		
		// Get the extent for the arena size
		JsonArray arenaArray = (JsonArray) arenaArrayObject;
		if (arenaArray.size() != 2)
		{
			_logger.severe("arenaSize needs to be an array of size 2");
			return;			
		}
		_arena = new Arena(arenaArray.getJsonNumber(0).doubleValue(), arenaArray.getJsonNumber(1).doubleValue());
		
		// Load the walls
		final JsonValue wallsObject = robotSimulatorObject.get("walls");
		
		if (wallsObject == null)
		{
			_logger.info("No walls are defined");
		}
		else
		{
			parseWalls(wallsObject);
		}

		// Load the robots
		final JsonValue robotsObject = robotSimulatorObject.get("robots");
		
		if (robotsObject == null)
		{
			_logger.info("No robots are defined");
		}
		else
		{
			parseRobots(robotsObject);
		}

		// Load the labels
		final JsonValue labelsObject = robotSimulatorObject.get("labels");
		if (labelsObject == null)
		{
			_logger.info("No labels are defined");
		}
		else
		{
			parseLabels(labelsObject);
		}		
	}
	
	private void parseWalls(JsonValue wallsObject)
	{
		// Check the walls are of the right type and, if so, get them
		if (wallsObject.getValueType() != JsonValue.ValueType.ARRAY)
		{
			_logger.severe("walls needs to contain an array of wall object types");
			return;
		}
		else
		{
			final JsonArray wallsArray = (JsonArray) wallsObject;
			for (int w = 0; w < wallsArray.size(); ++w)
			{
				final JsonObject wallObject =  (JsonObject) wallsArray.get(w);
				
				// If specified, set up the ranges for random perturbations on the walls
				double randomX = 0;
				double randomY = 0;
				
				final JsonArray randomArray = (JsonArray) wallObject.get("random");
				if (randomArray != null)
				{
					randomX = randomArray.getJsonNumber(0).doubleValue();					
					randomY = randomArray.getJsonNumber(1).doubleValue();
				}
				
				final JsonArray startPointArray = (JsonArray) wallObject.get("startPoint");

				// Construct the start and end points, adding random perturbations if specified
				Point2d startPoint = new Point2d(startPointArray.getJsonNumber(0).doubleValue()
						+ randomX * (Math.random() - 0.5),
						startPointArray.getJsonNumber(1).doubleValue()
						+ randomY * (Math.random() - 0.5));

				final JsonArray endPointArray = (JsonArray) wallObject.get("endPoint");
				Point2d endPoint = new Point2d(endPointArray.getJsonNumber(0).doubleValue()
						+ randomX * (Math.random() - 0.5),
						endPointArray.getJsonNumber(1).doubleValue()
						+ randomY * (Math.random() - 0.5));
				
				final JsonNumber thicknessObject = wallObject.getJsonNumber("thickness");
				
				double thickness = 5;
				
				if (thicknessObject != null)
				{
					thickness = thicknessObject.doubleValue();
				}
				
				Wall wall = new Wall(startPoint, endPoint, thickness);
				_walls.add(wall);
			}
		}
	}

	private void parseRobots(JsonValue robotsObject)
	{
		// Check the walls are of the right type and, if so, get them
		if (robotsObject.getValueType() != JsonValue.ValueType.ARRAY)
		{
			_logger.severe("robots needs to contain an array of wall object types");
			return;
		}
		else
		{
			final JsonArray robotsArray = (JsonArray) robotsObject;
			//System.out.println("number of robots = " + robotsArray.size());
			for (int r = 0; r < robotsArray.size(); ++r)
			{
				//System.out.println("r = " + r);
				final JsonObject robotObject =  (JsonObject) robotsArray.get(r);
				//System.out.println("robotObject = " + robotObject);
				
				final JsonNumber idObject = robotObject.getJsonNumber("id");
				//System.out.println("idObject = " + idObject);
				
				if (idObject == null)
				{
					_logger.severe("robot must specify an id");
					continue;
				}				

				String id = idObject.toString();
				
				//System.out.println("id = " + id);
				
				String type = robotObject.getString("type");
				
				if (type == null)
				{
					_logger.severe("robot must specify a type");
					continue;
				}
						
				// If specified set up ranges for random sampling of the robot pose
				double randomX = 0;
				double randomY = 0;
				double randomTheta = 0;

				final JsonArray randomArray = (JsonArray) robotObject.get("random");
				if (randomArray != null)
				{
					randomX = randomArray.getJsonNumber(0).doubleValue();					
					randomY = randomArray.getJsonNumber(1).doubleValue();
					randomTheta = randomArray.getJsonNumber(2).doubleValue() * Math.PI / 180.0;
				}

				
				final JsonArray initialPoseArray = (JsonArray) robotObject.get("startingPose");
				
				if (initialPoseArray == null)
				{
					_logger.severe("robot must specify an initial pose");
					continue;
				}
				
				// Construct the robot, adding a random offset to the state if specified
				Robot robot = new ActivityBot(id,
						initialPoseArray.getJsonNumber(0).doubleValue() 
						+ randomX * (Math.random() - 0.5), 
						initialPoseArray.getJsonNumber(1).doubleValue() 
						+ randomY * (Math.random() - 0.5),
						initialPoseArray.getJsonNumber(2).doubleValue() * Math.PI / 180.0
						+ randomTheta * (Math.random() - 0.5));
				_robots.add(robot);
				//System.out.println("_robots.size() = " + _robots.size());
			}
		}		
	}
	
	private void parseLabels(JsonValue labelsObject)
	{
		// Check the walls are of the right type and, if so, get them
		if (labelsObject.getValueType() != JsonValue.ValueType.ARRAY)
		{
			_logger.severe("labels needs to contain an array of wall object types");
			return;
		}

		final JsonArray labelsArray = (JsonArray) labelsObject;
		//System.out.println("number of robots = " + robotsArray.size());
		for (int r = 0; r < labelsArray.size(); ++r)
		{
			final JsonObject labelObject =  (JsonObject) labelsArray.get(r);
				
			// Get the label
			JsonValue textObject = labelObject.get("text");
				
			if (textObject.getValueType() != JsonValue.ValueType.STRING)
			{
				_logger.severe("Text needs to be a string type");
				return;
			}
				
			String text = ((JsonString) textObject).getString();

			final JsonArray centreArray = (JsonArray) labelObject.get("centre");
				
			if (centreArray == null)
			{
				_logger.severe("label must specify a centre");
					continue;
			}
				
			double centreX =  centreArray.getJsonNumber(0).doubleValue();
			double centreY = centreArray.getJsonNumber(1).doubleValue();
			
			Label label = new Label(new Point2d(centreX, centreY), text);
			_labels.add(label);
		}		
	}
	
	private void navigateTree(JsonValue tree, String key) {
		   if (key != null)
		      System.out.print("Key " + key + ": ");
		   switch(tree.getValueType()) {
		      case OBJECT:
		         System.out.println("OBJECT");
		         JsonObject object = (JsonObject) tree;
		         for (String name : object.keySet())
		            navigateTree(object.get(name), name);
		         break;
		      case ARRAY:
		         System.out.println("ARRAY");
		         JsonArray array = (JsonArray) tree;
		         for (JsonValue val : array)
		            navigateTree(val, null);
		         break;
		      case STRING:
		         JsonString st = (JsonString) tree;
		         System.out.println("STRING " + st.getString());
		         break;
		      case NUMBER:
		         JsonNumber num = (JsonNumber) tree;
		         System.out.println("NUMBER " + num.toString());
		         break;
		      case TRUE:
		      case FALSE:
		      case NULL:
		         System.out.println(tree.getValueType().toString());
		         break;
		   }
	}

	public String getGUIWindowTitle()
	{
		return _title;
	}

}
