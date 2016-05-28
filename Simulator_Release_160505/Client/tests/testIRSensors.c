#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "abdrive.h"
#include "simpletext.h"
#include "simpletools.h"
#include "ping.h"

int main(int argc, const char* argv[])
{

  // Drive ahead nice and slow
  drive_speed(64, 64);
  
  while(ping_cm(8) > 8)
    {
      long int lastCNT = CNT;
      
      
      // Read the left and right sensors
      int irLeft = 0;
      for(int dacVal = 0; dacVal < 160; dacVal += 8)  // <- add
        {                                               // <- add
          dac_ctr(26, 0, dacVal);                       // <- add
          freqout(11, 1, 38000);                        // <- add
          irLeft += input(10);
        }

      print("%f\n", (1.0 * (CNT - lastCNT)) / ms);
      

      int irRight = 0;
      for(int dacVal = 0; dacVal < 160; dacVal += 16)  // <- add
        {                                               // <- add
          dac_ctr(27, 1, dacVal);                       // <- add
          freqout(1, 1, 38000);                        // <- add
          irRight += input(2);
        }

      /*

      if (irLeft < irRight)
        {
          drive_speed(32, 64);
        }
      else if (irLeft > irRight)
        {
          drive_speed(64,32);
        }
      else
        {
          drive_speed(64, 64);
        }
      */
      
      printf("%d %d\n", irLeft, irRight);
      pause(100);
    }
  
  // Stop
  drive_speed(0, 0);
  
  return 0;
}
