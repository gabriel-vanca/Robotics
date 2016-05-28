package uk.ac.ucl.cs.robotsimulator.simulation.activitybot;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import uk.ac.ucl.cs.robotsimulator.maths.SE2;
import uk.ac.ucl.cs.robotsimulator.simulation.Robot;
import uk.ac.ucl.cs.robotsimulator.simulation.RobotParameters;

public class ActivityBotParameters extends RobotParameters
{

	// IR sensors
	final public SE2[] irSensors = new SE2[2];

	// Ping sensor
	final public SE2 pingSensor = new SE2();

	public ActivityBotParameters()
	{
		// Body frame data from
		// https://www.parallax.com/sites/default/files/downloads/700-00022-Boe-Bot-Chassis-Dimensions.pdf
		final double offset = (127 * 0.5) - 12.1 - (49.4 * 0.5);

		hubCentres[Robot.L] = new SE2(offset, 105.8 * 0.5, 0.0);
		hubCentres[Robot.R] = new SE2(offset, -105.8 * 0.5, 0.0);

		bodyExtent.x = 127;
		bodyExtent.y = 82.6;

		// Ping sensor location; assume 2cm back from the front of the robot
		pingSensor.t.x = (bodyExtent.x * 0.5) - 20;
		pingSensor.t.y = 0;
		pingSensor.theta = 0;

		// IR sensor locations; assume 3cm back from the front of the robot, 3cm
		// in from the edge
		irSensors[Robot.L] = new SE2((bodyExtent.x * 0.5) - 30, (bodyExtent.y * 0.5) - 30, 0.5 * Math.PI);
		irSensors[Robot.R] = new SE2((bodyExtent.x * 0.5) - 30, (-bodyExtent.y * 0.5) + 30, -0.5 * Math.PI);

		// Wheel information from https://www.parallax.com/product/28114
		wheelRadii[Robot.L] = 33;
		wheelRadii[Robot.R] = 33;

		// 1/8"
		wheelThickness[Robot.L] = 3.175;
		wheelThickness[Robot.R] = 3.175;

		// Encoder ticks information from abdrive.h
		encoderTicksPerRevolution[Robot.L] = 64;
		encoderTicksPerRevolution[Robot.R] = 64;

		// LEDs
		statusLights = new Vector4d[2];

		// Centre, extents
		statusLights[0] = new Vector4d((bodyExtent.x * 0.5) - 32, (-0.5 * bodyExtent.y) + 2, 12, 16);
		statusLights[1] = new Vector4d((bodyExtent.x * 0.5) - 14, (-0.5 * bodyExtent.y) + 2, 12, 16);

		// Stabilising balls
		stabilisingBalls = new Vector3d[1];
		stabilisingBalls[0] = new Vector3d(-bodyExtent.x * 0.5, 0, 13.5);
	}

}
