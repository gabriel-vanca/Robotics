/**
 * 
 */
package uk.ac.ucl.cs.robotsimulator.simulation;

import javax.vecmath.Point2d;

/**
 * This actor stores a label
 * @author ucacsjj
 *
 */
public class Label extends Actor
{
	
	private String _labelText;

	/**
	 * 
	 */
	public Label(Point2d centre, String labelText)
	{
		_pose.t.x = centre.x;
		_pose.t.y = centre.y;
		_pose.theta = 0;
		_labelText = new String(labelText);
	}

	public void setText(final String labelText)
	{
		_labelText = new String(labelText);
	}
	
	public final String getText()
	{
		return _labelText;
	}
	
}
