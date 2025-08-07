// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.team1891.common.LazyDashboard;
import frc.team1891.illegal.driverstation.DriverStationSpoofer;

/**
 * RadioLink T8S
 */
public class RadioController implements RadioControllerInterface {
    public enum SwitchState {
        LOW,
        MID,
        HIGH;
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

    // This thread is constantly running and updating the values of the Radio Controller to 
    private final Thread serialReaderThread = new Thread(() -> {
        final SerialPort port = new SerialPort(9600, Port.kUSB1);
        final SerialReader reader = new SerialReader(port);

        int disconnectedCounter = 0;
        while (!Thread.currentThread().isInterrupted()) {
            final String read = reader.read();
            String[] values = read.split(",");
            if (values.length == 1 || Integer.parseInt(values[2]) < -120) {
                disconnectedCounter++;
                if (disconnectedCounter > 5) {
                    isConnected.set(false);
                    SmartDashboard.putBoolean("RadioController/controller connected", false);
                
                    rightX.set(0);
                    rightY.set(0);
                    leftY.set(0);
                    leftX.set(0);
                    ch5.set(0);
                    ch6.set(0);
                    ch7.set(0);
                    ch8.set(0);
                }
            } else {
                SmartDashboard.putString("RadioController/reader", read);
                isConnected.set(true);
                SmartDashboard.putBoolean("RadioController/controller connected", true);
                disconnectedCounter = 0;

                rightX.set(Integer.parseInt(values[0]));
                rightY.set(Integer.parseInt(values[1]));
                leftY.set(Integer.parseInt(values[2]));
                leftX.set(Integer.parseInt(values[3]));
                ch5.set(Integer.parseInt(values[4]));
                ch6.set(Integer.parseInt(values[5]));
                ch7.set(Integer.parseInt(values[6]));
                ch8.set(Integer.parseInt(values[7]));
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

        if (Robot.isReal()) {
            int interval = 5; // every 5 run cycles each value will update.
            LazyDashboard.addNumber("RadioController/getRightX", interval, this::getRightX);
            LazyDashboard.addNumber("RadioController/getRightY", interval, this::getRightY);
            LazyDashboard.addNumber("RadioController/getLeftY", interval, this::getLeftY);
            LazyDashboard.addNumber("RadioController/getLeftX", interval, this::getLeftX);
            LazyDashboard.addString("RadioController/getRightSwitch-CH5", interval, () -> this.getRightSwitch().toString());
            LazyDashboard.addBoolean("RadioController/getRightButton-CH6", interval, this::getRightButton);
            LazyDashboard.addString("RadioController/getLeftSwitch-CH7", interval, () -> this.getLeftSwitch().toString());
            LazyDashboard.addNumber("RadioController/getLeftDial-CH8", interval, this::getLeftDial);
        } else {
            SmartDashboard.putNumber("RadioController/getRightX", 0);
            SmartDashboard.putNumber("RadioController/getRightY", 0);
            SmartDashboard.putNumber("RadioController/getLeftY", 0);
            SmartDashboard.putNumber("RadioController/getLeftX", 0);
            SmartDashboard.putString("RadioController/getRightSwitch-CH5", "0");
            SmartDashboard.putBoolean("RadioController/getRightButton-CH6", false);
            SmartDashboard.putString("RadioController/getLeftSwitch-CH7", "0");
            SmartDashboard.putNumber("RadioController/getLeftDial-CH8", 0);
        }
    }

    // TODO
    public boolean isConnected() {
        return isConnected.get();
    }

    public double getLeftX() {
        return (leftX.get() / 100.);
    }

    public double getLeftY() {
        return (leftY.get() / 100.);
    }

    public double getRightX() {
        return (rightX.get() / 100.);
    }

    public double getRightY() {
        return (rightY.get() / 100.);
    }

    public double getCH5() {
        return (ch5.get() / 100.);
    }

    public SwitchState getRightSwitch() {
        double _ch5 = getCH5();
        if (Math.abs(_ch5) < .2) {
            return SwitchState.MID;
        } else if (_ch5 > 0) {
            return SwitchState.HIGH;
        } else {
            return SwitchState.LOW;
        }
    }

    public double getCH6() {
        return (ch6.get() / 100.);
    }

    public boolean getRightButton() {
        return getCH6() > .1;
    }

    public double getCH7() {
        return (ch7.get() / 100.);
    }

    public SwitchState getLeftSwitch() {
        double _ch7 = getCH7();
        if (Math.abs(_ch7) < 20) {
            return SwitchState.MID;
        } else if (_ch7 > 0) {
            return SwitchState.HIGH;
        } else {
            return SwitchState.LOW;
        }
    }

    public double getCH8() {
        return (ch8.get() / 100.);
    }

    public double getLeftDial() {
        // return getCH8();
        return Robot.isReal() ? getCH8() : SmartDashboard.getNumber("RadioController/getLeftDial-CH8", 0);
    }
}
