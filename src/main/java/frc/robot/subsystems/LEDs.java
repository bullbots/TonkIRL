// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
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
    RobotContainer.logger.info("LEDs getInstance");
    return instance;
  }

  private final LEDStrip leds = new LEDStrip(9, 100);
  private final LEDStripSegment enabledStatusStrip = new LEDStripSegment(leds, 0, 2);
  private final LEDStripSegment pressureIndicatorStrip = new LEDStripSegment(leds, 2, 10);
  private final LEDStripSegment mainSegment = new LEDStripSegment(leds, 12, 99);

  private final LEDStripPattern enabledStatusPattern = (leds) -> {
    if (RobotContainer.spoofSwitchEnabled()) {
      if (DriverStationSpoofer.isEnabled()) {
        leds.setRGB(0, 0, 150, 0);
      } else {
        leds.setRGB(0, 150, 0, 0);
      }
    } else {
      if (DriverStationSpoofer.isEnabled()) {
        leds.setRGB(0, 0, 150, 0);
      } else {
        leds.setRGB(0, 0, 0, 0);
      }
    }
    if (RobotContainer.radioControllerConnected()) {
      leds.setRGB(1, 0, 150, 0);
    } else {
      leds.setRGB(1, 150, 0, 0);
    }
  };

  private final LEDStripPattern pressureIndicatorPattern = new LEDStripPattern() {
    private final AirTank tank = AirTank.getInstance();
    public void draw(LEDStripInterface leds) {
      leds.clear();

      double currentPressure = tank.getCurrentPressure(), desiredPressure = tank.getDesiredPressure();
      //System.out.println("current pressure "+currentPressure);
      // set leds red if too high
      if (currentPressure > desiredPressure * 1.4) {
        leds.setAllRGB(150, 0, 0);
      // set leds green if the pressure is reached
      } else if (currentPressure > desiredPressure) {
        leds.setAllRGB(0, 150, 0);
      // show a progress bar that's Bullbots blue
      } else {
        // System.out.println("test");
        for (int i = 0; i < 10 * (tank.getCurrentPressure() / tank.getDesiredPressure()); i++) {
          leds.setRGB(i, 18, 0, 222);
        }
  
      }
    }
  };

  private final LEDStripPattern rainbow = LEDStripPatterns.RAINBOW();

  private LEDs() {
    leds.start();
  }
  

  @Override
  public void periodic() {
    pressureIndicatorPattern.run(pressureIndicatorStrip);
    pressureIndicatorPattern.draw(pressureIndicatorStrip);
    enabledStatusPattern.run(enabledStatusStrip);
    rainbow.run(mainSegment);

    //RobotContainer.logger4j.info("LEDS periodic");
    
    //sad.run(matrix);
  }
}
