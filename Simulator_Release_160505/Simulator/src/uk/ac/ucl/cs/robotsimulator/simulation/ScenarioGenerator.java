package uk.ac.ucl.cs.robotsimulator.simulation;

import java.util.LinkedList;

/**
 * This class stores a scenario at its start conditions. It is used to (re)set a
 * scenario.
 *
 * The idea is to separate out scenario creation from the internal logic of the
 * simulation engine itself.
 *
 * Note that the objects given by this generator will be used by the engine
 * directly.
 *
 * @author ucacsjj
 *
 */

public abstract class ScenarioGenerator
{

	protected SimulationEngine _simulationEngine;

	public ScenarioGenerator()// SimulationEngine simulationEngine)
	{
		// _simulationEngine = simulationEngine;
	}

	public abstract Arena getArena();

	// Get the initial sequence of commands to be run. These are executed as if
	// through
	// a virtual client connection
	public String[] getInitialCommandList()
	{
		return null;
	}

	public abstract LinkedList<Robot> getRobots();

	public abstract LinkedList<Wall> getWalls();
	
	public abstract String getGUIWindowTitle();
}
