package uk.ac.ucl.cs.robotsimulator.simulation;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

/**
 * This class represents each wall. Walls are modelled as line segments. Need to
 * be rotated rectangles.
 *
 * @author ucacsjj
 *
 */

public class Wall extends Actor
{
	public Wall(Point2d startPoint, Point2d endPoint, double thickness)
	{
		super();

		// Figure out the geometry for the wall. The assumption is that the
		// local x axis points along the wall
		_pose.t.x = 0.5 * (startPoint.x + endPoint.x);
		_pose.t.y = 0.5 * (startPoint.y + endPoint.y);
		final Vector2d delta = new Vector2d();
		delta.sub(endPoint, startPoint);
		_pose.theta = Math.atan2(delta.y, delta.x);
		_extent.x = delta.length();
		_extent.y = thickness;

		update();
	}
}
