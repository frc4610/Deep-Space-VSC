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
//import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4610.robot.commands.sandAutoBasic;
import org.usfirst.frc.team4610.robot.commands.sandAutoPlace;
import org.usfirst.frc.team4610.robot.commands.tankDrive;
import org.usfirst.frc.team4610.robot.subsystems.CIntake;
import org.usfirst.frc.team4610.robot.subsystems.Crossbow;
import org.usfirst.frc.team4610.robot.subsystems.DriveBase;
import org.usfirst.frc.team4610.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team4610.robot.subsystems.FourBar;
import org.usfirst.frc.team4610.robot.subsystems.Lidar;
//import org.usfirst.frc.team4610.robot.subsystems.PIDtester;
import org.usfirst.frc.team4610.robot.subsystems.Pneum;
import org.usfirst.frc.team4610.robot.subsystems.Tail;

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

	// links to help with pid control, test extensivly
	//https://wpilib.screenstepslive.com/s/currentCS/m/java/l/599736-pidsubsystems-for-built-in-pid-control
	//https://frc-pdr.readthedocs.io/en/latest/control/pid_control.html
	public static Counter limCounter;
	//public static DigitalInput testingLimit;
	public static double encMultiFt = 800;//used to be 435 //Measure the distance the robot goes and its associated encoder value. Multiple feet wanted by this to get encoder value needed
	public static double encMultiIn = 36.25;//Enc value for inches. See above for how to use
	public static double encShopExtra = 400; //enc value the robot gains by slide. use acc/decel motor values to nullify
	public static double fbarPosBot = 0;// enc values for the set four bar positions
	public static double fbarPos2 = 0;
	public static double fbarPos3 = 0;
	public static double fbarPos4 = 0;
	public static double fbarPosTop = 0;
	public static double autoTimer;//tracks time in ms
	public static double autoTimeSec;//tracks time in seconds
	public static double autoSpeed;//default speed for drivebase in auto
	public static int front;//is the originally front still the front, for inversion
	public static boolean interrupt;//is the driver overriding auto?
	public static double acceptedTurnTolerance = 5;//obvious, for auto
	public static double acceptedJoyTolerance = 5;//sets it so that a small jolt doesn't stop auto, no longer used
	public static DriveBase driveBase;
	public static Lidar lidar;
	//public static PIDtester testTail;
	public static AHRS gyro;
	public static Tail tail;
	public static Crossbow cbow;
	public static FourBar bar;//Later, test with pidtesting after this subsystem works
	public static CIntake intake;
	public static Pneum pneum;
	public static Preferences prefs;
	public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
	public static OI m_oi;
	public Servo testServo = new Servo(1);//still untested, linear actuator testing. May be unused in the real bot
	SendableChooser<String> position;
	SendableChooser<String> driver;
	SendableChooser<String> operator;
	SendableChooser<String> goal;
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
		lidar = new Lidar(new DigitalInput(0));// the number is for the port
		//limCounter = new Counter(testingLimit);//use to find if value switched to quick, i.e. limCounter.get() > 0 means it was pressed. use limCounter.reset(); to set to 0
		driveBase = new DriveBase();
		pneum= new Pneum(1,2);//see subsystem for the parameters
		tail = new Tail(3,7,1);//see subsystem for the parameters
		cbow = new Crossbow(1,5,2,6);//see subsystem for the parameters
		tele = new tankDrive();
		CameraServer.getInstance().startAutomaticCapture();
		autoTimer = 0;
		autoTimeSec = 0;
		intake = new CIntake(2);//see subsystem for the parameters
		bar = new FourBar(3, 4);//see subsystem for the parameters
		//testTail = new PIDtester(1,2,3,4);
		position = new SendableChooser<>();
		driver = new SendableChooser<>();
		operator = new SendableChooser<>();
		goal = new SendableChooser<>();
		autoSpeed = .5;
		front = 0;
		interrupt = false;
		gyro = new AHRS(SPI.Port.kMXP);
		position.addOption("Left2", "L");//lower case is HAB 1, upper is HAB 2
		position.setDefaultOption("Middle", "m");
		position.addOption("Right2", "R");
		position.addOption("Left", "l");//should never be the case, but may be needed
		position.addOption("Right", "r");
		goal.addOption("No auto", "n");
		goal.setDefaultOption("Forward", "f");//f is forward, hatch is 1 place, r is place/grab, d is 2 places (with a grab obviously)
		goal.addOption("Direct Hatch", "h");
		//goal.addOption("Hatch and Regrab", "r"); //see below
		//goal.addOption("Double Hatch", "d"); //commented until further testing, don't want to rush too far ahead
		driver.setDefaultOption("Winte", "W");// may be deleted later, keep in for now as its harmless
		operator.setDefaultOption("Nathan", "N");//same as above
		SmartDashboard.putData("Position", position);
		SmartDashboard.putData("Goal", goal);
		SmartDashboard.putData("Driver", driver);
		SmartDashboard.putData("Operator", operator);
		//.getSelected to get value for smart dash board values
		m_oi = new OI(driver.getSelected(), operator.getSelected()); 
		prefs = Preferences.getInstance();
		//Sets subsystems to what should be their default positions, in case they weren't reset at the end of the last game
		driveBase.resetEnc(2);
		tail.resetEject();
		//intake.cInAdjustR();
		bar.resetBEnc();
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
		//position lower case is HAB 1, upper is HAB 2. l, r and m
		//goal is f is forward, h is 1 place, r is place/grab, d is 2 places (with a grab obviously)
		//Note: all auto code is either going to be inverted back to the front if we can't drive off backwards, if so set it to the crossbow
		if(goal.getSelected() == null ||position.getSelected() == null)
		{
			interrupt = true;
		}
		else if(goal.getSelected().equals("n"))
		{
			interrupt = true;
		}
		else if (position.getSelected().equals("m")||goal.getSelected().equals("f"))
		{
			autonomousCommand = new sandAutoBasic();
		}
		else if (goal.getSelected().equals("h"))
		{
			autonomousCommand = new sandAutoPlace(position.getSelected());//use params to better use left/right/hab lvl for command groups called. Auto functions still untested
		}
		else if (goal.getSelected().equals("r"))
		{
			autonomousCommand = new sandAutoPlace(position.getSelected());//Auto functions still untested
		}/*
		else if (goal.getSelected().equals("d")) //for now I've commented this out until the rest of auto is tested, will remian as such until the rest of auto is tested 
		{
			autonomousCommand = new sandAutoBasic();
		}*/
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
		autoTimer += 20; //Divide by 1000 for time in seconds, auto periodic is called every 20 ms, crude but works
		autoTimeSec = autoTimer / 1000;
		checkTeleop();//checks for override
		SmartDashboard.putNumber("Right Motor Enc", driveBase.getEncValue(true));//true is right, false is left, sends enc values
		SmartDashboard.putNumber("Left Motor Enc", driveBase.getEncValue(false));
		SmartDashboard.putNumber("FBar Enc", bar.getEncValue());
		SmartDashboard.putNumber("Tail Enc", tail.getEncValue());
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
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
		//for testing
		SmartDashboard.putNumber("LIDAR Inches", lidar.getDistanceIn(false));//the boolean is for whether its rounded or not
		/*testTail.enable();
		testTail.setSetpoint(10000);*/
		//driveBase.set(ControlMode.Position, encMultiFt, encMultiFt);
		SmartDashboard.putNumber("Right Motor Enc", driveBase.getEncValue(true));//true is right, false is left, sends enc values
		SmartDashboard.putNumber("Left Motor Enc", driveBase.getEncValue(false));
		SmartDashboard.putNumber("FBar Enc", bar.getEncValue());
		SmartDashboard.putNumber("Tail Enc", tail.getEncValue());
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
		motor.configPeakOutputForward(1.0, 0);
		motor.configPeakOutputReverse(-1.0, 0);
		motor.configClosedloopRamp(0.5, 0);
	}
	
	public static void initTalonBrake(TalonSRX motor) {
		motor.setNeutralMode(NeutralMode.Brake);
		motor.neutralOutput();
		motor.setSensorPhase(false);
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configPeakOutputForward(1.0, 0);
		motor.configPeakOutputReverse(-1.0, 0);
		motor.configClosedloopRamp(0.55, 0);//used to be 75, test further is this value is fine
	}
	public static void initTalonCoast(VictorSPX motor) {
		motor.setNeutralMode(NeutralMode.Coast);
		motor.neutralOutput();
		motor.setSensorPhase(false);
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configPeakOutputForward(1.0, 0);
		motor.configPeakOutputReverse(-1.0, 0);
		motor.configClosedloopRamp(0.5, 0);
	}
	
	public static void initTalonBrake(VictorSPX motor) {
		motor.setNeutralMode(NeutralMode.Brake);
		motor.neutralOutput();
		motor.setSensorPhase(false);
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configPeakOutputForward(1.0, 0);
		motor.configPeakOutputReverse(-1.0, 0);
		motor.configClosedloopRamp(0.55, 0);
	}
	public static void checkTeleop()
	{
		//if anything is pressed, stop auto. Seems to be broken. oops
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
