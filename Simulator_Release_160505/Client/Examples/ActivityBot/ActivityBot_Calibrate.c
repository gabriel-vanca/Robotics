/* 
  ActivityBot Calibrate.c

  Calibrate the ActivityBot's servos and encoders

  http://learn.parallax.com/activitybot/calibrate-your-activitybot
*/

#include "simpletools.h"
#include "abcalibrate.h"    
#include "abdrive.h"

int main()
{
  cal_servoPins(12, 13);
  cal_encoderPins(14, 15);

  for (int i = 0; i < 100; i+= 10)
  {
    high(26);
    high(27);
    pause((100-i)*10);
    low(26);
    low(27);
    pause((100-i)*10);
  }    
  
  high(26);
  high(27);
  cal_activityBot();
  low(26);
  low(27);
}
