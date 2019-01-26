/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
/*import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Timer;
import java.util.TimerTask;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;*/
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.DigitalSource;
/**
 * Add your docs here.
 */
public class Lidar extends Subsystem {
  //note: code is from https://github.com/WHS-FRC-3467/Skip-5.5/blob/master/src/org/usfirst/frc3467/subsystems/LIDAR/LIDAR.java and may be broken at parts. Test further
  //Commented code is from https://github.com/Barlow-Robotics/BarlowRobotics-2018-Code/blob/master/src/org/usfirst/frc/team4572/robot/subsystems/LIDARSubsystem.java and is for pwm
  /*private I2C i2c;
	private static byte[] distance;
  private java.util.Timer updater;
  private LIDARUpdater task;	
  
	private final int LIDAR_ADDR = 0x62;
	private final int LIDAR_CONFIG_REGISTER = 0x00;
  private final int LIDAR_DISTANCE_REGISTER = 0x8f;*/
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  
  private static final int CALIBRATION_OFFSET = -9;//originally -6
  private Counter counter;
  //private int printedWarningCount = 5;
  
  public Lidar(/*Port port*/DigitalInput source) {
    
    counter = new Counter(source);
    counter.setMaxPeriod(.5);
    // Configure for measuring rising to falling pulses
    counter.setSemiPeriodMode(true);
    counter.reset();
    

  //top is pwm, bottom is i2c

		/*i2c = new I2C(Port.kMXP, LIDAR_ADDR);
		
    distance = new byte[2];
    
    task = new LIDARUpdater();
		updater = new java.util.Timer();*/
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


//top is pwm, bottom is i2c

/*
public static int getDistance() {
  return (int)Integer.toUnsignedLong(distance[0] << 8) + Byte.toUnsignedInt(distance[1]);//returns in cm
}

public static double getDistanceIn()
{
  return getDistance() / 2.54;
}

public void start() {
  updater.scheduleAtFixedRate(task, 0, 20);
}

public void update() {
  i2c.write(LIDAR_CONFIG_REGISTER, 0x04); // Initiate measurement
  Timer.delay(0.04); // Delay for measurement to be taken
  i2c.read(LIDAR_DISTANCE_REGISTER, 2, distance); // Read in measurement
  Timer.delay(0.01); // Delay to prevent over polling
}

public void stop() {
  updater.cancel();
}

private class LIDARUpdater extends TimerTask 
{
  public void run() 
  {
    while(true) 
    {
      update();
      SmartDashboard.putNumber("LIDAR distance Inches", getDistanceIn());// this and the try section may be deleted if proven unneeded
      try 
      {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
*/
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
