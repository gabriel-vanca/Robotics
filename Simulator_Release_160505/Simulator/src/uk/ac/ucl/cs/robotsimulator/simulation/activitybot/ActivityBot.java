package uk.ac.ucl.cs.robotsimulator.simulation.activitybot;

import uk.ac.ucl.cs.robotsimulator.maths.Ray;
import uk.ac.ucl.cs.robotsimulator.simulation.RayQueryCollsionResult;
import uk.ac.ucl.cs.robotsimulator.simulation.Robot;

/**
 * This class implements the core logic for simulating the ActivityBot. This includes the
 * commands for driving the robot. In addition, the commands for accessing the sensors
 * (ping, ir sensor) are implemented here. The ping sensor is relatively "high level", but the
 * IR sensor needs a bunch of low-level calls.
 *
 * @author ucacsjj
 *
 */

public class ActivityBot extends Robot
{
	// The names of the supported robot commands

	// The modes undertaken by the low level controller on the robot. It turns
	// out that several operations (e.g., drive_goto) are built from these basic
	// types
	private enum DriveControllerMode
	{
		DRIVE_RAMP, DRIVE_SPEED, STOPPED
	}

	// abdrive commands
	private final static String driveGetTicks = "drive_getTicks";
	private final static String driveGoto = "drive_goto";
	private final static String driveRamp = "drive_ramp";
	private final static String driveRampStep = "drive_rampStep";
	private final static String driveSetMaxSpeed = "drive_setMaxSpeed";
	private final static String driveSetRampStep = "drive_setRampStep";
	private final static String driveSetSpeed = "drive_speed";
	private final static String driveClose = "drive_close";

	// Status lights / IR sensor commands
	private final static String high = "high";
	private final static String low = "low";
	
	// For querying the IR sensors in a low-level way
	private final static String dacCtr = "dac_ctr";
	private final static String freqout = "freqout";
	private final static String input = "input";

	// For setting the status lights
	// For querying the IR sensors in a high-level way
	private final static String measureIRRange = "measureIRRange";

	// For querying the ping sensor
	private final static String ping = "ping";

	// Size of the speed step to use in the drive ramp function
	private int _abd_rampStep = 4;

	// The maximum speed
	private int _abd_speedLimit = 128;

	// Not entirely sure what this is here for, but it's used in the ActivityBot
	// setSpeed, seemingly to add some kind of buffering / delay when reversing direction.
	private final boolean _abd_zeroDelay = true;

	// The last requested drive speed, in encoder ticks / second
	final private int[] _lastSpeed =
	{ 0, 0 };

	// The requested wheel speed, in encoder ticks / second
	protected final int[] _requestedWheelSpeed =
	{ 0, 0 };

	// The actual wheel speed, in encoder ticks / second
	protected final int[] _wheelSpeed =
	{ 0, 0 };

	// The IR Sensor data
	protected final IRSensorParameters _ir = new IRSensorParameters();
	
	// For drive goto, what are the targets?
	long[] _gotoEncoderTarget =
	{ 0, 0 };

	
	// Which mode is the drive controller in?	
	DriveControllerMode _mode;

	// Next time to ramp the speed up
	long _nextRampTimeNanos = 0;

	// The target speed in encoder ticks
	int[] _rampEncoderTarget =
	{ 0, 0 };

	// drive_ramp keeps running UNTIL the speed is achieved; drive_rampStep
	// merely takes one step towards the goal. Set to true if drive_rampStep is being used
	boolean _rampOneStepOnly;

	// Settings used for the dac_ptr setup
	int _dacPtrPin;
	int _dacPtrChannel;
	int _dacPtrVal;	
	
	// Settings used to ensure that the IR sensor is actually configured properly	
	int _freqoutFrequency;
	int _freqoutPin;
	
	// Flag to show if the IR sensor was actually configured properly
	boolean _irSensorsConfiguredProperly = false;
	
	public ActivityBot(String name, double startX, double startY, double startTheta)
	{
		super(name, startX, startY, startTheta);
		_parameters = new ActivityBotParameters();
		_statusLights = new int[2];
	}

