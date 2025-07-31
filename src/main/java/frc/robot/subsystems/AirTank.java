// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CannonConstants;
import frc.robot.Logger1891;
import frc.robot.Robot;
import frc.team1891.common.LazyDashboard;
import frc.team1891.common.hardware.AnalogPressureSensor;

public class AirTank extends SubsystemBase {
  public static final double MAX_PRESSURE = 80;

  private static AirTank instance = null;
  public static AirTank getInstance() {
    if (instance == null) {
      instance = new AirTank();
    }
    Logger1891.info("AirTank getInstance");
    return instance;
  }

  private final AnalogPressureSensor pressureSensor = new AnalogPressureSensor(CannonConstants.PRESSURE_SENSOR_PORT, .5, 4.5, 0, 100) {
    // Invert pressure just in case the sensor was installed wrong.
    @Override
    public double getPressure() {
      return Math.abs(super.getPressure());
    };
  };
  private final Solenoid pressureRegulator = new Solenoid(PneumaticsModuleType.CTREPCM, CannonConstants.PRESSURE_REGULATOR_CHANNEL);

  private double desiredPressure;

   AirTank() {
    // Start with pressure of 0
    setDesiredPressure(0);
    SmartDashboard.putNumber("AirTank/Desired Pressure", 0);
    SmartDashboard.putNumber("AirTank/Pulse Duration", Cannon.DEFAULT_PULSE_DURATION);
    if (Robot.isReal()) {
      LazyDashboard.addNumber("AirTank/Current Pressure", pressureSensor::getPressure);
    } else {
      SmartDashboard.putNumber("AirTank/Current Pressure", 0);
    }
    LazyDashboard.addNumber("AirTank/Raw Voltage of Pressure Sensor", pressureSensor::getVoltage);
    LazyDashboard.addBoolean("AirTank/Regulator Valve Open", pressureRegulator::get);
  }

  /**
   * Sets the desired pressure in the shooting tank.
   */
  public void setDesiredPressure(double pressure) {
    SmartDashboard.putNumber("AirTank/Desired Pressure", pressure);
    desiredPressure = pressure;
    // Logger1891.info("AirTank setPressure");
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
    // return Robot.isReal() ? pressureSensor.getPressure() : SmartDashboard.getNumber("AirTank/Current Pressure", 0);
  }

  /**
   * Opens the solenoid to let air into the shooting tank
   */
  public void openSolenoid() {
    pressureRegulator.set(true);
    Logger1891.info("AirTank openSolenoid");
  }

  /**
   * Opens the solenoid to let air into the shooting tank
   */
  public void closeSolenoid() {
    pressureRegulator.set(false);
    Logger1891.info("AirTank closeSolenoid");
  }
  //is true when the valve is open
  public boolean isOpen(){
    return pressureRegulator.get();
  }

  @Override
  public void periodic() {}
}
