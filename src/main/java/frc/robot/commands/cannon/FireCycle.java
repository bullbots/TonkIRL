// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.cannon;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.Cannon.Barrel;

public class FireCycle extends InstantCommand {
  private final Cannon cannon;
  private Barrel currentBarrel;

  public FireCycle(Cannon cannon) {
    addRequirements(cannon);
    this.cannon = cannon;
    this.currentBarrel = Barrel.TOP_LEFT;

    //this.cannon.setPulseDuration(FireBarrel.PULSE_DURATION);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    cannon.fire(currentBarrel);
    currentBarrel = next(currentBarrel);
  }

  /** Returns the next barrel in the cycle. */
  private static Barrel next(Barrel barrel) {
    switch (barrel) {
      case TOP_LEFT: return Barrel.TOP_RIGHT;
      case TOP_RIGHT: return Barrel.BOTTOM_RIGHT;
      case BOTTOM_RIGHT: return Barrel.BOTTOM_LEFT;
      case BOTTOM_LEFT: return Barrel.TOP_LEFT;
      default: return null;
    }
  }
}
