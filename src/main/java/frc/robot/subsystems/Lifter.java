package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lifter extends SubsystemBase{
    public static Lifter instance;
    public static Lifter getInstance(){
        if(instance == null){
            instance = new Lifter();
        }
        return instance;
    }
    private final VictorSPX liftMotor;

    public Lifter(){
        //TODO change can ID
        liftMotor = new VictorSPX(0);
        liftMotor.set(ControlMode.PercentOutput, 0);
    }

    public void setLiftPower(double pow){
        liftMotor.set(ControlMode.PercentOutput, pow);
    }
}
