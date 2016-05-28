package uk.ac.ucl.cs.robotsimulator.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import uk.ac.ucl.cs.robotsimulator.maths.SE2;
import uk.ac.ucl.cs.robotsimulator.simulation.Robot;
import uk.ac.ucl.cs.robotsimulator.simulation.RobotParameters;

public class RobotPortrayal extends JComponent
{

	private final static Logger _logger = Logger.getLogger(RobotPortrayal.class.getName());

	/**
	 *
	 */
	private static final long serialVersionUID = 5876790120723710440L;

	static float AXE_LENGTH = 30; // TODO equals to robot length
	private Line2D.Double _arrowBody;

	private RobotParameters _parameters;
	private Robot _robot;

	private Rectangle2D.Double _robotBody;

	private Ellipse2D.Double[] _stabilisingBalls;

	private Rectangle2D.Double[] _statusLights;

	private final Rectangle2D.Double[] _wheels = new Rectangle2D.Double[2];

	public RobotPortrayal(Robot robot)
	{
		_robot = robot;

		// TODO: Copy from the robot instead
		_parameters = robot.getParameters();

		final double width = _parameters.bodyExtent.x;
		final double height = _parameters.bodyExtent.y;

		_robotBody = new Rectangle2D.Double(-0.5 * width, -0.5 * height, width, height);

		// Left and right wheels
		_wheels[Robot.L] = new Rectangle2D.Double(_parameters.hubCentres[Robot.L].t.x - _parameters.wheelRadii[Robot.L],
				_parameters.hubCentres[Robot.L].t.y - (_parameters.wheelThickness[Robot.L] * 0.5),
				2 * _parameters.wheelRadii[Robot.L], _parameters.wheelThickness[Robot.L]);
		_wheels[Robot.R] = new Rectangle2D.Double(_parameters.hubCentres[Robot.L].t.x - _parameters.wheelRadii[Robot.R],
				_parameters.hubCentres[Robot.R].t.y - (_parameters.wheelThickness[Robot.R] * 0.5),
				2 * _parameters.wheelRadii[Robot.R], _parameters.wheelThickness[Robot.R]);

		// Arrow bits
		_arrowBody = new Line2D.Double(0, 0, AXE_LENGTH, 0);

		// Status lights
		if (_parameters.statusLights != null)
		{
			_statusLights = new Rectangle2D.Double[_parameters.statusLights.length];
			for (int k = 0; k < _parameters.statusLights.length; ++k)
			{
				final Vector4d s = _parameters.statusLights[k];
				_statusLights[k] = new Rectangle2D.Double(s.x - (0.5 * s.z), s.y + (0.5 * s.w), s.z, s.w);
			}
		}
		// System.exit(0);

		// Stabilising balls. Note that the ball is actually specified by the
		// bounding box which encloses it
		if (_parameters.stabilisingBalls == null)
		{
			return;
		}

		_stabilisingBalls = new Ellipse2D.Double[_parameters.stabilisingBalls.length];
		for (int k = 0; k < _parameters.stabilisingBalls.length; ++k)
		{
			final Vector3d ball = _parameters.stabilisingBalls[k];
			_stabilisingBalls[k] = new Ellipse2D.Double(ball.x - (0.5 * ball.z), ball.y - (0.5 * ball.z), ball.z,
					ball.z);
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		// _logger.info("RobotPortrayal.paintComponent: invoked");
		super.paintComponent(g);

		final Graphics2D graphics = (Graphics2D) g;

		final Graphics2D localGraphics = (Graphics2D) graphics.create();

		// Get the robot data

		SE2 pose;
		double[] wheelAngles;
		int[] statusLights;

		// Pull off copies of variables from the simulator. Block the robot
		// instance for the shortest time possible.
		synchronized (_robot)
		{
			pose = _robot.getPose();
			wheelAngles = _robot.getWheelAngles();
			statusLights = _robot.getStatusLights();
		}

		final double c = Math.cos(pose.theta);
		final double s = Math.sin(pose.theta);

		// The robot internally uses a right handed coordinate system, origin
		// bottom left.
		final AffineTransform transform = new AffineTransform(c, s, -s, c, pose.t.x, pose.t.y);

		localGraphics.transform(transform);

		// Draw the box which represents the robot body

		// Draw the arrow
		localGraphics.setColor(Color.RED);
		localGraphics.draw(_arrowBody);
		localGraphics.fillPolygon(new int[]
		{ (int) (AXE_LENGTH + 5), (int) (AXE_LENGTH), (int) (AXE_LENGTH) }, new int[]
		{ 0, 5, -5 }, 3);

		// Draw the body and the wheels
		localGraphics.setColor(Color.BLACK);
		localGraphics.draw(_robotBody);
		localGraphics.setColor(Color.GREEN);
		localGraphics.draw(_wheels[Robot.L]);
		localGraphics.setColor(Color.BLUE);
		localGraphics.draw(_wheels[Robot.R]);

		// Figure out the angle that each wheel has turned through. Note the -ve
		// signs. The reason for this is that when the vehicle moves forwards,
		// the wheels rotate in a clockwise (=negative) manner.
		double leftAngle = -wheelAngles[Robot.L];
		double rightAngle = -wheelAngles[Robot.R];

		// Now draw the lines for the wheels. The wheel tread is approximated by
		// 18 bars equally spaced around the tread.
		for (double theta = 0; theta < 18; ++theta)
		{
			double z = Math.sin(leftAngle);
			localGraphics.setColor(Color.GREEN);
			if (z > 0)
			{
				final double x = _parameters.hubCentres[Robot.L].t.x
						+ (_parameters.wheelRadii[Robot.L] * Math.cos(leftAngle));
				localGraphics.draw(new Line2D.Double(x,
						_parameters.hubCentres[Robot.L].t.y - (_parameters.wheelThickness[Robot.L] * 0.5), x,
						_parameters.hubCentres[Robot.L].t.y + (_parameters.wheelThickness[Robot.L] * 0.5)));
			}
			leftAngle += (2 * Math.PI) / 18.0;
			z = Math.sin(rightAngle);
			localGraphics.setColor(Color.BLUE);
			if (z > 0)
			{
				final double x = _parameters.hubCentres[Robot.R].t.x
						+ (_parameters.wheelRadii[Robot.R] * Math.cos(rightAngle));
				localGraphics.draw(new Line2D.Double(x,
						_parameters.hubCentres[Robot.R].t.y - (_parameters.wheelThickness[Robot.R] * 0.5), x,
						_parameters.hubCentres[Robot.R].t.y + (_parameters.wheelThickness[Robot.R] * 0.5)));
			}
			rightAngle += (2 * Math.PI) / 18.0;
		}

		// Draw the stabilisers
		if (_stabilisingBalls != null)
		{
			for (final Double _stabilisingBall : _stabilisingBalls)
			{
				localGraphics.draw(_stabilisingBall);
			}
		}

		// Draw the status lights.
		if (_statusLights != null)
		{
			for (int k = 0; k < _statusLights.length; ++k)
			{
				if (statusLights[k] == 1)
				{
					localGraphics.setPaint(Color.red);
					localGraphics.fill(_statusLights[k]);
				}
				localGraphics.setPaint(Color.black);
				localGraphics.draw(_statusLights[k]);
			}
		}

		// All done
		localGraphics.dispose();
	}

}
