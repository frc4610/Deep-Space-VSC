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
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class FourBar extends Subsystem {
  private VictorSPX fbV;
  private TalonSRX fbT;
  private DigitalInput fbLimitTop;
  private DigitalInput fbLimitBot;

  public FourBar(int topPort, int botPort)
  {
    //first is the 1st port the ds is plugged into, second is the second. topPort is the port for the limit switch on top, and botPort is the bottom limit switch. Pretty self-explanitory
    this.fbT = new TalonSRX(6);
    this.fbV = new VictorSPX(7);
    Robot.initTalonBrake(fbT);
    Robot.initTalonBrake(fbV);
    fbT.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 , 10);
    fbT.setSelectedSensorPosition(0, 0 ,10);
    fbV.setInverted(true);
    //fbV.follow(fbT);
    this.fbLimitTop = new DigitalInput(topPort);
    this.fbLimitBot = new DigitalInput(botPort);
  }
  
  public void setBar(double speed)
  {
    fbV.set(ControlMode.PercentOutput, speed);//is inverted, test to check if works
    fbT.set(ControlMode.PercentOutput, speed);
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
  public double getEncValue()
  {
    return fbT.getSelectedSensorPosition();
  }

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
