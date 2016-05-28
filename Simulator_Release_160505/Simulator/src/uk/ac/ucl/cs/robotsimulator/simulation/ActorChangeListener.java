package uk.ac.ucl.cs.robotsimulator.simulation;

/**
 * Listeners of this type can process changes to robots.
 */

public interface ActorChangeListener
{
	public void handleAddOrRemoveEvent(ActorAddOrRemoveEvent event);
}
