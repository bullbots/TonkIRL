// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class DriverStationSpoofer {
    static {
        System.out.println("WARNING: DriverStationSpoofer is not FRC legal.  Do not use in a match, and use with caution.");
    }

    private DriverStationSpoofer() {}

    private static Thread thread;

    private static boolean isSpoofing = false;

    private static void generateEnabledDsPacket(byte[] data, short sendCount) {
        data[0] = (byte) (sendCount >> 8);
        data[1] = (byte) sendCount;
        data[2] = 0x01; // general data tag
        data[3] = 0x04; // teleop enabled
        data[4] = 0x10; // normal data request
        data[5] = 0x00; // red 1 station
    }

    /**
     * Enables the robot in teleop from Red Alliance station 1.
     */
    public static void enable() {
        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(() -> {
            DatagramSocket socket;
            try {
                socket = new DatagramSocket();
            } catch (SocketException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return;
            }
            InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 1110);
            byte[] sendData = new byte[6];
            DatagramPacket packet = new DatagramPacket(sendData, 0, 6, addr);
            short sendCount = 0;
            int initCount = 0;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(20);
                    generateEnabledDsPacket(sendData, sendCount++);
                    // ~50 disabled packets are required to make the robot actually enable
                    // 1 is definitely not enough.
                    if (initCount < 50) {
                        initCount++;
                        // change data code to tell robot to be disabled
                        sendData[3] = 0;
                    }
                    packet.setData(sendData);
                    socket.send(packet);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket.close();
        });
        // Because of the test setup in Java, this thread will not be stopped
        // So it must be a daemon thread
        thread.setDaemon(true);
        thread.start();
        isSpoofing = true;
    }

    /**
     * Disables the robot.
     */
    public static void disable() {
        if (thread == null) {
            return;
        }
        thread.interrupt();
        try {
            thread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isSpoofing = false;
    }

    public static boolean isEnabled() {
        return isSpoofing;
    }
}
