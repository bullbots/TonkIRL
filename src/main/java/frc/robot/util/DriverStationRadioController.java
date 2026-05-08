// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.util.RadioController.SwitchState;
import frc.team1891.common.LazyDashboard;

/**
 * A {@link RadioControllerInterface} implementation backed by the WPILib
 * {@link DriverStation}. Works with any FRC-DS-protocol client: the official
 * NI DriverStation on a laptop with a USB gamepad, the Tonk Android Driver
 * Station, or any other tool that speaks DS UDP on port 1110.
 *
 * <p>Channel mapping (designed to work out-of-the-box with an Xbox-style pad
 * via the NI DS, while still letting the Android app send a richer payload):
 * <ul>
 *   <li>Axis 0  &rarr; leftX  (drive turn)</li>
 *   <li>Axis 1  &rarr; leftY  (drive forward; FRC convention -1 = forward)</li>
 *   <li>Axis 4  &rarr; rightX</li>
 *   <li>Axis 5  &rarr; rightY (cannon raise/lower)</li>
 *   <li>Axis 6  &rarr; left dial (-1..1, mapped to PSI). Xbox pads don't have
 *       a 7th axis, so {@code getRawAxis(6)} returns 0 there &mdash; meaning a
 *       laptop user defaults to ~50% PSI unless they remap.</li>
 *   <li>Button 1 (A)  &rarr; FIRE (rightButton)</li>
 *   <li>Button 2 (B)  &rarr; right switch HIGH (debug LED mode)</li>
 *   <li>Button 3 (X)  &rarr; right switch LOW</li>
 * </ul>
 *
 * <p>The "left switch" (controller-side enable in the Arduino flow) is
 * derived from {@link DriverStation#isEnabled()} so legacy code paths that
 * gate on {@code getLeftSwitch() == HIGH} still behave sanely. In FRC-DS
 * mode the spoofer is bypassed and Enable/Disable is owned by the DS itself.
 */
public class DriverStationRadioController extends Joystick implements RadioControllerInterface {
    private static final double SWITCH_DEADBAND = 0.5;

    public DriverStationRadioController(final int port) {
        super(port);

        int interval = 5;
        LazyDashboard.addBoolean("RadioController/connected", interval, this::isConnected);
        LazyDashboard.addNumber("RadioController/getRightX", interval, this::getRightX);
        LazyDashboard.addNumber("RadioController/getRightY", interval, this::getRightY);
        LazyDashboard.addNumber("RadioController/getLeftY", interval, this::getLeftY);
        LazyDashboard.addNumber("RadioController/getLeftX", interval, this::getLeftX);
        LazyDashboard.addString("RadioController/getRightSwitch-CH5", interval, () -> this.getRightSwitch().toString());
        LazyDashboard.addBoolean("RadioController/getRightButton-CH6", interval, this::getRightButton);
        LazyDashboard.addString("RadioController/getLeftSwitch-CH7", interval, () -> this.getLeftSwitch().toString());
        LazyDashboard.addNumber("RadioController/getLeftDial-CH8", interval, this::getLeftDial);
    }

    @Override
    public boolean isConnected() {
        return DriverStation.isDSAttached() && DriverStation.isJoystickConnected(getPort());
    }

    @Override
    public double getLeftX() {
        return getRawAxis(0);
    }

    @Override
    public double getLeftY() {
        return getRawAxis(1);
    }

    @Override
    public double getRightX() {
        return getRawAxis(4);
    }

    @Override
    public double getRightY() {
        return getRawAxis(5);
    }

    @Override
    public double getCH5() {
        if (getRawButton(2)) return 1.0;
        if (getRawButton(3)) return -1.0;
        return 0.0;
    }

    @Override
    public SwitchState getRightSwitch() {
        double v = getCH5();
        if (Math.abs(v) < SWITCH_DEADBAND) return SwitchState.MID;
        return v > 0 ? SwitchState.HIGH : SwitchState.LOW;
    }

    @Override
    public double getCH6() {
        return getRightButton() ? 1.0 : 0.0;
    }

    @Override
    public boolean getRightButton() {
        return getRawButton(1);
    }

    @Override
    public double getCH7() {
        // Driven by DS Enable so legacy gating logic keeps working.
        return DriverStation.isEnabled() ? 1.0 : 0.0;
    }

    @Override
    public SwitchState getLeftSwitch() {
        double v = getCH7();
        if (Math.abs(v) < SWITCH_DEADBAND) return SwitchState.MID;
        return v > 0 ? SwitchState.HIGH : SwitchState.LOW;
    }

    @Override
    public double getCH8() {
        return getRawAxis(6);
    }

    @Override
    public double getLeftDial() {
        return getCH8();
    }
}
