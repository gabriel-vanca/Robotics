#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "abdrive.h"
#include "simpletext.h"
#include "simpletools.h"                      // simpletools library
#include "ping.h"

int main(int argc, const char* argv[])
{
  // Blink the lights
  for (int i = 0; i < 100; i+= 10)
  {
    high(26);
    high(27);
    pause((100-i)*10);
    low(26);
    low(27);
    pause((100-i)*10);
  }

  // Spin through 360 degrees
  drive_goto(102, -102);
 
  int left, right;

  drive_speed(64, 64);

  while(1)
    {
      int distance = ping_cm(8); 
      
      while (distance > 10)
        {
          printf("distance=%d\n", distance);
          drive_getTicks(&left, &right);
          printf("left=%d right=%d\n", left, right);
          pause(20);
          distance = ping_cm(8);
        }
      drive_goto(26, -27);
      drive_speed(64, 64);
    }
  return 0;
  
}
