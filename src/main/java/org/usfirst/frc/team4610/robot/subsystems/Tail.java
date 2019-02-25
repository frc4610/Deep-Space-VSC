/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import org.usfirst.frc.team4610.robot.Robot;
import org.usfirst.frc.team4610.robot.commands.IntakeTailUp;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Tail extends Subsystem {
  private TalonSRX tailT;
  private DoubleSolenoid ejectDS45;
  private DigitalInput tailLimit;
  public Tail(int firstE, int secondE,int limitPort)
  {
    //first is the 1st port the ds is plugged into, second is the second. The E and T for these are for the eject and tail mechanisms respectively. limitPort is the port for the limit switch. Pretty self-explanitory
    this.tailT = new TalonSRX(8);
    Robot.initTalonBrake(tailT);
    tailT.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    tailT.setSelectedSensorPosition(0, 0 ,10);
    this.ejectDS45 = new DoubleSolenoid(0, firstE, secondE);
    this.tailLimit = new DigitalInput(1);//use testingLimit.get() to find value. Should return true when open, false when pressed
  }

  public void tailMove(double speed)
  {
    tailT.set(ControlMode.PercentOutput, speed);
  }
  public void eject()
  {
    ejectDS45.set(DoubleSolenoid.Value.kReverse);
  }
  public void resetEject()
  {
    ejectDS45.set(DoubleSolenoid.Value.kForward);
  }
  public boolean isUp()
  {
    return !tailLimit.get();
  }
  public double getEncValue()
  {
    return tailT.getSelectedSensorPosition();
  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new IntakeTailUp(true));
  }
}
