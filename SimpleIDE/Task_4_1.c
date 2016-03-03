#include "simpletools.h"
#include "abdrive.h"
#include "ping.h"
#include "basicmoves.h"
#include "irsensors.h"

double GetAveragecurrentIRvalueSensorValue()
{
  int currentIRvalue = 0;
  const int nrIterations = 4;
  for(int i=1; i<=nrIterations; i++)
  {
    currentIRvalue += IR_GetLeftSensorValue();
  }
  
  return currentIRvalue * 1.0 / nrIterations;
}

int LeftWheelSpeed;
int RightWheelSpeed;

void SetDriveSpeed(int left, int right)
{
  LeftWheelSpeed = left;
  RightWheelSpeed = right;
  drive_speed(LeftWheelSpeed, RightWheelSpeed);
}  

int const StandardSpeed = 50;
int const K = 0.6;
//int const LowSpeed = 40;
//int const HighSpeed = 20;

int main()
{
  //MarkUsingLED(1); 
  double const InitIRValue = (GetAveragecurrentIRvalueSensorValue() + GetAveragecurrentIRvalueSensorValue()) / 2.0;
 // MarkUsingLED(1);
  
  SetDriveSpeed(StandardSpeed, StandardSpeed);
  
  while(1)
  {
    // First we get the value from the left IR sensor
    double currentIRvalue = GetAveragecurrentIRvalueSensorValue();
    // Now we get the delta value between the current IR sensor value and the initial value
    double DeltaIR = currentIRvalue - InitIRValue;
    
    print("InitVal = %.2f   Left = %.2f   Delta = %.2f \n", InitIRValue, currentIRvalue, DeltaIR);
    print("LeftSpeed = %d   RightSpeed = %d \n\n", LeftWheelSpeed, RightWheelSpeed);
    
    if(ping_cm(8) <= 5)    // Case 1: The wall turned right 90 degrees
    {
        SetDriveSpeed(0,0);
        int distFromWall = ping_cm(8);
        int deltaDist = distFromWall - 5;
        //Move_In_Straight_Line(0, deltaDist, 0);
        double deltaDist_ticks = CM_TO_TICKS(deltaDist);
        drive_goto(deltaDist_ticks, deltaDist_ticks);
        break;
    }
    
  /*  if(currentIRvalue == 20)    //Case 2: The wall turned left 90 degrees
    {
        SetDriveSpeed(0,0);
        int distFromWall = ping_cm(15);
        int deltaDist = distFromWall - 5;
        Move_In_Straight_Line(0, deltaDist, 1);
        break;
    }*/
    
    if(DeltaIR <= 1.7 && DeltaIR >= - 1.7)    //Case 3: Straight wall
    {
      SetDriveSpeed(StandardSpeed, StandardSpeed);      
    }      
    else
    {
        int deltaSpeed = DeltaIR * K;
        if(DeltaIR > 0)     //Case 4: The distance has increased so the robot is moving too much to the right. He must move left.
        {
            SetDriveSpeed(StandardSpeed - deltaSpeed, StandardSpeed + deltaSpeed);
        }
        else     //Case 5: The distance has decreased so the robot is moving too much to the left. He must move right.
        {
            SetDriveSpeed(StandardSpeed + deltaSpeed, StandardSpeed - deltaSpeed);
        }    
    }
   // pause(100);
    //pause(100);    
  }
  
  return 0;
}      
    
    
    //52 50 68