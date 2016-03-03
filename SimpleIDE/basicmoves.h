#include "simpletools.h"
#include "abdrive.h"
#include "mathf.h"

#define CM_TO_TICKS(v) (v * 3.07)

void Rotate_ZeroRadius(double degrees, char rotateLeft)
{
    int ticks = (int) (degrees * Math_PI / 180.0 * 16.2769);
    if(rotateLeft)
        drive_goto(-ticks,ticks);
    else
        drive_goto(ticks,-ticks);
}

void Rotate_PivotTurn(double degrees, char rotateLeft)
{
    const double MM_Per_Radian = 105.8;
    double degrees_in_radian = degrees * Math_PI / 180;
    int ticks = (int) (degrees_in_radian / 3.25);
    if(rotateLeft)
        drive_goto(0, ticks);
    else
        drive_goto(ticks, 0);
}

void Move_In_Straight_Line(double currentPosition, double finalPosition, char facingRight)
{
  double distance_cm = finalPosition - currentPosition;
  double distance_ticks = CM_TO_TICKS(distance_cm);
    
	if((facingRight && distance_ticks < 0) || (!facingRight && distance_ticks > 0))
	{
		Rotate_ZeroRadius(180, 1);
        facingRight = 1 - facingRight;
	}
 	
     if(distance_ticks < 0)
        distance_ticks = -distance_ticks;

	drive_goto(distance_ticks, distance_ticks);
}

void MarkUsingLED(int howManyTimes)
{
    while(howManyTimes--)
    {
        high(26);
	    pause(1000);
	    low(26);
        
       if(howManyTimes)
           pause(600);
    }
}