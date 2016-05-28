#include <sys/time.h>
#include "simpletools.h"

int main(void)
{
  struct timeval tv;
  pause(250);
  int ret = gettimeofday(&tv, NULL);
  printi("%d\n", ret);
  printi("%d %d\n", tv.tv_sec, tv.tv_usec);
  
  tv.tv_usec = 0;
  printi("CNT=%d\n", CNT);
  ret = settimeofday(&tv, NULL);
  pause(250);
  printi("ret=%d; CNT=%d\n", ret, CNT);
  ret = gettimeofday(&tv, NULL);
  printi("%d %d\n", tv.tv_sec, tv.tv_usec);
  
  return 0;
}
