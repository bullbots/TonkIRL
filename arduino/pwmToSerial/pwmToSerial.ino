 
const int pwmPins[] = {2,3,4,5,6,7,8,9}; //pins in order of
const int numPwmPins = 8;

int pwmVal[numPwmPins];
String currentBus = "";
void setup() {
  Serial.begin(9600);  // Initialize serial communication
  for(int i = 0;i < numPwmPins;i++){
    pinMode(pwmPins[i], INPUT);
  }
}

void loop() {
  currentBus = "{";
  for(int i = 0;i < numPwmPins;i++){
    pwmVal[i] = map(pulseIn(pwmPins[i], HIGH),987,1987,-100,100);

    if(i != 0){
      currentBus += ",";
    }
    currentBus += (String)pwmVal[i];
  }
  currentBus += "}";
  //int pwm1 = map(pulseIn(pwmPin1, HIGH),987,1987,-100,100);
  //int pwm2 = map(pulseIn(pwmPin2, HIGH),987,1987,-100,100);
  //char buffer[40];
  //Serial.println("{pwm1: " + (String)pwm1 + " pwm2: " + pwm2 + "}");
  //sprintf(buffer, "{%d,%d}", pwm1, pwm2);
  //Serial.print(buffer);
  Serial.print(currentBus + "\n");
  //delay(100);  // Delay between readings
}
