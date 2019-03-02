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
private Timer timer;

    public place(String Object, boolean Auto) {
        //places a hatch or cargo
        this.object = Object;
        this.auto = Auto;
        this.timer = new Timer();
        requires(Robot.intake);
        //requires(Robot.tail);

    	//This always requires both systems, since if both were to be controlled we'd have to be manipulting two game pieces. May want to change later
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
            Robot.intake.setIntake(-.9);
        }
        else if(object.equals("Hatch")&&!(auto&&Robot.interrupt))
        {
            //Robot.tail.eject();
        }
        timer.start();
        
    	//check to see if safe, stop drive base
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//needed commands to make move
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (Robot.interrupt || timer.get() >= .5) && auto;
        //return timer.get() >= .5||(Robot.interrupt && auto);//older return after 5 seconds in tele, kept here if we want to revert back to original
    }

    // Called once after isFinished returns true
    protected void end() {
        if(object.equals("Cargo"))
        {
            Robot.intake.setIntake(0);
        }
        else if(object.equals("Hatch"))
        {
            //Robot.tail.resetEject();
        }
        timer.stop();
        timer.reset();
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
            //Robot.tail.resetEject();
        }
        timer.stop();
        timer.reset();
    }
}
