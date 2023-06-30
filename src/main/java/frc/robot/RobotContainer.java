// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.util.DriverStationSpoofer;
import frc.robot.util.RadioController;
import frc.team1891.common.LazyDashboard;

public class RobotContainer {

  private final DigitalInput spoofSwitch = new DigitalInput(0);
  
  private final Trigger spoofSwitchTrigger = new Trigger(() -> !spoofSwitch.get());

  private final RadioController controller = new RadioController();

  public RobotContainer() {
    configureBindings();

    // for (int i = 1; i < 10; i++) {
    //   DigitalInput dio = new DigitalInput(i);
    //   LazyDashboard.addBoolean(""+i, dio::get);
    // }

    LazyDashboard.addNumber("controllerX", controller::getX);
    LazyDashboard.addNumber("controllerY", controller::getY);

    LazyDashboard.addBoolean("enableSwitch", spoofSwitchTrigger::getAsBoolean);
    LazyDashboard.addBoolean("isSpoofing", DriverStationSpoofer::isEnabled);

    SmartDashboard.putNumber("ping", 0);
    LazyDashboard.addNumber("ping", () -> {
      return SmartDashboard.getNumber("ping", 0) + 1;
    });
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
