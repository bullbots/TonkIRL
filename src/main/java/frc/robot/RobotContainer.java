// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.Lifter.DefaultLifterCommand;
import frc.robot.commands.cannon.AirTankDefaultCommand;
import frc.robot.commands.cannon.FireBarrel;
import frc.robot.commands.cannon.FireCycle;
import frc.robot.commands.drivetrain.ControllerBasicDrive;
import frc.robot.subsystems.AirTank;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.Lifter;
import frc.robot.subsystems.Cannon.Barrel;
import frc.robot.util.RadioController;
import frc.robot.util.YamlLoader;
import frc.team1891.common.LazyDashboard;
import frc.team1891.illegal.driverstation.DriverStationSpoofer;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RobotContainer {
  // Subsystems
  private static final Drivetrain drivetrain = Drivetrain.getInstance();
  private static final AirTank airTank = AirTank.getInstance();
  private static final Cannon cannon = Cannon.getInstance();
  private static LEDs leds;
  private static final Lifter lifter = Lifter.getInstance();

  // Inputs
  private static final RadioController controller = new RadioController();
  private static final DigitalInput spoofSwitch = new DigitalInput(0);
  
  // Triggers
  private static final Trigger spoofSwitchTrigger = new Trigger(() -> !spoofSwitch.get());
  private static final Trigger shootTrigger = new Trigger(() -> controller.getCH6() > 0.1);

  private static final Trigger controllerArmed = new Trigger(() -> controller.getCH5() < -.9 && !spoofSwitch.get());
  private static final Trigger controllerDisarmed = new Trigger(() -> controller.getCH5() > .9 && !spoofSwitch.get());
  private static final Trigger controllerDisabled = new Trigger(() -> (controller.getCH5() < .1 || controller.getCH5() > -.1));

  public static DataLog log;
  public static StringLogEntry myStringLog;

  
  
  
  private static final Command airTankCommand = new AirTankDefaultCommand(airTank);
  public RobotContainer() {
    System.out.println("hello");
    configureBindings();
    //System.out.println(DataLogManager.getLogDir());

    //  DataLogManager.start("/media/sda1");
    // DataLogManager.start();
    // log = DataLogManager.getLog();
    // //DriverStation.startDataLog(log);
    // myStringLog = new StringLogEntry(log, "/test");
    // myStringLog.append("This is a test. Hi Mike");
    Logger1891.info("in RobotContainer");
    System.out.println("hello");
    System.out.println("testing");
    System.out.println("more commands");
    System.out.println("something else");
   
   // logger.append("Constructor for RobotContainer...");


    // for (int i = 1; i < 10; i++) {
    //   DigitalInput dio = new DigitalInput(i);
    //   LazyDashboard.addBoolean("DIO "+i, dio::get);
    // }

    
  


    LazyDashboard.addBoolean("DriverStationSpoofer/enableSwitch", spoofSwitchTrigger::getAsBoolean);
    LazyDashboard.addBoolean("DriverStationSpoofer/isSpoofing", DriverStationSpoofer::isEnabled);

    //create needed object things
    leds = LEDs.getInstance();

    // SmartDashboard.putNumber("ping", 0);
    // LazyDashboard.addNumber("ping", () -> {
    //   return SmartDashboard.getNumber("ping", 0) + 1;
    // });
  }
  
  private void configureBindings() {
    drivetrain.setDefaultCommand(new ControllerBasicDrive(drivetrain, controller::getLeftY, controller::getRightX));
    airTank.setDefaultCommand(airTankCommand);
    lifter.setDefaultCommand(new DefaultLifterCommand(lifter, ()-> controller.getRightY()));
    
    controllerArmed.onTrue(new InstantCommand(()->airTank.setDefaultCommand(airTankCommand), airTank));
    controllerDisarmed.onTrue(new InstantCommand(()->airTank.removeDefaultCommand(), airTank));
    
    spoofSwitchTrigger.onTrue(new BetterInstantCommand(() -> {
      Logger1891.info("spoofSwitchTrigger onTrue");
      //System.out.println(DataLogManager.getLogDir());
     // myStringLog.append("Robot Turn On!!!");
      DriverStationSpoofer.enable();
    }));

    spoofSwitchTrigger.onFalse(new BetterInstantCommand(() -> {
      Logger1891.info("spoofSwitchTrigger onFalse");
      //myStringLog.append("Robot Turn Off");
      DriverStationSpoofer.disable();
    }));
    //shootTrigger.onTrue(new FireCycle(cannon));
    //shootTrigger.onTrue(new SequentialCommandGroup(new FireBarrel(cannon, Barrel.BOTTOM_LEFT),new FireBarrel(cannon, Barrel.BOTTOM_RIGHT),new FireBarrel(cannon, Barrel.TOP_LEFT),new FireBarrel(cannon, Barrel.TOP_RIGHT)));
    shootTrigger.onTrue(new FireCycle(cannon));
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
