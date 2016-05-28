#ifndef IRSENSORS_HEADER_GUARD
#define IRSENSORS_HEADER_GUARD

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

int IR_GetSensorValue(int PIN_OUT, int PIN_IN, int PIN_IO, int CHANNEL)
{
    low(PIN_OUT);
    int value = 0;
    
    for(int dacVal = 0; dacVal <= 160; dacVal += 8)  
    {                                               
      dac_ctr(PIN_OUT, CHANNEL, dacVal);                       
      freqout(PIN_IO, 1, 38000);                        
      value += input(PIN_IN);                          
    }
    
    return value;
}

int IR_GetLeftSensorValue()
{   
    return IR_GetSensorValue(26, 10, 11, 0);
}

int IR_GetRightSensorValue()
{
    return IR_GetSensorValue(27, 2, 1, 1);
}

float IR_GetAverageSensorValue(int numberOfIterations, int (*getAverageSensorValueFunction)(), int waitingTime_ms)
{
  int currentIRvalue = 0;
  for(int i=1; i<= numberOfIterations; i++)
  {
    currentIRvalue += (*getAverageSensorValueFunction)();
    pause(waitingTime_ms);
  }
  
  return currentIRvalue * 1.0f / numberOfIterations;
}

#endif
