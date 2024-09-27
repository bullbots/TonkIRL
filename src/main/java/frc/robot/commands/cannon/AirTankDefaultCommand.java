// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.cannon;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.AirTank;
import frc.team1891.common.LazyDashboard;

public class AirTankDefaultCommand extends CommandBase {
  double desiredPressure = 60;
  private final DoubleSupplier getLeftDial;
  private final AirTank airTank;
  /**
   * Creates a command that regulates the pressure of the AirTank
   */
  public AirTankDefaultCommand(AirTank airTank, DoubleSupplier getLeftDial) {
    addRequirements(airTank);
    this.airTank = airTank;
    this.getLeftDial = getLeftDial;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //double desiredPressure = LazyDashboard.addNumber("AirTank/Current Pressure", 60);
    //double desiredPressure = SmartDashboard.getNumber("AirTank/Desired Pressure", 40);

    double slope = (Constants.CannonConstants.MAX_FIRING_PRESSURE-Constants.CannonConstants.MIN_FIRING_PRESSURE)/2;
    double yInt = (Constants.CannonConstants.MAX_FIRING_PRESSURE-Constants.CannonConstants.MIN_FIRING_PRESSURE)/2;
    double desiredPressure = slope * (getLeftDial.getAsDouble() - yInt);
    airTank.setDesiredPressure(desiredPressure);
    
    //LazyDashboard.addNumber("AirTank", ()-> desiredPressure);
    System.out.print("desired presure " + desiredPressure);
    
    
    if ((airTank.getCurrentPressure() < airTank.getDesiredPressure()) && (airTank.getCurrentPressure() >= -2)) {
      airTank.openSolenoid();
    } else {
      airTank.closeSolenoid();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // open solenoid for safety, so we don't accidentally build pressure
    airTank.openSolenoid();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