	@Override
	public String executeCommand(String[] parts)
	{
		_logger.fine("executeCommand: executing command " + parts[2]);

		if (driveGoto.equals(parts[2]))
		{
			return handleDriveGoto(parts);
		}
		else if (driveRamp.equals(parts[2]))
		{
			return handleDriveRamp(parts, false);
		}
		else if (driveRampStep.equals(parts[2]))
		{
			return handleDriveRamp(parts, true);
		}
		else if (driveSetRampStep.equals(parts[2]))
		{
			return handleDriveSetRampStep(parts);
		}
		else if (driveSetMaxSpeed.equals(parts[2]))
		{
			return handleDriveSetMaxSpeed(parts);
		}
		else if (driveSetSpeed.equals(parts[2]))
		{
			return handleSetDriveSpeed(parts);
		}
		else if (driveClose.equals(parts[2]))
		{
			return handleDriveClose(parts);
		}
		else if (driveGetTicks.equals(parts[2]))
		{
			return handleDriveGetTicks(parts);
		}
		else if (low.equals(parts[2]) || high.equals(parts[2]))
		{
			return handleHighLow(parts);
		}
		else if (ping.equals(parts[2]))
		{
			return handlePing(parts);
		}
		else if (dacCtr.equals(parts[2]))
		{
			return handleDacCtr(parts);
		}
		else if (freqout.equals(parts[2]))
		{
			return handleFreqout(parts);
		}
		else if (input.equals(parts[2]))
		{
			return handleInput(parts);
		}
		else if (measureIRRange.equals(parts[2]))
		{
			return handleMeasureIRRange(parts);
		}
		else
		{
			return super.executeCommand(parts);
		}
	}

	@Override
	public void reset()
	{
		super.reset();
		_abd_rampStep = 4;
		_abd_speedLimit = 128;
		_lastSpeed[0] = 0;
		_lastSpeed[1] = 0;
		_statusLights[0] = 0;
		_statusLights[1] = 0;
		
		_mode = DriveControllerMode.STOPPED;
		_irSensorsConfiguredProperly = false;
	}
	
	private synchronized String handleDriveGetTicks(String[] parts)
	{
		if (parts.length != 3)
		{
			return "ERR: TOO MANY ARGUMENTS";
		}
		return "OKAY TICKS " + _wheelOdometryInEncoderTicks[L] + " " + _wheelOdometryInEncoderTicks[R];
	}

	private synchronized String handleDriveClose(String[] parts)
	{
		_mode = DriveControllerMode.STOPPED;
		return "OKAY";
	}
	
	private String handleDriveGoto(String[] parts)
	{
		int targetL, targetR;

		if (parts.length != 5)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}

