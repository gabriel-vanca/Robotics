/*
  Blank Simple Project.c
  http://learn.parallax.com/propeller-c-tutorials 
*/
//#include "simpletools.h"                      // Include simple tools

int main()                                    // Main function
{
  // Add startup code here.

 
  while(1)
  {
    // Add main loop code here.
    
  }  
}

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
