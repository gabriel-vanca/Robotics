char IR_CheckLeftSensor();

char IR_CheckRightSensor();

int IR_GetLeftSensorValue();

int IR_GetRightSensorValue();

float IR_GetAverageSensorValue(int numberOfIterations, int (*getAverageSensorValueFunction)());