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
public class Crossbow extends Subsystem {

  private DoubleSolenoid gripDS15;
  private DoubleSolenoid crossDS26;
  public Crossbow(int gfirst, int gsecond, int cfirst, int csecond)
  {
    this.crossDS26 = new DoubleSolenoid(0, cfirst, csecond);
    this.gripDS15 = new DoubleSolenoid(0, gfirst, gsecond);
  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public void grip()
  {
    gripDS15.set(DoubleSolenoid.Value.kForward);
  }
  public void release()
  {
    gripDS15.set(DoubleSolenoid.Value.kReverse);
  }
  public void crossOut()
  {
    crossDS26.set(DoubleSolenoid.Value.kForward);
  }
  public void crossIn()
  {
    crossDS26.set(DoubleSolenoid.Value.kReverse);
  }
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}