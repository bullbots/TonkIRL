// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CannonConstants;
import frc.robot.util.AnalogPressureSensor;
import frc.team1891.common.LazyDashboard;

public class AirTank extends SubsystemBase {
  private static AirTank instance = null;
  public static AirTank getInstance() {
    if (instance == null) {
      instance = new AirTank();
    }
    return instance;
  }

  private final AnalogPressureSensor pressureSensor = new AnalogPressureSensor(CannonConstants.PRESSURE_SENSOR_PORT, .5, 4.5, 0, 100);;
  private final Solenoid pressureRegulator = new Solenoid(PneumaticsModuleType.CTREPCM, CannonConstants.PRESSURE_REGULATOR_CHANNEL);

  private AirTank() {
    // Start with pressure of 0
    setDesiredPressure(0);

    LazyDashboard.addNumber("AirTank/Current Pressure", pressureSensor::getPressure);
    LazyDashboard.addNumber("AirTank/Raw Voltage of Pressure Sensor", pressureSensor::getVoltage);
    LazyDashboard.addBoolean("AirTank/Regulator Valve Open", pressureRegulator::get);
  }

  public void setDesiredPressure(double pressure) {
    SmartDashboard.putNumber("AirTank/Desired Pressure", pressure);
  }

  public double getDesiredPressure() {
    return SmartDashboard.getNumber("AirTank/Desired Pressure", -1);
  }

  public double getPressure() {
    return pressureSensor.getPressure();
  }

  public void openSolenoid() {
    pressureRegulator.set(true);
  }

  public void closeSolenoid() {
    pressureRegulator.set(false);
  }

  @Override
  public void periodic() {}
}
