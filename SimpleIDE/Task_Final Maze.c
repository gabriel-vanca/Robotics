#include "simpletools.h"
#include "abdrive.h"
#include "ping.h"

#include "Headers/movement.h"
#include "Headers/mathf.h"

#define PIN_PING (8)

#define VAR_SIZE_OF_CELL_CM 40

#define DIR_NORTH (0)
#define DIR_EAST (1)
#define DIR_SOUTH (2)
#define DIR_WEST  (3)
#define DIR_NO_OF_AVAILABLE_DIRS (4)
#define DIR_TO_RIGHT(a)    (((a)+1)%4)
#define DIR_TO_OPPOSITE(a) (((a)+2)%4)
#define DIR_TO_LEFT(a)     (((a)+3)%4)

#define PATH_UNKNOWN (0)
#define PATH_FREE    (1)
#define PATH_BLOCKED (-1)

#define Map(a) Map[(a).line][(a).column]
#define Tremaux(a) Tremaux[(a).line][(a).column]

int const SizeOfCell_Ticks = CM_TO_TICKS(VAR_SIZE_OF_CELL_CM);

typedef struct CoordinatesStruct
{
    char line;
    char column;
} CoordinatesStruct;

typedef struct PathStruct
{
    char stepsForward;
    char direction;
} PathStruct;

char **Tremaux;   // 5 lines, 4 columns
char ***Map;      // 5 lines, 4 columns, 4 directions + 1 no. of available directions
PathStruct *ShortestPath;

int ShortestPathCounter;
char CurrentDirection;
CoordinatesStruct CurrentPosition;

void inline UpdatePosition(int l, int c)
{
    CurrentPosition.line = l;
    CurrentPosition.column = c;
}

void inline RotateAndSetDirection(int targetDirection)
{
    int deltaDir = (targetDirection - CurrentDirection)%4;
    if(deltaDir < 3)
    {
        //we turn right
        Rotate_ZeroRadiusTurn(DegreesToRadians(deltaDir * 90),0);
    }
    else
    {   //we turn left
        Rotate_ZeroRadiusTurn(DegreesToRadians(90),1);
    }
    
    CurrentDirection = targetDirection;
}

CoordinatesStruct GetNextCellCoordinates(int direction)
{
    CoordinatesStruct coordinates;
    coordinates.line = CurrentPosition.line;
    coordinates.column = CurrentPosition.column;
    
    switch(direction)
    {
        case DIR_NORTH: 
        {
            coordinates.line--;
            break;
        }
        case DIR_SOUTH:
        {
            coordinates.line++;
            break;
        }
        case DIR_WEST:
        {
            coordinates.column--;
            break;
        }
        case DIR_EAST:
        {
            coordinates.column++;
            break;
        }
    }
    
    return coordinates;
}

void inline MoveToNextCell()
{
    CoordinatesStruct nextCell;
    
    switch(Map(CurrentPosition)[DIR_NO_OF_AVAILABLE_DIRS])
    {
        case 1:     // dead end or starting point
        {
            int dir = DIR_TO_OPPOSITE(CurrentDirection);
            if(CurrentPosition.line < 4)
                Tremaux(CurrentPosition)++;
            for(;Map(CurrentPosition)[dir] != PATH_FREE; dir = DIR_TO_RIGHT(dir));
            RotateAndSetDirection(dir);
            nextCell = GetNextCellCoordinates(dir);
            break;         
        }
        case 2:     // new or return path
        {
            int dir = DIR_TO_LEFT(CurrentDirection);
            for(;dir != DIR_TO_OPPOSITE(CurrentDirection); dir = DIR_TO_RIGHT(dir))
            {
                if(Map(CurrentPosition)[dir] != PATH_FREE)
                    continue;
                nextCell = GetNextCellCoordinates(dir);
                    break;
            }
        }
        
        case 3:
        case 4:
        {
            int dir = DIR_TO_LEFT(CurrentDirection);
            if(Tremaux(CurrentPosition) == 0 || Tremaux(GetNextCellCoordinates(DIR_TO_OPPOSITE(CurrentDirection))) != 0)   
            // new junction or old junction with old path. We will search for a new path
            {        
                for(;dir != DIR_TO_OPPOSITE(CurrentDirection); dir = DIR_TO_RIGHT(dir))
                {
                    if(Map(CurrentPosition)[dir] != PATH_FREE)
                        continue;
                    nextCell = GetNextCellCoordinates(dir);
                    if(Tremaux(nextCell) == 0)
                    break;
                }
                
                if(Tremaux(nextCell) == 2 && Map(nextCell)[DIR_NO_OF_AVAILABLE_DIRS] == 2)
                {
                    nextCell = GetNextCellCoordinates(DIR_TO_OPPOSITE(CurrentDirection));
                }
            }
            else    // old junction with new path
            {
                nextCell = GetNextCellCoordinates(DIR_TO_OPPOSITE(CurrentDirection));
            }
            break;
        }
    }
    
    drive_goto(SizeOfCell_Ticks, SizeOfCell_Ticks);
    UpdatePosition(nextCell.line, nextCell.column);
}

