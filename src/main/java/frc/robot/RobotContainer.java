// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.cannon.AirTankDefaultCommand;
import frc.robot.commands.cannon.FireCycle;
import frc.robot.commands.drivetrain.ControllerBasicDrive;
import frc.robot.commands.leds.LEDsPresentationMode;
import frc.robot.commands.leds.LEDsDebugMode;
import frc.robot.commands.lifter.DefaultLifterCommand;
import frc.robot.subsystems.AirTank;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.Lifter;
import frc.robot.util.RadioController;
import frc.robot.util.RadioControllerInterface;
import frc.robot.util.SimRadioController;
import frc.robot.util.RadioController.SwitchState;
import frc.team1891.common.LazyDashboard;
import frc.team1891.illegal.driverstation.DriverStationSpoofer;

import java.util.function.DoubleSupplier;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;

public class RobotContainer {
  // Subsystems
  private static final Drivetrain drivetrain = Drivetrain.getInstance();
  private static final AirTank airTank = AirTank.getInstance();
  private static final Cannon cannon = Cannon.getInstance();
  private static final LEDs leds = LEDs.getInstance();
  private static final Lifter lifter = Lifter.getInstance();

  // RadioController
  private static final RadioControllerInterface controller = Robot.isReal()?
    new RadioController():
    new SimRadioController(0);

  // On-robot enable button
  private static final DigitalInput spoofSwitch = new DigitalInput(0);
  // Robot will only enable when the button is pressed AND the controller left switch is HIGH.
  private static final Trigger onRobotEnableTrigger = new Trigger(() -> !spoofSwitch.get());
  private static final Trigger onControllerEnableTrigger = new Trigger(() -> controller.getLeftSwitch().equals(SwitchState.HIGH));
  private static final Trigger spoofSwitchTrigger = onControllerEnableTrigger.and(onRobotEnableTrigger);
  
  // RadioController Triggers and ValueSuppliers
  private static final DoubleSupplier controllerLeftY = controller::getLeftY,
                                      controllerLeftX = controller::getLeftX,
                                      controllerRightY = controller::getRightY;

  private static final Trigger shootTrigger = new Trigger(controller::getRightButton);

  private static final Trigger debugLEDMode = new Trigger(() -> !controller.isConnected() || controller.getRightSwitch().equals(SwitchState.HIGH));

  public static final DoubleSupplier desiredPressure = () -> radioControllerConnected()?
    ((controller.getLeftDial() + 1) * AirTank.MAX_PRESSURE/2.):
    0; // Map controller dial [-1,1] to PSI [0,MAX] when controller is connected

  // System Triggers
  private static double lastDesiredPressure = desiredPressure.getAsDouble();
  private static final Trigger desiredPressureChange = new Trigger(() -> {
    double newDesiredPressure = desiredPressure.getAsDouble();
    // If the desired pressure differs by more than 3 PSI this will trigger.
    if (Math.abs(lastDesiredPressure - newDesiredPressure) > 3) {
      lastDesiredPressure = newDesiredPressure;
      return true;
    }
    return false;
  });

  private static final Trigger atDesiredPressure = new Trigger(() -> airTank.getCurrentPressure() > airTank.getCurrentPressure());

  public static DataLog log;
  public static StringLogEntry myStringLog;
  
  public RobotContainer() {
    configureBindings();

    LazyDashboard.addBoolean("DriverStationSpoofer/enableSwitch", 5, spoofSwitchTrigger::getAsBoolean);
    LazyDashboard.addBoolean("DriverStationSpoofer/isSpoofing", 5, DriverStationSpoofer::isEnabled);

    LazyDashboard.addNumber("Battery Voltage", 30, RobotController::getBatteryVoltage);
  }
  
  private void configureBindings() {
    Command airTankCommand = new AirTankDefaultCommand(airTank);

    drivetrain.setDefaultCommand(new ControllerBasicDrive(drivetrain, controllerLeftY, controllerLeftX));
    airTank.setDefaultCommand(airTankCommand);
    lifter.setDefaultCommand(new DefaultLifterCommand(lifter, controllerRightY));
    leds.setDefaultCommand(new LEDsPresentationMode(leds));

    debugLEDMode.whileTrue(new LEDsDebugMode(leds));
    
    spoofSwitchTrigger.onTrue(new BetterInstantCommand(() -> {
      Logger1891.info("spoofSwitchTrigger onTrue");
      DriverStationSpoofer.enable();
    }));

    spoofSwitchTrigger.onFalse(new BetterInstantCommand(() -> {
      Logger1891.info("spoofSwitchTrigger onFalse");
      DriverStationSpoofer.disable();
    }));
    //shootTrigger.onTrue(new FireCycle(cannon));
    //shootTrigger.onTrue(new SequentialCommandGroup(new FireBarrel(cannon, Barrel.BOTTOM_LEFT),new FireBarrel(cannon, Barrel.BOTTOM_RIGHT),new FireBarrel(cannon, Barrel.TOP_LEFT),new FireBarrel(cannon, Barrel.TOP_RIGHT)));
    shootTrigger.onTrue(new FireCycle(cannon));

    Command ledPressureDisplay = new LEDsDebugMode(leds).withTimeout(1);
    desiredPressureChange.and(debugLEDMode.negate()).onTrue(ledPressureDisplay);
    atDesiredPressure.and(debugLEDMode.negate()).onTrue(ledPressureDisplay);
  }

  public static boolean onRobotSwitchEnabled() {
    return onRobotEnableTrigger.getAsBoolean();
  }

  public static boolean onControllerSwitchEnabled() {
    return onControllerEnableTrigger.getAsBoolean();
  }

  public static boolean spoofSwitchEnabled() {
    return spoofSwitchTrigger.getAsBoolean();
  }

  public static boolean radioControllerConnected() {
    return controller.isConnected();
  }

  public static double getDesiredPressureFromRadioController() {
    return desiredPressure.getAsDouble();
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
