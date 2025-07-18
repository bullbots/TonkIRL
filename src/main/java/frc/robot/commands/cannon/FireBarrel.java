// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.cannon;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Logger1891;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.Cannon.Barrel;

public class FireBarrel extends InstantCommand {
  public static final double PULSE_DURATION = .06;

  public FireBarrel(Cannon cannon, Barrel barrel) {
    super(() -> cannon.fire(barrel));
    Logger1891.info("FireBarrel");
    addRequirements(cannon);

    //cannon.setPulseDuration(PULSE_DURATION);
  }
}
