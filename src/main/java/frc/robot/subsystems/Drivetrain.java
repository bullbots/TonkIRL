// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DrivetrainConstants;

public class Drivetrain extends SubsystemBase {
  private static Drivetrain instance = null;
  public static Drivetrain getInstance() {
    if (instance == null) {
      instance = new Drivetrain();
    }
    return instance;
  }

  private final WPI_TalonSRX leftMaster, leftSlave, rightMaster, rightSlave;

  private final DifferentialDrive diffDrive;

  public Drivetrain() {
    leftMaster  = new WPI_TalonSRX(DrivetrainConstants.LEFT_MASTER_ID);
    leftSlave   = new WPI_TalonSRX(DrivetrainConstants.LEFT_SLAVE_ID);
    rightMaster = new WPI_TalonSRX(DrivetrainConstants.RIGHT_MASTER_ID);
    rightSlave  = new WPI_TalonSRX(DrivetrainConstants.RIGHT_SLAVE_ID);

    leftMaster.configFactoryDefault();
    leftSlave.configFactoryDefault();
    rightMaster.configFactoryDefault();
    rightSlave.configFactoryDefault();

    diffDrive = new DifferentialDrive(leftMaster, rightMaster);
    diffDrive.setSafetyEnabled(false);

    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster); 
    leftMaster.setInverted(true);
    leftSlave.setInverted(InvertType.FollowMaster);
    rightMaster.setInverted(false);
    rightSlave.setInverted(InvertType.FollowMaster);
  }

  public void arcade(double x, double y) {
    diffDrive.arcadeDrive(x, y);
  }

  public void stop() {
    leftMaster.stopMotor();
    rightMaster.stopMotor();
  }
}
