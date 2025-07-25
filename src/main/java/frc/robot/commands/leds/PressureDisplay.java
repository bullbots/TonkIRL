// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.leds;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.AirTank;
import frc.robot.subsystems.LEDs;
import frc.team1891.common.led.LEDStripInterface;
import frc.team1891.common.led.LEDStripPattern;

public class PressureDisplay extends CommandBase {
  private final LEDStripInterface leds;

  private final LEDStripPattern pressureIndicatorPattern = new LEDStripPattern() {
    private final AirTank tank = AirTank.getInstance();
    public void draw(LEDStripInterface leds) {
      leds.clear();

      double currentPressure = tank.getCurrentPressure(), desiredPressure = tank.getDesiredPressure();
      //System.out.println("current pressure "+currentPressure);
      if (currentPressure > desiredPressure * 1.4 || currentPressure > AirTank.MAX_PRESSURE) {
        // set leds red if too high
        leds.setRangeRGB(0, (int) (leds.length() * (currentPressure / AirTank.MAX_PRESSURE)), 150, 0, 0);
      } else {
        // shows in white the desired pressure bar, and on top of it in Bullbots blue the actual pressure
        leds.setRangeRGB(0, (int) (leds.length() * (desiredPressure / AirTank.MAX_PRESSURE)), 150, 150, 150);
        leds.setRangeRGB(0, (int) (leds.length() * (currentPressure / AirTank.MAX_PRESSURE)), 18, 0, 222);
      }
    }
  };
  
  /** Creates a new PressureDisplay. */
  public PressureDisplay(LEDs ledsSubsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    // addRequirements(ledsSubsystem);
    this.leds = ledsSubsystem.topSegment;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    pressureIndicatorPattern.run(leds);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    leds.off();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
