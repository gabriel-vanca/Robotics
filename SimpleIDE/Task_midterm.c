#include "simpletools.h"
#include "abdrive.h"
#include "ping.h"

#include "Headers/movement.h"
#include "Headers/irsensors.h"
#include "Headers/mathf.h"

int LeftWheelSpeed;
int RightWheelSpeed;

int const StandardSpeed = 70;       //TODO:: Can it be increased
int const Acceleration = 10;        //TODO:: Still have to decide if this is the best value
float const Kp = 4.8;
float const Ki = 0;                 //TODO:: Can we make it work?
float const Kd = 0;                 //TODO:: Can we make it work?
float const WaitingTime_ms = 50;

double angle_radians = 0;
double distance_X = 0;
double distance_Y = 0;
int leftWheel_Ticks;
int rightWheel_Ticks;

int Position_Length;
short int Position_X[1000],Position_Y[1000];   //TODO :: Can we use CHAR?

/*void Write()
{
    double angle_degrees = RadiansToDegrees(angle_radians);
    sd_mount(22, 23, 24, 25);
    FILE* fp = fopen("robotics.txt", "w");
    char s[50];
    sprintf(s, "%.2f cm \n %.2f degrees", distance_Y, angle_degrees);
    fwrite(s, 1, 50, fp);
    fclose(fp);
}*/

void SetDriveSpeed(int left, int right)
{
  LeftWheelSpeed = left;
  RightWheelSpeed = right;
  //drive_speed(LeftWheelSpeed, RightWheelSpeed);
  drive_ramp(LeftWheelSpeed, RightWheelSpeed);          //TODO:: Stil have to decide if we should use drive_ramp or drive_speed
}  

void GoForward()
{
     int (*getLeftSensorValueFunction)() = &IR_GetLeftSensorValue;
     float const SetpointIRValue = IR_GetAverageSensorValue(3, getLeftSensorValueFunction, WaitingTime_ms);
     double previous_deltaIR = 0;
     double previous_integral = 0;
     SetDriveSpeed(StandardSpeed, StandardSpeed);
     
     int totalLeftWheel_Ticks = 0;
     int totalRightWheel_Ticks = 0;
     
     while(1)
    {
        // First we get the value from the IR sensor
        float currentIRvalue = (*getLeftSensorValueFunction)();
        // Now we get the delta value between the current IR sensor value and the initial value
        float deltaIR = SetpointIRValue - currentIRvalue;
       
        if(ping_cm(8) <= 7)    // We got to the wall. Break current loop
        {
            SetDriveSpeed(0,0);        
            break;
        }

        /* Now we send the values to the PID and adjust speed on each wheel accordingly. 
           This results in a change in the angle in which the robot is going. */
        double deltaSpeed = PID(deltaIR, Kp, Ki, Kd, WaitingTime_ms, &previous_deltaIR, &previous_integral);
        SetDriveSpeed(StandardSpeed + deltaSpeed, StandardSpeed - deltaSpeed);
    
        //Next: some attempt at registering steps that is not working ... at all!
        drive_getTicks(&leftWheel_Ticks, &rightWheel_Ticks);
        Position_X[Position_Length] = leftWheel_Ticks - totalLeftWheel_Ticks;
        Position_Y[Position_Length] = rightWheel_Ticks - totalRightWheel_Ticks;
        totalLeftWheel_Ticks = leftWheel_Ticks;
        totalRightWheel_Ticks = rightWheel_Ticks;

        /*
          print("SetpointIRValue = %.2f   Left = %.2f   Delta = %.2f \n", SetpointIRValue, currentIRvalue, deltaIR);
          print("LeftSpeed = %d   RightSpeed = %d \n", LeftWheelSpeed, RightWheelSpeed);
          print("Ticks_left = %d  Ticks_right= %d \n\n", Position_X[Position_Length], Position_Y[Position_Length]);
        */
    
        pause(WaitingTime_ms); 
     }
}

void GetBack()
{
  int j;
  for(j=Position_Length;j;j--) 
  {
    drive_ramp(Position_Y[j],Position_X[j]);
    pause(WaitingTime_ms);
  }
}

int main()
{  
  drive_setRampStep(Acceleration);
  
  GoForward();
  Rotate_ZeroRadius(180,1);
  GetBack();
  
  return 0;
}