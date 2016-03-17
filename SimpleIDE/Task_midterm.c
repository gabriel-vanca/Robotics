#include "simpletools.h"
#include "abdrive.h"
#include "ping.h"

#include "Headers/movement.h"
#include "Headers/irsensors.h"
#include "Headers/mathf.h"

int LeftWheelSpeed;     // TODO:: Change to pointer and put it in GoForward()
int RightWheelSpeed;    // TODO:: Change to pointer and put it in GoForward()


int PositionChange_Length;
short int PositionChange_AngleDegrees[1000];
short int PositionChange_Distance[1000];

void SetDriveSpeed(int left, int right)
{
  LeftWheelSpeed = left;
  RightWheelSpeed = right;
  //drive_speed(LeftWheelSpeed, RightWheelSpeed);
  drive_ramp(LeftWheelSpeed, RightWheelSpeed);          //TODO:: Stil have to decide if we should use drive_ramp or drive_speed
}

double GetDistance(double distance_X, double distance_Y)
{
    return sqrt(distance_X * distance_X + distance_Y * distance_Y);
}

void GoForward()
{
    int const StandardSpeed = 70;       //TODO:: Can it be increased
    int const Acceleration = 10;        //TODO:: Still have to decide if this is the best value
    float const Kp = 4.8;           //4.8/0/0
    float const Ki = 0;                 //TODO:: Can we make it work?
    float const Kd = 0;                 //TODO:: Can we make it work?
    int const WaitingTime_ms = 50;
    float const AngleLimit = DegreesToRadians(15);
    int (*getLeftSensorValueFunction)() = &IR_GetLeftSensorValue;
    float const SetpointIRValue = IR_GetAverageSensorValue(3, getLeftSensorValueFunction, WaitingTime_ms);
    
    double previous_deltaIR = 0;
    double previous_integral = 0;    
    int totalLeftWheel_Ticks = 0;
    int totalRightWheel_Ticks = 0;
    double angle_radians = Math_PI / 2;
    double distance_X = 0;
    double distance_Y = 0;
    double temp_change_angle_rad = 0;
    double temp_change_dist_cm = 0;
    
    drive_setRampStep(Acceleration);
    SetDriveSpeed(StandardSpeed, StandardSpeed);

    while(1)
    {
        // First we get the value from the IR sensor
        float currentIRvalue = (*getLeftSensorValueFunction)();
        // Now we get the delta value between the current IR sensor value and the initial value
        float deltaIR = SetpointIRValue - currentIRvalue;
       
        if(ping_cm(8) <= 10)    // We got to the wall. Break current loop
        {
            SetDriveSpeed(0,0);        
            PositionChange_AngleDegrees[PositionChange_Length] = (short int) RadiansToDegrees(temp_change_angle_rad);
            PositionChange_Distance[PositionChange_Length] = (short int) CM_TO_TICKS(temp_change_dist_cm);
            PositionChange_Length++;
            break;
        }

        /* Now we send the values to the PID and adjust speed on each wheel accordingly. 
           This results in a change in the angle in which the robot is going. */
        double deltaSpeed = PID(deltaIR, Kp, Ki, Kd, (float) WaitingTime_ms, &previous_deltaIR, &previous_integral);
        SetDriveSpeed(StandardSpeed + deltaSpeed, StandardSpeed - deltaSpeed);
        
        Track_Movement(&angle_radians, &distance_X, &distance_Y, &totalLeftWheel_Ticks, &totalRightWheel_Ticks);
        double distance_cm = GetDistance(distance_X, distance_Y);
        
        if(temp_change_angle_rad <= AngleLimit && temp_change_angle_rad >= -AngleLimit) // Normal speed until now
        {
            if(angle_radians <= AngleLimit && angle_radians >= -AngleLimit) // No turn
            {
                temp_change_angle_rad += angle_radians;
                temp_change_dist_cm += distance_cm;
            }
            else    // A turn starts
            {
                PositionChange_AngleDegrees[PositionChange_Length] = (short int) RadiansToDegrees(temp_change_angle_rad);
                PositionChange_Distance[PositionChange_Length] = (short int) CM_TO_TICKS(temp_change_dist_cm);
                PositionChange_Length++;
                temp_change_angle_rad = angle_radians;
                temp_change_dist_cm = distance_cm;
            }
        }
        else    //During a turn
        {
            temp_change_angle_rad += angle_radians;
            temp_change_dist_cm += distance_cm;
            
            if(angle_radians <= AngleLimit && angle_radians >= -AngleLimit)  // Turn is ending
            {
                PositionChange_AngleDegrees[PositionChange_Length] = (short int) RadiansToDegrees(temp_change_angle_rad);
                PositionChange_Distance[PositionChange_Length] = (short int) CM_TO_TICKS(temp_change_dist_cm);
                PositionChange_Length++;
                temp_change_angle_rad = 0;
                temp_change_dist_cm = 0;
            }
        }
        
        /*
          print("SetpointIRValue = %.2f   Left = %.2f   Delta = %.2f \n", SetpointIRValue, currentIRvalue, deltaIR);
          print("LeftSpeed = %d   RightSpeed = %d \n", LeftWheelSpeed, RightWheelSpeed);
        */

        print("angle change = %f, dist = %f \n", RadiansToDegrees(angle_radians), distance_cm);
    
        pause(WaitingTime_ms); 
    }
}

void GetBack()
{
  for(int index = PositionChange_Length - 1;index >= 0 ;index--) 
  {
      drive_goto(PositionChange_Distance[PositionChange_Length] - 10, PositionChange_Distance[PositionChange_Length] - 10);
      Rotate_ZeroRadiusTurn(DegreesToRadians(PositionChange_AngleDegrees[PositionChange_Length]),1);
  }
  
  int deltaDist = (PositionChange_Length - 1) * 3;
  drive_goto(CM_TO_TICKS(deltaDist), CM_TO_TICKS(deltaDist));
  
}

int main()
{   
  GoForward();
  Rotate_ZeroRadiusTurn(Math_PI, 0);
  GetBack();
  
  return 0;
}