#include "simpletools.h"
#include "abdrive.h"
#include "ping.h"
#include "Headers/basic.h"
#include "Headers/movement.h"
#include "Headers/irsensors.h"
#include "Headers/mathf.h"
#include "Headers/basic.c"
#include "Headers/movement.c"
#include "Headers/irsensors.c"
//#include "Headers/mathf.c"

int LeftWheelSpeed;
int RightWheelSpeed;

void SetDriveSpeed(int left, int right)
{
  LeftWheelSpeed = left;
  RightWheelSpeed = right;
  //drive_speed(LeftWheelSpeed, RightWheelSpeed);
  drive_ramp(LeftWheelSpeed, RightWheelSpeed);
}  

int const StandardSpeed = 70;       //52 50 68
//double const K = 1.25;
int const Acceleration = 10;
float const Kp = 4;
float const Ki = 6;
float const Kd = 0.05;
float const waitingTime_ms = 50;

double angle_radians = 0;
double distance_X = 0;
double distance_Y = 0;

//int const LowSpeed = 40;
//int const HighSpeed = 20;

void Write()
{
    double angle_degrees = RadiansToDegrees(angle_radians);
    sd_mount(22, 23, 24, 25);
    FILE* fp = fopen("robotics.txt", "w");
    char s[50];
    sprintf(s, "%.2f cm \n %.2f degrees", distance_Y, angle_degrees);
    fwrite(s, 1, 50, fp);
    fclose(fp);
}

int main()
{
  int (*getAverageLeftSensorValueFunction)() = &IR_GetLeftSensorValue;
  float const SetpointIRValue = ((*getAverageLeftSensorValueFunction)() + (*getAverageLeftSensorValueFunction)()) / 2.0;
  double previous_deltaIR = 0;
  double previous_integral = 0;
  drive_setRampStep(Acceleration);
  SetDriveSpeed(StandardSpeed, StandardSpeed);
  MarkUsingLED(1);
  
  while(1)
  {
    Track_Movement(&angle_radians, &distance_X, &distance_Y);
    // First we get the value from the left IR sensor
    float currentIRvalue = (*getAverageLeftSensorValueFunction)();
    // Now we get the delta value between the current IR sensor value and the initial value
    float deltaIR = SetpointIRValue - currentIRvalue;
    
    print("SetpointIRValue = %.2f   Left = %.2f   Delta = %.2f \n", SetpointIRValue, currentIRvalue, deltaIR);
    print("LeftSpeed = %d   RightSpeed = %d \n\n", LeftWheelSpeed, RightWheelSpeed);
    
    if(ping_cm(8) <= 5)    // Case 1: The wall turned right 90 degrees
    {
        SetDriveSpeed(0,0);
        int distFromWall = ping_cm(8);
        int deltaDist = distFromWall - 5;
        double deltaDist_ticks = CM_TO_TICKS(deltaDist);
        drive_goto(deltaDist_ticks, deltaDist_ticks);
        break;
    }

    double deltaSpeed = PID(deltaIR, Kp, Ki, Kd, waitingTime_ms, &previous_deltaIR, &previous_integral);
    SetDriveSpeed(StandardSpeed + deltaSpeed, StandardSpeed - deltaSpeed);
    
    pause(waitingTime_ms); 
  }
  
  Write();
  
  return 0;
}