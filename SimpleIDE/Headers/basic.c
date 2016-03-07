#include "simpletools.h"
#include "basic.h"

void MarkUsingLED(int howManyTimes)
{
    while(howManyTimes--)
    {
        high(26);
	    pause(1000);
	    low(26);
        
       if(howManyTimes)
           pause(600);
    }
}