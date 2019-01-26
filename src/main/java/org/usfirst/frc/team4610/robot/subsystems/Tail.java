/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.subsystems;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Tail extends Subsystem {
  private DoubleSolenoid tailDS34;
  private DoubleSolenoid ejectDS56;
  private DigitalInput tailLimit;
  public Tail(int firstT, int secondT, int firstE, int secondE,int limitPort)
  {
    //first is the 1st port the ds is plugged into, second is the second. The E and T for these are for the eject and tail mechanisms respectively. limitPort is the port for the limit switch. Pretty self-explanitory
    this.tailDS34 = new DoubleSolenoid(0, firstT, secondT);
    this.ejectDS56 = new DoubleSolenoid(0, firstE, secondE);
    this.tailLimit = new DigitalInput(1);//use testingLimit.get() to find value. Should return true when open, false when pressed
  }

  public void tailDown()
  {
    tailDS34.set(DoubleSolenoid.Value.kForward);
  }
  public void tailUp()
  {
    tailDS34.set(DoubleSolenoid.Value.kReverse);
  }
  public void eject()
  {
    ejectDS56.set(DoubleSolenoid.Value.kForward);
  }
  public void resetEject()
  {
    ejectDS56.set(DoubleSolenoid.Value.kReverse);
  }
  public boolean isUp()
  {
    return !tailLimit.get();
  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
