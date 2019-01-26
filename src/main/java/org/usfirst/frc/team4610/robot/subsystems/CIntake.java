/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import org.usfirst.frc.team4610.robot.Robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class CIntake extends Subsystem {
  private VictorSPX intakeV;
  private DigitalInput intakeLimit;
  private DoubleSolenoid intakeDS78;

  public CIntake(int first, int second, int limitPort)
  {
    this.intakeDS78 = new DoubleSolenoid(first, second);
    this.intakeV = new VictorSPX(5);//5 is the motor address
    this.intakeLimit = new DigitalInput(limitPort);
    Robot.initTalonBrake(intakeV);
  }

  public void setIntake(double speed)
  {
    intakeV.set(ControlMode.PercentOutput, speed);
  }
  public void cInAdjustF()
  {
    intakeDS78.set(DoubleSolenoid.Value.kForward);
  }
  public void cInAdjustR()
  {
    intakeDS78.set(DoubleSolenoid.Value.kReverse);
  }
  public boolean isCargoIn()
  {
    return !intakeLimit.get();
  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
