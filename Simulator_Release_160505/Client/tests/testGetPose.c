#include "simulator.h"
#include "simpletools.h"
#include "abdrive.h"
#include "ping.h"

int main(int argc, const char* argv[])
{
  drive_speed(64, 64);

  double x, y, theta;
  int left, right;
  
  while(ping_cm(8) > 5)
    {
      simulator_getPose(&x, &y, &theta);
      drive_getTicks(&left, &right);
      printf("%d %d %f %f %f\n", left, right, x, y, theta);
      pause(200);
    }
}

