#include "simpletools.h"
#include "abdrive.h" 

#define Math_PI (3.14159265359)
#define CM_TO_TICKS(v) (v * 3.07)

void Rotate_ZeroRadius(double degrees)
{
    int ticks = (int) (degrees * Math_PI / 180 * 16.2769);
    drive_goto(ticks,-ticks);
}

void Rotate_PivotTurn(double degrees, char moveRightWheel)
{
    const double MM_Per_Radian = 105.8;
    double degrees_in_radian = degrees * Math_PI / 180;
    int ticks = (int) (degrees_in_radian / 3.25);
    if(moveRightWheel)
        drive_goto(0, ticks);
    else
        drive_goto(ticks, 0);
}

void Move_In_Straight_Line(double currentPosition, double destination, char facingRight)
{
  double length_cm = destination - currentPosition;
  double length_ticks = CM_TO_TICKS(length_cm);
    
	if((facingRight && length_ticks < 0) || (!facingRight && length_ticks > 0))
	{
		Rotate_ZeroRadius(180);
        facingRight = 1 - facingRight;
	}
 	
     if(length_ticks < 0)
        length_ticks = -length_ticks;

	drive_goto(length_ticks, length_ticks);
}

void main() {}