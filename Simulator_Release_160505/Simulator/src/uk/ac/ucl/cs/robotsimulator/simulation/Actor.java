package uk.ac.ucl.cs.robotsimulator.simulation;

import java.util.HashSet;
import java.util.logging.Logger;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import uk.ac.ucl.cs.robotsimulator.maths.Ray;
import uk.ac.ucl.cs.robotsimulator.maths.SE2;

/**
 * See https://docs.unrealengine.com/latest/INT/Engine/Actors/index.html
 * 
 * @author ucacsjj
 *
 */

public class Actor
{
	// Axes
	protected Vector2d[] _axes;

	/*
	 * public Actor(SimulationEngine simulationEngine) { _logger =
	 * Logger.getLogger(this.getClass().getName()); _simulationEngine =
	 * simulationEngine; allocateStructures(); update(); }
	 * 
	 * public Actor(SimulationEngine simulationEngine, double width, double
	 * height) { _logger = Logger.getLogger(this.getClass().getName());
	 * _simulationEngine = simulationEngine; allocateStructures(); _extent.x =
	 * width; _extent.y = height; update(); }
	 */

	// Or - the actors associated with this actor???
	protected final HashSet<Actor> _children = new HashSet<Actor>();

	// Cached array of corners
	protected Point2d[] _corners;

	// Extent
	protected Vector2d _extent;

	// The logger - one per robot instance
	protected final Logger _logger;

	// Origin?
	protected double[] _origin;

	// If this actor is attached to another actor, what's the parent?
	protected Actor _parent;

	// The pose of this object
	protected SE2 _pose;

	// The simulation engine this actor is attached to
	protected SimulationEngine _simulationEngine;

	// The name (if assigned)
	String _name = new String();

	public Actor()
	{
		_logger = Logger.getLogger(this.getClass().getName());
		allocateStructures();
		update();
	}

	public void addChild(Actor a)
	{
		_children.add(a);
		a._parent = this;
	}

	public Point2d[] getCorners()
	{
		return _corners;
	}

	public Vector2d getExtent()
	{
		return _extent;
	}

	public SE2 getPose()
	{
		return _pose;
	}

	public boolean overlaps(final Actor other)
	{
		return overlaps1Way(other) && other.overlaps1Way(this);
	}

