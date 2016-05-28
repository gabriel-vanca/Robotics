#include "simpletools.h"                      // simpletools library
//#include "ping.h"

int main(int argc, const char* argv[])
{
  for (int i = 0; i < 100; ++i)
  {
    //printf("%d\n", i);
    high(26);
    high(27);
    pause(1);
    low(26);
    low(27);
    pause(1);
  }
}
