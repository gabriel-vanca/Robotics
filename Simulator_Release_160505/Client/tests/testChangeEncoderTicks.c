#include "simpletools.h"
#include "simulator.h"
#include "abdrive.h"


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

static void turnThroughAngle(double angle, double encoderMultiplier)
{
  printf("%f ", angle);
  int ticks = (int) round(angle*(encoderMultiplier*0.284));
  checkDriveGoto(-ticks, ticks);
}


static void runTurnTests(double encoderMultiplier)
{
  // First get the encoder ticks and show them
  int left, right;

  simulator_getEncoderTicksPerRevolution(&left, &right);

  print("Original encoder ticks per revolution = %d, %d\n", left, right);

  simulator_setEncoderTicksPerRevolution(left * encoderMultiplier, right * encoderMultiplier);

  // The maximum speed and ramp size also need to be scaled
  drive_setMaxSpeed(128 * encoderMultiplier);
  drive_setRampStep(4 * encoderMultiplier);
  
  int newLeft, newRight;
  
  simulator_getEncoderTicksPerRevolution(&newLeft, &newRight);
  print("New encoder ticks per revolution = %d, %d\n", newLeft, newRight);

  turnThroughAngle(120, encoderMultiplier);
  turnThroughAngle(90, encoderMultiplier);
  turnThroughAngle(72, encoderMultiplier);
  turnThroughAngle(60, encoderMultiplier);
  turnThroughAngle(45, encoderMultiplier);
  turnThroughAngle(36, encoderMultiplier);
  turnThroughAngle(10, encoderMultiplier);
  turnThroughAngle(-10, encoderMultiplier);

  // Restore original values
  simulator_setEncoderTicksPerRevolution(left, right);
  drive_setMaxSpeed(128);
  drive_setRampStep(4);
}

int main(void)
{
  // Run with the original encoder settings
  runTurnTests(1);

  // Now run with apparently 10 times as many ticks / revolution
  runTurnTests(10);

  // Now run with apparently 100 times as many ticks / revolution
  runTurnTests(100);

  // Finally go back to the original
  runTurnTests(1);
}
