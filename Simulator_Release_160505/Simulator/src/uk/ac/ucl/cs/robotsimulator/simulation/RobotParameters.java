package uk.ac.ucl.cs.robotsimulator.simulation;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import uk.ac.ucl.cs.robotsimulator.maths.SE2;

/**
 * This class stores a set of values for the robots. The values are taken from
 * the drawings Graeme provided on 18/11/2015.
 *
 * @author ucacsjj
 *
 */

public class RobotParameters
{

	// Body size (drawing / collision detection)
	public Vector2d bodyExtent = new Vector2d();

	// The number of encoder ticks per complete revolution of a wheel
	public double[] encoderTicksPerRevolution =
	{ 0, 0 };

	// Offset of the left and right wheels from the centre of the robot
	public SE2[] hubCentres =
	{ null, null };

	// Stabilising balls (just for drawing)
	public Vector3d[] stabilisingBalls;

	// Status lights (just for drawing)
	public Vector4d[] statusLights;

	// Wheel radii
	public double[] wheelRadii =
	{ 0, 0 };

	// Wheel thickness (just for drawing)
	public double[] wheelThickness =
	{ 8, 8 };

	// The maximum angular acceleration of each wheel
	public double[] maximumAngularAccelerationInRadians = {1e6, 1e6};

	public RobotParameters()
	{
		// TODO Auto-generated constructor stub
	}
}
