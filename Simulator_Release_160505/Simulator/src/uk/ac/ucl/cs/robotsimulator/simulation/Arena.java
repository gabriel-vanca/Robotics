package uk.ac.ucl.cs.robotsimulator.simulation;

/**
 * This class represents the arena, which is the area in which the robots
 * operate. It includes the walls and the robots.
 * 
 * @author ucacsjj
 *
 */

public class Arena extends Actor
{
	public Arena(double xExtent, double yExtent)
	{
		super();

		_pose.t.x = 0.5 * xExtent;
		_pose.t.y = 0.5 * yExtent;
		_extent.x = xExtent;
		_extent.y = yExtent;
	}
}
