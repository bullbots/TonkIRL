// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1891.common.LazyDashboard;
import frc.team1891.illegal.driverstation.DriverStationSpoofer;

/**
 * RadioLink T8S
 */
public class RadioController {
    public enum SwitchState {
        LOW,
        MID,
        HIGH
    }
    
    private final AtomicInteger 
        rightX = new AtomicInteger(0), 
        rightY = new AtomicInteger(0),
        leftY = new AtomicInteger(0),
        leftX = new AtomicInteger(0),
        ch5 = new AtomicInteger(0),
        ch6 = new AtomicInteger(0),
        ch7 = new AtomicInteger(0),
        ch8 = new AtomicInteger(0);

    private final AtomicBoolean isConnected = new AtomicBoolean(false);

    private final Thread serialReaderThread = new Thread(() -> {
        final SerialPort port = new SerialPort(9600, Port.kUSB1);
        final SerialReader reader = new SerialReader(port);

        int disconnectedCounter = 0;
        while (!Thread.currentThread().isInterrupted()) {
            final String read = reader.read();
            String[] values = read.split(",");
            if (values.length == 1) {
                disconnectedCounter++;
                if (disconnectedCounter > 5) {
                    // TODO: Safety Check
                    // if (DriverStationSpoofer.isEnabled()) {
                    //     DriverStationSpoofer.disable();
                    // }
                    rightX.set(0);
                    rightY.set(0);
                    leftY.set(0);
                    leftX.set(0);
                    ch5.set(0);
                    ch6.set(0);
                    ch7.set(0);
                    ch8.set(0);
                    SmartDashboard.putBoolean("RadioController/controller connected", false);
                }
            } else {
                disconnectedCounter = 0;
                rightX.set(Integer.parseInt(values[0]));
                rightY.set(Integer.parseInt(values[1]));
                leftY.set(Integer.parseInt(values[2]));
                leftX.set(Integer.parseInt(values[3]));
                ch5.set(Integer.parseInt(values[4]));
                ch6.set(Integer.parseInt(values[5]));
                ch7.set(Integer.parseInt(values[6]));
                ch8.set(Integer.parseInt(values[7]));
                SmartDashboard.putBoolean("RadioController/controller connected", true);
            }
            // sleep the thread because otherwise the while loop will run faster than the serial buffer can keep up with
            // this delay makes it more likely to read a complete message each loop and not report the controller as disconnected
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                if (DriverStationSpoofer.isEnabled()) {
                    DriverStationSpoofer.disable();
                }
                e.printStackTrace();
            }
        }

        port.close();
    });

    public RadioController() {
        serialReaderThread.setDaemon(true);
        serialReaderThread.start();

        LazyDashboard.addNumber("RadioController/getRightX", 1, this::getRightX);
        LazyDashboard.addNumber("RadioController/getRightY", 1, this::getRightY);
        LazyDashboard.addNumber("RadioController/getLeftY", 1, this::getLeftY);
        LazyDashboard.addNumber("RadioController/getLeftX", 1, this::getLeftX);
        LazyDashboard.addNumber("RadioController/getCH5", 1, this::getCH5);
        LazyDashboard.addNumber("RadioController/getCH6", 1, this::getCH6);
        LazyDashboard.addNumber("RadioController/getCH7", 1, this::getCH7);
        LazyDashboard.addNumber("RadioController/getCH8", 1, this::getCH8);
    }

    /**
     * @return if the controller is connnected
     */
    // TODO
    public boolean isConnected() {
        return isConnected.get();
    }

    /**
     * @return x axis of left stick
     */
    public double getLeftX() {
        return (leftX.get() / 100.);
    }

    /**
     * @return y axis of left stick
     */
    public double getLeftY() {
        return (leftY.get() / 100.);
    }

    /**
     * @return x axis of right stick
     */
    public double getRightX() {
        return (rightX.get() / 100.);
    }

    /**
     * @return y axis of right stick
     */
    public double getRightY() {
        return (rightY.get() / 100.);
    }

    /**
     * @return raw value of CH5
     */
    public double getCH5() {
        return (ch5.get() / 100.);
    }

    /**
     * @return state of right three state switch
     */
    public SwitchState getRightSwitch() {
        if (Math.abs(getCH5()) < 20) {
            return SwitchState.MID;
        } else if (getCH5() > 0) {
            return SwitchState.HIGH;
        } else {
            return SwitchState.LOW;
        }
    }

    /**
     * @return raw value of CH6
     */
    public double getCH6() {
        return (ch6.get() / 100.);
    }

    /**
     * @return if the right button is pressed
     */
    public boolean getRightButton() {
        return getCH6() > 0;
    }

    /**
     * @return raw value of CH7
     */
    public double getCH7() {
        return (ch7.get() / 100.);
    }
    
    /**
     * @return state of left three state switch
     */
    public SwitchState getLeftSwitch() {
        if (Math.abs(getCH7()) < 20) {
            return SwitchState.MID;
        } else if (getCH7() > 0) {
            return SwitchState.HIGH;
        } else {
            return SwitchState.LOW;
        }
    }

    /**
     * @return raw value of CH8
     */
    public double getCH8() {
        return (ch8.get() / 100.);
    }

    /**
     * @return value of the left dial
     */
    public double getLeftDial() {
        return getCH8();
    }
}
