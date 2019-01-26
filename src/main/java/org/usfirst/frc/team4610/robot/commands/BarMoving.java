/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.commands;

import org.usfirst.frc.team4610.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class BarMoving extends Command {

private double speed;

  public BarMoving(double Speed) {
    //moves bar based on inputted speed
    this.speed = Speed;
    requires(Robot.bar);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.interrupt = true;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.bar.setBar(speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    //stops when button is released. change later to set to the limit switches or enc values.
    if(speed < 0)
    {
      return !Robot.m_oi.buttonL1.get();
    }
    else if(speed > 0)
    {
      return !Robot.m_oi.buttonR1.get();
    }
    else
    {
      return true;
    }
    
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.bar.setBar(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.bar.setBar(0);
  }
}
