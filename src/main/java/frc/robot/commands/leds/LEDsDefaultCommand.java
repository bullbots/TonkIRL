// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.leds;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LEDs;
import frc.team1891.common.led.LEDStripInterface;
import frc.team1891.common.led.LEDStripPattern;
import frc.team1891.common.led.LEDStripPatterns;

public class LEDsDefaultCommand extends CommandBase {
  private final LEDStripInterface top;
  private final LEDStripInterface underGlow;
  private final LEDStripInterface all;

  private final LEDStripPattern underRainbow = LEDStripPatterns.RAINBOW();

  /** Creates a new LEDsDefaultCommand. */
  public LEDsDefaultCommand(LEDs leds) {
    addRequirements(leds);
    this.top = leds.topSegment;
    this.underGlow = leds.underGlowSegment;
    this.all = leds.leds;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!RobotContainer.radioControllerConnected()) {
      // If the controller is disconnected, show red
      top.setAllRGB(100, 0, 0);
      top.update();
    } else if (RobotContainer.spoofSwitchEnabled()) {
      // If the robot is enabled, show all rainbow
      underRainbow.run(all);
      return;
    } else {
      // If the robot is disabled, the top will flash the bullbot colors.
      top.flashAllRGB(2,  18, 0, 222, 18, 222, 20);
    }
    underRainbow.run(underGlow);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

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
