/*
  Test Encoder Connections.c

  Load to ActivityBot EEPROM. PWR to Position 2.
  Gently turn each wheel by hand.  Right wheel
  blinks P27 LED, left wheel blinks P26 LED.
  If not, encoders are not seeing wheel spokes.
  Go to Extras/Troubleshooting section of tutorial.

  http://learn.parallax.com/activitybot/test-encoder-connections
*/

#include "simpletools.h"

/* 14 is default */

#define LE 14

int main()                    
{
  int lastL = input(LE);
  int lastR = input(LE+1);
  
  pause(1000);
 
  print("%04d %04d\n", lastL, lastR);
  
  low(26);
  low(27);

  while(1)
  {
    int l = input(LE);
    int r = input(LE+1);
    set_output(26, l);
    //print("%04d %04d\n", l, r);
    set_output(27, r);
    if ((lastL != l) || (lastR != r))
    {
      lastL = l;
      lastR = r;
      print("%04d %04d\n", lastL, lastR);
      fflush(stdout);
    }      
  }
}

