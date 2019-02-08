package org.usfirst.frc.team4610.robot.subsystems;

import org.usfirst.frc.team4610.robot.Robot;
import org.usfirst.frc.team4610.robot.commands.tankDrive;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
//import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveBase extends Subsystem {

	private TalonSRX FRightMotor;//fr
	private VictorSPX BRightMotorFollow;//br
	private TalonSRX BLeftMotor;//bl
	private VictorSPX FLeftMotorFollow;//fl

	
	public DriveBase() {
		//sets up motors and encoders for talons
		this.FRightMotor = new TalonSRX(1); 
		this.BLeftMotor = new TalonSRX(3);
		this.BRightMotorFollow = new VictorSPX(2); 
		this.FLeftMotorFollow = new VictorSPX(4);
		Robot.initTalonBrake(BLeftMotor);
		Robot.initTalonBrake(FRightMotor);
		Robot.initTalonBrake(FLeftMotorFollow);
		Robot.initTalonBrake(BRightMotorFollow);
		FRightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		FRightMotor.setSelectedSensorPosition(0, 0 ,10);
		FRightMotor.setInverted(true);
		/*FRightMotor.selectProfileSlot(0, 0);//not quite sure on the values, 2nd should be 0, look at section 10 on https://content.vexrobotics.com/vexpro/pdf/SoftwareReferenceManual20180113.pdf
		FRightMotor.config_kF(0, .2, 200);//200 ms timeout, see later.
		FRightMotor.config_kP(0, .2, 200);//in theory the control mode position should set it to a specific enc value.
		FRightMotor.config_kI(0, 0, 200);
		FRightMotor.config_kD(0, 0, 200);
		BLeftMotor.selectProfileSlot(0, 0);
		BLeftMotor.config_kF(0, .2, 200);//200 ms timeout, see later.
		BLeftMotor.config_kP(0, .2, 200);
		BLeftMotor.config_kI(0, 0, 200);
		BLeftMotor.config_kD(0, 0, 200);*/
		BRightMotorFollow.setInverted(true);
		//FRightMotor.configClosedloopRamp(1, 0);
		BLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		BLeftMotor.setSelectedSensorPosition(0, 0 ,10);
		//BLeftMotor.configClosedloopRamp(1, 0);
		FLeftMotorFollow.follow(BLeftMotor);
		BRightMotorFollow.follow(FRightMotor);
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public void set(ControlMode mode, double leftvalue, double rightvalue) {
		//sets motors based on inversion
		if(Robot.front == 0)
		{
		BLeftMotor.set(mode, leftvalue);
		FRightMotor.set(mode, rightvalue);
		}
		else if(Robot.front == 1)
		{
		BLeftMotor.set(mode, rightvalue);
		FRightMotor.set(mode, leftvalue);
		}

	}
	
	public double getEncValue(boolean motor)//if motor is true, find right motor, false gets left. Returns encoder value
	{
		if(motor)
		{
			return FRightMotor.getSelectedSensorPosition(0);
		}
		else
		{
			return BLeftMotor.getSelectedSensorPosition(0);

		}
	}
	
	public void resetEnc(int motor)//0 is right, 1 is left, 2 is both
	{
		if(motor == 0)
		{
			FRightMotor.setSelectedSensorPosition(0, 0 ,10);
		}
		else if(motor == 1)
		{
			BLeftMotor.setSelectedSensorPosition(0, 0 ,10);
		}
		else if(motor == 2)
		{
			FRightMotor.setSelectedSensorPosition(0, 0 ,10);
			BLeftMotor.setSelectedSensorPosition(0, 0 ,10);
		}
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new tankDrive());
    }
}

