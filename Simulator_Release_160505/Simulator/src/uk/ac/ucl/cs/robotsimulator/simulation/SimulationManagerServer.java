package uk.ac.ucl.cs.robotsimulator.simulation;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

/**
 * This class handles the server operations with the clients. Specifically, it
 * handles two things: simulation-level configuration (new scenarios, etc.) and
 * getting access to a new robot
 * 
 * @author ucacsjj
 *
 */

public class SimulationManagerServer implements Runnable
{
	// The logger
	private final Logger _logger = Logger.getLogger(SimulationManagerServer.class.getName());

	private final int _portNumber;

	// The thread within which the simulation engine runs
	private Thread _serverThread;

	private final SimulationEngine _simulationEngine;

	public SimulationManagerServer(String[] args, SimulationEngine simulationEngine)
	{
		_portNumber = 55442;// Integer.parseInt(args[0]);
		_simulationEngine = simulationEngine;
		_logger.info(
				"SimulationManagerServer.SimulationManagerServer: constructed to listen on port " + _simulationEngine);
	}

	@Override
	public void run()
	{
		_logger.info("SimulationManagerServer.run: service thread started");

		// TODO Auto-generated method stub
		final boolean listening = true;

		try (ServerSocket serverSocket = new ServerSocket(_portNumber))
		{
			while (listening)
			{
				// _logger.info("SimulationManagerServer.run: started new client
				// end point thread");
				new ClientConnection(serverSocket.accept(), _simulationEngine).start();
			}
		}
		catch (final IOException e)
		{
			System.err.println("Could not listen on port " + _portNumber);
			System.exit(-1);
		}
	}

	public void start()
	{
		_serverThread = new Thread(this);
		_serverThread.start();
	}
}
