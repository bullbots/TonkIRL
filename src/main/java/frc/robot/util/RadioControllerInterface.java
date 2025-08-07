package frc.robot.util;

import frc.robot.util.RadioController.SwitchState;

public interface RadioControllerInterface {
    /**
     * @return if the controller is connnected
     */
    public boolean isConnected();
    
    /**
     * @return x axis of left stick
     */
    public double getLeftX();

    /**
     * @return y axis of left stick
     */
    public double getLeftY();

    /**
     * @return x axis of right stick
     */
    public double getRightX();

    /**
     * @return y axis of right stick
     */
    public double getRightY();

    /**
     * @return raw value of CH5 (right switch)
     */
    public double getCH5();

    /**
     * @return state of right three state switch (CH5)
     */
    public SwitchState getRightSwitch();

    /**
     * @return raw value of CH6 (right button)
     */
    public double getCH6();

    /**
     * @return if the right button is pressed (CH6)
     */
    public boolean getRightButton();

    /**
     * @return raw value of CH7 (left switch)
     */
    public double getCH7();
    
    /**
     * @return state of left three state switch (CH7)
     */
    public SwitchState getLeftSwitch();

    /**
     * @return raw value of CH8 (left dial)
     */
    public double getCH8();

    /**
     * @return value of the left dial [-1,1] (CH8)
     */
    public double getLeftDial();
}