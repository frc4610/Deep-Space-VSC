package org.usfirst.frc.team4610.robot.commands;

import org.usfirst.frc.team4610.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class forward extends Command {

	private double encValue;
	private double distR;
	private double distL;
	
    public forward(double distance, double DistL, double DistR) {
    	//go forward with different or same speeds on each side
    	requires(Robot.driveBase);
    	this.encValue = distance;
    	this.distR = DistR;
    	this.distL = DistL;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveBase.resetEnc(2);
    	//stop extraneous moving parts
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.driveBase.set(ControlMode.Position, distL, distR);
    	//move at a speed after testing. may need to be a variable
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(encValue >= 0)
    	{
    		return Robot.driveBase.getEncValue(true) >= encValue;//is encValue at wanted value (compare to current encoders), or Robot.interrupted
    	}
    	else
    	{
    		return Robot.driveBase.getEncValue(true) <= encValue;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveBase.resetEnc(2);
    	Robot.driveBase.set(ControlMode.PercentOutput, 0, 0);
    	//turn off motors
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.driveBase.resetEnc(2);
    }
}
