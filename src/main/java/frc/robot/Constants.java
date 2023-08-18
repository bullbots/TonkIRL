// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public class Constants {
    public static class DrivetrainConstants {
        // TODO
        public static final int LEFT_MASTER_ID = 3;
        public static final int LEFT_SLAVE_ID = 4;
        public static final int RIGHT_MASTER_ID = 1;
        public static final int RIGHT_SLAVE_ID = 2;
    }
    public static class CannonConstants {
        /**
         * 2     4
         * |_____|
         * |  |  |
         * 3  |  1
         *    |
         *  [Tank]
         *    |
         *    7____[Compressor and Tank]
         */

        public static final int TOP_LEFT_CHANNEL = 1;
        public static final int TOP_RIGHT_CHANNEL = 3;
        public static final int BOTTOM_LEFT_CHANNEL = 2;
        public static final int BOTTOM_RIGHT_CHANNEL = 0;
        public static final int PRESSURE_REGULATOR_CHANNEL = 7;

        public static final int PRESSURE_SENSOR_PORT = 0;

        public static final int MAX_PRESSURE = 100;
    }
}
