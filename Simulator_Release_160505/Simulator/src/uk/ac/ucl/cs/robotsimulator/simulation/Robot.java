/**
 *
 */
package uk.ac.ucl.cs.robotsimulator.simulation;

import Jama.Matrix;
import uk.ac.ucl.cs.robotsimulator.maths.SE2;

/**
 * This class
 *
 * @author ucacsjj
 *
 */

public abstract class Robot extends Pawn
{

	// Command to get the robot pose
	private final static String getPoseCommand = "getPose";	

	// Command to set the encoder ticks per wheel revolution
	private final static String setEncoderTicksPerRevolutionCommand = "setEncoderTicksPerRevolution";	

	// Command to get the encoder ticks per wheel revolution
	private final static String getEncoderTicksPerRevolutionCommand = "getEncoderTicksPerRevolution";	
	
	// Constants to index the left and right wheels
	public final static int L = 0;

	public final static int R = 1;

	// The starting pose
	private final SE2 _startingPose = new SE2();
	protected final double[] _encoderTicksToRadians =
	{ 0, 0 };
	protected final double[] _encoderTicksToWheelOdometry =
	{ 0, 0 };
	// The layout of the robot
	protected RobotParameters _parameters;

	protected final double[] _radiansToEncoderTicks =
	{ 0, 0 };

	// The requested wheel speed - in rad /s
	protected final double[] _requestedWheelSpeedInRadians =
	{ 0, 0 };

	// The status lights
	protected int[] _statusLights;

	// The angles of the wheels
	protected final double[] _wheelAngles =
	{ 0, 0 };

	// Wheel odometry - distance in mm
	protected final double[] _wheelOdometry =
	{ 0, 0 };

	// The wheel odometry in encoder ticks
	protected final long[] _wheelOdometryInEncoderTicks =
	{ 0, 0 };

	// Helper unit conversions
	protected final double[] _wheelOdometryToEncoderTicks =
	{ 0, 0 };
	// Wheel speed - in rad / s
	protected final double[] _wheelSpeedInRadians =
	{ 0, 0 };

	/**
	 *
	 */
	public Robot()
	{
		super();
	}

	public Robot(String name, double startX, double startY, double startTheta)
	{
		super();
		setStartingPose(startX, startY, startTheta);
		setName(name);
	}

	public long[] getEncoderTicks()
	{
		final long[] encoderTicks = new long[2];
		encoderTicks[L] = _wheelOdometryInEncoderTicks[L];
		encoderTicks[R] = _wheelOdometryInEncoderTicks[R];
		return encoderTicks;
	}

	public RobotParameters getParameters()
	{
		return _parameters;
	}

	// The derivative of the vehicle kinematics. This tries to solve the linear
	// equation associating wheel
	// speed with overall platform velocity. Note that because the system is
	// over constrained, it is possible that
	// a precise solution does not exist. In this case, I guess the physical
	// explanation is that it's minimising the
	// magnitude of wheel slip.

	public int[] getStatusLights()
	{
		final int[] statusLights = new int[_statusLights.length];
		for (int k = 0; k < _statusLights.length; ++k)
		{
			statusLights[k] = _statusLights[k];
		}
		return statusLights;
	}

	public double[] getWheelAngles()
	{
		final double[] wheelAngles = new double[2];
		wheelAngles[L] = _wheelAngles[L];
		wheelAngles[R] = _wheelAngles[R];
		return wheelAngles;
	}

	public double[] getWheelOdometry()
	{
		final double[] wheelOdometry = new double[2];
		wheelOdometry[L] = _wheelOdometry[L];
		wheelOdometry[R] = _wheelOdometry[R];
		return wheelOdometry;
	}

	public String executeCommand(String[] parts)
	{
		if (getPoseCommand.equals(parts[2]))
		{
			return handleGetPoseCommand(parts);
		}
		else if (setEncoderTicksPerRevolutionCommand.equals(parts[2]))
		{
			return handleSetEncoderTicksPerRevolution(parts);
		}
		else if (getEncoderTicksPerRevolutionCommand.equals(parts[2]))
		{
			return handleGetEncoderTicksPerRevolution(parts);
		}
		return "ERR: UNKNOWN COMMAND " + parts[2];
	}
	
	private String handleGetPoseCommand(String[] parts)
	{
		synchronized(this)
		{
			return "OKAY pose " + _pose.t.x + " " + _pose.t.y + " " + _pose.theta;
		}
	}

	private String handleSetEncoderTicksPerRevolution(String[] parts)
	{
		if (parts.length != 5)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}
		
		int left, right;

