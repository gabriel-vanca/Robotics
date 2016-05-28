package uk.ac.ucl.cs.robotsimulator.simulation;

/**
 * This event is used to signal when a robot is added or removed. Since this
 * event is only created when a change in robots happens, it's sufficient to set
 * added to true when the robot is created; false means the robot is removed.
 * 
 * @author ucacsjj
 *
 */

public class ActorAddOrRemoveEvent
{
	private final Actor _actor;

	private final boolean _added;

	public ActorAddOrRemoveEvent(Actor actor, boolean added)
	{
		_actor = actor;
		_added = added;
	}

	public Actor getActor()
	{
		return _actor;
	}

	public boolean getAdded()
	{
		return _added;
	}
	
	public String toString()
	{
		return _actor + " (" + _added + ")";
	}
}
