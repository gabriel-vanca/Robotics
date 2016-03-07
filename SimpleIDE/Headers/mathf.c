#include "mathf.h"

#define Math_PI (3.14159265359)

char IsPositive(float value)
{
    if(value>0)
      return 1;
    return 0;
}

int Maxim(int a, int b)
{
  if(a>b)
    return a;
  return b;
}

int Minim(int a, int b)
{
  if(a<b)
    return a;
  return b;
}

double RadiansToDegrees(double radians)
{
    return radians * 180 / Math_PI;
}    

double DegreesToRadians(double degrees)
{
    return degrees * Math_PI / 180;
}