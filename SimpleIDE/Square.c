#include "simpletools.h"
#include "abdrive.h" 

#define METER (307)

int main()
{
  for(int i=1; i<=4; i++)
  {
    drive_goto(51,0);
    drive_goto(METER,METER);
  }  
}