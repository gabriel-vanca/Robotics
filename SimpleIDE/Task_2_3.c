#include "simpletools.h"
#include "abdrive.h"
#include <stdio.h>
#include <stdlib.h>

#define PaperWidth_cm (14.8)
#define SpaceBetweenPapers_cm (0)
#define CM_TO_TICKS(v) (v * 3.07)

int N;
int *List;
int CurrentPosition = PaperWidth_cm / 2;

char FacingRight = 1;

void Swap(int i, int j)
{
	int aux = List[i];
	List[i] = List[j];
	List[j] = aux;
}

void GetInput()
{
 	N = 5;
	List = (int*) malloc(N * sizeof(int));
 	List[0] = 100;
 	List[1] = 33;
 	List[2] = 11;
 	List[3] = 1;
 	List[4] = 40;
 }

void GoTo(float Destination)
{
  float refDest = (Destination + 0.5) * PaperWidth_cm;
	float length_cm = refDest - CurrentPosition;
   CurrentPosition = refDest;
	float length_ticks = CM_TO_TICKS(length_cm);
	if((FacingRight && length_ticks < 0) || (!FacingRight && length_ticks > 0))
	{
		drive_goto(50, -50);      //Rotate 180 degrees
     FacingRight = 1 - FacingRight;
	}
 
   if(length_ticks < 0)
    length_ticks = -length_ticks;

	drive_goto(length_ticks, length_ticks);
	high(26);
	pause(1000);
	low(26); 
}

void BubbleSort()
{
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N - 1; j++)
			if (List[j] > List[j + 1])
			{
				Swap(j, j + 1);
          GoTo(j + 0.5);
			}
   
   GoTo(N/2 - 0.5);
   drive_goto(26,-25);
}

int main()
{
	GetInput();
	BubbleSort();
	return 0;
}

