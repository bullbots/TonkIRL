// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.util.DriverStationSpoofer;

public class RobotContainer {

  private final DigitalInput spoofSwitch = new DigitalInput(0);
  private final Trigger spoofSwitchTrigger = new Trigger(() -> {
    return spoofSwitch.get();
  });

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    spoofSwitchTrigger.onTrue(new InstantCommand(() -> {
      DriverStationSpoofer.enable();
    }));

    spoofSwitchTrigger.onFalse(new InstantCommand(() -> {
      DriverStationSpoofer.disable();
    }));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
