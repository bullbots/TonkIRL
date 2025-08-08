# TonkIRL
 Code Repo for our T-Shirt Cannon

# Controls (RadioController)
- **Enable Robot:** Left switch HIGH.
- **Drive:** Left joystick.
- **Adjust Desired Cannon PSI:** Left dial.
- **Raise and Lower Cannon:** Right stick up and down.
- **Shoot Cannon:** Right button.
- **LED Debug Mode:** Right switch HIGH.

# LEDs

When not in debug mode, the robot will flash the bullbot colors when disabled, and animate a rainbow when it is enabled.

# Light Codes (Debug Mode)

There are 4 LED sections. Two on the top left and right, and two on the bottom front and back.  Each section shows a different of the following indicators.

Debug Mode can be activated by putting the right switch on the RadioController in HIGH.

## Pressure Indicator
Updates according to the desired and current pressure, shown as white and blue progress bars.  The desired pressure is adjusted by the dial on the RadioController.

## Voltage Indicator
Updates according to the voltage of the robot's battery, as a progress bar between 0 and 14 volts. The color appear green when the voltage is 12+, white when 10+, and red when voltage drops below 10.

## Controller Connection Indicator
- **Flashing Red:** The Radio Controller is disconnected (The Debug Mode will be activated and the robot will disable automatically when the controller disconnects).
- **Bright Green:** The robot is enabled.
- **Flashing Green:** The on-controller enable switch is active, but the on-robot enable button is not.
- **Dim Green:** The controller is connected and the on-controller enable switch is inactive

## Enable Status Indicator
- **Flashing Red:** Both the on-robot and on-controller enable switches are active, but the Driverstation did not enable.
- **Bright Green:** The Robot is enabled.
- **White:** The robot is disabled.
- **Pulsing White:** The on-robot enable button is active, and waiting for the on-controller enable switch to be activated to enable the robot.