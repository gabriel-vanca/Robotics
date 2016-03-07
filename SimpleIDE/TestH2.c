/*
  Blank Simple Project.c
  http://learn.parallax.com/propeller-c-tutorials 
*/
#include "simpletools.h"                      // Include simple tools

int main()                                    // Main function
{
  // Add startup code here.

 
  while(1)
  {
    // Add main loop code here.
    
  }  
}

void Track_Movement(double *angle_Radians, double *distance_X, double *distance_Y)
{
    int leftWheel_Ticks = 0;
    int rightWheel_Ticks = 0;
    drive_getTicks(int &leftWheel_Ticks, int &rightWheel_Ticks);
    
    double leftWheel_CM = TICKS_TO_CM(leftWheel_Ticks);
    
    if(leftWheel_Ticks == rightWheel_Ticks)
    {
        (*angle_Radians) = 0;
        distance_X = 0;
        distance_Y = leftWheel_CM;
        return;
    }
    
    double rightWheel_CM = TICKS_TO_CM(rightWheel_Ticks);
    
    double oldAngle_Radians = (*angle_Radians);
    double newAngle_Radians = (leftWheel_CM - rightWheel_CM) / DistanceBetweenWheels_CM;
    
    double leftRadius = leftWheel_CM / newAngleRadians;
    double rightRadius = rightWheel_CM / newAngleRadians;
    double robotRadius = (leftRadius + rightRadius) / 2;
    
    (*distance_Y) = robotRadius * ( sin(oldAngle_Radians + newAngleRadians) - sin(oldAngle_Radians) );
    (*distance_X) = robotRadius * ( cos(oldAngle_Radians) - cos(oldAngle_Radians + newAngleRadians) );
    angle_Radians = &newAngleRadians;
}

