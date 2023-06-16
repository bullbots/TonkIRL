// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import edu.wpi.first.wpilibj.PWM;
import frc.team1891.common.LazyDashboard;

public class PWMReader {
    public static void startReading() {
        for (int i = 10; i <= 19; i++) {
            PWM port = new PWM(i);
            LazyDashboard.addNumber("PWM port: " + i, () -> port.getRaw());
        }
    }
}
