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
        while (!Thread.currentThread().isInterrupted()) {
            final String read = reader.read();
            SmartDashboard.putString("reader.read()", read);
            String[] values = read.split(",");
            SmartDashboard.putString("values[0]", values[0]);
            if (values.length == 1) {
                if (DriverStationSpoofer.isEnabled()) {
                    DriverStationSpoofer.disable();
                }
                
                x.set(0);
                y.set(0);
            } else {
                x.set(Integer.parseInt(values[0]));
                y.set(Integer.parseInt(values[1]));
            }
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
