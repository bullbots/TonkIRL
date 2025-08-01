// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Logger1891;
import frc.team1891.common.led.LEDStrip;
import frc.team1891.common.led.LEDStripSegment;

public class LEDs extends SubsystemBase {
  private static LEDs instance = null;
  public static LEDs getInstance() {
    if (instance == null) {
      instance = new LEDs();
    }
    Logger1891.info("LEDs getInstance");
    return instance;
  }

  // TODO: I don't know how long the actual LED strip is
  public final LEDStrip leds = new LEDStrip(9, 100);
  public final LEDStripSegment topSegment = new LEDStripSegment(leds, 0, 22);
  public final LEDStripSegment underGlowSegment = new LEDStripSegment(leds, 22, 88);

  private LEDs() {
    leds.start();
  }
  

  @Override
  public void periodic() {

    //RobotContainer.logger4j.info("LEDS periodic");
    
    //sad.run(matrix);
  }
}
