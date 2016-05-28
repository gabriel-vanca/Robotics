#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "abdrive.h"
#include "simpletext.h"
#include "simpletools.h"
#include "ping.h"

int DO = 22, CLK = 23, DI = 24, CS = 25;      // SD card pins on Propeller BOE

int main(int argc, const char* argv[])
{
  sd_mount(DO, CLK, DI, CS);                  // Mount SD card

  FILE* fp = fopen("test.txt", "w");          // Open a file for writing

  // Drive ahead nice and slow
  drive_speed(64, 64);
  
  while(ping_cm(8) > 8)
    {
      // Read the left and right sensors
      int irLeft = 0;
      for(int dacVal = 0; dacVal <= 160; dacVal += 8)  // <- add
        {                                               // <- add
          dac_ctr(26, 0, dacVal);                       // <- add
          freqout(11, 1, 38000);                        // <- add
          irLeft += input(10);
        }

      //print("%f\n", (1.0 * (CNT - lastCNT)) / ms);
      

      int irRight = 0;
      for(int dacVal = 0; dacVal <= 160; dacVal += 8)  // <- add
        {                                               // <- add
          dac_ctr(27, 1, dacVal);                       // <- add
          freqout(1, 1, 38000);                        // <- add
          irRight += input(2);
        }

      fprintf(fp, "%d %d\n", irLeft, irRight);
      fflush(fp);
      
      if (irLeft > irRight)
        {
          drive_speed(60, 64);
        }
      else if (irLeft < irRight)
        {
          drive_speed(64,60);
        }
      else
        {
          drive_speed(64, 64);
        }
      
      printf("%d %d\n", irLeft, irRight);
      pause(100);
    }
  
  // Stop
  drive_speed(0, 0);

  fclose(fp);
  
  return 0;
}
