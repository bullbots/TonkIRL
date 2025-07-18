// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.cannon;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Logger1891;
import frc.robot.subsystems.AirTank;
import frc.team1891.common.LazyDashboard;

public class AirTankDefaultCommand extends CommandBase {
  double desiredPressure = 60;
  private final AirTank airTank;
  private final Double Tolerance = 5.0;
  /**
   * Creates a command that regulates the pressure of the AirTank
   */
  public AirTankDefaultCommand(AirTank airTank) {
    addRequirements(airTank);
    this.airTank = airTank;
    Logger1891.info("AirtankDefault command");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //double desiredPressure = LazyDashboard.addNumber("AirTank/Current Pressure", 60);
    double desiredPressure = SmartDashboard.getNumber("AirTank/Desired Pressure", 40);
    airTank.setDesiredPressure(desiredPressure);
    
    //LazyDashboard.addNumber("AirTank", ()-> desiredPressure);
    System.out.print("desired presure " + desiredPressure);
    
    if(airTank.isOpen()){
      if(airTank.getCurrentPressure() > airTank.getDesiredPressure()){
        airTank.closeSolenoid();
      }
    }else{
      if(airTank.getCurrentPressure() <= airTank.getDesiredPressure() - Tolerance){
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
