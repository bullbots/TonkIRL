// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.util.RadioController.SwitchState;
import frc.team1891.common.LazyDashboard;

/** Add your docs here. */
public class SimRadioController extends Joystick implements RadioControllerInterface {
    public SimRadioController(final int port) {
        super(port);

        int interval = 5; // every 5 run cycles each value will update.
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
    public double getLeftX() {
        return getRawAxis(0);
    }

    @Override
    public double getLeftY() {
        return getRawAxis(1);
    }

    @Override
    public double getRightX() {
        return getRawAxis(2);
    }

    @Override
    public double getRightY() {
        return getRawAxis(3);
    }

    @Override
    public double getCH5() {
        return getRawAxis(4);
    }

    @Override
    public SwitchState getRightSwitch() {
        double _ch5 = getCH5();
        if (Math.abs(_ch5) < .7) {
            return SwitchState.MID;
        } else if (_ch5 > 0) {
            return SwitchState.HIGH;
        } else {
            return SwitchState.LOW;
        }
    }

    @Override
    public double getCH6() {
        return getRightButton() ? 1 : 0;
    }

    @Override
    public boolean getRightButton() {
        return getRawButton(1);
    }

    @Override
    public double getCH7() {
        return getRawAxis(5);
    }

    @Override
    public SwitchState getLeftSwitch() {
        double _ch7 = getCH7();
        if (Math.abs(_ch7) < .7) {
            return SwitchState.MID;
        } else if (_ch7 > 0) {
            return SwitchState.HIGH;
        } else {
            return SwitchState.LOW;
        }
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
