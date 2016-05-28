/*
  SD with Tests.side

  SD Minimal modified so that it tests for drive and file before performing
  any read/write operations.  
  
  http://learn.parallax.com/propeller-c-simple-devices/sd-card-data
*/

#include "simpletools.h"                            // Include simpletools header     

int DO = 22, CLK = 23, DI = 24, CS = 25;            // SD card pins on Activity Board

int main(void)                                      // main function
{
  printf("AAAA");
  int erc = sd_mount(DO, CLK, DI, CS);              // Mount SD card
  printf("erc=%d\n", erc);
  if(!erc)                                          // Error code = 0, good, continue
  {
    #if 1
    printf("%d\n", __LINE__);
    FILE* fp = fopen("test.txt", "w");              // Open a file for writing
    printf("%d\n", __LINE__);
    
    printf("%d\n", __LINE__);
    
    if(fp)                                          // Nonzero file pointer?  
    {                                               // Good, continue
      fwrite("Test message, hello!\n", 1, 21, fp);  // Add contents to the file
    printf("%d\n", __LINE__);
    }
    else                                            // Zero file pinter?
    {                                               // Bad, error message.
      print("File did not open.\n");         
    printf("%d\n", __LINE__);
    }                                         
    fclose(fp);                                     // Close the file
    printf("%d\n", __LINE__);
    printf("%d\n", __LINE__);
    fp = fopen("test.txt", "r");                    // Reopen file for reading.
  
    printf("%d\n", __LINE__);
    char s[80];                                     // Buffer for characters.
  
    printf("%d\n", __LINE__);
    if(fp)                                          // Nonzero file pinter?  
    {                                               // Good, continue.
    printf("%d\n", __LINE__);
      print("First 21 chars in test.txt:\n");       // Display heading
    printf("%d\n", __LINE__);
      fread(s, 1, 21, fp);                          // Read 21 characters
    printf("%d\n", __LINE__);
      print("%s", s);                               // Display them
    printf("%d\n", __LINE__);
      print("\n");                                  // With a newline at the end.
    }
    else                                            // Zero file pointer?
    {                                               // Bad, print error.
    printf("%d\n", __LINE__);
      print("File did not open.\n");         
    printf("%d\n", __LINE__);
      print("\n");
    }
    printf("%d\n", __LINE__);
    fclose(fp);                                     // Close the file.
    printf("%d\n", __LINE__);
  #endif
  }
  else                                              // Mount error code not zero?
  {                                                 // Bad, display code
    printf("%d\n", __LINE__);
    print("Error opening card.");
    printf("%d\n", __LINE__);
    print("error code = %d\n", erc);
    printf("%d\n", __LINE__);
  }
}    
            