		try
		{
			left = Integer.parseInt(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL LEFT ENCODER TICKS PER REVOLUTION " + parts[3];
		}

		try
		{
			right = Integer.parseInt(parts[4]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL RIGHT ENCODER TICKS PER REVOLUTION " + parts[4];
		}
		
		synchronized(this)
		{
			_parameters.encoderTicksPerRevolution[L] = left;
			_parameters.encoderTicksPerRevolution[R] = right;
			computeEncoderTickConstants();
		}
		
		return "OKAY";
	}

	private String handleGetEncoderTicksPerRevolution(String[] parts)
	{
		return "OKAY encoderTicksPerRevolution " + _parameters.encoderTicksPerRevolution[L] 
				+ " " + _parameters.encoderTicksPerRevolution[R];
	}	
	
	/**
	 * Reset the state of the robot back to its initial condition.
	 *
	 * @param now
	 */
	@Override
	public void reset()
	{
		super.reset();

		// Get the information about the robot's shape
		_extent.set(_parameters.bodyExtent);

		// Set nominal position
		_pose.set(_startingPose);

		// The encoders
		resetWheelOdometry();

		computeEncoderTickConstants();
	}

	// Set various useful 
	private void computeEncoderTickConstants()
	{
		// Set up useful scale factors
		_radiansToEncoderTicks[L] = _parameters.encoderTicksPerRevolution[L] / (2.0 * Math.PI);
		_radiansToEncoderTicks[R] = _parameters.encoderTicksPerRevolution[R] / (2.0 * Math.PI);
		_encoderTicksToRadians[L] = 1 / _radiansToEncoderTicks[L];
		_encoderTicksToRadians[R] = 1 / _radiansToEncoderTicks[R];

		_wheelOdometryToEncoderTicks[L] = _parameters.encoderTicksPerRevolution[L]
				/ (2.0 * Math.PI * _parameters.wheelRadii[L]);
		_wheelOdometryToEncoderTicks[R] = _parameters.encoderTicksPerRevolution[R]
				/ (2.0 * Math.PI * _parameters.wheelRadii[R]);

		_encoderTicksToWheelOdometry[L] = 1.0 / _wheelOdometryToEncoderTicks[L];
		_encoderTicksToWheelOdometry[R] = 1.0 / _wheelOdometryToEncoderTicks[R];
		
		// Recompute the odometry values
		_wheelOdometryInEncoderTicks[L] = (long) (_wheelOdometryToEncoderTicks[L] * _wheelOdometry[L]);
		_wheelOdometryInEncoderTicks[R] = (long) (_wheelOdometryToEncoderTicks[R] * _wheelOdometry[R]);
	}
	
	public void resetWheelOdometry()
	{
		_wheelOdometry[L] = 0;
		_wheelOdometry[R] = 0;
	}

	public void setStartingPose(double startX, double startY, double startTheta)
	{
		_startingPose.t.x = startX;
		_startingPose.t.y = startY;
		_startingPose.theta = startTheta;
	}

	@Override
	public void start(long now)
	{
		// Ensure we are reset
		super.start(now);
	}

	@Override
	public void step(long now)
	{
		super.step(now);

		// Work out the time step length
		final double dT = 1e-9 * (_currentTimeNanos - _lastTimeNanos);

		// Handle any wheel speed control
		updateMotorControl(dT);

		// Step the vehicle kinematics
		updatePose(dT);
	}

	// This method advances the state of the robot to the current specified
	// simulation time

	private SE2 fdot(SE2 k, double[] currentWheelSpeed, double h)
	{
		final SE2 f = new SE2();
		final Matrix A = new Matrix(4, 3);
		final double c = Math.cos(_pose.theta);
		final double s = Math.sin(_pose.theta);
		A.set(0, 0, c);
		A.set(0, 1, s);
		A.set(0, 2, -_parameters.hubCentres[L].t.y);
		A.set(1, 0, -s);
		A.set(1, 1, c);
		A.set(1, 2, _parameters.hubCentres[L].t.x);
		A.set(2, 0, c);
		A.set(2, 1, s);
		A.set(2, 2, -_parameters.hubCentres[R].t.y);
		A.set(3, 0, -s);
		A.set(3, 1, c);
		A.set(3, 2, _parameters.hubCentres[R].t.x);
		final Matrix b = new Matrix(4, 1);
		b.set(0, 0, _parameters.wheelRadii[L] * currentWheelSpeed[L]);
		b.set(1, 0, 0);
		b.set(2, 0, _parameters.wheelRadii[R] * currentWheelSpeed[R]);
		b.set(3, 0, 0);
		final Matrix x = A.solve(b);
		f.t.x = x.get(0, 0);
		f.t.y = x.get(1, 0);
		f.theta = x.get(2, 0);
		return f;
	}
	
	// Step the wheel speed; move towards the requested wheel speed
	private double[] fdotWheelSpeed(double[] currentWheelSpeed, double h)
	{
		double[] newWheelSpeed = new double[currentWheelSpeed.length];
		
		for (int i = 0; i < currentWheelSpeed.length; ++i)
		{
			double delta = _requestedWheelSpeedInRadians[i] - currentWheelSpeed[i];
			if (delta < 0)
			{
				delta = Math.max(delta, - _parameters.maximumAngularAccelerationInRadians[i] * h);
			}
			else
			{
				delta = Math.min(delta, _parameters.maximumAngularAccelerationInRadians[i] * h);
			}
			newWheelSpeed[i] = currentWheelSpeed[i] + delta;
		}
		
		return newWheelSpeed;
	}

	// Handle a collision event
	protected abstract void handleCollision();

	// Handle onboard motor control. This changes the requested robot wheel speed
	protected abstract void updateMotorControl(double dT);

	/**
	 * Numerically integrate the state forwards. This uses a fourth order
	 * Runge-Kutta numerical integration scheme because this trades off accuracy
	 * with speed.
	 *
	 * @param dT
	 */
	protected synchronized void updatePose(double dT)
	{
		// Simple dynamics model - the actual wheels can be driven with a maximum angular
		// velocity and stops as soon as the requested speed is reached
		double[] currentWheelSpeed = new double[2];
		currentWheelSpeed[L] = _wheelSpeedInRadians[L];
		currentWheelSpeed[R] = _wheelSpeedInRadians[R];
		

		// _logger.info("updatePose: wheelSpeedInRadians = " +
		// _wheelSpeedInRadians[L] + " " + _wheelSpeedInRadians[R]);

		// Store the last pose
		final SE2 lastPose = new SE2(_pose);

		// Do Runge-Kutta; interleave with computing the new wheel speeds as well
		final SE2 k1 = fdot(_pose, currentWheelSpeed, 0);
		final SE2 k2 = fdot(k1, currentWheelSpeed, 0.5f * dT);
		currentWheelSpeed = fdotWheelSpeed(currentWheelSpeed, 0.5f * dT);
		final SE2 k3 = fdot(k2, currentWheelSpeed, 0.5f * dT);
		currentWheelSpeed = fdotWheelSpeed(currentWheelSpeed, 0.5f * dT);
		final SE2 k4 = fdot(k3, currentWheelSpeed, dT);

		final SE2 candidatePose = new SE2(_pose);

		candidatePose.t.x += (dT * (k1.t.x + (2 * k2.t.x) + (2 * k3.t.x) + k4.t.x)) / 6;
		candidatePose.t.y += (dT * (k1.t.y + (2 * k2.t.y) + (2 * k3.t.y) + k4.t.y)) / 6;
		candidatePose.theta += (dT * (k1.theta + (2 * k2.theta) + (2 * k3.theta) + k4.theta)) / 6;

		// Update the wheel speed
		_wheelSpeedInRadians[L] = _requestedWheelSpeedInRadians[L];
		_wheelSpeedInRadians[R] = _requestedWheelSpeedInRadians[R];
		
		// TODO: This is not right yet
		_wheelAngles[L] += dT * _wheelSpeedInRadians[L];
		_wheelAngles[R] += dT * _wheelSpeedInRadians[R];

		// Is this move valid?
		setPose(candidatePose);

		// System.out.println("dT = " + dT);

		// If the pose is clear, advance. If not, revert to the old version

		if (_simulationEngine.isCandidatePoseClear(this) == false)
		{
			setPose(lastPose);
			handleCollision();
		}

		// Update the odometry - this reflects how far the wheels have turned, so is unaffected by collisions
		_wheelOdometry[L] += dT * _parameters.wheelRadii[L] * _wheelSpeedInRadians[L];
		_wheelOdometry[R] += dT * _parameters.wheelRadii[R] * _wheelSpeedInRadians[R];

		// Update the encoder ticks; note that we get these from the
		// continuous-valued odometry by casting. This
		// naturally rounds the number down.
		_wheelOdometryInEncoderTicks[L] = (long) (_wheelOdometryToEncoderTicks[L] * _wheelOdometry[L]);
		_wheelOdometryInEncoderTicks[R] = (long) (_wheelOdometryToEncoderTicks[R] * _wheelOdometry[R]);
	}
}
