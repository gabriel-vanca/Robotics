package uk.ac.ucl.cs.robotsimulator.tests;

import uk.ac.ucl.cs.robotsimulator.simulation.activitybot.IRSensorParameters;

public class TestIRSensorParameters
{

	public TestIRSensorParameters()
	{
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
		IRSensorParameters ir = new IRSensorParameters();
		
		for (double d = 0; d < 400; d += 10)
		{
			double pD = ir.getProbabilityOfOne(d, "WhiteDoor", 0);
			System.out.println(d + " " + pD);
		}

	}

}
