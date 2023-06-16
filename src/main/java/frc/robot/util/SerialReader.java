// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SerialReader {
    private final SerialPort serialPort;
    public SerialReader(SerialPort port) {
        serialPort = port;

        SmartDashboard.putString("serial", "none");
        SmartDashboard.putString("last_serial", "none");
    }

    public void read() {
        try {
            // System.out.println(serialPort.readString());
            String newString = serialPort.readString();
            if (!newString.equals("")) {
              SmartDashboard.putString("last_serial", newString);
            }
            SmartDashboard.putString("serial", newString);
          } catch (Exception e) {}
    }
}
