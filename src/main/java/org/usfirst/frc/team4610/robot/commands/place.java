package org.usfirst.frc.team4610.robot.commands;

import org.usfirst.frc.team4610.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class place extends Command {

private String object;
private boolean auto;

    public place(String Object, boolean Auto) {
        //places a hatch or cargo
        this.object = Object;
        this.auto = Auto;
        requires(Robot.intake);
        requires(Robot.tail);
    	//this.Object = object;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if(!auto)
        {
            Robot.interrupt = true;
        }
        if(object.equals("Cargo"))
        {
            Robot.intake.setIntake(-.5);
        }
        else if(object.equals("Hatch"))
        {
            Robot.tail.eject();
        }
        Timer.delay(.5);
    	//check to see if safe, stop drive base
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//needed commands to make move
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;//sensor place/ mechanism moved || Robot.interrupted;
    }

    // Called once after isFinished returns true
    protected void end() {
        if(object.equals("Cargo"))
        {
            Robot.intake.setIntake(0);
        }
        else if(object.equals("Hatch"))
        {
            Robot.tail.resetEject();
        }
    	//stop all moving parts save for drive base
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        if(object.equals("Cargo"))
        {
            Robot.intake.setIntake(0);
        }
        else if(object.equals("Hatch"))
        {
            Robot.tail.resetEject();
        }
    }
}
