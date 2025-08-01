// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Logger1891;
import frc.team1891.common.led.LEDStrip;
import frc.team1891.common.led.LEDStripInterface;
import frc.team1891.common.led.LEDStripPattern;
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

  public final LEDStrip leds = new LEDStrip(9, 88);
  public final LEDStripSegment topSegment = new LEDStripSegment(leds, 0, 22);
  public final LEDStripSegment underGlowSegment = new LEDStripSegment(leds, 22, 66);

  private final LEDStripPattern pressureIndicatorPattern = new LEDStripPattern() {
    private final AirTank tank = AirTank.getInstance();
    public void draw(LEDStripInterface leds) {
      leds.clear();

      double currentPressure = tank.getCurrentPressure(), desiredPressure = tank.getDesiredPressure();
      //System.out.println("current pressure "+currentPressure);
      if (currentPressure > desiredPressure * 1.4 || currentPressure > AirTank.MAX_PRESSURE) {
        // set leds red if too high
        leds.setRangeRGB(0, (int) (leds.length() * (currentPressure / AirTank.MAX_PRESSURE)), 150, 0, 0);
      } else {
        // shows in white the desired pressure bar, and on top of it in Bullbots blue the actual pressure
        leds.setRangeRGB(0, (int) (leds.length() * (desiredPressure / AirTank.MAX_PRESSURE)), 150, 150, 150);
        leds.setRangeRGB(0, (int) (leds.length() * (currentPressure / AirTank.MAX_PRESSURE)), 18, 0, 222);
      }
    }
  };

  private LEDs() {
    leds.start();
  }
  

  @Override
  public void periodic() {

    //RobotContainer.logger4j.info("LEDS periodic");
    
    //sad.run(matrix);
  }
}
