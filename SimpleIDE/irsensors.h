#include "simpletools.h"                     
#include "abdrive.h"

void IR_InitialiseSensors()
{
    low(26);                                      // D/A0 & D/A1 to 0 V
    low(27);
}

char IR_CheckLeftSensor()
{
    IR_InitialiseSensors();
    freqout(11, 1, 38000);
    return input(10);
}

char IR_CheckRightSensor()
{
    IR_InitialiseSensors();
    freqout(1, 1, 38000);
    return input(2);
}

int IR_GetLeftSensorValue()
{
    IR_InitialiseSensors();
    int irLeft = 0;
    
    for(int dacVal = 0; dacVal < 160; dacVal += 8)  
    {                                               
      dac_ctr(26, 0, dacVal);                       
      freqout(11, 1, 38000);                        
      irLeft += input(10);                          
    }
    
    return irLeft;
}

/*int ledDist(int irOut, int irIn, int led) {
	int dist = 0;
	int i;
	for(i = 0; i < 260; i += 8) {
		dac_ctr(led, 0, i);
		freqout(irOut, 1, 38000 + i * 100);
		dist += input(irIn);
	}
	return dist;
}*/

int IR_GetRightSensorValue()
{
    IR_InitialiseSensors();
    int irRight = 0;
    
    for(int dacVal = 0; dacVal < 160; dacVal += 8)  
    {
        dac_ctr(27, 1, dacVal);                       
        freqout(1, 1, 38000);
        irRight += input(2);                          
    }
    
    return irRight;
}