	public RayQueryCollsionResult rayQuery(Ray ray)
	{
		RayQueryCollsionResult closestIntersection = getLineIntersection(ray, _corners[0], _corners[1]);
		RayQueryCollsionResult intersection = getLineIntersection(ray, _corners[1], _corners[2]);
		if (intersection.d < closestIntersection.d)
		{
			closestIntersection = intersection;
		}
		intersection = getLineIntersection(ray, _corners[2], _corners[3]);
		if (intersection.d < closestIntersection.d)
		{
			closestIntersection = intersection;
		}
		intersection = getLineIntersection(ray, _corners[3], _corners[0]);
		if (intersection.d < closestIntersection.d)
		{
			closestIntersection = intersection;
		}
		return closestIntersection;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public void setPose(SE2 newPose)
	{
		_pose.set(newPose);
		update();
	}

	public void setSimulationEngine(SimulationEngine simulationEngine)
	{
		// Can't change ownership once it's set
		assert ((_simulationEngine == null) || (_simulationEngine == simulationEngine));
		_simulationEngine = simulationEngine;
	}

	// Helper to allocate the internal structures
	private void allocateStructures()
	{
		// Allocate the pose
		_pose = new SE2();

		// Allocate the corners
		_corners = new Point2d[4];
		for (int k = 0; k < 4; ++k)
		{
			_corners[k] = new Point2d();
		}

		// Allocate the axes
		_axes = new Vector2d[2];
		for (int k = 0; k < 2; ++k)
		{
			_axes[k] = new Vector2d();
		}

		_extent = new Vector2d();
		_origin = new double[2];
	}

	protected RayQueryCollsionResult getLineIntersection(Ray ray, Point2d point1, Point2d point2)
	{
		final double p0_x = ray.start.x;
		final double p0_y = ray.start.y;
		final double p1_x = ray.start.x + ray.direction.x;
		final double p1_y = ray.start.y + ray.direction.y;
		final double p2_x = point1.x;
		final double p2_y = point1.y;
		final double p3_x = point2.x;
		final double p3_y = point2.y;

		/*
		 * 
		 * double s1_x = ray.direction.x; double s1_y = ray.direction.y; double
		 * s2_x = point2.x - point1.x; double s2_y = point2.y - point1.y;
		 */

		double s1_x, s1_y, s2_x, s2_y;
		s1_x = p1_x - p0_x;
		s1_y = p1_y - p0_y;
		s2_x = p3_x - p2_x;
		s2_y = p3_y - p2_y;

		final RayQueryCollsionResult intersection = new RayQueryCollsionResult();

		double s, t, m;
		m = (-s2_x * s1_y) + (s1_x * s2_y);

		// Very small => lines pretty much parallel
		if (Math.abs(m) < 1e-6)
		{
			return intersection;
		}

		s = ((-s1_y * (p0_x - p2_x)) + (s1_x * (p0_y - p2_y))) / m;
		t = ((s2_x * (p0_y - p2_y)) - (s2_y * (p0_x - p2_x))) / m;

		if ((s >= 0) && (s <= 1) && (t >= 0))
		{
			intersection.actor = this;
			intersection.d = t;
			intersection.point.x = ray.start.x + (ray.direction.x * t);
			intersection.point.y = ray.start.y + (ray.direction.y * t);
		}

		return intersection;
	}

	protected boolean overlaps1Way(Actor other)
	{
		for (int a = 0; a < 2; ++a)
		{
			double t = (_axes[a].x * other._corners[0].x) + (_axes[a].y * other._corners[0].y);

			// Find the extent of box 2 on axis a
			double tMin = t;
			double tMax = t;

			for (int c = 1; c < 4; ++c)
			{
				t = (_axes[a].x * other._corners[c].x) + (_axes[a].y * other._corners[c].y);
				tMin = Math.min(tMin, t);
				tMax = Math.max(tMax, t);
			}

			if ((tMin > (1 + _origin[a])) || (tMax < _origin[a]))
			{
				// There was no intersection along this dimension;
				// the boxes cannot possibly overlap.
				return false;
			}
		}
		return true;
	}

	// Update all the geometry associated with this object
	protected void update()
	{
		// This is inefficient - we should be able to use some of the SE2 stuff
		// instead
		final Vector2d X = new Vector2d(Math.cos(_pose.theta), Math.sin(_pose.theta));
		X.scale(0.5 * _extent.x);
		final Vector2d Y = new Vector2d(-Math.sin(_pose.theta), Math.cos(_pose.theta));
		Y.scale(0.5 * _extent.y);

		_corners[0].x = _pose.t.x - X.x - Y.x;
		_corners[0].y = _pose.t.y - X.y - Y.y;
		_corners[1].x = (_pose.t.x + X.x) - Y.x;
		_corners[1].y = (_pose.t.y + X.y) - Y.y;
		_corners[2].x = _pose.t.x + X.x + Y.x;
		_corners[2].y = _pose.t.y + X.y + Y.y;
		_corners[3].x = (_pose.t.x - X.x) + Y.x;
		_corners[3].y = (_pose.t.y - X.y) + Y.y;

		// Make the length of each axis 1/edge length so we know any
		// dot product must be less than 1 to fall within the edge.

		_axes[0].sub(_corners[1], _corners[0]);
		_axes[0].scale(1.0 / _axes[0].lengthSquared());
		_origin[0] = (_axes[0].x * _corners[0].x) + (_axes[0].y * _corners[0].y);

		_axes[1].sub(_corners[3], _corners[0]);
		_axes[1].scale(1.0 / _axes[1].lengthSquared());
		_origin[1] = (_axes[1].x * _corners[1].x) + (_axes[1].y * _corners[1].y);

		// Now update the geometry associated with all the children
	}

	final String getName()
	{
		return _name;
	}

	void reset()
	{

	}
}
