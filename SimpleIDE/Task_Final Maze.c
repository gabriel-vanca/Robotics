#include "simpletools.h"
#include "abdrive.h"
#include "ping.h"

//#include "Headers/movement.h"
#include "Headers/mathf.h"

#define PIN_PING (8)

#define DIR_FRONT (0)
#define DIR_RIGHT (1)
#define DIR_BACK  (2)
#define DIR_LEFT  (3)
#define DIR_TO_RIGHT(a)    (((a)+1)%4)
#define DIR_TO_OPPOSITE(a) (((a)+2)%4)
#define DIR_TO_LEFT(a)     (((a)+3)%4)

#define PATH_UNKNOWN  (0)
#define PATH_FREE     (1)
#define PATH_BLOCKED (-1)

#define Map(a) Map[(a).line][(a).column]


int const SizeOfCell_CM = 40;   // ussualy 40 cm or 60 cm
int const SizeOfCell_Ticks = CM_TO_TICKS(SizeOfCell_CM);

char Map[4][4][4];      // 4 lines, 4 columns, 4 directions

typedef struct CoordinatesStruct
{
    int line;
    int column;
} CoordinatesStruct;

CoordinatesStruct CurrentPosition;

int CurrentDirection;

void inline UpdatePosition(int l, int c)
{
    CurrentPosition.line = l;
    CurrentPosition.column = c;
}

void inline MoveToNextCell()
{
    drive_goto(SizeOfCell_Ticks, SizeOfCell_Ticks);
}

int GetPathStatus()        // we check if there is a wall in front of the robot
{
    if(ping_cm(PIN_PING) >= (SizeOfCell_CM - 10))
        return PATH_FREE;
    return PATH_BLOCKED;
}

CoordinatesStruct GetNextCellCoordinates(CoordinatesStruct coordinates, int direction)
{
    switch(direction)
    {
        case DIR_FRONT 
        {
            coordinates.line--;
            break;
        }
        case DIR_BACK
        {
            coordinates.line++;
            break;
        }
        case DIR_LEFT
        {
            coordinates.column--;
            break;
        }
        case DIR_RIGHT
        {
            coordinates.column++;
            break;
        }
    }
    
    return coordinates;
}

int IsCellValid(CoordinatesStruct coordinates)
{
    return 0 <= coordinates.line && coordinates.line < 4 && 0 <= coordinates.column && coordinates.column < 4;
}

void MapCell()
{    
    CoordinatesStruct nextCell;
    
    //We check in front of the robot
    if(Map(CurrentPosition)[CurrentDirection] == PATH_UNKNOWN)
    {
        nextCell = GetNextCellCoordinates(CurrentPosition, CurrentDirection);
        if(IsCellValid(nextCell))
        {
            Map(CurrentPosition)[CurrentDirection] = Map(nextCell)[DIR_TO_OPPOSITE(CurrentDirection)] = GetPathStatus();
        }
        else
        {
            Map(CurrentPosition)[CurrentDirection] = PATH_BLOCKED;
        }
    }
    
    int tempDirection;
    int rotatedToTheRight = 0;      // 0 = false, 1 = true
    
    //We check right side
    tempDirection = DIR_TO_RIGHT(CurrentDirection);
     
    if(Map(CurrentPosition)[tempDirection] == PATH_UNKNOWN)
    {
        nextCell = GetNextCellCoordinates(CurrentPosition, tempDirection);
        if(IsCellValid(nextCell))
        {
            //We turn right
            CurrentDirection = tempDirection;
            Rotate_ZeroRadiusTurn(DegreesToRadians(90),0);
            Map(CurrentPosition)[CurrentDirection] = Map(nextCell)[DIR_TO_OPPOSITE(CurrentDirection)] = GetPathStatus();
            rotatedToTheRight = 1;
        }
        else
        {
            Map(CurrentPosition)[tempDirection] = PATH_BLOCKED;
        }  
    }
    
    //We check left side
    if(rotatedToTheRight == 1)
        tempDirection = DIR_TO_OPPOSITE(CurrentDirection);
    else
        tempDirection = DIR_TO_LEFT(CurrentDirection);
     
    if(Map(CurrentPosition)[tempDirection] == PATH_UNKNOWN)
    {
        nextCell = GetNextCellCoordinates(CurrentPosition, tempDirection);
        if(IsCellValid(nextCell))
        {
            //We turn left
            CurrentDirection = tempDirection;
            Rotate_ZeroRadiusTurn(DegreesToRadians(90 * (1 + rotatedToTheRight)),1);   //We turn 90 or 180 degrees, depending on what we need
            Map(CurrentPosition)[CurrentDirection] = Map(nextCell)[DIR_TO_OPPOSITE(CurrentDirection)] = GetPathStatus();
        }
        else
        {
            Map(CurrentPosition)[tempDirection] = PATH_BLOCKED;
        }  
    }
}

void MapPhase()
{
    //We move away from the initial cell and do some basic initialisation
    CurrentDirection = DIR_FRONT;
    MoveToNextCell();
    UpdatePosition(3,0);
    Map(CurrentPosition)[DIR_BACK] = PATH_BLOCKED;
    MapCell();
    
    int 
}

int main()                                    
{
  
}
