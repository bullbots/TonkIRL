// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import java.util.concurrent.atomic.AtomicInteger;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RadioController {
    private final AtomicInteger x = new AtomicInteger(0), y = new AtomicInteger(0);

    private final Thread thread = new Thread(() -> {
        final SerialPort port = new SerialPort(9600, Port.kUSB1);
        final SerialReader reader = new SerialReader(port);

        int disconnectedCounter = 0;
        // long time = 0;
        while (!Thread.currentThread().isInterrupted()) {
            // time = System.currentTimeMillis();
            final String read = reader.read();
            // SmartDashboard.putString("reader.read()", read);
            String[] values = read.split(",");
            // SmartDashboard.putString("values[0]", values[0]);
            // SmartDashboard.putNumber("agh", disconnectedCounter);
            if (values.length == 1) {
                disconnectedCounter++;
                if (disconnectedCounter > 5) {
                    if (DriverStationSpoofer.isEnabled()) {
                        DriverStationSpoofer.disable();
                    }
                    x.set(0);
                    y.set(0);
                    SmartDashboard.putBoolean("controller disconnected", true);
                }
            } else {
                disconnectedCounter = 0;
                x.set(Integer.parseInt(values[0]));
                y.set(Integer.parseInt(values[1]));
                SmartDashboard.putBoolean("controller disconnected", false);
            }
            // SmartDashboard.putNumber("threadTime", (System.currentTimeMillis() - time));
            // System.out.println((System.currentTimeMillis() - time));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }

        port.close();
    });

    public RadioController() {
        thread.setDaemon(true);
        thread.start();
    }


    public double getX() {
        return (x.get() / 100.);
    }

    public double getY() {
        return (y.get() / 100.);
    }
}
