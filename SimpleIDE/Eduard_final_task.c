/*
  Blank Simple Project.c
  http://learn.parallax.com/propeller-c-tutorials 
*/
#include "simpletools.h"                      // Include simple tools
#include "abdrive.h"
#include "ping.h"
#define FRONT_DISTANCE  25 // CM
#define LENGTH  40 //  Cm
#define LENGTH_TICKS  (int)((float)LENGTH*10/3.25)
#define INF 99999

void move(int x);
void calibrate();



int getleftdistance()
{
    int irLeft = 0;
    for(int Val = 0; Val < 160; Val += 8)
    {
        dac_ctr(26, 0, Val);
        freqout(11, 1, 38000);
        irLeft += input(10);
    }
    return irLeft;
}
int getrightdistance()
{
    int irRight=0;
    for(int dacVal = 0; dacVal < 160; dacVal += 8)
    {
        dac_ctr(27, 1, dacVal);
        freqout(1, 1, 38000);
        irRight += input(2);
    }
    return irRight;
}
int check_front()
{
    if (ping_cm(8)>FRONT_DISTANCE) return 1; else
        return 0;
}

int B[5][5],value[5][5],visited[5][5],queueX[16],queueY[16];

void turnRight()
{
    drive_goto(26,-25);
}

void turnLeft()
{
    drive_goto(-26,25);
}

void turnAround()
{
    drive_goto(51,-51);
}

void move (int x) /*
                     1 for move forward,
                    -1 for move backwards 
                  */
 {  
       if(x>0) for(int count=1; count <=x; count++)
       {
        drive_goto(LENGTH_TICKS,LENGTH_TICKS);
       }
       else for(int count=-1; count >=x; count--)
       {
        drive_goto(-LENGTH_TICKS,-LENGTH_TICKS);
       }
       drive_speed(0,0);
       calibrate();

} 



void goThroughPath(int queueX[16],int queueY[16],int nrElements)
{ 
    int qX,qY; 
    qX=queueX[nrElements];
    qY=queueY[nrElements];
    int direction=3;   //the direction is set to 3 because 3 means South , which is the current direction of the robot when it enters the matrix
    for(int index = nrElements - 1; index >= 0;index--)
    {
       switch(direction)
       { 
         case 1:{ 
                   if(qX==queueX[index]-1 && qY==queueY[index]) 
                        {
                          move(1);
                          qX=queueX[index]-1;
                          direction=1;
                        }
                   else if(qX==queueX[index]+1&& qY==queueY[index])
                        {
                          turnAround();
                          move(1);qX=queueX[index]+ 1; 
                          direction=3; 
                        } 
                   else if(qX==queueX[index]&& qY==queueY[index]-1) 
                        {
                          turnLeft();
                          move(1);
                          qY=queueY[index]-1;
                          direction=4;
                        }
                   else if(qX==queueX[index]&& qY==queueY[index]+1) 
                        {
                          turnRight();
                          move(1);
                          qY=queueY[index]+1;
                          direction=2;
                        }
                          
                   break;
                }                                                                                                
         case 2:{ 
                   if(qX==queueX[index]-1&& qY==queueY[index]) 
                        {
                          turnLeft();
                          move(1);
                          qX=queueX[index]-1;
                          direction=1;          
                        }
                   else if(qX==queueX[index]+1&& qY==queueY[index]) 
                        {
                          turnRight();
                          move(1);
                          qX=queueX[index]+1;
                          direction=3;
                        }
                  else if(qX==queueX[index]&& qY==queueY[index]-1) 
                        {
                          turnAround();
                          move(1);
                          qY=queueY[index]-1;
                          direction=4;
                        }
                  else if(qX==queueX[index]&& qY==queueY[index]+1) 
                        {
                          move(1);                                                                                       // N 1  
                          qY=queueY[index]-1;
                          direction=2;                                                                                    
                        }
                          
                  break;}                                                                                         // W 4         //E 2
         case 3:{ 
                  if(qX==queueX[index]-1&& qY==queueY[index]) 
                        {
                          turnAround();                                                                                  // S 3
                          move(1);
                          qX=queueX[index]-1;
                          direction=1;          
                        }
                  else if(qX==queueX[index]+1&& qY==queueY[index]) 
                        {
                          move(1);
                          qX=queueX[index]+ 1;
                          direction=3;       
                        }
                  else if(qX==queueX[index]&& qY==queueY[index]-1) 
                        {
                          turnRight();
                          move(1);
                          qY=queueY[index]-1;
                          direction=4;
                        }
                  else if(qX==queueX[index]&& qY==queueY[index]+1) 
                        { 
                          turnLeft();
                          move(1);
                          qY=queueY[index]-1;
                          direction=2;
                        }
                          
                  break;}                                                                                                 
         case 4:{ 
                  if(qX==queueX[index]-1&& qY==queueY[index]) 
                        {
                          turnRight();
                          move(1);
                          qX=queueX[index]-1;
                          direction=1;          
                        }
                  else if(qX==queueX[index]+1&& qY==queueY[index]) 
                        { 
                          turnLeft();
                          move(1);
                          qX=queueX[index]+ 1;
                          direction=3;       }
                  else if(qX==queueX[index]&& qY==queueY[index]-1) 
                        {
                          move(1);
                          qY=queueY[index]-1;
                          direction=4;
                        }
                  else if(qX==queueX[index]&& qY==queueY[index]+1) 
                        { 
                          turnAround();
                          move(1);
                          qY=queueY[index]-1;
                          direction=2;
                        }
                          
                  break;} 
           
        }
    }
}

