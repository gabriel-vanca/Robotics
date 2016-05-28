package uk.ac.ucl.cs.robotsimulator.simulation;

import java.util.LinkedList;

/**
 * This scenario generator creates an initial population of objects for default
 * tests.
 * 
 * @author ucacsjj
 *
 */

public class TestScenarioGenerator extends ScenarioGenerator
{

	public TestScenarioGenerator()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public Arena getArena()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<Robot> getRobots()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<Wall> getWalls()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getGUIWindowTitle()
	{
		return "Test";
	}
}
