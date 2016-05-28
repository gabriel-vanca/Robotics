package uk.ac.ucl.cs.robotsimulator;

import java.awt.EventQueue;

import uk.ac.ucl.cs.robotsimulator.gui.RobotSimulatorGUI;
import uk.ac.ucl.cs.robotsimulator.simulation.InitialScenarioGenerator;
import uk.ac.ucl.cs.robotsimulator.simulation.ScenarioGenerator;
import uk.ac.ucl.cs.robotsimulator.simulation.SimulationEngine;
import uk.ac.ucl.cs.robotsimulator.simulation.SimulationManagerServer;

public class Launcher
{

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		// Needed for setting up the GUI loop
		final String[] finalizedArgs = args;

		// Create the simulation engine, which coordinates everything
		final SimulationEngine simulationEngine = new SimulationEngine(finalizedArgs);

		// Set up the initial scene and set the simulator to use it
		final ScenarioGenerator initialScenarioGenerator = new InitialScenarioGenerator();
		simulationEngine.setScenarioGenerator(initialScenarioGenerator);
		simulationEngine.populateScene();

		// Create the interface through which networked clients can communicate
		final SimulationManagerServer simulationEngineManager = new SimulationManagerServer(finalizedArgs,
				simulationEngine);
		simulationEngineManager.start();

		// Create the GUI. NOTE: this is optional; if you remove this, you can
		// run the simulator in a headless configuration.
		EventQueue.invokeLater(new GUILauncher(args, simulationEngine));
	}
	
	// Helper class for deferred execution; use this rather than lambda so that Java 1.7
	// can be used.
	protected static class GUILauncher implements Runnable
	{
		private String[] _args;
		private SimulationEngine _simulationEngine;
		
		public GUILauncher(final String[] args, SimulationEngine simulationEngine)
		{
			_args = args;
			_simulationEngine = simulationEngine;
		}
		
		public void run()
		{
			try
			{
				final RobotSimulatorGUI window = new RobotSimulatorGUI();
				window.initialize(_args, _simulationEngine);
				window.setVisible(true);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
}
