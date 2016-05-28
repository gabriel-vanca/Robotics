package uk.ac.ucl.cs.robotsimulator.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.swing.JPanel;

import uk.ac.ucl.cs.robotsimulator.simulation.Actor;
import uk.ac.ucl.cs.robotsimulator.simulation.ActorAddOrRemoveEvent;
import uk.ac.ucl.cs.robotsimulator.simulation.ActorChangeListener;
import uk.ac.ucl.cs.robotsimulator.simulation.Arena;
import uk.ac.ucl.cs.robotsimulator.simulation.Robot;
import uk.ac.ucl.cs.robotsimulator.simulation.SimulationEngine;
import uk.ac.ucl.cs.robotsimulator.simulation.Wall;

public class SimulationEngineViewDrawingPanel extends JPanel implements ActorChangeListener
{

	class MovingAdapter extends MouseAdapter
	{

		private int x;
		private int y;

		@Override
		public void mouseDragged(MouseEvent e)
		{
			/*
			 * if (!MOVEMENT_BLOCKED) { doMove(e); } else {
			 * //LOGGER.log(Level.WARNING,
			 * "Can't move robot till the execution of the script terminates!",
			 * ""); //outputTextArea.append(
			 * "Can't move robot till the execution of the script terminates!");
			 * //outputTextArea.append(separator); }
			 */
			//_logger.info(e.toString());
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			/*
			 * if (MouseXYToggleButton.isSelected()) { // System.out.println(
			 * "MouseXYToggleButton.isSelected() = " + //
			 * MouseXYToggleButton.isSelected()); MouseXYtextField.setText("x="
			 * + e.getX() + " , y=" + e.getY()); }
			 */
		}

		@Override
		public void mousePressed(MouseEvent e)
		{

			x = e.getX();
			y = e.getY();
		}

		private void doMove(MouseEvent e)
		{

			final int dx = e.getX() - x;
			final int dy = e.getY() - y;

			// _tr
			/*
			 * if (robot.isHit(x, y)) { if (MouseXYToggleButton.isSelected()) {
			 * MouseXYtextField.setText("x=" + e.getX() + " , y=" + e.getY()); }
			 * if (RobotXYToggleButton.isSelected()) {
			 * RobotXYtextField.setText("x=" + e.getX() + " , y=" + e.getY()); }
			 *
			 * robot.addX(dx); robot.addY(dy); centerAxes.addX(dx);
			 * centerAxes.addY(dy);
			 *
			 * repaint(); }
			 */

			x += dx;
			y += dy;

			// System.out.println("X = " + robot.getX() + " Y = " +
			// robot.getY());
		}

	}

	private final static Logger _logger = Logger.getLogger(SimulationEngineViewDrawingPanel.class.getName());
	/**
	 *
	 */
	private static final long serialVersionUID = 1592887759821205920L;
	private SimulationEngine _simulationEngine;

	// TODO: I think we can actually change these and make these more generic

	private final double _translationX = 0;

	private final double _translationY = 0;

	// Information about the viewing transformation
	private double _viewScale = 1;

	private final AffineTransform _viewTransform = new AffineTransform();

	private final AffineTransform _worldToGUITransform = new AffineTransform();

	// AffineTransform transform = new AffineTransform(c, s, -s, c, pose.x,
	// pose.y);

	// The portrayal for the ground plane
	ArenaPortrayal _arenaPortrayal;

	// The set of portrayals for the robots
	HashMap<Robot, RobotPortrayal> _robotPortrayals = new HashMap<Robot, RobotPortrayal>();

	// The set of portrayals for the walls
	HashMap<Wall, WallPortrayal> _wallPortrayals = new HashMap<Wall, WallPortrayal>();

	public SimulationEngineViewDrawingPanel(boolean isDoubleBuffered)
	{
		super(isDoubleBuffered);
	}

	public SimulationEngineViewDrawingPanel(LayoutManager layout)
	{
		super(layout);
	}

	public SimulationEngineViewDrawingPanel(LayoutManager layout, boolean isDoubleBuffered)
	{
		super(layout, isDoubleBuffered);
	}

	public SimulationEngineViewDrawingPanel(SimulationEngine simulationEngine)
	{
		simulationEngine.addListener(this);
	}

