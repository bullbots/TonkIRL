import serial
import struct

ser = serial.Serial('COM5', 921600)  # Adjust port as needed

while True:
    data = ser.read(20)  # 20 bytes for the packet
    header, *pwm_vals, checksum = struct.unpack('<H8hH', data)
    if header == 0xABCD:  # Valid packet
        print(f"PWM: {pwm_vals}")