		try
		{
			targetL = Integer.parseInt(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL LEFT WHEEL SPEED " + parts[3];
		}

		try
		{
			targetR = Integer.parseInt(parts[4]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL LEFT WHEEL SPEED " + parts[4];
		}

		final long ticksLstart = _wheelOdometryInEncoderTicks[L];
		final long ticksRstart = _wheelOdometryInEncoderTicks[R];
		final long ticksLtarget = ticksLstart + targetL;
		final long ticksRtarget = ticksRstart + targetR;
		int distRampL = 0;
		int distRampR = 0;
		int rampClampL = 0;
		int rampClampR = 0;

		// This seems to be done because the time base is shifted. dt (which is
		// the time required for each jump of the
		// ramp) is 20ms.

		targetL *= 1000;
		targetR *= 1000;
		final int dt = 20;

		if (targetL > 0)
		{
			if (_requestedWheelSpeed[L] != 0)
			{
				for (int i = _requestedWheelSpeed[L]; i > 0; i -= _abd_rampStep)
				{
					distRampL += (i * dt);
				}
			}
			for (int i = _requestedWheelSpeed[L]; i <= _abd_speedLimit; i += _abd_rampStep)
			{
				i = Math.min(i, _abd_speedLimit);
				distRampL += (i * dt * 2);
				rampClampL = i;
				if (targetL <= distRampL)
				{
					distRampL = targetL;
					break;
				}
			}
		}

		if (targetR > 0)
		{
			if (_requestedWheelSpeed[R] != 0)
			{
				for (int i = _requestedWheelSpeed[R]; i > 0; i -= _abd_rampStep)
				{
					distRampR += (i * dt);
				}
			}
			for (int i = _requestedWheelSpeed[R]; i <= _abd_speedLimit; i += _abd_rampStep)
			{
				i = Math.min(i, _abd_speedLimit);
				distRampR += (i * dt * 2);
				rampClampR = i;
				if (targetR <= distRampR)
				{
					distRampR = targetR;
					break;
				}
			}
		}

		if (targetL < 0)
		{
			if (_requestedWheelSpeed[L] != 0)
			{
				for (int i = _requestedWheelSpeed[L]; i < 0; i += _abd_rampStep)
				{
					distRampL -= (i * dt);
				}
			}
			for (int i = _requestedWheelSpeed[L]; i >= -_abd_speedLimit; i -= _abd_rampStep)
			{
				i = Math.max(-_abd_speedLimit, i);
				distRampL += (i * dt * 2);
				rampClampL = i;
				if (targetL >= distRampL)
				{
					distRampL = targetL;
					break;
				}
			}
		}

		if (targetR < 0)
		{
			if (_requestedWheelSpeed[R] != 0)
			{
				for (int i = _requestedWheelSpeed[R]; i < 0; i += _abd_rampStep)
				{
					distRampR -= (i * dt);
				}
			}
			for (int i = _requestedWheelSpeed[R]; i >= -_abd_speedLimit; i -= _abd_rampStep)
			{
				i = Math.max(-_abd_speedLimit, i);
				distRampR += (i * dt * 2);
				rampClampR = i;
				if (targetR >= distRampR)
				{
					distRampR = targetR;
					break;
				}
			}
		}

		final int distCruiseL = targetL - distRampL;
		final long tCruiseL = (rampClampL == 0) ? 0 : (distCruiseL / rampClampL);

		// System.out.println("distRampL=" + distRampL);
		// System.out.println("distCruiseL=" + distCruiseL);
		// System.out.println("distCruiseL+distRampL=" +
		// (distCruiseL+distRampL));
		// System.out.println("tCruiseL=" + tCruiseL);
		// System.out.println("rampClampL=" + rampClampL);
		// System.out.println("distCruiseL=" + distCruiseL);
		// System.out.println("distRampR=" + distRampR);
		// System.out.println("distCruiseL=" + distCruiseR);
		// System.out.println("distCruiseL+distRampL=" +
		// (distCruiseL+distRampL));
		// System.out.println("tCruiseL=" + tCruiseL);
		// System.out.println("rampClampR=" + rampClampR);
		// System.exit(0);

		// _logger.info("Target encoder values are " + rampClampL + ", " +
		// rampClampR);

		_logger.info("Running open loop trajectory");

		// Set the mode to do the ramp
		synchronized (this)
		{
			_rampEncoderTarget[L] = rampClampL;
			_rampEncoderTarget[R] = rampClampR;
			_nextRampTimeNanos = _currentTimeNanos + (20 * 1000000L);
			_rampOneStepOnly = false;
			_mode = DriveControllerMode.DRIVE_RAMP;
		}

		// Now wait for the drive controller to finish its business
		waitForCommandToComplete();

		// Pause for a while
		if (tCruiseL > 0)
		{
			_simulationEngine.pauseMillis(tCruiseL, this);
		}

		// Now ramp down to zero
		synchronized (this)
		{
			_rampEncoderTarget[L] = 0;
			_rampEncoderTarget[R] = 0;
			_nextRampTimeNanos = _currentTimeNanos + (20 * 1000000L);
			_rampOneStepOnly = false;
			_mode = DriveControllerMode.DRIVE_RAMP;
		}

		// Now wait for the drive controller to finish its business
		waitForCommandToComplete();

		// Now do the fine-grained correction

		int reps = 0;
		int tempL = 0, tempR = 0;

		_logger.info("Running closed loop error correction; current errors are "
				+ (_wheelOdometryInEncoderTicks[L] - ticksLtarget) + ", "
				+ (_wheelOdometryInEncoderTicks[R] - ticksRtarget));

		while (true)
		{
			_simulationEngine.pauseMillis(20, this);

			if (_wheelOdometryInEncoderTicks[L] > ticksLtarget)
			{
				tempL = -10;
			}
			else if (_wheelOdometryInEncoderTicks[L] < ticksLtarget)
			{
				tempL = 10;
			}
			else
			{
				tempL = 0;
			}

			if (_wheelOdometryInEncoderTicks[R] > ticksRtarget)
			{
				tempR = -10;
			}
			else if (_wheelOdometryInEncoderTicks[R] < ticksRtarget)
			{
				tempR = 10;
			}
			else
			{
				tempR = 0;
			}
			// _logger.info("tempL=" + tempL + "; tempR=" + tempR);

			synchronized (this)
			{
				_requestedWheelSpeed[L] = Math.min(Math.max(tempL, -_abd_speedLimit), _abd_speedLimit);
				_requestedWheelSpeed[R] = Math.min(Math.max(tempR, -_abd_speedLimit), _abd_speedLimit);

				// Assigning this down here handles maxSpeed clamping
				_lastSpeed[L] = _requestedWheelSpeed[L];
				_lastSpeed[R] = _requestedWheelSpeed[R];
				_mode = DriveControllerMode.DRIVE_SPEED;
			}
			// _logger.info("tempL=" + tempL + "; tempR=" + tempR);
			// _logger.info((_wheelOdometryInEncoderTicks[L] - ticksLtarget) +
			// ", " +
			// (_wheelOdometryInEncoderTicks[R] - ticksRtarget));
			if ((_wheelOdometryInEncoderTicks[L] == ticksLtarget) && (_wheelOdometryInEncoderTicks[R] == ticksRtarget))
			{
				reps++;
			}
			else
			{
				reps = 0;
			}
			if (reps == 10)
			{
				break;
			}
		}

		return "OKAY drive_goto";
	}

	private String handleDriveRamp(String[] parts, boolean rampOneStepOnly)
	{
		int targetL, targetR;

		if (parts.length != 5)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}

		try
		{
			targetL = Integer.parseInt(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL LEFT WHEEL SPEED " + parts[3];
		}

		try
		{
			targetR = Integer.parseInt(parts[4]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL LEFT WHEEL SPEED " + parts[4];
		}

		// _logger.info("handleDriveRamp: the target values are " + targetL + ",
		// " +targetR);

		synchronized (this)
		{
			_rampEncoderTarget[L] = targetL;
			_rampEncoderTarget[R] = targetR;
			_nextRampTimeNanos = _currentTimeNanos + (20 * 1000000L);
			_rampOneStepOnly = rampOneStepOnly;
			_mode = DriveControllerMode.DRIVE_RAMP;
		}

		// Now wait for the drive controller to finish its business
		waitForCommandToComplete();

		return "OKAY drive_ramp";
	}

	private String handleDriveSetMaxSpeed(String[] parts)
	{
		if (parts.length != 4)
		{
			return "ERR: INSUFFICIENT ARGUMENTS";
		}

		int maxSpeed;

		try
		{
			maxSpeed = Integer.parseInt(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL MAX SPEED " + parts[3];
		}

		synchronized (this)
		{
			_abd_speedLimit = maxSpeed;
		}

		return "OKAY";
	}

	private String handleDriveSetRampStep(String[] parts)
	{
		int rampStepSize;

		if (parts.length != 4)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}

		try
		{
			rampStepSize = Integer.parseInt(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL RAMP STEP SIZE " + parts[3];
		}

		// Now set the ramp step size
		synchronized (this)
		{
			_abd_rampStep = rampStepSize;
		}

		return "OKAY";
	}

	private synchronized String handleDacCtr(String[] parts)
	{
		if (parts.length != 6)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}
				
		try
		{
			_dacPtrPin = Integer.parseInt(parts[3]);
			_dacPtrChannel = Integer.parseInt(parts[4]);
			_dacPtrVal = Integer.parseInt(parts[5]);
		}
		catch (final Exception e)
		{
			return "ERR: PIN " + parts[3];
		}

		// Generate warning if the pin value is unknown
		if ((_dacPtrPin != 26) && (_dacPtrPin != 27))
		{
			return "WARN: UNSUPPORTED PIN " + _dacPtrPin;
		}
		
		// If the pin happens to be a status light, set it to the dacVal
		if (_dacPtrPin == 26)
		{
			_statusLights[0] = _dacPtrVal;
		}
		else if (_dacPtrPin == 27)
		{
			_statusLights[1] = _dacPtrVal;			
		}		

		return "OKAY";
	}
	
	private String handleFreqout(String[] parts)
	{
		int pin, msTime, frequency;
		if (parts.length != 6)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}
		try
		{
			pin = Integer.parseInt(parts[3]);
			msTime = Integer.parseInt(parts[4]);
			frequency = Integer.parseInt(parts[5]);
		}
		catch (final Exception e)
		{
			return "ERR: PIN " + parts[3];
		}

		if ((pin != 11) && (pin != 1))
		{
			return "WARN: UNSUPPORTED PIN " + pin;
		}

		_freqoutPin = pin;
		_freqoutFrequency = frequency;

		// Pause for the duration of the signal broadcast
		_simulationEngine.pauseMillis(msTime, this);
		
		// Clear the status lights if we need to
		if (_dacPtrPin == 26)
		{
			_statusLights[0] = 0;
		}
		else if (_dacPtrPin == 27)
		{
			_statusLights[1] = 0;			
		}

		return "OKAY";
	}

	private String handleHighLow(String[] parts)
	{
		int pin;
		if (parts.length != 4)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}
		try
		{
			pin = Integer.parseInt(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR: PIN " + parts[3];
		}

		if ((pin != 26) && (pin != 27))
		{
			return "ERR: UNSUPPORTED PIN " + pin + "; MUST BE 26 OR 27";
		}

		final int pinHigh = (high.equals(parts[2]) == true) ? 1 : 0;

		_logger.info("Setting pin " + pin + " to " + pinHigh);

		// Random bit of hard coding
		if (pin == 26)
		{
			_statusLights[0] = pinHigh;
		}
		else if (pin == 27)
		{
			_statusLights[1] = pinHigh;
		}

		return "OKAY";

	}

	private String handleInput(String[] parts)
	{
		if (parts.length != 4)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}

		int pin;

		try
		{
			pin = Integer.parseInt(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR: PIN " + parts[3];
		}

		// Handle the correct cases
		if ((pin != 2) && (pin != 10))
		{
			return "ERR: UNSUPPORTED PIN " + pin + "; SUPPORTED VALUES ARE 2 AND 10";
		}

		// Check if we have a valid combination. This involves both setting the
		// transmission (via freqout) to the
		// previous value and
		final boolean validCombination = ((pin == 10) && (_freqoutPin == 11)) || ((pin == 2) && (_freqoutPin == 1));

		// TODO: Check what happens
		if (validCombination == false)
		{
			return "ERR: UNSUPPORTED FREQOUT PIN /  PIN COMBINATION " + _freqoutPin + " " + pin;
		}

		final int idx = (pin == 10) ? L : R;

		// Get the range from the IR sensor to the target
		final double range = readIRSensor(idx);
		
		// Now figure out whether it's a 1 or a 0 based on the range, dacCtr value and 
		// the probability table. Note this is confusing because 1 means "does not detect".

		int result = 0;
		
		double probabilityOfOne = _ir.getProbabilityOfOne(range,  "MazeWall", _dacPtrVal);
		if (Math.random() < probabilityOfOne)
		{
			result = 1;
		}
			
		return "OKAY " + result + " " + range;
	}

	private String handleMeasureIRRange(String[] parts)
	{
		boolean readRightSensor;
		if (parts.length != 3)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}
		try
		{
			readRightSensor = Boolean.parseBoolean(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR: PIN " + parts[3];
		}

		final int idx = readRightSensor ? R : L;

		final double range = readIRSensor(idx);

		return "OKAY " + range;
	}

	private void handleMotorControlRamp()
	{
		// Step the speed up if scheduled
		if (_currentTimeNanos < _nextRampTimeNanos)
		{
			return;
		}

		// Reschedule
		_nextRampTimeNanos = _nextRampTimeNanos + (20 * 1000000L);

		// Try to move the speed towards the requested speed by no more than the
		// ramp step size
		final int leftSpeed = _requestedWheelSpeed[L]
				+ Math.min(Math.max(_rampEncoderTarget[L] - _requestedWheelSpeed[L], -_abd_rampStep), _abd_rampStep);
		final int rightSpeed = _requestedWheelSpeed[R]
				+ Math.min(Math.max(_rampEncoderTarget[R] - _requestedWheelSpeed[R], -_abd_rampStep), _abd_rampStep);

		// Now trim to the maximum speed. NOTE: THIS INTRODUCES A BUG. IF THE
		// RAMP TARGET IS OUTSIDE THE SPEED LIMIT, THE ROUTINE
		// DOES NOT RETURN. THIS IS THE SAME BEHAVIOUR IN THE ACTUAL abdrive
		// SYSTEM.
		_requestedWheelSpeed[L] = Math.min(Math.max(leftSpeed, -_abd_speedLimit), _abd_speedLimit);
		_requestedWheelSpeed[R] = Math.min(Math.max(rightSpeed, -_abd_speedLimit), _abd_speedLimit);

		_requestedWheelSpeedInRadians[L] = _encoderTicksToRadians[L] * _requestedWheelSpeed[L];
		_requestedWheelSpeedInRadians[R] = _encoderTicksToRadians[R] * _requestedWheelSpeed[R];

		// _logger.info("_requestedWheelSpeed[L] = " + _requestedWheelSpeed[L] +
		// "; _rampEncoderTarget = " + _rampEncoderTarget[L]);

		// If we have reached the target, then the mode becomes travel at a
		// constant speed
		if ((_requestedWheelSpeed[L] == _rampEncoderTarget[L]) || (_requestedWheelSpeed[R] == _rampEncoderTarget[R])
				|| (_rampOneStepOnly == true))
		{
			_mode = DriveControllerMode.DRIVE_SPEED;
			signalCommandComplete();
		}
	}

	private void handleMotorControlSpeed()
	{
		// Update the actual wheel speed to converge to the requested one
		_requestedWheelSpeedInRadians[L] = _encoderTicksToRadians[L] * _requestedWheelSpeed[L];
		_requestedWheelSpeedInRadians[R] = _encoderTicksToRadians[R] * _requestedWheelSpeed[R];
		// _logger.info("handleMotorControlSpeed: requested drive speed is " +
		// _requestedWheelSpeed[L] + " " + _requestedWheelSpeed[R]);
		// _logger.info("handleMotorControlSpeed: assigned speed is " +
		// _requestedWheelSpeedInRadians[L] + " " +
		// _requestedWheelSpeedInRadians[R]);
	}

	private void handleMotorControlStopped()
	{
		_requestedWheelSpeed[L] = 0;
		_requestedWheelSpeed[R] = 0;
		_requestedWheelSpeedInRadians[L] = 0;
		_requestedWheelSpeedInRadians[R] = 0;
	}

	private String handlePing(String[] parts)
	{		
		long pin, pingLength, timeout;
		if (parts.length != 6)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}
		try
		{
			pin = Integer.parseInt(parts[3]);
			pingLength = Integer.parseInt(parts[4]);
			timeout = Integer.parseInt(parts[5]);
		}
		catch (final Exception e)
		{
			return "ERR: PIN " + parts[3];
		}
		
		// If the pin is not 8, then we haven't set things up correctly. In that case,
		// we wait for the full timeout and return a range of 0.
		if (pin != 8)
		{
			_simulationEngine.pause(timeout, this);
			return "OKAY 0";
		}

		// Construct query ray
		final double c = Math.cos(_pose.theta);
		final double s = Math.sin(_pose.theta);
		final ActivityBotParameters parameters = (ActivityBotParameters) _parameters;
		final double x = (_pose.t.x + (c * parameters.pingSensor.t.x)) - (s * parameters.pingSensor.t.y);
		final double y = _pose.t.y + (s * parameters.pingSensor.t.x) + (c * parameters.pingSensor.t.y);
		final double dx = c;
		final double dy = s;

		final Ray queryRay = new Ray(x, y, dx, dy);

		// Find the ray intersection with the robot
		final RayQueryCollsionResult intersection = _simulationEngine.rayQuery(this, queryRay);
		
		// From a few experiments, the measured range is never greater than 315cm, even
		// if the sensor is pointing at the sky.
		double d = Math.min(intersection.d, 3150);

		// Figure out the delay time - this is the ping duration from the sensor plus either the
		// time out time or the time it takes the sound to propagate back. From
		// https://en.wikipedia.org/wiki/Speed_of_sound, we assume the speed of sound
		// is 343.2m/s. This seems to hold true even if the sensor has maxed out

		long delayTime = pingLength + (long) (1e9 * d / (343.0 * 1e3));
		_simulationEngine.pause(delayTime, this);
		
		return "OKAY " + (d * 5.8);
	}

