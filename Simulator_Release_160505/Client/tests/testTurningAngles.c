#include "simpletools.h"

static void checkDriveGoto(int left, int right)
{
  // Get the initial position and orientation
  double x0, y0, theta0;
  simulator_getPose(&x0, &y0, &theta0);

  // Now turn
  drive_goto(left, right);

  // Get the final position and orientation
  double x1, y1, theta1;
  simulator_getPose(&x1, &y1, &theta1);

  print("%d %d %f %f %f\n", left, right, (x1-x0), (y1-y0), 180.0*(theta1-theta0)/M_PI);
}

static void turnThroughAngle(double angle)
{
  printf("%f ", angle);
  int ticks = (int) round(angle*0.284);
  checkDriveGoto(-ticks, ticks);
}


int main(int argc, const char* argv[])
{
  turnThroughAngle(120);
  turnThroughAngle(90);

  // Override
  printf("%f*",90.0);
  checkDriveGoto(26,-25);

  
  turnThroughAngle(72);
  turnThroughAngle(60);
  turnThroughAngle(45);
  turnThroughAngle(36);
  turnThroughAngle(10);
  turnThroughAngle(-10);
}
