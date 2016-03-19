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

clock_t time_begin, time_end;                              // Our code is time-based so we use clock() to track the time it takes for different functions to execute

typedef struct SpeedChangeStruct
{
   short int deltaSpeed;                // The variation from the StandardSpeed
   unsigned short int deltaTime;        // The time in ms for which the time remained constant
   struct SpeedChangeStruct *next;
} SpeedChangeStruct;

SpeedChangeStruct *headStack = NULL;

void SetDriveSpeed(int left, int right)
{
  LeftWheelSpeed = left;
  RightWheelSpeed = right;
  drive_ramp(LeftWheelSpeed, RightWheelSpeed);          //TODO:: Stil have to decide if drive_ramp or drive_speed is the best choice
}

void Push(short int deltaSpeed, unsigned short int deltaTime)
{
    struct SpeedChangeStruct* newChange = (SpeedChangeStruct*) malloc(sizeof(SpeedChangeStruct));
    newChange -> deltaSpeed = deltaSpeed;
    newChange -> deltaTime = deltaTime;
    newChange -> next = headStack;
    headStack = newChange;
}

double GetTimeInMiliseconds()
{
    time_end = clock();
    double deltaTime = (double)(time_end- time_begin) / CLOCKS_PER_SEC * 1000;
    time_begin = clock();
    return deltaTime;
}  

void GoForward()
{
    float const Kp = 4.8;               //TODO:: Is this the best choice?
    float const Ki = 0;                 //TODO:: Can we make it work?
    float const Kd = 0;                 //TODO:: Can we make it work?
    float const AngleChangeLimit = DegreesToRadians(7);         // The maximum angle change allowed. A value bigger (in module) than thsi will generate a new element in the stack
    int (*getLeftSensorValueFunction)() = &IR_GetLeftSensorValue;
    float const SetpointIRValue = IR_GetAverageSensorValue(3, getLeftSensorValueFunction, WaitingTime_ms);
    
    double previous_deltaIR = 0;
    double previous_integral = 0;    
    int totalLeftWheel_Ticks = 0;
    int totalRightWheel_Ticks = 0;
    double current_angleChange_radians = Math_PI / 2;     // Angle change from last cycle. It is initialised with 90 degrees as that's the angle at which the robot is being positioned in the time_beginning.
    double total_angleChange_radians = 0;                 // Tracks angle change over time 
    double distance_X = 0;                                // Distance robot moved in the last cycle
    double distance_Y = 0;
    double deltaSpeed;                                    // The difference between the StandardSpeed and the speed of each wheel (+ for left, - for right)
    float currentIRvalue;                                 // The current value of the left IR sensor
    float deltaIR;                                        // The differece between the set value and the current value for the left IR sensor
    
    SetDriveSpeed(StandardSpeed, StandardSpeed);
    time_begin = clock();
    int cyclesCounter = 0;                                // Counts cycles for which the speed remained constant. Useful for the ponderate average.
    Push(0,0);                                            // Initial value pushed in the stack
    
    while(1)
    {
        // First we get the value from the IR sensor
        float currentIRvalue = (*getLeftSensorValueFunction)();
        // Now we get the delta value between the current IR sensor value and the initial value
        float deltaIR = SetpointIRValue - currentIRvalue;
       
        if(ping_cm(8) <= 10)        // We got to the wall. Break current loop
        {
            SetDriveSpeed(0,0);
            (headStack -> deltaTime) += Round(GetTimeInMiliseconds());
            break;
        }

        /* Now we send the values to the PID and adjust speed on each wheel accordingly. 
           Before seting the new speed, we update the time the robot went with the current speeds.
           This results in a change in the angle in which the robot is going. */
        double deltaSpeed = PID(deltaIR, Kp, Ki, Kd, (float) WaitingTime_ms, &previous_deltaIR, &previous_integral);
        (headStack -> deltaTime) += Round(GetTimeInMiliseconds());
        SetDriveSpeed(StandardSpeed + deltaSpeed, StandardSpeed - deltaSpeed);
        
        // Next we use Dead Reckoning to figure out if the robot is maintaining a certain course or if it is at a turn.
        Track_Movement(&current_angleChange_radians, &distance_X, &distance_Y, &totalLeftWheel_Ticks, &totalRightWheel_Ticks);
        total_angleChange_radians += current_angleChange_radians;
        
        if(-AngleChangeLimit < total_angleChange_radians && total_angleChange_radians < AngleChangeLimit)   // Small changes due to walls not being straight or IRdetection faults or PID faults
        {
            /* We update the previous deltaSpeed using a ponderate average so that we don't loose any speed values.
               We increase by one the number of cycles for which the angle remained almost unchanged. */    
            headStack -> deltaSpeed = (short int) Round(((headStack -> deltaSpeed) * cyclesCounter + deltaSpeed) / (cyclesCounter + 1));
            (headStack -> deltaTime) += Round(GetTimeInMiliseconds());
            cyclesCounter ++;
        }
        else        // A sharp angle change was detected. The robot is at a turn.
        {
            Push(deltaSpeed, Round(GetTimeInMiliseconds()));          // We are making sure the timing is right. Time precission is very important.
            total_angleChange_radians = 0;
            cyclesCounter = 0;
        }
        
        pause(WaitingTime_ms); 
    }
}

void GetBack()
{
  for(SpeedChangeStruct* p = headStack; p != NULL ; p = p -> next) 
  {
      /* We are parsing the stack and we add the deltaSpeeds optained when the robot went forward to the same StandardSpeed.
         After that, we are pausing the program accordingly with the number of cycles for which the angle has remained pretty mcuh constant.
         It is worth nothing that we are reversing the speeds of the wheels then going back. */
       
      SetDriveSpeed(StandardSpeed - (p->deltaSpeed), StandardSpeed + (p->deltaSpeed));
      pause(p->deltaTime);          //TODO: Maybe slowly decrease pause by 20 ms to account for lateness in sensor detection, speed change and acceleration
  }
  SetDriveSpeed(0,0);
  drive_close(); 
}

int main()
{  
  drive_setRampStep(Acceleration);
  GoForward();
  Rotate_ZeroRadiusTurn(Math_PI, 0);
  GetBack();
  
  return 0;
}