int GetPathStatus()        // we check if there is a wall in front of the robot
{
    if(ping_cm(PIN_PING) >= (VAR_SIZE_OF_CELL_CM - 10))
        return PATH_FREE;
    return PATH_BLOCKED;
}

int IsCellValid(CoordinatesStruct coordinates)
{
    return (0 <= coordinates.line && coordinates.line < 4 && 0 <= coordinates.column && coordinates.column < 4) || (coordinates.line == 4 && coordinates.column == 0);
}

void MapCell()
{    
    CoordinatesStruct nextCell;
    
    //We check in front of the robot
    if(Map(CurrentPosition)[CurrentDirection] == PATH_UNKNOWN)
    {
        nextCell = GetNextCellCoordinates(CurrentDirection);
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
        nextCell = GetNextCellCoordinates(tempDirection);
        if(IsCellValid(nextCell))
        {
            //We turn right
            RotateAndSetDirection(tempDirection);
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
        nextCell = GetNextCellCoordinates(tempDirection);
        if(IsCellValid(nextCell))
        {
            //We turn left 
            RotateAndSetDirection(tempDirection);     //We turn 90 or 180 degrees, depending on what we need
            Map(CurrentPosition)[CurrentDirection] = Map(nextCell)[DIR_TO_OPPOSITE(CurrentDirection)] = GetPathStatus();
        }
        else
        {
            Map(CurrentPosition)[tempDirection] = PATH_BLOCKED;
        }  
    }
    
    int i;
    for(i=0; i<4; i++)
        if(Map(CurrentPosition)[i] == PATH_FREE)
            Map(CurrentPosition)[DIR_NO_OF_AVAILABLE_DIRS] ++ ;    
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

void InitialiseMapPhase()
{
    int i,j;
        
    Tremaux = (char**) calloc(5, sizeof(char*));
    for(i=0; i<5; i++)
        Tremaux[i] = (char*) calloc(4, sizeof(char));
        
    Map = (char***) calloc(5, sizeof(char**));
    for(i=0;i<5;i++)
    {
        Map[i] = (char**) calloc(4, sizeof(char*));
        
        for(j=0;j<4;j++)
            Map[i][j] = (char*) calloc(5, sizeof(char));
    }
        
    UpdatePosition(4,0);
    CurrentDirection = DIR_NORTH;
    Map(CurrentPosition)[DIR_SOUTH] = Map(CurrentPosition)[DIR_EAST] = Map(CurrentPosition)[DIR_WEST] = PATH_BLOCKED;
    Map(CurrentPosition)[DIR_NORTH] = PATH_FREE;
    Map(CurrentPosition)[DIR_NO_OF_AVAILABLE_DIRS] = 1;
    Tremaux[4][0] = 1;
}

void FreeTremaux()
{
    int i;
    for(i=0; i<5; i++)
        free(Tremaux[i]);
    free(Tremaux);
}

void FreeMap()
{
    int i,j;
    for(i=0;i<5;i++)
    {      
        for(j=0;j<4;j++)
            free(Map[i][j]);
        free(Map[i]);
    }
    free(Map);
}

void ComputeShortestPath_Lee()
{
    int i;        
    char **Lee;       // 5 lines, 4 columns
    Lee = (char**) calloc(5, sizeof(char*));
    for(i=0; i<5; i++)
        Lee[i] = (char*) calloc(4, sizeof(char));
           
    CoordinatesStruct newCell;
    CoordinatesStruct* Queue = (CoordinatesStruct*) calloc(25, sizeof(CoordinatesStruct));
    CoordinatesStruct Previous[5][4];
    char DirectionIn[5][4];
    int dir;
    int QueueK = 1;
    Queue[0] = CurrentPosition;
    Lee[CurrentPosition.line][CurrentPosition.column] = 1;
    
    while(QueueK != 0)
    {
        for(dir=0; dir<4; dir++)
        {
            newCell = GetNextCellCoordinates(dir);
            if(!IsCellValid(newCell))
                continue;
            if(Map(CurrentPosition)[dir] != PATH_FREE)
                continue;
            if(Lee[newCell.line][newCell.column] != 0)
                continue;
            
            Lee[newCell.line][newCell.column] = Lee[CurrentPosition.line][CurrentPosition.column] + 1;
            Previous[newCell.line][newCell.column].line = CurrentPosition.line;
            Previous[newCell.line][newCell.column].column = CurrentPosition.column;
            DirectionIn[newCell.line][newCell.column] = dir;
            QueueK++;
            Queue[QueueK] = newCell;
        }
    }
    
    QueueK = Lee[0][3];
    
    FreeMap();
    
    for(i=0; i<5; i++)
        free(Lee[i]);
    free(Lee);
    
    free(Queue);
    
    ShortestPath = (PathStruct*) calloc(QueueK + 1, sizeof(PathStruct));
    ShortestPathCounter = 0;
    CoordinatesStruct currentCell;
    currentCell.line = 0;
    currentCell.column = 3;
    Previous[4][0].line = Previous[4][0].column = -1;
    while(Previous[newCell.line][newCell.column].line != -1)
    {
        char steps = 1;
        newCell = Previous[currentCell.line][currentCell.column];
        
        ShortestPath[ShortestPathCounter].direction = DirectionIn[currentCell.line][currentCell.column];
        while(Previous[newCell.line][newCell.column].line != -1 && DirectionIn[currentCell.line][currentCell.column] == DirectionIn[newCell.line][newCell.column])
        {
            steps++;
            newCell = Previous[newCell.line][newCell.column];
        }
        ShortestPath[ShortestPathCounter].stepsForward = steps;
        ShortestPathCounter++;
    } 
}

void MapPhase()
{
    //We move away from the initial cell and do some basic initialisation
    InitialiseMapPhase();
    MoveToNextCell();
    Map(CurrentPosition)[DIR_TO_OPPOSITE(CurrentDirection)] = PATH_FREE;
        
    while(CurrentPosition.line < 4)
    {   
        MapCell();
        Tremaux(CurrentPosition)++;
        MoveToNextCell();
    }
    
    FreeTremaux();
}

void IntermediaryPhase()
{
    RotateAndSetDirection(DIR_TO_OPPOSITE(CurrentDirection));
    MarkUsingLED(3);
    ComputeShortestPath_Lee();
    pause(500); //Should be one second in total;
}

void TimingPhase()
{
    int i = ShortestPathCounter;
    while(i>=0)
    {
        int aux = ShortestPath[i].stepsForward * SizeOfCell_Ticks;
        drive_goto(aux, aux);
        RotateAndSetDirection(ShortestPath[i].direction);
        ShortestPathCounter--;
    }
}

int main()                                    
{
    MapPhase();
    IntermediaryPhase();
    TimingPhase();
    return 0;
}
