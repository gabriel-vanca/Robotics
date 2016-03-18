#include "simpletools.h"
#include "abdrive.h"
#include "ping.h"

#include "Headers/movement.h"
#include "Headers/irsensors.h"
#include "Headers/mathf.h"

int const StandardSpeed = 70;       //TODO:: Can it be increased?
int const Acceleration = 10;        //TODO:: Still have to decide if this is the best value. Maybe go with 12.
int const WaitingTime_ms = 40;      //TODO:: Should it be lower?
int LeftWheelSpeed;
int RightWheelSpeed;

typedef struct SpeedChangeStruct
{
   short int deltaSpeed;                // The variation from the StandardSpeed
   unsigned short int cyclesCount;      // The number of check cycles for which the time remained constant. 
                                        // The time in ms optained by multypling with WaitingTime_ms
   struct SpeedChangeStruct *next;
} SpeedChangeStruct;

SpeedChangeStruct *headList;

void SetDriveSpeed(int left, int right)
{
  LeftWheelSpeed = left;
  RightWheelSpeed = right;
  //drive_speed(LeftWheelSpeed, RightWheelSpeed);
  drive_ramp(LeftWheelSpeed, RightWheelSpeed);          //TODO:: Stil have to decide if we should use drive_ramp or drive_speed
}

void Push(short int deltaSpeed, unsigned short int cyclesCount)
{
    struct SpeedChangeStruct* newChange = (struct SpeedChangeStruct*) malloc(sizeof(struct SpeedChangeStruct));
    newChange -> deltaSpeed = deltaSpeed;
    newChange -> cyclesCount = cyclesCount;
    newChange -> next = headList;
    headList = newChange;
}  

void GoForward()
{
    float const Kp = 4.8;               //TODO:: Is this the best choice?
    float const Ki = 0;                 //TODO:: Can we make it work?
    float const Kd = 0;                 //TODO:: Can we make it work?
    float const AngleChangeLimit = DegreesToRadians(12);
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
    Push(0,0);

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
               We increase by one the number of cycles for which the angle remained almost unchanged. */
               
            headList -> deltaSpeed = (short int) Round(((headList -> deltaSpeed) * (headList -> cyclesCount) + deltaSpeed) / ((headList -> cyclesCount) + 1));
            (headList -> cyclesCount) ++;
        }
        else    // A sharp angle change was detected. The robot is at a turn.
        {
            Push(deltaSpeed, 1);
            total_angleChange_radians = 0;
        }
        
          //print("SetpointIRValue = %.2f   Left = %.2f   Delta = %.2f \n", SetpointIRValue, currentIRvalue, deltaIR);
        //  print("LeftSpeed = %d   RightSpeed = %d \n", LeftWheelSpeed, RightWheelSpeed);

    //    print("delta = %f    registered delta = %d time = %d \n", deltaSpeed, SpeedChange_DeltaSpeed[SpeedChange_Length], SpeedChange_Cycles[SpeedChange_Length]);
          
        pause(WaitingTime_ms); 
    }
}

void GetBack()
{
  for(struct SpeedChangeStruct* p = headList; p; p = p -> next) 
  {
      /* We are parsing the array and we add the deltaSpeeds optained when the robot went forward to the same StandardSpeed.
         After that, we are pausing the program accordingly with the number of cycles for which the angle has remained pretty mcuh constant.
         It is worth nothing that we are reversing the speeds of the wheels then going back. */
       
      SetDriveSpeed(StandardSpeed - (p->deltaSpeed), StandardSpeed + (p->deltaSpeed));
      
      pause(WaitingTime_ms * (p-> cyclesCount));  //TODO:: Maybe slowly decrease pause by 20 ms to account for lateness in sensor detection, speed change and acceleration
  }
  SetDriveSpeed(0,0); 
}

int main()
{  
  drive_setRampStep(Acceleration);
  GoForward();
  Rotate_ZeroRadiusTurn(Math_PI, 0);
  GetBack();
  
  return 0;
}