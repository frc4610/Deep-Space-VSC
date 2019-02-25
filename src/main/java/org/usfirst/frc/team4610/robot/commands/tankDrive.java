package org.usfirst.frc.team4610.robot.commands;

import org.usfirst.frc.team4610.robot.Robot;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class tankDrive extends Command {
	
	public tankDrive(){
		requires(Robot.driveBase);
	}
	
	public void initialize(){
		
	}
	
	public void execute() {
		//double throttle = ( 1.0 -Robot.m_oi.LEFT_JOY.getThrottle()) / -2.0;
		//top is w/o curve, bottom has curve
		//Robot.driveBase.set(ControlMode.PercentOutput, -Robot.m_oi.CON/*LEFT_JOY*/.getRawAxis(1), -Robot.m_oi.CON/*RIGHT_JOY*/.getRawAxis(3));//remember joysticks automatically return inverted values
		Robot.driveBase.set(ControlMode.PercentOutput, (Robot.curveA*Robot.curveX*Robot.curveX*Robot.curveX) + ((1-Robot.curveA)*Robot.curveX), (Robot.curveA*Robot.curveY*Robot.curveY*Robot.curveY) + ((1-Robot.curveA)*Robot.curveY));
	}
	
	
	protected boolean isFinished() {

		return false;
	}
	
	protected void end() {
		
	}
	protected void interupted() {
		end();
	}
}
