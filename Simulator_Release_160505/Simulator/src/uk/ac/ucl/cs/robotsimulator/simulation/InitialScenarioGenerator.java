package uk.ac.ucl.cs.robotsimulator.simulation;

import java.util.LinkedList;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import uk.ac.ucl.cs.robotsimulator.simulation.activitybot.ActivityBot;

public class InitialScenarioGenerator extends ScenarioGenerator
{

	private final Vector2d _arenaSize;

	public InitialScenarioGenerator()
	{
		// Create the arena
		_arenaSize = new Vector2d(1600, 1600);
	}

	@Override
	public Arena getArena()
	{
		final Arena arena = new Arena(_arenaSize.x, _arenaSize.y);
		return arena;
	}

	@Override
	public LinkedList<Robot> getRobots()
	{
		final LinkedList<Robot> robots = new LinkedList<Robot>();

		robots.add(new ActivityBot("0", 800, 800, 0));

		return robots;
	}

	@Override
	public LinkedList<Wall> getWalls()
	{
		final LinkedList<Wall> walls = new LinkedList<Wall>();
		walls.add(new Wall(new Point2d(0, 0), new Point2d(_arenaSize.x, 0), 5.0));
		walls.add(new Wall(new Point2d(_arenaSize.x, 0), new Point2d(_arenaSize.x, _arenaSize.y), 5.0));
		walls.add(new Wall(new Point2d(_arenaSize.x, _arenaSize.y), new Point2d(0, _arenaSize.y), 5.0));
		walls.add(new Wall(new Point2d(0, _arenaSize.y), new Point2d(0, 0), 5.0));

		return walls;
	}

	public String getGUIWindowTitle()
	{
		return "Default";
	}
}