	@Override
	public synchronized void handleAddOrRemoveEvent(ActorAddOrRemoveEvent event)
	{
		//_logger.info("Received event " + event);
		final Actor actor = event.getActor();
		// Handle the events
		if (actor instanceof Arena)
		{
			if (event.getAdded() == true)
			{
				_arenaPortrayal = new ArenaPortrayal((Arena) actor);
			}
			else
			{
				_arenaPortrayal = null;
			}
		}
		else if (actor instanceof Robot)
		{
			final Robot robot = (Robot) actor;

			// If a new robot has been created, add it to the list of
			// portrayals. Otherwise, remove it
			if (event.getAdded() == true)
			{
				final RobotPortrayal robotPortrayal = new RobotPortrayal(robot);
				_robotPortrayals.put(robot, robotPortrayal);
			}
			else
			{
				_robotPortrayals.remove(robot);
			}
		}
		else if (actor instanceof Wall)
		{
			final Wall wall = (Wall) actor;
			// If a new robot has been created, add it to the list of
			// portrayals. Otherwise, remove it
			if (event.getAdded() == true)
			{
				final WallPortrayal wallPortrayal = new WallPortrayal(wall);
				_wallPortrayals.put(wall, wallPortrayal);
			}
			else
			{
				_wallPortrayals.remove(wall);
			}
		}
	}

	public void initialize(SimulationEngine simulationEngine)
	{
		_simulationEngine = simulationEngine;

		// _logger.info("ScenarioViewDrawingPanel.initialize: invoked");
		final MovingAdapter ma = new MovingAdapter();

		// Set up the mouse listeners for control
		addMouseMotionListener(ma);
		addMouseListener(ma);

		// Construct the arena
		final Arena arena = _simulationEngine.getArena();
		if (arena != null)
		{
			_arenaPortrayal = new ArenaPortrayal(arena);
		}

		final HashMap<String, Robot> robots = _simulationEngine.getRobots();

		for (final Robot robot : robots.values())
		{
			final RobotPortrayal robotPortrayal = new RobotPortrayal(robot);
			_robotPortrayals.put(robot, robotPortrayal);
		}
		//
		final LinkedList<Wall> walls = _simulationEngine.getWalls();

		for (final Wall wall : walls)
		{
			//_logger.info("adding portrayal for wall " + wall);
			final WallPortrayal wallPortrayal = new WallPortrayal(wall);
			_wallPortrayals.put(wall, wallPortrayal);
		}

	}

	@Override
	public void paintComponent(Graphics g)
	{
		// _logger.info("ScenarioViewDrawingPanel.paintComponent: invoked");

		super.paintComponent(g);

		// Can't do anything if no arena is set up
		Arena arena = _simulationEngine.getArena();
		if (arena == null)
		{
			return;
		}
		
		final Graphics2D graphics = (Graphics2D) g;

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// This graphics component takes account of the view transformation.
		// This comes from the things like the sliders and mouse wheels which
		// zoom and translate the overall display
		final Graphics2D scaledTransformedGraphicsContext = (Graphics2D) graphics.create();
		scaledTransformedGraphicsContext.transform(_viewTransform);

		// This graphics component accounts for the fact that the simulator
		// itself uses the standard coordinate system used by everything apart
		// from GUIs: the coordinate system is right handed (+ve x to the right,
		// +ve y up), +ve rotation is anticlockwise
		final Graphics2D worldToGUIGraphicsContext = (Graphics2D) scaledTransformedGraphicsContext.create();

		final AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
		final AffineTransform move = AffineTransform.getTranslateInstance(50,
				-5 - arena.getExtent().y);
		worldToGUIGraphicsContext.transform(yFlip);
		worldToGUIGraphicsContext.transform(move);

		// TODO: Change this to have just a big list of the portrayals, and call them in turn
		
		// Draw the arena
		if (_arenaPortrayal != null)
		{
			_arenaPortrayal.paintComponent(worldToGUIGraphicsContext);
		}
		
		// Draw the robots.
		synchronized (_robotPortrayals)
		{
			for (final RobotPortrayal robotPortrayal : _robotPortrayals.values())
			{
				robotPortrayal.paintComponent(worldToGUIGraphicsContext);
			}
		}

		// Draw the walls
		for (final WallPortrayal wallPortrayal : _wallPortrayals.values())
		{
			wallPortrayal.paintComponent(worldToGUIGraphicsContext);
		}

		// Now clean up the resources
		worldToGUIGraphicsContext.dispose();
		scaledTransformedGraphicsContext.dispose();
	}

	// This method resets the display from the simulation engine. It clears out
	// all the display values and pulls them from scratch again.
	public void reset()
	{
		// Anything to do???
	}

	public void setViewScale(double viewScale)
	{
		_viewTransform.setToScale(viewScale, viewScale);
	}
}
