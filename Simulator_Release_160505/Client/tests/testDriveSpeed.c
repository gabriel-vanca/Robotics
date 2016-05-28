#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "abdrive.h"
#include "simpletext.h"
#include "simpletools.h"
#include "ping.h"

int main(int argc, const char* argv[])
{
  dac_ctr_stop();
  
  // Drive ahead nice and slow
  drive_speed(-1, 1);

  struct timeval tv;
  
  gettimeofday(&tv, NULL);

  long int startSecs = tv.tv_sec;
  
  while(ping_cm(8) > 8)
    {
      pause(100);
      struct timeval now;
      gettimeofday(&now, NULL);
      if (now.tv_sec != tv.tv_sec)
        {
          int left, right;
          drive_getTicks(&left, &right);
          printf("%ld %d %d\n", now.tv_sec-startSecs, left, right);
          tv.tv_sec = now.tv_sec;
        }
    }
}