void calibrate()
{
  if( getleftdistance() < 7 ) { 
                                turnLeft();
                                drive_goto( -15,-15);
                                turnRight();
                              }                                
  else if( getrightdistance() < 7 ) { 
                                      turnRight();
                                      drive_goto( -15,-15);
                                      turnLeft();
                                    }         
} 

void dfs(int A[5][5],int x, int y, int direction) // x and y are the coordinates 
{
 visited[x][y]=1;
 switch(direction){ //checking all 4 neighbour squares for all directions the robot could face
 case 1: {
         if(x-1>=1&&x-1<=4&&y>=1&&y<=4&&!visited[x-1][y]&&check_front())
           {
               move(1);                         
               value[x-1][y]= value[x][y]+1;     
               dfs(A,x-1,y,1);
               move(-1);
           }
          
          
           turnLeft();
           if(x>=1&&x<=4&&y-1>=1&&y-1<=4&&!visited[x][y-1]&&check_front())
           {
               move(1);
               value[x][y-1]= value[x][y]+1;
               dfs(A,x,y-1,4);
               move(-1);
               turnRight();
           }
          else turnRight();
          
          
          
          turnRight();
           if(x>=1&&x<=4&&y+1>=1&&y+1<=4&&!visited[x][y+1]&&check_front())
           {
               move(1);
               value[x][y+1]= value[x][y]+1;
               dfs(A,x,y+1,2);
               move(-1);
               turnLeft();
           }
           else turnLeft();
     break;}

 case 2: {if(x>=1&&x<=4&&y+1>=1&&y+1<=4&&!visited[x][y+1]&&check_front())
           {
               move(1);
               value[x][y+1]= value[x][y]+1;
               dfs(A,x,y+1,2);
               move(-1);
           }
           turnLeft();
           if(x-1>=1&&x-1<=4&&y>=1&&y<=4&&!visited[x-1][y]&&check_front())
           {
               move(1);
               value[x-1][y]= value[x][y]+1;
               dfs(A,x-1,y,1);
               move(-1);
               turnRight();
           }
           else turnRight();
          
          
          
           turnRight();
           if(x+1>=1&&x+1<=4&&y>=1&&y<=4&&!visited[x+1][y]&&check_front())
           {
               move(1);
               value[x+1][y]= value[x][y]+1;
               dfs(A,x+1,y,3);
               move(-1);
               turnLeft();
           }
           else turnLeft();
     break;}
     
 case 3: {
           if(x+1>=1&&x+1<=4&&y>=1&&y<=4&&!visited[x+1][y]&&check_front())
           {
               move(1);
               value[x+1][y]= value[x][y]+1;
               dfs(A,x+1,y,3);
               move(-1);
           }
           turnLeft();
           if(x>=1&&x<=4&&y+1>=1&&y+1<=4&&!visited[x][y+1]&&check_front())
           {
               move(1);
               value[x][y+1]= value[x][y]+1;
               dfs(A,x,y+1,2);
               move(-1);
               turnRight();
           }
           else turnRight();
           
           turnRight();
           if(x>=1&&x<=4&&y-1>=1&&y-1<=4&&!visited[x][y-1]&&check_front())
           {
               move(1);
               value[x][y-1]= value[x][y]+1;
               dfs(A,x,y-1,4);
               move(-1);
               turnLeft();
           }
          else turnLeft();
             break;}
             
 case 4: {if(x>=1&&x<=4&&y-1>=1&&y-1<=4&&!visited[x][y-1]&&check_front())
           {
               move(1);
               value[x][y-1]= value[x][y]+1;
               dfs(A,x,y-1,4);
               move(-1);
           }
           turnLeft();
           if(x+1>=1&&x+1<=4&&y>=1&&y<=4&&!visited[x+1][y]&&check_front())
           {
               move(1);
               value[x+1][y]= value[x][y]+1;
               dfs(A,x+1,y,3);
               move(-1);
               turnRight();
           }
           else turnRight();
           
           turnRight();
           if(x-1>=1&&x-1<=4&&y>=1&&y<=4&&!visited[x-1][y]&&check_front())
           {
               move(1);
               value[x-1][y]= value[x][y]+1;
               dfs(A,x-1,y,1);
               move(-1);
               turnLeft();
           }
           else turnLeft();
     break;}
}
}



