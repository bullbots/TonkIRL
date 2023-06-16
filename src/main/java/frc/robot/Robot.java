// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.util.DriverStationSpoofer;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  // private CANSparkMax spark = new CANSparkMax(8, MotorType.kBrushless);

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();

    // Enable the robot on startup.
    if (isSimulation()) {
      DriverStationSim.setEnabled(true);
    } else {
      // DriverStationSpoofer.enable();
    }

    SmartDashboard.putString("spoofer", "empty");
  }

  SerialPort port = new SerialPort(9600, Port.kUSB1);

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

    try {
      System.out.println(port.readString());
      String newString = port.readString();
      if (!newString.equals("")) {
        SmartDashboard.putString("spoofer", newString);
      }
    } catch (Exception e) {}

    // System.out.println(DriverStation.isEnabled());
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    // spark.set(.1);
  }

  @Override
  public void teleopExit() {
    // spark.stopMotor();
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
