/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.commands;

import org.usfirst.frc.team4610.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeTailUp extends Command {


  public IntakeTailUp() {
    requires(Robot.tail);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.tail.tailMove(-.25);//if this proves to be problematic, switch to position control mode. is currently at this because it really isnt too terrible important to be precise
    Robot.interrupt = true;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return  Robot.tail.isUp() ||  Robot.tail.getEncValue() <= 0;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.tail.tailMove(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.tail.tailMove(0);
  }
}
