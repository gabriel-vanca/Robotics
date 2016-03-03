#include "simpletools.h"
#include "abdrive.h"

int disks[3];
int nrMoves = 0;

void Hanoi(int nrDisks, char source, char destination, char spare)
{ 
  if(nrMoves==0)
     print("Step %d: %c: %d %c: %d %c: %d\n", nrMoves, source, disks[source-'A'], destination, disks[destination-'A'], spare, disks[spare-'A']);
 
  nrMoves++;
  if(nrDisks == 1)
  {
    disks[source - 'A']--;
    disks[destination - 'A']++;
    print("Step %d: %c: %d %c: %d %c: %d\n", nrMoves, source, disks[source-'A'], destination, disks[destination-'A'], spare, disks[spare-'A']);
  }
  else
  {  
    Hanoi(nrDisks-1, source, spare, destination);
    disks[source - 'A']--;
    disks[destination - 'A']++;
     print("Step %d: %c: %d %c: %d %c: %d\n", nrMoves, source, disks[source-'A'], destination, disks[destination-'A'], spare, disks[spare-'A']);
    Hanoi(nrDisks-1, spare, destination, source);
  }
}

int main()
{
  disks[0]=disks[1]=disks[2]=0;
  char source;
  int nrDisks;
  print("Input source peg:\n");
  scanf("%c",&source);
  print("Input initial number of disks:\n");
  scanf("%d", &nrDisks);
  disks[source-'A'] = nrDisks;
  switch(source)
  {
    case 'A': {Hanoi(nrDisks, 'A', 'B', 'C'); break;}
    case 'B': {Hanoi(nrDisks, 'B', 'A', 'C'); break;}
    case 'C': {Hanoi(nrDisks, 'C', 'B', 'A'); break;}
  }    
  
  return 0;  
  
}

