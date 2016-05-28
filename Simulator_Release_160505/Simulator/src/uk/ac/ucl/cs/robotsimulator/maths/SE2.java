package uk.ac.ucl.cs.robotsimulator.maths;

/**
 * This class encapsulates the position and orientation of a coordinate frame in
 * 2D. In fancy parlance, this is called SE(2) after the Special Euclidean Group
 * 2. See https://en.wikipedia.org/wiki/Euclidean_group for gory details.
 *
 * The convention is to perform the rotation first followed by the translation.
 *
 * @author ucacsjj
 *
 */

import javax.vecmath.Vector2d;

public class SE2
{

	public Vector2d t = new Vector2d();
	public double theta;

	/*
	 * public void set(float x, float y, float theta_) { _x = x; _y = y; _theta;
	 * }
	 *
	 * public void set(MutableSE2 newSE2) { position.x = newSE2.position.x;
	 * position.y = newSE2.position.y; theta = newSE2.theta; }
	 *
	 * public Mutablefloat2D getPosition() { return position; }
	 *
	 * public void setPosition(Mutablefloat2D position_) { position = position_;
	 * }
	 *
	 * public float getOrientation() { return theta; }
	 *
	 * public void setOrientation(float theta_) { theta = theta_; }
	 *
	 * // Apply the transformation here to public MutableSE2
	 * postMultiply(MutableSE2 b) { MutableSE2 newSE2 = new MutableSE2();
	 * newSE2.position.x = Math.cos(theta) * b.position.x - Math.sin(theta) *
	 * b.position.y + position.x; newSE2.position.y = Math.sin(theta) *
	 * b.position.x + Math.cos(theta) * b.position.y + position.y; newSE2.theta
	 * = theta + b.theta;
	 *
	 * return newSE2; }
	 *
	 *
	 * private Mutablefloat2D position = new Mutablefloat2D();
	 *
	 * private float theta = 0;
	 */
	public SE2()
	{
		// TODO Auto-generated constructor stub
	}

	public SE2(double x, double y, double theta)
	{
		this.t.x = x;
		this.t.y = y;
		this.theta = theta;
	}

	public SE2(final SE2 other)
	{
		set(other);
	}

	// result = other * this
	public SE2 postMultiply(final SE2 other)
	{
		final SE2 newSE2 = new SE2();
		newSE2.t.x = ((Math.cos(theta) * other.t.x) - (Math.sin(theta) * other.t.y)) + t.x;
		newSE2.t.y = (Math.sin(theta) * other.t.x) + (Math.cos(theta) * other.t.y) + t.y;
		newSE2.theta = theta + other.theta;

		return newSE2;
	}

	public void set(final SE2 other)
	{
		this.t.set(other.t);
		this.theta = other.theta;
	}

	public void setZero()
	{
		t.x = 0;
		t.y = 0;
		theta = 0;
	}

	@Override
	public String toString()
	{
		return "(x: " + t.x + ", y: " + t.y + ", theta: " + theta + ")";
	}

}
