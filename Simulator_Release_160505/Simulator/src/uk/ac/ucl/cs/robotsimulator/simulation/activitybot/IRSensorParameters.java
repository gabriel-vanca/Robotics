package uk.ac.ucl.cs.robotsimulator.simulation.activitybot;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.logging.Logger;

import javax.vecmath.Vector2d;

/** This class stores the probability of detection from an object as a function of
 * the dac_ctr value. From experiments, the probability is best described by a sigmoid of
 * the form 1/(1+exp(-a*(d-b))), where d is the distance. The table stores, for each dac_ctr
 * value, a and b. Linear interpolation over the parameters is used.
 * @author ucacsjj
 *
 */

public class IRSensorParameters
{
	// Map which stores material type and 
	private HashMap<String, DetectionProfile> _materialsMap = new HashMap<String, DetectionProfile>();	
	
	private final Logger _logger = Logger.getLogger(IRSensorParameters.class.getName());

	public IRSensorParameters()
	{
		// TODO: Change this to something like a JSON file which can be loaded
		
		// Add profile for the maze wall
		DetectionProfile mazeWallProfile = new DetectionProfile("MazeWall", 40);
		mazeWallProfile.add(0,0.1581,165.4238);
		mazeWallProfile.add(8,0.1167,157.4579);
		mazeWallProfile.add(16,0.1338,152.8557);
		mazeWallProfile.add(24,0.1809,148.6998);
		mazeWallProfile.add(32,0.1930,146.8053);
		mazeWallProfile.add(40,0.1662,144.0243);
		mazeWallProfile.add(48,0.2055,139.3153);
		mazeWallProfile.add(56,0.1641,132.0311);
		mazeWallProfile.add(64,0.2088,127.3463);
		mazeWallProfile.add(72,0.3745,120.7660);
		mazeWallProfile.add(80,0.2532,118.1500);
		mazeWallProfile.add(88,0.1827,109.9362);
		mazeWallProfile.add(96,0.1837,100.0172);
		mazeWallProfile.add(104,0.2614,97.6333);
		mazeWallProfile.add(112,0.2197,89.4677);
		mazeWallProfile.add(120,0.1686,78.9466);
		mazeWallProfile.add(128,0.3583,70.9527);
		mazeWallProfile.add(136,0.2306,61.2177);
		mazeWallProfile.add(144,0.6285,48.6520);
		mazeWallProfile.add(152,0.3071,37.3274);
		mazeWallProfile.add(160,0.0235,-503.1190);
		_materialsMap.put(mazeWallProfile.getMaterialName(), mazeWallProfile);
		
		// Add profile for testing the sensor against a white door
		DetectionProfile doorProfile = new DetectionProfile("WhiteDoor", 30);
		doorProfile.add(0,0.0464,317.7902);
		doorProfile.add(8,0.0524,305.5518);
		doorProfile.add(16,0.0555,293.6031);
		doorProfile.add(24,0.0601,281.9709);
		doorProfile.add(32,0.0583,259.9405);
		doorProfile.add(40,0.0597,250.1715);
		doorProfile.add(48,0.0534,243.0027);
		doorProfile.add(56,0.0582,227.6193);
		doorProfile.add(64,0.0843,213.8131);
		doorProfile.add(72,0.0771,200.2726);
		doorProfile.add(80,0.0820,185.4289);
		doorProfile.add(88,0.0938,174.3879);
		doorProfile.add(96,0.0937,157.3842);
		doorProfile.add(104,0.1503,144.5134);
		doorProfile.add(112,0.1530,130.5616);
		doorProfile.add(120,0.1154,116.7097);
		doorProfile.add(128,0.1213,97.8165);
		doorProfile.add(136,0.1806,73.6980);
		doorProfile.add(144,0.2057,60.6833);
		doorProfile.add(152,0.4358,48.1639);
		doorProfile.add(160,0.2874,34.0123);
		_materialsMap.put(doorProfile.getMaterialName(), doorProfile);
	}
	
	public double[] getSigmoidParameters(String materialName, int dacCtr)
	{
		if (_materialsMap.containsKey(materialName) == false)
		{
			return null;
		}
		return _materialsMap.get(materialName).getSigmoidParameters(dacCtr);
	}
	
