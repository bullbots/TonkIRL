// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CannonConstants;
import frc.robot.commands.cannon.FireBarrel;
import frc.team1891.common.LazyDashboard;
import frc.team1891.common.hardware.AnalogPressureSensor;

public class AirTank extends SubsystemBase {
  private static AirTank instance = null;
  public static AirTank getInstance() {
    if (instance == null) {
      instance = new AirTank();
    }
    return instance;
  }

  private final AnalogPressureSensor pressureSensor = new AnalogPressureSensor(CannonConstants.PRESSURE_SENSOR_PORT, .5, 4.5, 0, 100);
  private final Solenoid pressureRegulator = new Solenoid(PneumaticsModuleType.CTREPCM, CannonConstants.PRESSURE_REGULATOR_CHANNEL);

  private double desiredPressure;

  private AirTank() {
    // Start with pressure of 0
    setDesiredPressure(50);
    SmartDashboard.putNumber("AirTank/Desired Pressure", 40);
    SmartDashboard.putNumber("AirTank/Pulse Duration", FireBarrel.PULSE_DURATION);
    LazyDashboard.addNumber("AirTank/Current Pressure", pressureSensor::getPressure);
    LazyDashboard.addNumber("AirTank/Raw Voltage of Pressure Sensor", pressureSensor::getVoltage);
    LazyDashboard.addBoolean("AirTank/Regulator Valve Open", pressureRegulator::get);
  }

  /**
   * Sets the desired pressure in the shooting tank.
   */
  public void setDesiredPressure(double pressure) {
    // SmartDashboard.putNumber("AirTank/Desired Pressure", pressure);
    desiredPressure = pressure;
  }

  /**
   * Returns the desired pressure in the shooting tank.
   */
  public double getDesiredPressure() {
    // return SmartDashboard.getNumber("AirTank/Desired Pressure", -1);
    return desiredPressure;
  }

  // public boolean atDesiredPressure() {
  //   return (getPressure() >= getDesiredPressure() && getPressure() <= getDesiredPressure() * 1.5);
  // }

  /**
   * Returns current pressure in the shooting tank.
   */
  public double getCurrentPressure() {
    return pressureSensor.getPressure();
  }

  /**
   * Opens the solenoid to let air into the shooting tank
   */
  public void openSolenoid() {
    pressureRegulator.set(true);
  }

  /**
   * Opens the solenoid to let air into the shooting tank
   */
  public void closeSolenoid() {
    pressureRegulator.set(false);
  }

  @Override
  public void periodic() {}
}
