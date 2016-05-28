package uk.ac.ucl.cs.robotsimulator.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.vecmath.Point2d;

import uk.ac.ucl.cs.robotsimulator.simulation.Wall;

/**
 * This class draws the walls.
 *
 * @author ucacsjj
 *
 */

public class WallPortrayal extends JComponent
{
	private final static Logger _logger = Logger.getLogger(RobotPortrayal.class.getName());

	private static final long serialVersionUID = 6204640922718637175L;

	private final Wall _wall;

	private final Line2D.Double[] _wallLines;

	public WallPortrayal(Wall wall)
	{
		_wall = wall;

		final Point2d[] corners = wall.getCorners();

		_wallLines = new Line2D.Double[4];

		_wallLines[0] = new Line2D.Double(corners[0].x, corners[0].y, corners[1].x, corners[1].y);
		_wallLines[1] = new Line2D.Double(corners[1].x, corners[1].y, corners[2].x, corners[2].y);
		_wallLines[2] = new Line2D.Double(corners[2].x, corners[2].y, corners[3].x, corners[3].y);
		_wallLines[3] = new Line2D.Double(corners[3].x, corners[3].y, corners[0].x, corners[0].y);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		// _logger.info("WallPortrayal.paintComponent: invoked");
		super.paintComponent(g);
		final Graphics2D g2d = (Graphics2D) g;

		for (int w = 0; w < 4; ++w)
		{
			g2d.draw(_wallLines[w]);
		}
	}
}

/*
 * _wallRectangle = new Rectangle.Double(-0.5 * extent.x, -0.5 * extent.y,
 * extent.x, extent.y); }
 * 
 * public void paintComponent(Graphics g) { _logger.info(
 * "WallPortrayal.paintComponent: invoked"); super.paintComponent(g);
 * 
 * Graphics2D graphics = (Graphics2D) g; Graphics2D localGraphics = (Graphics2D)
 * graphics.create();
 * 
 * SE2 pose = _wall.getPose();
 * 
 * double c = Math.cos(pose.theta); double s = Math.sin(pose.theta);
 * 
 * // The robot internally uses a right handed coordinate system, origin //
 * bottom left. AffineTransform transform = new AffineTransform(c, s, -s, c,
 * pose.t.x, pose.t.y);
 * 
 * localGraphics.dispose(); }
 */
