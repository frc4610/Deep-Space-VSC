/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import org.usfirst.frc.team4610.robot.Robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class FourBar extends Subsystem {
  private VictorSPX fbV;
  private TalonSRX fbT;
  private DoubleSolenoid fbDS34;
  private DigitalInput fbLimitTop;
  private DigitalInput fbLimitBot;

  public FourBar(int first, int second, int topPort, int botPort)
  {
    this.fbT = new TalonSRX(6);
    this.fbV = new VictorSPX(7);
    Robot.initTalonBrake(fbT);
    Robot.initTalonBrake(fbV);
    fbT.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 , 10);
    fbT.setSelectedSensorPosition(0, 0 ,10);
    //fbV.follow(fbT);
    this.fbDS34 = new DoubleSolenoid(1, first, second);
    this.fbLimitTop = new DigitalInput(topPort);
    this.fbLimitBot = new DigitalInput(botPort);
  }
  
  public void setBar(double speed)
  {
    fbV.set(ControlMode.PercentOutput, -speed);//is inverted, test to check if
    fbT.set(ControlMode.PercentOutput, speed);
  }
  public void barHighG()
  {
    fbDS34.set(DoubleSolenoid.Value.kForward);
  }
  public void barLowG()
  {
    fbDS34.set(DoubleSolenoid.Value.kReverse);
  }
  public boolean barTop()
  {
    return !fbLimitTop.get();
  }
  public boolean barBot()
  {
    return !fbLimitBot.get();
  }
  public void resetBEnc()
  {
    fbT.setSelectedSensorPosition(0, 0 ,10);
  }

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
