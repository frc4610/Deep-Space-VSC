/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4610.robot.Robot;


public class Intake extends Command {

  private String object;
  IntakeTailUp fix = new IntakeTailUp();

  public Intake(String Object) {
    //Intakes a hatch or Cargo
    this.object = Object;
    requires(Robot.intake);
    requires(Robot.tail);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.interrupt = true;
    if(object.equals("Cargo"))
        {
            Robot.intake.setIntake(.5);
        }
        else if(object.equals("Hatch"))
        {
            Robot.tail.tailMove(.5);
        }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if(object.equals("Hatch"))
    {
      return !Robot.m_oi.buttonL3.get();
    }
    else if(object.equals("Cargo"))
    {
      return !Robot.m_oi.buttonR3.get();//||Robot.intake.isCargoIn();
    }
    else
    {
      return true;
    }
    
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    if(object.equals("Cargo"))
        {
            Robot.intake.setIntake(0);
        }
        else if(object.equals("Hatch"))
        {
            Robot.tail.tailMove(0);//add new command to re put tail up
            fix.start();
        }
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    if(object.equals("Cargo"))
        {
            Robot.intake.setIntake(0);
        }
        else if(object.equals("Hatch"))
        {
          Robot.tail.tailMove(0);
        }
  }
}
