#include "simpletools.h"
#include "abdrive.h"
#include "ping.h"

#include "Headers/movement.h"
#include "Headers/irsensors.h"
#include "Headers/mathf.h"

int const StandardSpeed = 70;       //TODO:: Can it be increased?
int const Acceleration = 10;        //TODO:: Still have to decide if this is the best value. Maybe go with 12.
int const WaitingTime_ms = 50;      //TODO:: Should it be lower?
int LeftWheelSpeed;
int RightWheelSpeed;

unsigned int SpeedChange_Length;
char SpeedChange_DeltaSpeed[3200];               // The variation from the StandardSpeed
unsigned short int SpeedChange_Cycles[3200];      // The number of check cycles for which the time remained constant. The time in ms optained by multypling with WaitingTime_ms 

void SetDriveSpeed(int left, int right)
{
  LeftWheelSpeed = left;
  RightWheelSpeed = right;
  //drive_speed(LeftWheelSpeed, RightWheelSpeed);
  drive_ramp(LeftWheelSpeed, RightWheelSpeed);          //TODO:: Stil have to decide if we should use drive_ramp or drive_speed
}

void GoForward()
{
    float const Kp = 4.8;               //TODO:: Is this the best choice?
    float const Ki = 0;                 //TODO:: Can we make it work?
    float const Kd = 0;                 //TODO:: Can we make it work?
    float const AngleChangeLimit = DegreesToRadians(7);
    int (*getLeftSensorValueFunction)() = &IR_GetLeftSensorValue;
    float const SetpointIRValue = IR_GetAverageSensorValue(3, getLeftSensorValueFunction, WaitingTime_ms);
    
    double previous_deltaIR = 0;
    double previous_integral = 0;    
    int totalLeftWheel_Ticks = 0;
    int totalRightWheel_Ticks = 0;
    double current_angleChange_radians = Math_PI / 2;
    double total_angleChange_radians = 0;
    double distance_X = 0;
    double distance_Y = 0;
    
    SetDriveSpeed(StandardSpeed, StandardSpeed);
    SpeedChange_DeltaSpeed[0] = 0;
    SpeedChange_Cycles[0] = 0;
    SpeedChange_Length = 0;

    while(1)
    {
        // First we get the value from the IR sensor
        float currentIRvalue = (*getLeftSensorValueFunction)();
        // Now we get the delta value between the current IR sensor value and the initial value
        float deltaIR = SetpointIRValue - currentIRvalue;
       
        if(ping_cm(8) <= 10)    // We got to the wall. Break current loop
        {
            SetDriveSpeed(0,0);
            break;
        }

        /* Now we send the values to the PID and adjust speed on each wheel accordingly. 
           This results in a change in the angle in which the robot is going. */
        double deltaSpeed = PID(deltaIR, Kp, Ki, Kd, (float) WaitingTime_ms, &previous_deltaIR, &previous_integral);
        SetDriveSpeed(StandardSpeed + deltaSpeed, StandardSpeed - deltaSpeed);
        
        Track_Movement(&current_angleChange_radians, &distance_X, &distance_Y, &totalLeftWheel_Ticks, &totalRightWheel_Ticks);
        total_angleChange_radians += current_angleChange_radians;
        
        if(-AngleChangeLimit < total_angleChange_radians && total_angleChange_radians < AngleChangeLimit)   // Small changes due to walls not being straight or IRdetection faults or PID faults
        {
            /* We update the previous deltaSpeed using a ponderate average so that we don't loose values.
               We increase by one the number of cycles for which the angle remained almost unchanged.
               We reset the total_angleChange_radians. */
               
            SpeedChange_DeltaSpeed[SpeedChange_Length] = Round((SpeedChange_DeltaSpeed[SpeedChange_Length] * SpeedChange_Cycles[SpeedChange_Length] + deltaSpeed) / (++SpeedChange_Cycles[SpeedChange_Length]));
            total_angleChange_radians = 0;
        }
        else    // A sharp angle change was detected. The robot is at a turn.
        {
            SpeedChange_Length++;
            SpeedChange_Cycles[SpeedChange_Length] = 1;
            SpeedChange_DeltaSpeed[SpeedChange_Length] = deltaSpeed;
        }
        
           
        /*
          print("SetpointIRValue = %.2f   Left = %.2f   Delta = %.2f \n", SetpointIRValue, currentIRvalue, deltaIR);
          print("LeftSpeed = %d   RightSpeed = %d \n", LeftWheelSpeed, RightWheelSpeed);
        */

        //print("angle change = %f", RadiansToDegrees(angle_radians));
    
        pause(WaitingTime_ms); 
    }
}

void GetBack()
{
  for(int index = SpeedChange_Length;index >= 0;index--) 
  {
      /* We are parsing the array and we add the deltaSpeeds optained when the robot went forward to the same StandardSpeed.
         After that, we are pausing the program accordingly with the number of cycles for which the angle has remained pretty mcuh constant.
         It is worth nothing that we are reversing the speeds of the wheels then going back. */
         
      SetDriveSpeed(StandardSpeed - SpeedChange_DeltaSpeed[index], StandardSpeed + SpeedChange_DeltaSpeed[index]);
      pause(SpeedChange_Cycles[index] * WaitingTime_ms);        //TODO:: Maybe slowly decrease pause by 20 ms to account for lateness in sensor detection, speed change and acceleration
  }
}

int main()
{   
  drive_setRampStep(Acceleration);
  GoForward();
  Rotate_ZeroRadiusTurn(Math_PI, 0);
  GetBack();
  
  return 0;
}