/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot;



import org.usfirst.frc.team4610.robot.commands.BarMoving;
import org.usfirst.frc.team4610.robot.commands.Intake;
import org.usfirst.frc.team4610.robot.commands.IntakeTailUp;
import org.usfirst.frc.team4610.robot.commands.place;
import org.usfirst.frc.team4610.robot.commands.CrossIntake;
import org.usfirst.frc.team4610.robot.commands.CrossRelease;
import org.usfirst.frc.team4610.robot.commands.CIntPneums;

//import org.usfirst.frc.team4610.robot.commands.Invert;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//mapping of joysticks
	public  Joystick LEFT_JOY = new Joystick(0);
	public  Joystick RIGHT_JOY = new Joystick(1);
	public  Joystick BACKUP_JOY = new Joystick(2);
	public Button buttonR1 = new JoystickButton(RIGHT_JOY, 1);
	public Button buttonR3 = new JoystickButton(RIGHT_JOY, 3);
	public Button buttonR4 = new JoystickButton(RIGHT_JOY, 4);
	public Button buttonR5 = new JoystickButton(RIGHT_JOY, 5);
	public Button buttonR6 = new JoystickButton(RIGHT_JOY, 6);
	public Button buttonR7 = new JoystickButton(RIGHT_JOY, 7);
	public Button buttonR8 = new JoystickButton(RIGHT_JOY, 8);
	public Button buttonR11 = new JoystickButton(RIGHT_JOY, 11);
	public Button buttonR12 = new JoystickButton(RIGHT_JOY, 12);
	public Button buttonL1 = new JoystickButton(LEFT_JOY, 1);
	public Button buttonL3 = new JoystickButton(LEFT_JOY, 3);
	public Button buttonL4 = new JoystickButton(LEFT_JOY, 4);
	public Button buttonL5 = new JoystickButton(LEFT_JOY, 5);
	//public Button buttonL1 = new JoystickButton(LEFT_JOY, 1);
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	public OI(String driver,String operator) {
		//different diver control schemes
		if(driver.equals("W"))
		{
			buttonL1.whenPressed(new BarMoving(-.5)); 
			buttonL3.whenPressed(new Intake("Hatch")); 
			buttonL5.whenPressed(new IntakeTailUp());
			buttonL4.whenPressed(new place("Hatch", false)); 
			buttonR1.whenPressed(new BarMoving(.5)); 
			buttonR3.whenPressed(new Intake("Cargo"));
			buttonR4.whenPressed(new place("Cargo", false)); 
			buttonR5.whenPressed(new CrossIntake());
			buttonR6.whenPressed(new CrossRelease());
			buttonR7.whenPressed(new CrossIntake());
			buttonR8.whenPressed(new CrossRelease());
			buttonR11.whenPressed(new CIntPneums(true));
			buttonR12.whenPressed(new CIntPneums(false));

			//buttonR3.whenPressed(new Invert(0));//normal
			// buttonR4.whenPressed(new Invert(1));//inverted
		}
		else if (driver.equals("N"))
		{
		}
		if(operator.equals("N"))
		{
			
		}
		
	}
}
