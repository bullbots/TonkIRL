// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Lifter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Logger1891;
import frc.robot.subsystems.Lifter;

public class DefaultLifterCommand extends CommandBase {
  private final Lifter lifter;
  private final DoubleSupplier inputSupplier;
  /** Creates a new DefaultLifterCommand. */
  public DefaultLifterCommand(Lifter lifter, DoubleSupplier input) {
    addRequirements(lifter);
    this.lifter = lifter;
    inputSupplier = input;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("lifter");
    Logger1891.info("in lifter initialize");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    lifter.setLiftPower(inputSupplier.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
