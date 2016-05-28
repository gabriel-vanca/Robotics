package uk.ac.ucl.cs.robotsimulator.simulation;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.logging.Logger;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import uk.ac.ucl.cs.robotsimulator.maths.Ray;
import uk.ac.ucl.cs.robotsimulator.simulation.activitybot.ActivityBot;
import uk.ac.ucl.cs.robotsimulator.simulation.uclbot.UCLRobot;

public class SimulationEngine implements Runnable
{
	// The logger
	private final static Logger _logger = Logger.getLogger(SimulationEngine.class.getName());

	// The listeners used to
	private final HashSet<ActorChangeListener> _actorChangeEventListeners = new HashSet<ActorChangeListener>();

	// The set of waiting pawns and their wait time 
	private final HashMap<Long, Object> _waiters = new HashMap<Long, Object>();
	
	// The scenario generator
	private ScenarioGenerator _scenarioGenerator;
	
	// The arena
	private Arena _arena;

	// The robot instances
	private final HashMap<String, Robot> _robots = new HashMap<String, Robot>();

	// The thread within which the simulation engine runs
	private Thread _simulationThread;

	// The current simulation time in nanoseconds
	private long _simulationTimeNanos = 0;

	// For each wall clock tick, how many ns does this correspond to?
	private long _simulationTimeStepLengthNanos = 1 * 1000 * 1000;

	// The target time between each simulation step. This is more of an
	// aspiration because timing is not particularly precise.
	private final long _wallClockStepLength = 2;

	// The wall instances
	private final LinkedList<Wall> _walls = new LinkedList<Wall>();

	// Flag to show that the simulator thread should keep running
	boolean _keepRunning = true;

	public SimulationEngine(String[] args)
	{
		_logger.info("SimulationEngine.SimulationEngine: invoked");
	}

	public synchronized void addListener(ActorChangeListener changeListener)
	{
		synchronized (_actorChangeEventListeners)
		{
			_actorChangeEventListeners.add(changeListener);
		}
	}

	public String checkOrCreateNewRobot(String id, String type)
	{

		final Robot robot = getOrCreateNewRobot(id, type);

		if (robot == null)
		{
			return "ERR: COULD NOT CREATE OR GET ROBOT";
		}

		return "OKAY";
	}

	public Arena getArena()
	{
		return _arena;
	}

	synchronized public Robot getOrCreateNewRobot(String id, String type)
	{
		Robot robot;

		// If the robot exists, return the existing key to it
		if (_robots.containsKey(id) == true)
		{
			robot = _robots.get(id);
			return robot;
		}

		if (type.equals("ActivityBot"))
		{
			robot = new ActivityBot(id, Math.random() * 100, Math.random() * 100, Math.random() * 180);
		}
		else
		{
			robot = new UCLRobot(id, Math.random() * 100, Math.random() * 100, Math.random() * 180);
		}

		addRobot(robot);

		_logger.info("Created robot with id " + id + " at " + robot.getPose());
		return robot;
	}

	public Robot getRobot(String id)
	{
		synchronized (_robots)
		{
			if (_robots.containsKey(id) == false)
			{
				return null;
			}
			return _robots.get(id);
		}
	}

	// Get the list of robots. Used in the GUI, but this is actually pretty
	// nasty
	public HashMap<String, Robot> getRobots()
	{
		return _robots;
	}

	public long getSimulationTimeNanos()
	{
		return _simulationTimeNanos;
	}

	public long getSimulationTimeStepLengthNanos()
	{
		return _simulationTimeStepLengthNanos;
	}

	// Get the list of walls. Used in the GUI, but this is actually pretty
	// nasty
	public LinkedList<Wall> getWalls()
	{
		return _walls;
	}

	/**
	 * Determine if the candidate pose by the actor is clear or not
	 * 
	 * @param candidatePose
	 * @param robot
	 * @return
	 */
	public boolean isCandidatePoseClear(Actor actor)
	{
		// First check if any point in the actor is outside the arena area
		final Point2d[] corners = actor.getCorners();
		for (int c = 0; c < 4; ++c)
		{
			if ((corners[c].x < 0) || (corners[c].x > _arena.getExtent().x) || (corners[c].y < 0)
					|| (corners[c].y > _arena.getExtent().y))
			{
				return false;
			}
		}

		// Check if the actor collides with any wall
		for (final Wall wall : _walls)
		{
			// Don't check intersection with self
			if (wall == actor)
			{
				continue;
			}
			if (wall.overlaps(actor) == true)
			{
				return false;
			}
		}

		// Check if the actor collides with any other robot
		synchronized (this)
		{
			for (final Robot robot : _robots.values())
			{
				// Don't check intersection with self
				if (robot == actor)
				{
					continue;
				}
				if (robot.overlaps(actor) == true)
				{
					return false;
				}
			}
		}

		return true;
	}

