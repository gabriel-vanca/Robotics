#include "simpletools.h"
#include "abdrive.h" 

#define METRE (307)

int main()
{
  drive_goto(METRE,METRE);
  
  for(int i=1; i<=4; i++)
  {
    drive_goto(51,0);
    drive_goto(METRE,METRE);
  }  
  
  while(1)
  {
    drive_goto(102,-102); 
  }
}
