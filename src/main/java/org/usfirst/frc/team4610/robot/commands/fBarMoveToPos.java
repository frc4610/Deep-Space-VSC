/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import org.usfirst.frc.team4610.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class fBarMoveToPos extends Command {
  private double pos;
  public fBarMoveToPos(double Pos) {
    requires(Robot.bar);
    this.pos = Pos;
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
      Robot.bar.setBar(ControlMode.Position, pos);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if(pos > Robot.bar.getEncValue())
    {
      return pos >= Robot.bar.getEncValue();
    }
    else
    {
      return pos <= Robot.bar.getEncValue();
    }
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.bar.setBar(ControlMode.PercentOutput, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.bar.setBar(ControlMode.PercentOutput, 0);
  }
}
