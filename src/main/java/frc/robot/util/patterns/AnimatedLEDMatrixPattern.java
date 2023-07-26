// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util.patterns;

import java.util.List;

import org.opencv.core.Mat;

import frc.team1891.common.led.LEDMatrixInterface;
import frc.team1891.common.led.LEDMatrixPattern;

public class AnimatedLEDMatrixPattern implements LEDMatrixPattern {
    public enum RunType {
        ONCE,
        CONTINUOUS
    }

    private final RunType runType;
    private final int fps;
    private final List<Mat> frames;
    
    private long startTime;
    private int currentFrame;

    public AnimatedLEDMatrixPattern(int fps, List<Mat> frames) {
        this(RunType.CONTINUOUS, fps, frames);
    }

    public AnimatedLEDMatrixPattern(RunType runType, int fps, List<Mat> frames) {
        this.runType = runType;
        this.fps = fps;
        this.frames = frames;
        this.currentFrame = frames.size() + 1; // in order to trigger reset upon first call to draw
    }

    @Override
    public void draw(LEDMatrixInterface leds) {
        leds.setMatrixRGB(frames.get(currentFrame));

        double currentTime = (System.currentTimeMillis() - startTime) / 1000.;
        currentFrame = (int) (currentTime * fps);
    }

    @Override
    public void run(LEDMatrixInterface leds) {
        if (runType.equals(RunType.CONTINUOUS) && isFinished()) {
            reset();
        }

        LEDMatrixPattern.super.run(leds);
    }

    @Override
    public boolean isFinished() {
        return currentFrame >= frames.size();
    }

    @Override
    public void reset() {
        startTime = System.currentTimeMillis();
        currentFrame = 0;
    }
}
