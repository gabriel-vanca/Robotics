package uk.ac.ucl.cs.robotsimulator.simulation.uclbot;

import uk.ac.ucl.cs.robotsimulator.maths.SE2;
import uk.ac.ucl.cs.robotsimulator.simulation.Robot;
import uk.ac.ucl.cs.robotsimulator.simulation.RobotParameters;

public class UCLRobotParameters extends RobotParameters
{

	public UCLRobotParameters()
	{
		// Offset of the left and right wheels from the centre of the robot
		hubCentres[Robot.L] = new SE2(0.0f, -35.8f, 0.0f);
		hubCentres[Robot.R] = new SE2(0.0f, -35.8f, 0.0f);

		encoderTicksPerRevolution[Robot.L] = 100;
		encoderTicksPerRevolution[Robot.R] = 100;

		wheelRadii[Robot.L] = 28f;
		wheelRadii[Robot.R] = 28f;

		wheelThickness[Robot.L] = 8;
		wheelThickness[Robot.R] = 8;

		bodyExtent.x = 80;
		bodyExtent.y = 80;
	}

}
