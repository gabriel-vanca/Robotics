/*
  Test IR Detectors.c

  Test IR LEDs and IR receivers used together as object detectors.

  http://learn.parallax.com/activitybot/test-ir-sensor-circuits
*/

#include "simpletools.h"                        // Include simpletools
#include "ping.h"                             // Include ping header

int irLeft, irRight;                            // Global variables
int distance;                                 // Declare distance variable

int main()                                      // Main function
{
  low(26);                                      // D/A0 & D/A1 to 0 V
  low(27);

  while(1)                                      // Main loop
  {
    freqout(11, 1, 38000);                      // Left IR LED light
    irLeft = input(10);                         // Check left IR detector

    freqout(1, 1, 38000);                       // Repeat for right detector
    irRight = input(2);

    distance = ping_cm(8);                    // Get cm distance from Ping)))

    print("%c irLeft = %d, irRight = %d,  distance = %d cm    ",       // Display detector states
           HOME,       irLeft,       irRight, distance);

    pause(100);                                 // Pause before repeating
  }
}