	public double getProbabilityOfOne(double range, String materialName, int dacCtr)
	{
		if (_materialsMap.containsKey(materialName) == false)
		{
			return 0;
		}
		
		DetectionProfile d = _materialsMap.get(materialName);
		
		return d.getProbabilityOfOne(range, dacCtr);
	}
	
	static class DetectionProfile
	{		
		static class ProfileEntry
		{
			int dacCtr;
			double a;
			double b;
			
			public ProfileEntry(int dacCtr, double a, double b)
			{
				this.dacCtr = dacCtr;
				this.a = a;
				this.b = b;
			}
		};
		
		static class ProfileEntryComparator implements Comparator<ProfileEntry>
		{
			public int compare(ProfileEntry x, ProfileEntry y)
			{
				if (x.dacCtr < y.dacCtr)
				{
					return -1;
				}
				else if (x.dacCtr > y.dacCtr)
				{
					return 1;
				}
				return 0;
			}
		}
		
		private final Logger _logger = Logger.getLogger(DetectionProfile.class.getName());

		// Create the detection profile based on the material
		// The range offset is the offset between the range value
		// used in the calibration, and the actual offset of the IR sensors.
		// e.g., in some cases, the left wheel was placed at the different
		// distance, but the IR sensor is approximately 40mm further away.
		
		public DetectionProfile(String materialName, double rangeOffset)
		{
			_materialName = materialName;
			_rangeOffset = rangeOffset;
		}
		
		public void add(int dacCtr, double a, double b)
		{
			_sigmoidParameters.add(new ProfileEntry(dacCtr, a, b));
		}
		
		public final String getMaterialName()
		{
			return _materialName;
		}
		
		public double getProbabilityOfOne(double range, int dacCtr)
		{			
			range -= _rangeOffset;
			double[] v = getSigmoidParameters(dacCtr);
			//System.out.println(v[0] + " " + v[1]);
			return 1.0 / (1.0 + Math.exp(-v[0]*(range-v[1])));
		}
		
		public double[] getSigmoidParameters(int dacCtr)
		{
			ProfileEntry[] entries = getProfileEntries(dacCtr);

			double[] parameters = new double[2];
			
			//System.out.println(entries[0].dacCtr + " " + entries[1].dacCtr);
			
			// The dacCtr is "too big"; in that case, the probability of getting a 1
			// should be 1 everywhere. Cook the parameters to do this
			if (entries[1] == null)
			{
				parameters[0] = -1000;
				parameters[1] = 0;
				return parameters;
			}
			
			// Get the parameters using linear interpolation
			double fraction;

			if (entries[0] == entries[1])
			{
				fraction = 0.5;
				parameters[0] = entries[0].a;
				parameters[1] = entries[0].b;
			}
			else
			{
				fraction = (dacCtr - entries[0].dacCtr) / (entries[1].dacCtr - entries[0].dacCtr);
				parameters[0] = entries[1].a * fraction + entries[0].a * (1.0 - fraction);
				parameters[1] = entries[1].b * fraction + entries[0].b * (1.0 - fraction);
			}
			

			return parameters;
		}
		
		// Search through the profiles and find the closest ones spanning the 
		// fed in value of dacCtr.
		private ProfileEntry[] getProfileEntries(int dacCtr)
		{
			
			ProfileEntry[] entries = {null, null};
			
			for (ProfileEntry p : _sigmoidParameters)
			{
				if (p.dacCtr <= dacCtr)
				{
					entries[0] = p;
				}
				else if (p.dacCtr >= dacCtr)
				{
					entries[1] = p;
					break;
				}
			}

			return entries;
		}
		
		// Name of the material
		private String _materialName;
		
		// Comparator used to order the profiles		
		private ProfileEntryComparator _comparator = new ProfileEntryComparator();
		
		// The range offset
		double _rangeOffset;
		
		// The store of distribution parameters		
		private PriorityQueue<ProfileEntry> _sigmoidParameters = 
	            new PriorityQueue<ProfileEntry>(10, _comparator);
	}
}
