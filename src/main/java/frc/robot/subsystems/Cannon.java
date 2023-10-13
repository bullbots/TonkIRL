// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CannonConstants;
import frc.team1891.common.LazyDashboard;

public class Cannon extends SubsystemBase {
  private static Cannon instance;
  public static Cannon getInstance() {
    if (instance == null) {
      instance = new Cannon();
    }
    return instance;
  }

  public enum Barrel {
    TOP_LEFT(new Solenoid(PneumaticsModuleType.CTREPCM, CannonConstants.TOP_LEFT_CHANNEL)),
    TOP_RIGHT(new Solenoid(PneumaticsModuleType.CTREPCM, CannonConstants.TOP_RIGHT_CHANNEL)),
    BOTTOM_LEFT(new Solenoid(PneumaticsModuleType.CTREPCM, CannonConstants.BOTTOM_LEFT_CHANNEL)),
    BOTTOM_RIGHT(new Solenoid(PneumaticsModuleType.CTREPCM, CannonConstants.BOTTOM_RIGHT_CHANNEL));

    private final Solenoid solenoid;

    private Barrel(Solenoid solenoid) {
      this.solenoid = solenoid;
    }
  }
  private Cannon() {

    LazyDashboard.addBoolean("Cannon/Top Left Open", () -> Barrel.TOP_LEFT.solenoid.get());
    LazyDashboard.addBoolean("Cannon/Top Right Open", () -> Barrel.TOP_RIGHT.solenoid.get());
    LazyDashboard.addBoolean("Cannon/Bottom Left Open", () -> Barrel.BOTTOM_LEFT.solenoid.get());
    LazyDashboard.addBoolean("Cannon/Bottom Right Open", () -> Barrel.BOTTOM_RIGHT.solenoid.get());
    setPulseDuration(.06);
  }

  public void open(Barrel barrel) {
    barrel.solenoid.set(true);
  }

  public void close(Barrel barrel) {
    barrel.solenoid.set(false);
  }

  public void fire(Barrel barrel) {
    setPulseDuration(SmartDashboard.getNumber("AirTank/Pulse Duration", .08));
    barrel.solenoid.startPulse();
    System.out.printf("Shot out of '%s' barrel.\n", barrel.name());
    
  }

  public void setPulseDuration(double durationSeconds) {
    Barrel.TOP_LEFT.solenoid.setPulseDuration(durationSeconds);
    Barrel.TOP_RIGHT.solenoid.setPulseDuration(durationSeconds);
    Barrel.BOTTOM_LEFT.solenoid.setPulseDuration(durationSeconds);
    Barrel.BOTTOM_RIGHT.solenoid.setPulseDuration(durationSeconds);
  }

  @Override
  public void periodic() {}
}
