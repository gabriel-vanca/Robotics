package uk.ac.ucl.cs.robotsimulator.maths;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

public class Ray
{
	public final Vector2d direction;

	public final Point2d start;

	public Ray(double x, double y, double dx, double dy)
	{
		start = new Point2d(x, y);
		direction = new Vector2d(dx, dy);
		direction.normalize();
	}
}
