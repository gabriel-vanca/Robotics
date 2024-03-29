/*
  Test IR Detectors.c
*/

#include "simpletools.h"                        // Include simpletools
//#include "abddrive.h"

int irLeft, irRight;                            // Global variables

int main()                                      // main function
{
  low(26);                                      // D/A0 & D/A1 to 0 V
  low(27);

  while(1)                                      // Main loop
  {
    freqout(11, 1, 38000);                      // Left IR LED light
    irLeft = input(10);                         // Check left IR detector

    freqout(1, 1, 38000);                       // Repeat for right detector
    irRight = input(2);

    print("%c irLeft = %d, irRight = %d",       // Display detector states
           HOME,       irLeft,       irRight);
    pause(50);                                 // Pause before repeating
  }
}