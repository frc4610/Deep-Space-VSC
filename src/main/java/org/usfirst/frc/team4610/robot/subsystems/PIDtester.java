/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
//import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
//import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.usfirst.frc.team4610.robot.Robot;
/**
 * Add your docs here.
 */
public class PIDtester extends PIDSubsystem {
  private Victor fbV;
  private TalonSRX fbT;
  /*private DoubleSolenoid fbDS34;
  private DigitalInput fbLimitTop;
  private DigitalInput fbLimitBot;*/
  //Ues fourbar as a reference
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public PIDtester(int first, int second, int topPort, int botPort) {
    super("PIDtester", 2.0, 0.0, 0.0);// The constructor passes a name for the subsystem and the P, I and D constants that are useed when computing the motor output
    setAbsoluteTolerance(0.05);
    getPIDController().setContinuous(false); //manipulating the raw internal PID Controller
    //this.fbT = new TalonSRX(8);
    //this.fbV = new Victor(2);
    Robot.initTalonBrake(fbT);
    //Robot.initTalonBrake(fbV); - fix this later dummy boi
    fbT.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 , 10);
    fbT.setSelectedSensorPosition(0, 0 ,10);
    // fbV.setInverted(true);
    //fbV.follow(fbT);
    /*this.fbDS34 = new DoubleSolenoid(1, first, second);
    this.fbLimitTop = new DigitalInput(topPort);
    this.fbLimitBot = new DigitalInput(botPort);*/
}

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  protected double returnPIDInput() {
    return fbT.getSelectedSensorPosition();
    //return pot.getAverageVoltage(); // returns the sensor value that is providing the feedback for the system
}

protected void usePIDOutput(double output) {
  fbV.pidWrite(output);
  fbT.set(ControlMode.PercentOutput, fbV.get());
   // motor.pidWrite(output); // this is where the computed output value fromthe PIDController is applied to the motor
}
public double getEncValue()
{
  return fbT.getSelectedSensorPosition();
}

}
