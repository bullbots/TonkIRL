// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RadioController {
    private final AtomicInteger x = new AtomicInteger(0), y = new AtomicInteger(0);

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
                    x.set(0);
                    y.set(0);
                    SmartDashboard.putBoolean("controller disconnected", true);
                    isConnected.set(false);
                }
            } else {
                disconnectedCounter = 0;
                x.set(Integer.parseInt(values[0]));
                y.set(Integer.parseInt(values[1]));
                SmartDashboard.putBoolean("controller disconnected", false);
                isConnected.set(true);
            }
            // sleep the thread because otherwise the while loop will run faster than the serial buffer can keep up with
            // this delay makes it more likely to read a complete message each loop and not report the controller as disconnected
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                if (DriverStationSpoofer.isEnabled()) {
                    DriverStationSpoofer.disable();
                }
            }
        }

        port.close();
    });

    public RadioController() {
        serialReaderThread.setDaemon(true);
        serialReaderThread.start();
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public double getX() {
        return (x.get() / 100.);
    }

    public double getY() {
        return (y.get() / 100.);
    }
}
