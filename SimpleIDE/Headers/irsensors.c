#include "simpletools.h"
#include "irsensors.h"

char IR_CheckLeftSensor()
{
    low(26);
    freqout(11, 1, 38000);
    return input(10);
}

char IR_CheckRightSensor()
{
    low(27);
    freqout(1, 1, 38000);
    return input(2);
}

int IR_GetLeftSensorValue()
{
    low(26);
    int irLeft = 0;
    
    for(int dacVal = 0; dacVal < 160; dacVal += 8)  
    {                                               
      dac_ctr(26, 0, dacVal);                       
      freqout(11, 1, 38000);                        
      irLeft += input(10);                          
    }
    
    return irLeft;
}

int IR_GetRightSensorValue()
{
    low(27);
    int irRight = 0;
    
    for(int dacVal = 0; dacVal < 160; dacVal += 8)  
    {
        dac_ctr(27, 1, dacVal);                       
        freqout(1, 1, 38000);
        irRight += input(2);                          
    }
    
    return irRight;
}

float IR_GetAverageSensorValue(int numberOfIterations, int (*getAverageSensorValueFunction)())
{
  int currentIRvalue = 0;
  for(int i=1; i<= numberOfIterations; i++)
  {
    currentIRvalue += (*getAverageSensorValueFunction)();
  }
  
  return currentIRvalue * 1.0f / numberOfIterations;
}