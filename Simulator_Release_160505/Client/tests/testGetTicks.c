#include "abdrive.h"
#include "simpletext.h"
#include "simpletools.h"
#include "ping.h"

static void showTicks()
{
  int left, right;
  drive_getTicks(&left, &right);
  print("%d %d\n", left, right);
}


int main(int argc, const char* argv[])
{
  showTicks();
  
  drive_goto(10, 10);
  showTicks();

  drive_goto(-10, -10);
  showTicks();
}
