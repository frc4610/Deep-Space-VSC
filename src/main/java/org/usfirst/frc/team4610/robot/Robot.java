/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot;

import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4610.robot.commands.sandAutoBasic;
import org.usfirst.frc.team4610.robot.commands.tankDrive;
import org.usfirst.frc.team4610.robot.subsystems.DriveBase;
//import org.usfirst.frc.team4610.robot.subsystems.Lidar;
import org.usfirst.frc.team4610.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.cameraserver.CameraServer;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	//use https://stackoverflow.com/questions/46877667/how-to-push-a-new-initial-project-to-github-using-vs-code to connect to github
	public static Counter limCounter;
	public static DigitalInput testingLimit;
	public static double encMultiFt = 435; //Measure the distance the robot goes and its associated encoder value. Multiple feet wanted by this to get encoder value needed
	public static double encMultiIn = 36.25;//Enc value for inches. See above for how to use
	public static double encShopExtra = 400; //enc value the robot gains by slide. use acc/decel motor values to nullify
	public static double autoTimer;//tracks time in ms
	public static double autoTimeSec;//tracks time in seconds
	public static double autoSpeed;//default speed for drivebase in auto
	public static int front;//is the originally front still the front, for inversion
	public static boolean interrupt;//is the driver overriding auto?
	public static double acceptedTurnTolerance = 5;//obvious, for auto
	public static double acceptedJoyTolerance = 5;//sets it so that a small jolt doesn't stop auto
	public static DriveBase driveBase;
	//public static Lidar lidar;
	public static AHRS gyro;
	public static Preferences prefs;
	public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
	public static OI m_oi;
	public Servo testServo = new Servo(1);
	SendableChooser<String> position;
	SendableChooser<String> driver;
	SendableChooser<String> operator;
	Command autonomousCommand;
	Command tele;
	//SendableChooser<Command> m_chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//testServo.setBounds(2, 2, 1.5, 1, 1);//check values, min is 1, and max is 2, but other values are unknown. this is for am L-16, check for other acuators
		//testServo.setSpeed(1);//forward, check both functions
		//testServo.setSpeed(0);//reverse, check both functions
		//testServo.getPosition();//input for the acutator?
		//lidar = new Lidar(Port.kMXP);
		testingLimit = new DigitalInput(1);//use testingLimit.get() to find value. Should return true when open, false when pressed
		limCounter = new Counter(testingLimit);//use to find if value switched to quick, i.e. limCounter.get() > 0 means it was pressed. use limCounter.reset(); to set to 0
		driveBase = new DriveBase();
		tele = new tankDrive();
		CameraServer.getInstance().startAutomaticCapture();
		autoTimer = 0;
		autoTimeSec = 0;
		position = new SendableChooser<>();
		driver = new SendableChooser<>();
		operator = new SendableChooser<>();
		autoSpeed = .5;
		front = 0;
		interrupt = false;
		gyro = new AHRS(SPI.Port.kMXP);
		position.addOption("Left2", "L");//lower case is HAB 1, upper is HAB 2
		position.setDefaultOption("Middle", "m");
		position.addOption("Right2", "R");
		position.addOption("Left", "l");//should never be the case, but may be needed
		position.addOption("Right", "r");
		driver.setDefaultOption("Winte", "W");
		operator.setDefaultOption("Nathan", "N");
		SmartDashboard.putData("Position", position);
		SmartDashboard.putData("Driver", driver);
		SmartDashboard.putData("Operator", operator);
		//.getSelected to get value for smart dash board values
		m_oi = new OI(driver.getSelected(), operator.getSelected()); 
		prefs = Preferences.getInstance();
		driveBase.resetEnc(2);
		//m_chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		//SmartDashboard.putData("Auto mode", m_chooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		driveBase.resetEnc(2);
		//autonomousCommand = m_chooser.getSelected();
		//new tankDrive();?
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */
		//
		if (position.getSelected().equals("L")||position.getSelected().equals("R"))
		{
			autonomousCommand = new sandAutoBasic();
		}
		// schedule the autonomous command (example)
		 //Basic setter, try to use as reference
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		if(interrupt)
		{
			tele.start();
		}
		autoTimer += 20; //Divide by 1000 for time in seconds, auto periodic is called every 20 ms
		autoTimeSec = autoTimer / 1000;
		checkTeleop();//checks for override
		SmartDashboard.putNumber("Right Motor Enc", driveBase.getEncValue(true));//true is right, false is left, send enc values
		SmartDashboard.putNumber("Left Motor Enc", driveBase.getEncValue(false));
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		//lidar.start();
		driveBase.resetEnc(2);
		tele.start();//starts teleop, may be unessecary, test further
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		SmartDashboard.putNumber("Right Motor Enc", driveBase.getEncValue(true));
		SmartDashboard.putNumber("Left Motor Enc", driveBase.getEncValue(false));
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
	
	public static void initTalonCoast(TalonSRX motor) {
		motor.setNeutralMode(NeutralMode.Coast);
		motor.neutralOutput();
		motor.setSensorPhase(false);
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configClosedloopRamp(0.5, 0);
	}
	
	public static void initTalonBrake(TalonSRX motor) {
		motor.setNeutralMode(NeutralMode.Brake);
		motor.neutralOutput();
		motor.setSensorPhase(false);
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configClosedloopRamp(0.75, 0);
	}
	public static void initTalonCoast(VictorSPX motor) {
		motor.setNeutralMode(NeutralMode.Coast);
		motor.neutralOutput();
		motor.setSensorPhase(false);
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configClosedloopRamp(0.5, 0);
	}
	
	public static void initTalonBrake(VictorSPX motor) {
		motor.setNeutralMode(NeutralMode.Brake);
		motor.neutralOutput();
		motor.setSensorPhase(false);
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configClosedloopRamp(0.75, 0);
	}
	public static void checkTeleop()
	{
		//if anything is pressed, stop auto
		if(m_oi.buttonR3.get() || m_oi.buttonR4.get() || m_oi.LEFT_JOY.getRawAxis(1) >= 0 + acceptedJoyTolerance|| 
		   m_oi.LEFT_JOY.getRawAxis(1)  <= 0-acceptedJoyTolerance ||  m_oi.RIGHT_JOY.getRawAxis(1) - acceptedJoyTolerance >= 0 ||  
		   m_oi.RIGHT_JOY.getRawAxis(1) - acceptedJoyTolerance >= 0)
		{
			interrupt = true;
		}
		else
		{
			interrupt = false;
		}
	}
}
