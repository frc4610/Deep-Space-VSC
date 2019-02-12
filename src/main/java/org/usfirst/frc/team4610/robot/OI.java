/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot;



import org.usfirst.frc.team4610.robot.commands.BarMoving;
import org.usfirst.frc.team4610.robot.commands.Intake;
//import org.usfirst.frc.team4610.robot.commands.IntakeTailUp;
import org.usfirst.frc.team4610.robot.commands.place;
import org.usfirst.frc.team4610.robot.commands.CrossIntake;
import org.usfirst.frc.team4610.robot.commands.CrossRelease;
import org.usfirst.frc.team4610.robot.commands.DrivePneums;
//import org.usfirst.frc.team4610.robot.commands.GripIntake;
//import org.usfirst.frc.team4610.robot.commands.GripRelease;
//import org.usfirst.frc.team4610.robot.commands.CIntPneums;
//import org.usfirst.frc.team4610.robot.commands.fBarMoveToPos;
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
	public  Joystick OP_JOY = new Joystick(2);
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
	public Button buttonO1 = new JoystickButton(OP_JOY, 1);
	public Button buttonO2 = new JoystickButton(OP_JOY, 2);
	public Button buttonO3 = new JoystickButton(OP_JOY, 3);
	public Button buttonO4 = new JoystickButton(OP_JOY, 4);
	public Button buttonO5 = new JoystickButton(OP_JOY, 5);
	public Button buttonO6 = new JoystickButton(OP_JOY, 6);
	public Button buttonO7 = new JoystickButton(OP_JOY, 7);
	public Button buttonO8 = new JoystickButton(OP_JOY, 8);
	public Button buttonO9 = new JoystickButton(OP_JOY, 9);
	public Button buttonO10 = new JoystickButton(OP_JOY, 10);
	public Button buttonO12 = new JoystickButton(OP_JOY, 12);
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
			buttonL3.whenPressed(new DrivePneums(1)); // 1 is high gear? test
			buttonL4.whenPressed(new DrivePneums(0)); // 0 is low gear? test
			buttonR3.whenPressed(new Intake("Cargo"));
			buttonR4.whileHeld(new place("Cargo", false)); //whileHeld interrupts once released, may want to change function back to original if this is undesired, along with whenPressed
			//removed functions
			//buttonR11.whenPressed(new CIntPneums(true));
			//buttonR12.whenPressed(new CIntPneums(false));
			//buttonR3.whenPressed(new Invert(0));//normal
			//buttonR4.whenPressed(new Invert(1));//inverted
		}
		else if (driver.equals("N"))
		{
		}
		if(operator.equals("N"))
		{
			buttonO3.whenPressed(new BarMoving(-.1)); //bar also should be operator, look at cross' comment
			buttonO4.whenPressed(new BarMoving(.1)); 
			buttonO2.whenPressed(new Intake("Hatch"));//may want to combine this with tail up
			//buttonO6.whileHeld(new IntakeTailUp()); currently in intake already, test further
			buttonO1.whileHeld(new place("Hatch", false));//whileHeld interrupts once releassed, may want to change function back to original if this is undesired, along with whenPressed
			buttonO5.whenPressed(new CrossIntake());//crossbow should be operator controlled, but we either need to consildate its functions or lower fbar positions, or somehow change overall scheme
			buttonO6.whenPressed(new CrossRelease());// a possible to solution to button problem could be a button to switch between tail/cintake control
			//buttonO9.whenPressed(new GripIntake());// current in place solution would be to set 3 fbar positions and to recompress the crossbow functions.
			//buttonO8.whenPressed(new GripRelease());
			/* commented until placeholders are set to be used, may want to set it to be on the right contrller while testing, later we can combine functions.
			buttonO7.whenPressed(new fBarMoveToPos(Robot.fbarPosBot));
			buttonO8.whenPressed(new fBarMoveToPos(Robot.fbarPos2));
			buttonO9.whenPressed(new fBarMoveToPos(Robot.fbarPosTop);
			buttonO10.whenPressed(new fBarMoveToPos(Robot.fbarPos3));
			buttonO12.whenPressed(new fBarMoveToPos(Robot.fbarPos4));
			*/
		}
		
	}
}
