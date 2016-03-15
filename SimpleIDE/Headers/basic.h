#ifndef BASIC_HEADER_GUARD
#define BASIC_HEADER_GUARD

#include "simpletools.h"

void MarkUsingLED(int howManyTimes)
{
    while(howManyTimes--)
    {
        high(26);       // Not Working anymore
	    pause(1000);
	    low(26);
        
       if(howManyTimes)
           pause(600);
    }
}

#endif