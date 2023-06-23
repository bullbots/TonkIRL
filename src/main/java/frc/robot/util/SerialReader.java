// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SerialReader {
  private final SerialPort serialPort;
  private final char startChar = '{', endChar = '}';
  private final StringBuilder message = new StringBuilder();
  public SerialReader(SerialPort port) {
    serialPort = port;

    SmartDashboard.putString("serial", "none");
    SmartDashboard.putString("last_serial", "none");
  }

  public String read() {
    return read(false);
  }

  public String read(boolean all) {
    String newString = serialPort.readString();
    // try {
      //     // System.out.println(serialPort.readString());
      //     String newString = serialPort.readString();
      //     if (!newString.equals("")) {
      //       SmartDashboard.putString("last_serial", newString);
      //     }
      //     SmartDashboard.putString("serial", newString);
      //   } catch (Exception e) {}
      
    String ret = "{}";
    System.out.println(newString);
    for (int i = 0; i < newString.length(); i++) {
      // System.out.println(i);
      final char currentChar = newString.charAt(i);
      if (currentChar == startChar) {
        message.delete(0, message.length());
        newString = newString.substring(i);
        i = 0;
      } else if (currentChar == endChar) {
        message.append(newString.substring(0, i+1));
        newString = newString.substring(i);
        i = 0;
        if (all && ret != null) {
          ret += message.toString();
        } else {
          ret = message.toString();
        }
      }
    }
    message.append(newString);
    return ret.substring(1, ret.length()-1);
  }
}
