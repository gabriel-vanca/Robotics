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
int const Acceleration = 10;
float const Kp = 4.8;
float const Ki = 0; //6
float const Kd = 0;  //0.05
float const waitingTime_ms = 50;

double angle_radians = 0;
double distance_X = 0;
double distance_Y = 0;
    int leftWheel_Ticks;
    int rightWheel_Ticks;

  short int vx[1000],vy[1000];
  int x,y;

void Write()
{
  /*  double angle_degrees = RadiansToDegrees(angle_radians);
    sd_mount(22, 23, 24, 25);
    FILE* fp = fopen("robotics.txt", "w");
    char s[50];
    sprintf(s, "%.2f cm \n %.2f degrees", distance_Y, angle_degrees);
    fwrite(s, 1, 50, fp);
    fclose(fp);*/
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

    int index = 0;
  
  while(1)
  {
    index++;
    // First we get the value from the left IR sensor
    float currentIRvalue = (*getAverageLeftSensorValueFunction)();
    // Now we get the delta value between the current IR sensor value and the initial value
    float deltaIR = SetpointIRValue - currentIRvalue;
       
    if(ping_cm(8) <= 7)    // Case 1: The wall turned right 90 degrees
    {
        SetDriveSpeed(0,0);
        Rotate_ZeroRadius(180,1);        
        break;
    }

    double deltaSpeed = PID(deltaIR, Kp, Ki, Kd, waitingTime_ms, &previous_deltaIR, &previous_integral);
    SetDriveSpeed(StandardSpeed + deltaSpeed, StandardSpeed - deltaSpeed);
    
    //print("SetpointIRValue = %.2f   Left = %.2f   Delta = %.2f \n", SetpointIRValue, currentIRvalue, deltaIR);
    //print("LeftSpeed = %d   RightSpeed = %d \n", LeftWheelSpeed, RightWheelSpeed);
    
    drive_getTicks(&leftWheel_Ticks, &rightWheel_Ticks);
    vx[index] = leftWheel_Ticks - x;
    vy[index] = rightWheel_Ticks - y;
    x = leftWheel_Ticks;
    y = rightWheel_Ticks;

    //print("Ticks_left = %d  Ticks_right= %d \n\n", vx[index], vy[index]);
    
    pause(waitingTime_ms); 
  }
  int j;
  for(j=index;j;j--) 
  {
    drive_ramp(vy[j],vx[j]);
    pause(waitingTime_ms);
  }
  
  return 0;
}