int main()
{ 
  move(1);         //the first step into the maze
  value[1][4] = 1;   //value initialised to 1 (because the starting square is 0 (Lee's Algorithm)
  int counter = 0;   
  dfs(B,1,4,3);    /*Running the DFS which fills the bidimensional array as it explores and visits it, 
                   which will have in the ending point the minimum number of steps to it and the starting 
                   point is initialised with 1
                   */
  int row = 4;
  int column = 1;
  int currentPoint = value[row][column]; // the current point is initialised to the ending point in the maze 
  queueX[0] = 4; queueY[0] = 1;
  while(currentPoint!=1)                   //while the backtracking doesn't reach the starting point
  { //checking all for directions for the point which is lower by 1
    if(value[row-1][column] == currentPoint-1) {
                                               row=row-1;
                                               currentPoint = value[row][column];
                                               counter++; 
                                               queueX[counter]=row;
                                               queueY[counter]=column;
                                             }  
    else if(value[row+1][column] == currentPoint-1) 
                                             {
                                               row=row+1;
                                               currentPoint = value[row][column];
                                               counter++;
                                               queueX[counter]=row;
                                               queueY[counter]=column;
                                             }
    else if(value[row][column-1] == currentPoint-1) 
                                             {
                                               column=column-1;
                                               currentPoint = value[row][column];
                                               counter++;
                                               queueX[counter]=row;
                                               queueY[counter]=column;
                                             }
    else if(value[row][column+1] == currentPoint-1) 
                                             {
                                               column=column+1;
                                               currentPoint = value[row][column];
                                               counter++;
                                               queueX[counter]=row;
                                               queueY[counter]=column;
                                             }
  }
  
  
  move(-1);
  move(1);
  goThroughPath(queueX,queueY,counter);
  
  
  
  
  
  
    return 0;
}