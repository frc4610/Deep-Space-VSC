/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Add your docs here.
 */
public class HabClimber extends Subsystem {

  private DoubleSolenoid frontDS;
  private DoubleSolenoid rearDS;

  public HabClimber(int frontFirst, int frontSecond, int rearFirst, int rearSecond)
  {
    frontDS = new DoubleSolenoid(1, frontFirst, frontSecond);
    rearDS = new DoubleSolenoid(1, rearFirst, rearSecond);
  }

  public void frontClimb()
  {
    frontDS.set(DoubleSolenoid.Value.kForward);
  }
  public void frontRetract()
  {
    frontDS.set(DoubleSolenoid.Value.kReverse);
  }
  public void rearClimb()
  {
    rearDS.set(DoubleSolenoid.Value.kForward);
  }
  public void rearRetract()
  {
    rearDS.set(DoubleSolenoid.Value.kReverse);
  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
