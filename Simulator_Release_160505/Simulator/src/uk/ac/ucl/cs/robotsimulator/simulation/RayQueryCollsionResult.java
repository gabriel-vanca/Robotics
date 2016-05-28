package uk.ac.ucl.cs.robotsimulator.simulation;

import javax.vecmath.Point2d;

public class RayQueryCollsionResult
{
	// The actor the ray intersects with
	public Actor actor = null;

	// Distance to the intersection
	public double d = Double.MAX_VALUE;

	// The point of intersection

	public final Point2d point = new Point2d();
}
