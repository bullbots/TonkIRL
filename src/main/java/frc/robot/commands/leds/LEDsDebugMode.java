// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.AirTank;
import frc.robot.subsystems.LEDs;
import frc.team1891.common.led.LEDStripInterface;
import frc.team1891.common.led.LEDStripPattern;

public class LEDsDebugMode extends CommandBase {
  private final LEDStripInterface allLEDs, topA, topB, underA, underB;

  private final LEDStripPattern pulse = new LEDStripPattern() {
    private int value = 200;
    public void draw(LEDStripInterface leds) {
      leds.setAllHSV(0, 0, value);
      value = value <= 10 ? 200 : value - 2;
    };
  };

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

  private final LEDStripPattern voltageIndicatorPattern = new LEDStripPattern() {
    public void draw(LEDStripInterface leds) {
      leds.clear();
      
      double maxVoltage = 14;
      double voltage = RobotController.getBatteryVoltage();
      if (voltage > 12) {
        leds.setRangeRGB(0, (int) ((voltage/maxVoltage)*leds.length()), 0, 150, 0);
      } else if (voltage < 10) {
        leds.setRangeRGB(0, (int) ((voltage/maxVoltage)*leds.length()), 150, 0, 0);
      } else {
        leds.setRangeRGB(0, (int) ((voltage/maxVoltage)*leds.length()), 100, 100, 100);
      }
    };
  };

  private final LEDStripPattern controllerConnectionPattern = new LEDStripPattern() {
    public void draw(LEDStripInterface leds) {
      if (RobotContainer.radioControllerConnected()) {
        if (RobotContainer.spoofSwitchEnabled()) {
          leds.setAllRGB(0, 255, 0);
        } else if (RobotContainer.onControllerSwitchEnabled()) {
          leds.flashAllRGB(.5, 0, 255, 0, 0, 100, 0);
        } else {
          leds.setAllRGB(0, 100, 0);
        }
      } else {
        leds.flashAllRGB(.5, 255, 0, 0, 100, 0, 0);
      }
    };
  };

  private final LEDStripPattern enableStatusPatturn = new LEDStripPattern() {
    public void draw(LEDStripInterface leds) {
      if (RobotContainer.spoofSwitchEnabled()) {
        if (DriverStation.isEnabled()) {
          leds.setAllRGB(0, 255, 0);
        } else {
          leds.flashAllRGB(.5, 255, 0, 0, 100, 0, 0);
        }
      } else if (RobotContainer.onRobotSwitchEnabled()) {
        pulse.draw(leds);
      } else {
        leds.setAllRGB(100, 100, 100);
      }
    };
  };
  
  /** Creates a new PressureDisplay. */
  public LEDsDebugMode(LEDs leds) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(leds);
    this.allLEDs = leds.leds;
    this.topA = leds.topA;
    this.underA = leds.underA;
    this.topB = leds.topB;
    this.underB = leds.underB;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    pressureIndicatorPattern.run(topA);
    voltageIndicatorPattern.run(topB);
    controllerConnectionPattern.run(underA);
    enableStatusPatturn.run(underB);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    allLEDs.off();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
