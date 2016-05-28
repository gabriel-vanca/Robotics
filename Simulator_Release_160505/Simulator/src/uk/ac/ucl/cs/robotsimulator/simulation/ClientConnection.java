package uk.ac.ucl.cs.robotsimulator.simulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Each client is granted its own threaded connection to talk to the simulation
 * engine. The rationale for this is that this thread can block until the
 * request has been completed without stalling everything.
 * 
 * @author ucacsjj
 *
 */

public class ClientConnection extends Thread
{

	private final static String getRobotHandle = "getRobotHandle";
	private final static String getTimeInNanos = "getTimeInNanos";
	private final static String pauseClientInNanos = "pauseClientInNanos";
	private final static String reset = "reset";

	// The logger - one per robot instance
	private final Logger _logger = Logger.getLogger(ClientConnection.class.getName());

	private final SimulationEngine _simulationEngine;

	private final Socket _socket;

	public ClientConnection(Socket socket, SimulationEngine simulationEngine)
	{
		super("ClientConnection");
		_socket = socket;
		_simulationEngine = simulationEngine;
	}

	@Override
	public void run()
	{
		_logger.info("ClientConnection.run: started new client end point thread");
		try (PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));)
		{
			String inputLine;
			final String outputLine;
			while ((inputLine = in.readLine()) != null)
			{
				_logger.fine("ClientConnection.run: received " + _socket + ": " + inputLine);

				// Split the input string
				final String[] parts = inputLine.split("\\s+");
				final String cmd = parts[0];

				String result = "";

				// Is this a robot or a simulator command? Handle each
				// separately

				// Now process the command
				if (cmd.startsWith("SIMULATOR"))
				{
					result = handleSimulatorCommand(parts);
				}
				else if (cmd.startsWith("ROBOT"))
				{
					result = handleRobotCommand(parts);
				}
				else
				{
					result = "Unknown command " + parts[0] + "; it must start with SIMULATOR or ROBOT";
				}
				
				// Send back the message, prepending with the time stamp first
				final String timeSync = "TIMESYNC " + _simulationEngine.getSimulationTimeNanos() + " ";
				out.println(timeSync + result);
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		_logger.info("Connection completed");
	}

	// Get the requested robot ID and handle
	private String handleGetRobotHandle(final String[] parts)
	{
		// Check we have the right number of arguments
		if (parts.length != 4)
		{
			return "ERR:incorrect number of arguments; should be 4 but is " + parts.length;
		}

		// Check the type is known
		if (parts[3].equals("ActivityBot") == false)
		{
			return "ERR: ROBOT TYPE " + parts[3] + " UNKNOWN; THE SUPPORTED TYPE IS ActivityBot";
		}

		// Now get or create the robot instance
		final Robot robot = _simulationEngine.getOrCreateNewRobot(parts[2], parts[3]);

		if (robot == null)
		{
			return "ERR: COULD NOT CREATE OR GET ROBOT";
		}

		// If the result is okay, return
		return "OKAY " + parts[2];
	}

	// Get the requested robot ID and handle
	private String handlePauseClientInNanos(final String[] parts)
	{
		// Check we have the right number of arguments
		if (parts.length != 3)
		{
			return "ERR: incorrect number of arguments; should be 3 but is " + parts.length;
		}

		long pauseTimeInNanos;

		try
		{
			pauseTimeInNanos = Long.parseLong(parts[2]);
		}
		catch (final Exception e)
		{
			return "ERR: Could not parse the pause period " + parts[3];
		}

		_simulationEngine.pause(pauseTimeInNanos, this);

		return "OKAY";
	}

	private String handleRobotCommand(String[] parts)
	{
		// Get the robot
		final Robot robot = _simulationEngine.getRobot(parts[1]);

		// If it does not exist, return
		if (robot == null)
		{
			return "ERR: robot handle " + parts[1] + " not valid";
		}

		return robot.executeCommand(parts);
	}

	private String handleSimulatorCommand(String[] parts)
	{
		// If the command is to get a robot
		if (getRobotHandle.equals(parts[1]))
		{
			return handleGetRobotHandle(parts);
		}
		else if (reset.equals(parts[1]))
		{
			_simulationEngine.reset();
			return "OKAY";
		}
		else if (pauseClientInNanos.equals(parts[1]))
		{
			return handlePauseClientInNanos(parts);
		}
		else if (getTimeInNanos.equals(parts[1]))
		{
			// Gets the current time; actually a NO-OP
			return "OKAY";
		}

		return "ERR: not implemented yet";
	}
}
