
/* 
  Forward Stop Face Right.c

  http://learn.parallax.com/activitybot/go-certain-distances
*/

#include "simpletools.h"
#include "abdrive.h"

#include "simpletools.h"                      // simpletools library
#include "abdrive.h"                          // abdrive library

int main()                    
{
  drive_open();
  drive_goto(0, 0);
  drive_close();
  for (int i = 0; i < 100; i+= 10)
  {
    high(26);
    high(27);
    pause((100-i)*10);
    low(26);
    low(27);
    pause((100-i)*10);
  }    
    printf("0\n");
  drive_goto(100, 100);
  printf("1\n");
  pause(2000);
  printf("2\n");
  drive_goto(-100, 100);
  printf("3\n");
  pause(2000);
  drive_goto(100, -100);
  //drive_goto(0, 0);
  drive_close();
}
