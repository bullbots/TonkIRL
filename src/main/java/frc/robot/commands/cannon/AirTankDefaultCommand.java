// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.cannon;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Logger1891;
import frc.robot.subsystems.AirTank;

public class AirTankDefaultCommand extends CommandBase {
  // private double desiredPressure = 60;
  private final DoubleSupplier desiredPressureSupplier;
  private final AirTank airTank;
  private final Double tolerance = 5.0;
  
  /**
   * Creates a command that regulates the pressure of the AirTank, with a default desired pressure of 60
   */
  public AirTankDefaultCommand(AirTank airTank) {
    this(airTank, () -> 60);
  }

  /**
   * Creates a command that regulates the pressure of the AirTank
   */
  public AirTankDefaultCommand(AirTank airTank, DoubleSupplier desiredPressureSupplier) {
    addRequirements(airTank);
    this.airTank = airTank;
    this.desiredPressureSupplier = desiredPressureSupplier;
    Logger1891.info("AirtankDefault command");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double desiredPressure = desiredPressureSupplier.getAsDouble();
    //double desiredPressure = LazyDashboard.addNumber("AirTank/Current Pressure", 60);
    // double desiredPressure = SmartDashboard.getNumber("AirTank/Desired Pressure", 40);
    airTank.setDesiredPressure(desiredPressure);
    
    // System.out.print("desired presure " + desiredPressure);
    
    if(airTank.isOpen()){
      if(airTank.getCurrentPressure() > airTank.getDesiredPressure()){
        airTank.closeSolenoid();
      }
    }else{
      if(airTank.getCurrentPressure() <= airTank.getDesiredPressure() - tolerance){
        airTank.openSolenoid();
      }
    }

    // if ((airTank.getCurrentPressure() < airTank.getDesiredPressure()) && (airTank.getCurrentPressure() >= -2)) {
    //   airTank.openSolenoid();
    // } else {
    //   airTank.closeSolenoid();
    // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // open solenoid for safety, so we don't accidentally build pressure
    airTank.openSolenoid();
    Logger1891.info("AirtankDefault end");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
