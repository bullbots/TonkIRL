// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.cannon.AirTankDefaultCommand;
import frc.robot.commands.drivetrain.ControllerBasicDrive;
import frc.robot.subsystems.AirTank;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LEDs;
import frc.robot.util.DriverStationSpoofer;
import frc.robot.util.RadioController;
import frc.team1891.common.LazyDashboard;

public class RobotContainer {
  // Subsystems
  private static final Drivetrain drivetrain = Drivetrain.getInstance();
  private static final AirTank airTank = AirTank.getInstance();
  private static final Cannon cannon = Cannon.getInstance();
  private static final LEDs leds = LEDs.getInstance();

  // Inputs
  private static final RadioController controller = new RadioController();
  private static final DigitalInput spoofSwitch = new DigitalInput(0);
  
  // Triggers
  private static final Trigger spoofSwitchTrigger = new Trigger(() -> !spoofSwitch.get());

  public RobotContainer() {
    configureBindings();

    // for (int i = 1; i < 10; i++) {
    //   DigitalInput dio = new DigitalInput(i);
    //   LazyDashboard.addBoolean("DIO "+i, dio::get);
    // }


    LazyDashboard.addBoolean("DriverStationSpoofer/enableSwitch", spoofSwitchTrigger::getAsBoolean);
    LazyDashboard.addBoolean("DriverStationSpoofer/isSpoofing", DriverStationSpoofer::isEnabled);

    // SmartDashboard.putNumber("ping", 0);
    // LazyDashboard.addNumber("ping", () -> {
    //   return SmartDashboard.getNumber("ping", 0) + 1;
    // });
    
    LazyDashboard.addNumber("RadioController/controllerX", 1, controller::getX);
    LazyDashboard.addNumber("RadioController/controllerY", 1, controller::getY);
  }

  private void configureBindings() {
    drivetrain.setDefaultCommand(new ControllerBasicDrive(drivetrain, controller::getX, controller::getY));
    airTank.setDefaultCommand(new AirTankDefaultCommand(airTank));

    spoofSwitchTrigger.onTrue(new BetterInstantCommand(() -> {
      DriverStationSpoofer.enable();
    }));

    spoofSwitchTrigger.onFalse(new BetterInstantCommand(() -> {
      DriverStationSpoofer.disable();
    }));
  }

  public static boolean spoofSwitchEnabled() {
    return spoofSwitchTrigger.getAsBoolean();
  }

  public static boolean radioControllerConnected() {
    return controller.isConnected();
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }

  @SuppressWarnings("unused")
  private class BetterInstantCommand extends InstantCommand {
    /**
     * Creates a new InstantCommand that runs the given Runnable with the given requirements.
     *
     * @param toRun the Runnable to run
     * @param requirements the subsystems required by this command
     */
    public BetterInstantCommand(Runnable toRun, Subsystem... requirements) {
      super(toRun, requirements);
    }

    /**
     * Creates a new InstantCommand with a Runnable that does nothing. Useful only as a no-arg
     * constructor to call implicitly from subclass constructors.
     */
    public BetterInstantCommand() {
      this(() -> {});
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
  }
}
