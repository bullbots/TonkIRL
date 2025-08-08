// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LEDs;
import frc.team1891.common.led.LEDStripInterface;
import frc.team1891.common.led.LEDStripPattern;
import frc.team1891.common.led.LEDStripPatterns;

public class LEDsPresentationMode extends CommandBase {
  private final LEDStripInterface allLEDs;

  private final LEDStripPattern enabled = LEDStripPatterns.RAINBOW(),
                                disabled = new LEDStripPattern() {
    @Override
    public void draw(LEDStripInterface leds) {
      if (RobotContainer.radioControllerConnected()) {
        leds.flashAllRGB(1, 0, 0, 100, 0, 100, 0);
      } else {
        leds.setAllRGB(100, 100, 100);
      }
    }
  };

  /** Creates a new LEDsDefaultCommand. */
  public LEDsPresentationMode(LEDs leds) {
    addRequirements(leds);
    this.allLEDs = leds.leds;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (DriverStation.isEnabled()) {
      enabled.run(allLEDs);
      return;
    } else {
      disabled.run(allLEDs);
    }
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

  // LEDs should look cool when disabled too.
  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
