package uk.ac.ucl.cs.robotsimulator.simulation.uclbot;

import uk.ac.ucl.cs.robotsimulator.simulation.Robot;

public class UCLRobot extends Robot
{
	// Convenience enum used to keep track of what's been requested on the robot
	protected enum MotorSide
	{
		LEFT, LEFT_AND_RIGHT, RIGHT, UNKNOWN
	}

	// The scale factor from the wheel speed to the angular velocity of the
	// motor. TODO: put it in robot parameters
	private final float _motorSpeedToMotorAngularVelocity = 1;

	public UCLRobot()
	{
		super();
		// TODO Auto-generated constructor stub
		_parameters = new UCLRobotParameters();
	}

	public UCLRobot(String name, double startX, double startY, double startTheta)
	{
		super(name, startX, startY, startTheta);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method interprets the commands and queries the platform. The
	 * assumption here is that the commands are processed instantly.
	 *
	 * @param line
	 * @return
	 */
	@Override
	public synchronized String executeCommand(String[] parts)
	{
		// System.out.println("executeCommand: executing command " + line);

		// Store the time we got the message - this is used to reset the
		// watchdog timer
		// _lastMessageReceivedTime = System.currentTimeMillis();
		/*
		 * if (line == null) { return "Command line is empty"; } final String[]
		 * parts = line.split("\\s+"); // System.out.println(parts.length);
		 * final String cmd = parts[0]; switch (cmd) { case "M": // motor
		 * command return executeMotorCommand(parts); default: return
		 * "Unknown command " + line; }
		 */
		return "OKAY";
	}

	public String executeMotorCommand(String[] parts)
	{
		// First check we have the right number of parameters
		if (parts.length < 3)
		{
			return "ERR need at least 2 params";// + s; break;
		}

		// TODO: CHECK THAT THE ORDER IN WHICH THE COMMANDS ARE PROCESSED IS
		// CONSISTENT TO GIVE CONSISTENT ERROR MESSAGES.

		MotorSide motorSide = MotorSide.UNKNOWN;

		if (parts[1].equals("L") == true)
		{
			motorSide = MotorSide.LEFT;
		}
		else if (parts[1].equals("R") == true)
		{
			motorSide = MotorSide.RIGHT;
		}
		else if (parts[1].equals("LR") == true)
		{
			motorSide = MotorSide.LEFT_AND_RIGHT;
		}

		if (motorSide == MotorSide.UNKNOWN)
		{
			return "wrong motor side. Valid values: L or R or LR";
		}

		if (motorSide == MotorSide.LEFT_AND_RIGHT)
		{
			if (parts.length != 4)
			{
				return "ERR need 3 params";
			}
		}
		else
		{
			if (parts.length != 3)
			{
				return "ERR need 2 params";
			}
		}

		// Check we have a valid speed value
		int speed1;

		try
		{
			speed1 = Integer.parseInt(parts[2]);
		}
		catch (final Exception e)
		{
			return "ERR not an int";// + s;;
		}

		if ((speed1 < -255) || (speed1 > 255))
		{
			return "ERR invalid speed (integer between -255 and 255)";
		}

		// If it's just the left or the right wheel, our job is done
		if (motorSide == MotorSide.LEFT)
		{
			synchronized (this)
			{
				_requestedWheelSpeedInRadians[L] = speed1 * _motorSpeedToMotorAngularVelocity;
			}
			return ".";
		}
		else if (motorSide == MotorSide.RIGHT)
		{
			synchronized (this)
			{
				_requestedWheelSpeedInRadians[R] = speed1 * _motorSpeedToMotorAngularVelocity;
			}
			return ".";
		}

		// Down here we should have the case that we set the speed on both
		// wheels simultaneously. To do this, we need two valid numbers, one for
		// each wheel
		assert (motorSide == MotorSide.LEFT_AND_RIGHT);

		int speed2;

		try
		{
			speed2 = Integer.parseInt(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR not an int";// + s;;
		}

		if ((speed2 < -255) || (speed2 > 255))
		{
			return "ERR invalid speed (integer between -255 and 255)";
		}

		synchronized (this)
		{
			_requestedWheelSpeedInRadians[L] = speed1 * _motorSpeedToMotorAngularVelocity;
			_requestedWheelSpeedInRadians[R] = speed2 * _motorSpeedToMotorAngularVelocity;
			// System.out.println(_requestedWheelSpeed[L] + "," +
			// _requestedWheelSpeed[R]);
		}
		return ".";
	}

	@Override
	protected void handleCollision()
	{
		// Do nothing
	}

	@Override
	protected void updateMotorControl(double dT)
	{
		// TODO Auto-generated method stub

	}

	protected void updateMotorControl(float dT)
	{
		// Update the actual wheel speed to converge to the requested one
		_wheelSpeedInRadians[L] = _requestedWheelSpeedInRadians[L];
		_wheelSpeedInRadians[R] = _requestedWheelSpeedInRadians[R];
	}

}
