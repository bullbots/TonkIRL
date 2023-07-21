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
import frc.robot.commands.cannon.AirTankDefaultCommand;
import frc.robot.commands.drivetrain.ControllerBasicDrive;
import frc.robot.subsystems.AirTank;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.DriverStationSpoofer;
import frc.robot.util.RadioController;
import frc.team1891.common.LazyDashboard;

public class RobotContainer {
  // Subsystems
  private final Drivetrain drivetrain = Drivetrain.getInstance();
  // private final AirTank airTank = AirTank.getInstance();
  // private final Cannon cannon = Cannon.getInstance();

  // Inputs
  private final RadioController controller = new RadioController();
  private final DigitalInput spoofSwitch = new DigitalInput(0);
  
  // Triggers
  private final Trigger spoofSwitchTrigger = new Trigger(() -> !spoofSwitch.get());

  public RobotContainer() {
    configureBindings();

    // for (int i = 1; i < 10; i++) {
    //   DigitalInput dio = new DigitalInput(i);
    //   LazyDashboard.addBoolean(""+i, dio::get);
    // }


    LazyDashboard.addBoolean("DriverStationSpoofer/enableSwitch", spoofSwitchTrigger::getAsBoolean);
    LazyDashboard.addBoolean("DriverStationSpoofer/isSpoofing", DriverStationSpoofer::isEnabled);

    // SmartDashboard.putNumber("ping", 0);
    // LazyDashboard.addNumber("ping", () -> {
    //   return SmartDashboard.getNumber("ping", 0) + 1;
    // });
  }

  private void configureBindings() {
    drivetrain.setDefaultCommand(new ControllerBasicDrive(drivetrain, controller::getLeftY, controller::getRightX));
    // airTank.setDefaultCommand(new AirTankDefaultCommand(airTank));

    spoofSwitchTrigger.onTrue(new InstantCommand(() -> {
      DriverStationSpoofer.enable();
    }));
    SmartDashboard.putNumber("onFalse", 0);
    spoofSwitchTrigger.onFalse(new InstantCommand(() -> {
      DriverStationSpoofer.disable();
      SmartDashboard.putNumber("onFalse", SmartDashboard.getNumber("onFalse", 0)+1);
    }));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
