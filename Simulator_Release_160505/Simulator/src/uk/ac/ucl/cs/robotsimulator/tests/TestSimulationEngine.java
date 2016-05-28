package uk.ac.ucl.cs.robotsimulator.tests;

import uk.ac.ucl.cs.robotsimulator.simulation.SimulationEngine;

/**
 * This class tests the simulation engine
 *
 * @author ucacsjj
 *
 */

public class TestSimulationEngine
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		final SimulationEngine simulationEngine = new SimulationEngine(args);

		simulationEngine.getOrCreateNewRobot("test", "ActivityBot");

		simulationEngine.start();

		simulationEngine.run();

		System.out.println("Hello");

		/*
		 * // Cheesy spin loop while (true) { // simulationEngine.step(); try {
		 * Thread.sleep(1000); } catch (final Exception e) { System.exit(1); } }
		 */
	}

}
