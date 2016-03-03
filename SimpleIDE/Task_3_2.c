#include "simpletools.h"
#include "abdrive.h"

#define PegWidth_CM (15)
#define SpaceBetweenPegs_CM (12)

#define CM_TO_TICKS(v) (v * 3.07)

char CurrentPeg;
float Positions[3];
int disks[3];
int nrMoves = 0;

char FacingRight = 1;     //Robot must face right at start

void Initialise()
{
    Positions[0] = 0;                                // Peg 'A' - Starting point
    Positions[1] = PegWidth_CM * 1.5 + SpaceBetweenPegs_CM;         // Peg 'B'
    Positions[2] = PegWidth_CM * 2.5 + SpaceBetweenPegs_CM * 2;     //Peg 'C'
}

float GetPositionForPeg(char peg)
{
    switch(peg)
    {
        case 'A':
        {
            return Positions[0];
        }
        case 'B':
        {
            return Positions[1];
        }
        case 'C':
        {
            return Positions[2];
        }
        default:
        {
            print("Error: Undefined destination. Please use only A, B, C for describing the destination.\n");
            return 0;
        }
    }
}

void GoTo(float currentPosition, float destination)
{
  float length_cm = destination - currentPosition;
  float length_ticks = CM_TO_TICKS(length_cm);
    
	if((FacingRight && length_ticks < 0) || (!FacingRight && length_ticks > 0))
	{
		drive_goto(51, -51);      //Rotate 180 degrees
        FacingRight = 1 - FacingRight;
	}
 		if(length_ticks < 0)
            length_ticks = -length_ticks;

	drive_goto(length_ticks, length_ticks);
}

void MarkUsingLED(int howManyTimes)
{
    while(howManyTimes--)
    {
      high(26);
	    pause(1000);
	    low(26);
       if(howManyTimes)
           pause(600);
    }
}

void MoveDisk(char peg1, char peg2)     //Move a disk from peg1 to peg2
{
    float currentPosition = GetPositionForPeg(CurrentPeg);
    float peg1_position = GetPositionForPeg(peg1);
    float peg2_position = GetPositionForPeg(peg2);
    GoTo(currentPosition, peg1_position);
    MarkUsingLED(1);
    GoTo(peg1_position, peg2_position);
    MarkUsingLED(2);
    CurrentPeg = peg2;
}


void GoToFinalPosition()
{
  float currentPosition = GetPositionForPeg(CurrentPeg);
  float peg_position = GetPositionForPeg('B');
  GoTo(currentPosition, peg_position);
  drive_goto(26,-25);   // turn 90 degrees
  MarkUsingLED(3);
}

void Hanoi(int nrDisks, char source, char destination, char spare)
{ 
  nrMoves++;
  if(nrDisks == 1)
  {
    disks[source - 'A']--;
    disks[destination - 'A']++;
    MoveDisk(source, destination);
  }
  else
  {  
    Hanoi(nrDisks-1, source, spare, destination);
    disks[source - 'A']--;
    disks[destination - 'A']++;
    MoveDisk(source, destination);
    Hanoi(nrDisks-1, spare, destination, source);
  }
}

int main()
{  
  disks[0]=disks[1]=disks[2]=0;
  char source;
  int nrDisks;
  
  source = 'A';
  nrDisks = 3;

  disks[source-'A'] = nrDisks;
  CurrentPeg = source;
  Initialise();
  switch(source)
  {
    case 'A': {Hanoi(nrDisks, 'A', 'B', 'C'); break;}
    case 'B': {Hanoi(nrDisks, 'B', 'A', 'C'); break;}
    case 'C': {Hanoi(nrDisks, 'C', 'B', 'A'); break;}
  }    
  GoToFinalPosition();
  return 0;  
  
}

