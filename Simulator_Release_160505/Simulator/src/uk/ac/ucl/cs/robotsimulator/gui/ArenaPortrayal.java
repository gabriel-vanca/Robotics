package uk.ac.ucl.cs.robotsimulator.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.logging.Logger;

import javax.swing.JComponent;

import uk.ac.ucl.cs.robotsimulator.simulation.Arena;

public class ArenaPortrayal extends JComponent
{

	private final static Logger _logger = Logger.getLogger(ArenaPortrayal.class.getName());

	private final int STEP_SIZE = 20;
	
	/**
	 *
	 */
	private static final long serialVersionUID = 540343110089471588L;

	private final double _arenaHeight;
	private final double _arenaWidth;

	public ArenaPortrayal(Arena arena)
	{
		_arenaWidth = arena.getExtent().x;
		_arenaHeight = arena.getExtent().y;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		// _logger.info("GroundPlanePortrayal.paintComponent: invoked");
		super.paintComponent(g);

		final Graphics2D g2d = (Graphics2D) g;

		// Get the last colour which was being used
		Color lastColour = g2d.getColor();
		
		g2d.setColor(Color.LIGHT_GRAY);

		// draw vertical lines
		for (int i = STEP_SIZE; i <= _arenaWidth - STEP_SIZE; i += STEP_SIZE)
		{
			final Line2D line = new Line2D.Double(i, 0, i, _arenaHeight);
			g2d.draw(line);
		}

		// draw horizontal lines
		for (int i = STEP_SIZE; i <= _arenaHeight - STEP_SIZE; i += STEP_SIZE)
		{
			final Line2D line = new Line2D.Double(0, i, _arenaWidth, i);
			g2d.draw(line);
		}
		
		// Restore the last colour which was being used
		g2d.setColor(lastColour);
	}
}
