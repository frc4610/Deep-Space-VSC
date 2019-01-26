/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
/**
 * Add your docs here.
 */
public class Lidar extends Subsystem {
  //note: old code is from https://github.com/WHS-FRC-3467/Skip-5.5/blob/master/src/org/usfirst/frc3467/subsystems/LIDAR/LIDAR.java and has since been deleted, but this link has been kept
  //code is from https://github.com/Barlow-Robotics/BarlowRobotics-2018-Code/blob/master/src/org/usfirst/frc/team4572/robot/subsystems/LIDARSubsystem.java and is somewhat good
  
  private static final int CALIBRATION_OFFSET = -9;//originally -6
  private Counter counter;
  //private int printedWarningCount = 5;//commented out because I'm lazy
  
  public Lidar(DigitalInput source) {
    //source is the port  of DigitalInput the LIDAR is plugged into
    counter = new Counter(source);
    counter.setMaxPeriod(.5);
    // Configure for measuring rising to falling pulses, read screensteps for more in depth info
    counter.setSemiPeriodMode(true);
    counter.reset();

}

public double getDistanceCm(boolean rounded) {
  double cm;
  // If we haven't seen the first rising to falling pulse, then we have no measurement.
  // This happens when there is no LIDAR-Lite plugged in, btw.
  if (counter.get() < 1) {
    return 0;
  }
  // getPeriod returns time in seconds. The hardware resolution is microseconds.
  // The LIDAR-Lite unit sends a high signal for 10 microseconds per cm of distance.
  cm = (counter.getPeriod() * 1000000.0 / 10.0) + CALIBRATION_OFFSET;
  if(!rounded) {
  return cm;
  }else {
     return  Math.floor(cm*10)/10;
  }
}

 public double getDistanceIn(boolean rounded) {
    	double in = getDistanceCm(true) * 0.393700787;
    	if(!rounded) {
    	return in;
    	}else {
    	return  Math.floor(in*10)/10;
    	}
}

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