	private String handleSetDriveSpeed(String[] parts)
	{
		if (parts.length != 5)
		{
			return "ERR: INCORRECT NUMBER OF ARGUMENTS";
		}

		int leftSpeed, rightSpeed;

		try
		{
			leftSpeed = Integer.parseInt(parts[3]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL LEFT WHEEL SPEED " + parts[3];
		}

		try
		{
			rightSpeed = Integer.parseInt(parts[4]);
		}
		catch (final Exception e)
		{
			return "ERR: ILLEGAL RIGHT WHEEL SPEED " + parts[4];
		}

		// Set to drive speed mode
		// This logic, which is enabled by default, seems to add a slight pause
		// when reversing directions
		if (_abd_zeroDelay == true)
		{
			final boolean reverseLeft = ((_lastSpeed[L] > 0) && (leftSpeed <= 0))
					|| ((_lastSpeed[L] < 0) && (leftSpeed >= 0));
			final boolean reverseRight = ((_lastSpeed[R] > 0) && (rightSpeed <= 0))
					|| ((_lastSpeed[R] < 0) && (rightSpeed >= 0));
			if (reverseLeft | reverseRight)
			{
				final int tempLeftZ = (reverseLeft == true) ? 0 : leftSpeed;
				final int tempRightZ = (reverseRight == true) ? 0 : rightSpeed;
				synchronized (this)
				{
					_mode = DriveControllerMode.DRIVE_SPEED;
					_requestedWheelSpeed[L] = Math.min(Math.max(tempLeftZ, -_abd_speedLimit), _abd_speedLimit);
					_requestedWheelSpeed[R] = Math.min(Math.max(tempRightZ, -_abd_speedLimit), _abd_speedLimit);
				}
				_simulationEngine.pauseMillis(120, this);
			}

			// Now actually set the robot speed
			synchronized (this)
			{
				_mode = DriveControllerMode.DRIVE_SPEED;
				_requestedWheelSpeed[L] = Math.min(Math.max(leftSpeed, -_abd_speedLimit), _abd_speedLimit);
				_requestedWheelSpeed[R] = Math.min(Math.max(rightSpeed, -_abd_speedLimit), _abd_speedLimit);

				// Assigning this down here handles maxSpeed clamping
				_lastSpeed[L] = _requestedWheelSpeed[L];
				_lastSpeed[R] = _requestedWheelSpeed[R];
			}
		}

		// _logger.info("handleSetDriveSpeed: requested drive speed is " +
		// _requestedWheelSpeed[L] + " " + _requestedWheelSpeed[R]);

		return "OKAY";
	}

	private double readIRSensor(int whichSensor)
	{
		// Construct the query ray. The ray depends upon whether we are using
		// the left or right IR sensor.
		final ActivityBotParameters parameters = (ActivityBotParameters) _parameters;

		final double c = Math.cos(_pose.theta);
		final double s = Math.sin(_pose.theta);

		final double x = (_pose.t.x + (c * parameters.irSensors[whichSensor].t.x))
				- (s * parameters.irSensors[whichSensor].t.y);
		final double y = _pose.t.y + (s * parameters.irSensors[whichSensor].t.x)
				+ (c * parameters.irSensors[whichSensor].t.y);
		final double dx = Math.cos(_pose.theta + parameters.irSensors[whichSensor].theta);
		final double dy = Math.sin(_pose.theta + parameters.irSensors[whichSensor].theta);

		final Ray queryRay = new Ray(x, y, dx, dy);

		// Find the ray intersection with the robot
		final RayQueryCollsionResult intersection = _simulationEngine.rayQuery(this, queryRay);

		return intersection.d;
	}

	@Override
	protected void handleCollision()
	{
		// Do nothing
	}

	@Override
	protected void updateMotorControl(double dT)
	{
		switch (_mode)
		{
		case STOPPED:
		{
			handleMotorControlStopped();
			break;
		}
		case DRIVE_RAMP:
		{
			handleMotorControlRamp();
			break;
		}
		case DRIVE_SPEED:
		{
			handleMotorControlSpeed();
			break;
		}

		default:
		{
		}
		}
	}

}
