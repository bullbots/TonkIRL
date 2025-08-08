const int pwmPins[] = {2, 3, 4, 5, 6, 7, 8, 9};
const int numPwmPins = 8;
const uint16_t PACKET_HEADER = 0xABCD; // Sync header

// Binary packet structure
struct PWMPacket {
  uint16_t header;           // Sync bytes 0xABCD  
  int16_t pwmValues[8];      // PWM values (-100 to 100)
  uint16_t checksum;         // Simple checksum
} __attribute__((packed));

PWMPacket packet;

void setup() {
  Serial.begin(921600);  // Much faster baud rate
  
  // Initialize packet header
  packet.header = PACKET_HEADER;
  
  for(int i = 0; i < numPwmPins; i++){
    pinMode(pwmPins[i], INPUT);
  }
  
  // Wait for serial to be ready
  while (!Serial);
}

uint16_t calculateChecksum(int16_t* values, int count) {
  uint16_t sum = 0;
  for (int i = 0; i < count; i++) {
    sum += (uint16_t)values[i];
  }
  return sum;
}

void loop() {
  // Read all PWM values with timeout to prevent hanging
  for(int i = 0; i < numPwmPins; i++){
    unsigned long pulseWidth = pulseIn(pwmPins[i], HIGH, 30000); // 25ms timeout
    
    // Only map if we got a valid reading
    if(pulseWidth > 0 && pulseWidth >= 800 && pulseWidth <= 2200) {
      packet.pwmValues[i] = map(pulseWidth, 987, 1987, -100, 100);
    } else {
      // Keep previous value or set to neutral if no signal
      packet.pwmValues[i] = 0; // or keep previous value
    }
  }
  
  // Calculate checksum
  packet.checksum = calculateChecksum(packet.pwmValues, numPwmPins);
  
  // Send binary packet (much faster than string concatenation)
  Serial.write((uint8_t*)&packet, sizeof(PWMPacket));
  
  // No delay - run as fast as possible
}

// Optional: Function for debugging packet structure
void printPacketInfo() {
  Serial.print(F("Packet size: "));
  Serial.print(sizeof(PWMPacket));
  Serial.println(F(" bytes"));
  Serial.print(F("Header: 0x"));
  Serial.println(PACKET_HEADER, HEX);
}
