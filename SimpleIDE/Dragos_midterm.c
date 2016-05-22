#include "simpletools.h"
#include "ping.h"
#include "abdrive.h"
#include <math.h>

int irLeft, irRight, distance;
int constant = 2;
int setPoint = 9;
float currentError, p;
float leftSpeed[200], rightSpeed[200];
int size;
int leftSize, rightSize;



int getArraySize(float *array) {
   for(int j = 0; j < 200; j++) {
       if(*(array+j)==0) {
           size = j;
           break;
        }
    }
    return size;
}

void navigateBack() {
  leftSize = getArraySize(leftSpeed);
  for(int x = leftSize; x >= 0; x--) {
     drive_speed(rightSpeed[x], leftSpeed[x]);
     //print("right back : %f, left back %f\n", rightSpeed[x], leftSpeed[x]);
     pause(180);
  }
  drive_goto(0,0);    
}

int main()
{
  low(26);
  low(27);
  int i = 0;

  while(1)
  {
    irLeft = 0;
    irRight = 0;
    
    for(int dacVal = 0; dacVal < 160; dacVal += 8)
    {
      dac_ctr(26, 0, dacVal);
      freqout(11, 1, 38000);
      irLeft += input(10);
      dac_ctr(27, 1, dacVal);
      freqout(1, 1, 38000);
      irRight += input(2);
    }
    
    
    currentError = setPoint - irLeft;
    p = constant * currentError;

    if (p > 40) {
      p = 40;
    }
    if (p < -40) {
      p = -40;
    }
    drive_speed(40 + p, 40 - p);
    leftSpeed[i] = 40 + p;
    rightSpeed[i] = 40 - p;
    pause(120);
    //print("right forward : %f, left forward %f\n", rightSpeed[i], leftSpeed[i]);
    i++;
    
    distance = ping_cm(8); // Get distance in cm from Ping)))

    if(distance < 6) {

      drive_speed(0,0);
      drive_goto(51, -51);
      
      break;
    }
  }
  navigateBack();
}