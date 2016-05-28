package uk.ac.ucl.cs.robotsimulator.simulation;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Pawn extends Actor
{
	// Command completion lock and condition variable
	protected final Lock _commandCompletionLock = new ReentrantLock();
	protected final Condition _commandCompletionCondition = _commandCompletionLock.newCondition();

	// The latest time
	protected long _currentTimeNanos;

	// The last time the pawn was updated
	protected long _lastTimeNanos;

	public Pawn()
	{
		super();
	}

	// Command to cause the pawn to perform some action
	public abstract String executeCommand(String[] command);

	public void pause(long pauseTimeInNanos)
	{
		_simulationEngine.pause(pauseTimeInNanos, this);
	}
	
	public void signalCommandComplete()
	{
		_commandCompletionLock.lock();
		_commandCompletionCondition.signal();
		_commandCompletionLock.unlock();
	}

	public void start(long now)
	{
		_currentTimeNanos = now;
		_lastTimeNanos = now;
	}

	// Helper functions

	public void step(long now)
	{
		_lastTimeNanos = _currentTimeNanos;
		_currentTimeNanos = now;
	}

	public void waitForCommandToComplete()
	{
		// Wait until the command was completed
		_commandCompletionLock.lock();

		try
		{
			_commandCompletionCondition.await();
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
		finally
		{
			_commandCompletionLock.unlock();
		}
	}

}
