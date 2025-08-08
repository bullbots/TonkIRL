// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SerialReader {
  private final SerialPort serialPort;
  private final StringBuilder message = new StringBuilder();
  public SerialReader(SerialPort port) {
    serialPort = port;
  }

  public String read() {
    return this.read(false);
  }

  /**
   * Reads from the {@link SerialPort} and returns all full messages that start with { and end with }.
   */
  public String read(boolean all) {
    try {
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
      if (!newString.equals("")) {
        // SmartDashboard.putString("newString", newString);
        // System.out.println(newString);
      }
      for (int i = 0; i < newString.length(); i++) {
        // System.out.println(i);
        final char currentChar = newString.charAt(i);
        if (currentChar == '{') {
          message.delete(0, message.length());
          newString = newString.substring(i);
          i = 0;
        } else if (currentChar == '}') {
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
      // SmartDashboard.putString("ret (with brackets)", ret);
      // SmartDashboard.putString("message", message.toString());
      if (!ret.equals("{}")) {
        // SmartDashboard.putString("ret dljsfa", ret);
        return ret.substring(1, ret.length()-1);
      } else {
        return "";
      }
      // System.out.println(ret);
      
    } catch (Exception e) {
      DriverStation.reportWarning("USB to Arduino is most likely disconnected", false);
      return "ERROR";
    }
  }

  public int[] readBytes() {
    byte[] bytes = serialPort.read(20);

    ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

    short header = buffer.getShort();
    String cheeeeese = header + " | ";
    int[] pwmValues = new int[8];
    for (int i = 0; i < 8; i++) {
      pwmValues[i] = (int) buffer.getShort();
      cheeeeese += pwmValues[i] + ", ";
    }
    short checksum = buffer.getShort();
    cheeeeese += checksum + ".";
    SmartDashboard.putString("cheeeeese", cheeeeese);

    return pwmValues;
  }
}
