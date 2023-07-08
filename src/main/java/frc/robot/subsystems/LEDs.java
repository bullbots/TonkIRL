// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team1891.common.led.LEDStrip;
import frc.team1891.common.led.LEDStripInterface;
import frc.team1891.common.led.LEDStripSegment;
import frc.team1891.common.led.LEDStrip.LEDPattern;
import frc.team1891.common.led.LEDStrip.LEDPatterns;

public class LEDs extends SubsystemBase {
  private static LEDs instance = null;
  public static LEDs getInstance() {
    if (instance == null) {
      instance = new LEDs();
    }
    return instance;
  }

  private final LEDStrip leds = new LEDStrip(0, 100);
  private final LEDStripSegment pressureIndicatorStrip = new LEDStripSegment(leds, 0, 10);
  private final LEDStripSegment mainSegment = new LEDStripSegment(leds, 10, 90);
  
  private final LEDPattern pressureIndicatorPattern = new LEDPattern() {
    private final AirTank tank = AirTank.getInstance();
    public void draw(LEDStripInterface leds) {
      leds.clear();

      double currentPressure = tank.getPressure(), desiredPressure = tank.getDesiredPressure();
      // set leds orange if too high
      if (currentPressure > desiredPressure * 1.4) {
        leds.setAllRGB(150, 100, 0);
      // set leds green if the pressure is reached
      } else if (currentPressure > desiredPressure) {
        leds.setAllRGB(0, 150, 0);
      // show a progress bar
      } else {
        for (int i = 0; i < leds.length() * (tank.getPressure() / tank.getDesiredPressure()); i++) {
          leds.setRGB(i, 150, 150, 150);
        }
  
      }
    }
  };

  private LEDs() {
    leds.start();
  }

  @Override
  public void periodic() {
    pressureIndicatorPattern.run(pressureIndicatorStrip);
    LEDPatterns.RAINBOW.run(mainSegment);
  }
}
