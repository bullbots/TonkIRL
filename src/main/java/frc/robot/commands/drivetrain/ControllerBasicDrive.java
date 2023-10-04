// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class ControllerBasicDrive extends CommandBase {
  private final Drivetrain drivetrain;
  private final DoubleSupplier xSupplier, ySupplier;
  public ControllerBasicDrive(Drivetrain drivetrain, DoubleSupplier xSupplier, DoubleSupplier ySupplier) {
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
    this.xSupplier = xSupplier;
    this.ySupplier = ySupplier;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Double scale = SmartDashboard.getNumber("DriveTrain/Turning Scale", .5);
    drivetrain.arcade(MathUtil.applyDeadband(xSupplier.getAsDouble(), .1), MathUtil.applyDeadband(ySupplier.getAsDouble() * scale, .1));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
