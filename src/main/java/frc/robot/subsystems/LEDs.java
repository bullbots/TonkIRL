// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.util.YamlLoader;
import frc.team1891.common.led.LEDMatrix;
import frc.team1891.common.led.LEDMatrixPattern;
import frc.team1891.common.led.LEDMatrixPattern.AnimatedLEDMatrixPattern;
import frc.team1891.common.led.LEDStrip;
import frc.team1891.common.led.LEDStripInterface;
import frc.team1891.common.led.LEDStripPattern;
import frc.team1891.common.led.LEDStripPatterns;
import frc.team1891.common.led.LEDStripSegment;
import frc.team1891.illegal.driverstation.DriverStationSpoofer;

public class LEDs extends SubsystemBase {
  private static LEDs instance = null;
  public static LEDs getInstance() {
    if (instance == null) {
      instance = new LEDs();
    }
    return instance;
  }

  private final LEDStrip leds = new LEDStrip(0, 336);
  private final LEDStripSegment rightSide = new LEDStripSegment(leds, 0, 20);
  private final LEDStripSegment leftSide = new LEDStripSegment(leds, 20, 20);



  private final LEDStripPattern pressureIndicatorPattern = new LEDStripPattern() {

    private final AirTank tank = AirTank.getInstance();
    
    public void draw(LEDStripInterface leds) {
      leds.clear();

      double currentPressure = tank.getCurrentPressure();
      double desiredPressure = tank.getDesiredPressure();

      double fractionCompleted = currentPressure / desiredPressure;
      int numLedsToLight = (int) Math.rint(leds.length() * fractionCompleted);
      for (int i = 0; i < numLedsToLight; i++) {
        leds.setRGB(i, 255, 255, 255);
      }

      if (Math.abs(desiredPressure - currentPressure) < 2) {
        leds.setAllRGB(0, 150, 0);
      }

      // // set leds orange if too high
      // if (currentPressure > desiredPressure * 1.4) {
      //   leds.setAllRGB(150, 100, 0);
      // // set leds green if the pressure is reached
      // } else if (currentPressure > desiredPressure) {
      //   leds.setAllRGB(0, 150, 0);
      // // show a progress bar
      // } else {
      //   for (int i = 0; i < leds.length() * (tank.getCurrentPressure() / tank.getDesiredPressure()); i++) {
      //     leds.setRGB(i, 150, 150, 150);
      //   }
  
      // }
    }
  };

  private final LEDStripPattern rainbow = LEDStripPatterns.RAINBOW();

  private LEDs() {
    leds.start();
  }
  

  @Override
  public void periodic() {
    pressureIndicatorPattern.run(leftSide);
    pressureIndicatorPattern.run(rightSide);
  }
}