	public void pause(long pauseIntervalInNanos, Object waitingObject)
	{
		synchronized(this)
		{
			final long sleepTime = _simulationTimeNanos + pauseIntervalInNanos;			
			_waiters.put(new Long(sleepTime), waitingObject);
		}
		
		try
		{
			synchronized(waitingObject)
			{
				waitingObject.wait();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Wait the specified number of ms, in simulation time
	 */
	public void pauseMillis(long pauseIntervalInMillis, Object waitingObject)
	{
		pause(pauseIntervalInMillis * 1000000L, waitingObject);
	}

	
	public void setScenarioGenerator(ScenarioGenerator scenarioGenerator)
	{
		_scenarioGenerator = scenarioGenerator;
	}
	
	public final ScenarioGenerator getScenarioGenerator()
	{
		return _scenarioGenerator;
	}
	
	public void populateScene()
	{
		//_logger.info("SimulationEngine.populateScene: invoked");

		// Stop the previously running simulation
		//_logger.info("SimulationEngine.populateScene: stop");
		stop();

		// Clear it of all objects

		//_logger.info("SimulationEngine.populateScene: removeAllActors");
		removeAllActors();
		
		// If a scenario generator was defined, use it
		
		if (_scenarioGenerator == null)
		{
			return;
		}

		// Set up the arena
		//_logger.info("SimulationEngine.populateScene: created the arena");
		_logger.fine("SimulationEngine.populateScene: creating the area");
		_arena = _scenarioGenerator.getArena();
		
		
		sendActorAddOrRemoveChangeEvent(_arena, true);
		
		_logger.fine("SimulationEngine.populateScene: registering the robots");

		// Now insert the robots
		if (_scenarioGenerator.getRobots() != null)
		{
			for (final Robot robot : _scenarioGenerator.getRobots())
			{
				addRobot(robot);
			}
		}
		
		_logger.fine("SimulationEngine.populateScene: registering the walls");
		// Now insert the walls
		if (_scenarioGenerator.getWalls() != null)
		{
			for (final Wall wall : _scenarioGenerator.getWalls())
			{
				addWall(wall);
			}
		}
		
		_logger.info("SimulationEngine.populateScene: start");
		start();

		// Now run the initial scripts
	}

	// Query to see if the ray intersects with any actor
	public synchronized RayQueryCollsionResult rayQuery(Actor originator, Ray ray)
	{
		RayQueryCollsionResult closestIntersection = new RayQueryCollsionResult();

		// Check if the actor collides with any wall
		for (final Wall wall : _walls)
		{
			// Don't check intersection with self
			if (wall == originator)
			{
				continue;
			}

			final RayQueryCollsionResult intersection = wall.rayQuery(ray);
			if ((intersection.actor != null) && (intersection.d < closestIntersection.d))
			{
				closestIntersection = intersection;
			}
		}

		// Check if the actor collides with any other robot
		for (final Robot robot : _robots.values())
		{
			// Don't check intersection with self
			if (robot == originator)
			{
				continue;
			}

			final RayQueryCollsionResult intersection = robot.rayQuery(ray);

			if ((intersection.actor != null) && (intersection.d < closestIntersection.d))
			{
				closestIntersection = intersection;
			}
		}

		return closestIntersection;
	}

	/**
	 * Reset the engine. Populate the scene again
	 */
	public void reset()
	{
		populateScene();
	}

	/**
	 * This method loops the engine until it ends. The engine is a discrete time
	 * simulator:
	 */

	@Override
	public void run()
	{
		// Current time - used to keep track of where we are and what's going on
		final long wallClockTime = System.currentTimeMillis();

		// The current simulation time
		_simulationTimeNanos = 0;

		// _logger.log(Log.BRIEF, "")
		_logger.info("SimulationEngine.run: started at wallclock time " + wallClockTime + ", simulation time "
				+ _simulationTimeNanos);

		// Start the robot(s) up
		for (final Robot _robot : _robots.values())
		{
			_robot.start(_simulationTimeNanos);
		}

		// Flag to keep the simulator running
		_keepRunning = true;

		// Target time that we will sleep until
		long targetWallClockSleepTime = wallClockTime;

		// Keep looping until we stop
		while (_keepRunning == true)
		{
			// This list is used 
			//LinkedList<Pawn> pawnPauseExpired = new LinkedList<Pawn>();			
			Long waiterPauseExpiredKey = null;
			
			// Synchronize this to ensure that each step is completed as an atomic operation
			synchronized(this)
			{
				// First figure out the time step length; this should either be to the next
				// default step or the next wait step; this is a pretty gross misuse of a 
				// hash map, but I did it to avoid having to set up arrays or priority queues
				// with additional classes
				long nextSimulationTimeNanos = _simulationTimeNanos + _simulationTimeStepLengthNanos;
				
				for (final Long pauseTime : _waiters.keySet())
				{
					if (pauseTime.longValue() < nextSimulationTimeNanos)
					{
						nextSimulationTimeNanos = pauseTime.longValue();
						waiterPauseExpiredKey = pauseTime;
					}
				}				
				_simulationTimeNanos = nextSimulationTimeNanos;
				
				// Now step all the robots; this updates all the motor controllers, etc.
				for (final Robot robot : _robots.values())
				{
					robot.step(_simulationTimeNanos);
				}				
			}
			
			// If the pause has expired, remove it from the schedule and notify all
			if (waiterPauseExpiredKey != null)
			{
				Object waiterPauseExpired = _waiters.get(waiterPauseExpiredKey);
				_waiters.remove(waiterPauseExpiredKey);
				synchronized(waiterPauseExpired)
				{
					waiterPauseExpired.notifyAll();
				}
			}
			
			// Now figure out how long the this thread has to sleep in wallclock time
			// to hit the next simulator step
			final long now = System.currentTimeMillis();
			targetWallClockSleepTime += _wallClockStepLength;
			final long sleepDuration = targetWallClockSleepTime - now;

			// _logger.info("SimulationEngine.run: sleepDuration=" +
			// sleepDuration);

			// If the sleep time is positive, this means that our target sleep
			// time is ahead of the current time and all is well.
			// If the sleep time is negative, the target sleep time is earlier
			// than the current time - the simulator has fallen behind. The
			// policy in this case is that we note this fact and advance the sleep time.
			if (sleepDuration >= 0)
			{
				try
				{
					Thread.sleep(sleepDuration);
				}
				catch (final Exception e)
				{
					_keepRunning = false;
					break;
				}
			}
			else
			{
				_logger.warning("SimulationEngine.run: missed last sleep time; overshoot is "
						+ (-sleepDuration / 1000.0) + "s");
				targetWallClockSleepTime = now + _wallClockStepLength;
			}
		}
		_logger.info("SimulationEngine.run: simulation loop finished");
	}

	public synchronized void setSimulationTimeStepLengthNanos(long simulationTimeStepLengthNanos)
	{
		_simulationTimeStepLengthNanos = simulationTimeStepLengthNanos;
	}

	/**
	 * Start the simulation engine going.
	 */
	public void start()
	{
		_simulationThread = new Thread(this);
		_simulationThread.start();
	}

	public void stop()
	{
		if (_simulationThread == null)
		{
			return;
		}		
		
		//_logger.info("SimulationEngine.stop: invoked");
		// Stop the simulator
		_keepRunning = false;

		// Wait until the simulation thread has terminated
		_logger.info("SimulationEngine.stop: waiting for simulation thread to join");

		try
		{
			_simulationThread.join();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			_logger.severe(e.getMessage());
		}
	}

	private void sendActorAddOrRemoveChangeEvent(Actor actor, boolean created)
	{
		final ActorAddOrRemoveEvent re = new ActorAddOrRemoveEvent(actor, created);
		for (final ActorChangeListener changeListener : _actorChangeEventListeners)
		{
			changeListener.handleAddOrRemoveEvent(re);
		}		
	}
	
	private void removeAllActors()
	{
		// Signal all robots and walls are being removed
		for (final Robot robot : _robots.values())
		{
			sendActorAddOrRemoveChangeEvent(robot, false);
		}

		for (final Wall wall : _walls)
		{
			sendActorAddOrRemoveChangeEvent(wall, false);
		}

		// Now clear them
		_robots.clear();
		_walls.clear();
		
		// Finally remove the arena
		sendActorAddOrRemoveChangeEvent(_arena, false);
		_arena = null;
	}

	// The map
	// private Map _map;

	protected void addRobot(Robot robot)
	{
		_logger.finest("SimulationEngine.addRobot: invoked");
		synchronized (_robots)
		{
			final String id = robot.getName();
			if (_robots.containsKey(id) == false)
			{
				robot.setSimulationEngine(this);
				_robots.put(id, robot);
				robot.reset();
				robot.start(_simulationTimeNanos);
				final ActorAddOrRemoveEvent re = new ActorAddOrRemoveEvent(robot, true);
				synchronized (_actorChangeEventListeners)
				{
					for (final ActorChangeListener changeListener : _actorChangeEventListeners)
					{
						changeListener.handleAddOrRemoveEvent(re);
					}
				}
			}
			else
			{
				_logger.severe("Robot " + robot.getName() + " already registered");
			}
		}
	}

	protected void addWall(Wall wall)
	{
		_logger.finest("SimulationEngine.addWall: invoked");
		synchronized (_walls)
		{
			wall.setSimulationEngine(this);
			_walls.add(wall);
			final ActorAddOrRemoveEvent re = new ActorAddOrRemoveEvent(wall, true);
			synchronized (_actorChangeEventListeners)
			{
				for (final ActorChangeListener changeListener : _actorChangeEventListeners)
				{
					changeListener.handleAddOrRemoveEvent(re);
				}
			}
		}
	}

	protected void loadSceneFile(File sceneryFile)
	{
		stop();

	}

}
