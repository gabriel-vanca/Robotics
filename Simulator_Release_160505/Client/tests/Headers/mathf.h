#ifndef MATHF_HEADER_GUARD
#define MATHF_HEADER_GUARD

#define Math_PI (3.14159265359)

char IsPositive(float value)
{
    if(value>0)
      return 1;
    return 0;
}

int Maxim(double a, double b)
{
  if(a>b)
    return a;
  return b;
}

int Minim(double a, double b)
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

int Round(double number)
{
    return (number >= 0) ? (int)(number + 0.5) : (int)(number - 0.5);
}

#endif
