void Rotate_ZeroRadius(double degrees, char rotateLeft);

void Rotate_PivotTurn(double degrees, char rotateLeft);

void Move_In_Straight_Line(double currentPosition, double finalPosition, char facingRight);

void Track_Movement(double *angle_Radians, double *distance_X, double *distance_Y);

double PID(double distanceError, float Kp, float Ki, float Kd, int waitingTime_ms,
                 double *previous_error, double *integral);