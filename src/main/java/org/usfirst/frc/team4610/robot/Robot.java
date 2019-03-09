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

import org.usfirst.frc.team4610.robot.commands.fBarMoveToPos;
import org.usfirst.frc.team4610.robot.commands.sandAutoBasic;
import org.usfirst.frc.team4610.robot.commands.sandAutoPlace;
import org.usfirst.frc.team4610.robot.commands.sandAutoSideHatch;
import org.usfirst.frc.team4610.robot.commands.tankDrive;
import org.usfirst.frc.team4610.robot.subsystems.CIntake;
import org.usfirst.frc.team4610.robot.subsystems.Crossbow;
import org.usfirst.frc.team4610.robot.subsystems.DriveBase;
import org.usfirst.frc.team4610.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team4610.robot.subsystems.FourBar;
import org.usfirst.frc.team4610.robot.subsystems.Lidar;
//import org.usfirst.frc.team4610.robot.subsystems.PIDtester;
import org.usfirst.frc.team4610.robot.subsystems.Pneum;
//import org.usfirst.frc.team4610.robot.subsystems.Tail;

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
// | || || |_  

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
	public static double encMultiFt = 400;//used to be 435 //Measure the distance the robot goes and its associated encoder value. Multiple feet wanted by this to get encoder value needed
	public static double encMultiIn = 36.25;//Enc value for inches. See above for how to use
	public static double encShopExtra = 400; //enc value the robot gains by slide. use acc/decel motor values to nullify
	public static double fbarPosBot = 635;// enc values for the set four bar positions
	public static double fbarPos2 = 2600;
	public static double fbarPos3 = 3345;
	public static double fbarPos4 = 5415;
	public static double fbarPosTop = 6170;
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
	//public static Tail tail;
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
	Command fbarJoy;
	Command fbarFix;
	public static double curveX;
	public static double curveY;
	public static double curveA = .6;
	boolean holder = false;
	//SendableChooser<Command> m_chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		holder = false;
		driver = new SendableChooser<>();
		operator = new SendableChooser<>();
		//testServo.setBounds(2, 2, 1.5, 1, 1);//check values, min is 1, and max is 2, but other values are unknown. this is for am L-16, check for other acuators
		//testServo.setSpeed(1);//forward, check both functions
		//testServo.setSpeed(0);//reverse, check both functions
		//testServo.getPosition();//input for the acutator?
		lidar = new Lidar(new DigitalInput(0));// the number is for the port
		//limCounter = new Counter(testingLimit);//use to find if value switched to quick, i.e. limCounter.get() > 0 means it was pressed. use limCounter.reset(); to set to 0
		driveBase = new DriveBase();
		pneum= new Pneum(6,7);//see subsystem for the parameters
		//tail = new Tail(4,5,1);//see subsystem for the parameters
		cbow = new Crossbow(2,3,0,1);//see subsystem for the parameters
		tele = new tankDrive();
		CameraServer.getInstance().startAutomaticCapture();
		autoTimer = 0;
		autoTimeSec = 0;
		intake = new CIntake(2);//see subsystem for the parameters
		bar = new FourBar(3, 4);//see subsystem for the parameters
		//testTail = new PIDtester(1,2,3,4);
		autoSpeed = .5;
		position = new SendableChooser<>();
		goal = new SendableChooser<>();
		position.addOption("Left2", "L");//lower case is HAB 1, upper is HAB 2
		position.setDefaultOption("Left2", "L");
		position.addOption("Middle", "m");
		position.addOption("Right2", "R");
		position.addOption("Left", "l");//should never be the case, but may be needed
		position.addOption("Right", "r");
		goal.addOption("No auto", "n");
		goal.addOption("Forward", "f");
		goal.setDefaultOption("Forward", "f");//f is forward, hatch is 1 place, r is place/grab, d is 2 places (with a grab obviously)
		goal.addOption("Direct Hatch", "h");
		goal.addOption("Side Hatch", "s");
		SmartDashboard.putNumber("Delay", 0);
		SmartDashboard.putData("Position", position);
		SmartDashboard.putData("Goal", goal);
		driver.addOption("Winte", "W");
		operator.addOption("Nathan", "N");
		driver.setDefaultOption("Winte", "W");// may be deleted later, keep in for now as its harmless
		operator.setDefaultOption("Nathan", "N");//same as above
		SmartDashboard.putData("Driver", driver);
		SmartDashboard.putData("Operator", operator);
		front = 0;
		interrupt = false;
		gyro = new AHRS(SPI.Port.kMXP);
		//.getSelected to get value for smart dash board values
		m_oi = new OI(driver.getSelected(), operator.getSelected()); 
		prefs = Preferences.getInstance();
		//Sets subsystems to what should be their default positions, in case they weren't reset at the end of the last game
		driveBase.resetEnc(2);
		//tail.resetEject();
		//intake.cInAdjustR();
		bar.resetBEnc();
		cbow.grip();
		intake.cinIn();
		cbow.crossOut();
		intake.cinIn();
		driveBase.limitSpeed(true);
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
		driveBase.limitSpeed(true);
		SmartDashboard.putNumber("Delay", 0);
		position.addOption("Left2", "L");//lower case is HAB 1, upper is HAB 2
		position.setDefaultOption("Left2", "L");
		position.addOption("Middle", "m");
		position.addOption("Right2", "R");
		position.addOption("Left", "l");//should never be the case, but may be needed
		position.addOption("Right", "r");
		goal.addOption("No auto", "n");
		goal.addOption("Forward", "f");
		goal.setDefaultOption("Forward", "f");//f is forward, hatch is 1 place, r is place/grab, d is 2 places (with a grab obviously)
		goal.addOption("Direct Hatch", "h");
		goal.addOption("Side Hatch", "s");
		//goal.addOption("Hatch and Regrab", "r"); //see below
		//goal.addOption("Double Hatch", "d"); //commented until further testing, don't want to rush too far ahead
		holder = false;
		SmartDashboard.putNumber("Delay", 0);
		SmartDashboard.putData("Position", position);
		SmartDashboard.putData("Goal", goal);
		driveBase.resetEnc(2);
		gyro.reset();
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
			autonomousCommand = new fBarMoveToPos(300, true);
		}
		else if (position.getSelected().equals("m")||goal.getSelected().equals("f")/*||position.getSelected().equals("L")||position.getSelected().equals("R")*/)
		{
			autonomousCommand = new sandAutoBasic();
		}
		else if (goal.getSelected().equals("h")&&(position.getSelected().equals("l")||position.getSelected().equals("r")))
		{
			autonomousCommand = new sandAutoPlace(position.getSelected());//use params to better use left/right/hab lvl for command groups called. Auto functions still untested
		}
		else if (goal.getSelected().equals("s"))
		{
			autonomousCommand = new sandAutoSideHatch(position.getSelected());//Auto functions still untested
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
		if(interrupt&&!holder)
		{
			holder = true;
			tele.start();//once interrupted starts teleop. All auto functons should automatically stop as well
		}
		curveX = -m_oi.CON.getRawAxis(1);
		curveY = -m_oi.CON.getRawAxis(3);
		autoTimer += 20; //Divide by 1000 for time in seconds, auto periodic is called every 20 ms, crude but works
		autoTimeSec = autoTimer / 1000;
		SmartDashboard.putNumber("Right Motor Enc", driveBase.getEncValue(true));//true is right, false is left, sends enc values
		SmartDashboard.putNumber("Left Motor Enc", driveBase.getEncValue(false));
		SmartDashboard.putNumber("FBar Enc", bar.getEncValue());
		//SmartDashboard.putNumber("Tail Enc", tail.getEncValue());
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
		SmartDashboard.updateValues();
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		driveBase.limitSpeed(true);
		position = new SendableChooser<>();
		goal = new SendableChooser<>();
		SmartDashboard.putNumber("Delay", 0);
		position.addOption("Left2", "L");//lower case is HAB 1, upper is HAB 2
		position.setDefaultOption("Left2", "L");
		position.addOption("Middle", "m");
		position.addOption("Right2", "R");
		position.addOption("Left", "l");//should never be the case, but may be needed
		position.addOption("Right", "r");
		goal.addOption("No auto", "n");
		goal.addOption("Forward", "f");
		goal.setDefaultOption("Forward", "f");//f is forward, hatch is 1 place, r is place/grab, d is 2 places (with a grab obviously)
		goal.addOption("Direct Hatch", "h");
		goal.addOption("Side Hatch", "s");
		SmartDashboard.putNumber("Delay", 0);
		SmartDashboard.putData("Position", position);
		SmartDashboard.putData("Goal", goal);
		SmartDashboard.putData("Driver", driver);
		SmartDashboard.putData("Operator", operator);
		tele.start();//starts teleop
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		gyro.reset();
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		SmartDashboard.updateValues();
		curveX = -m_oi.CON.getRawAxis(1);
		curveY = -m_oi.CON.getRawAxis(3);
		//bar.setBar(ControlMode.PercentOutput, m_oi.OP_JOY.getRawAxis(1));//a potential curve for sensitivy is axxx + (1-a)x, where x is the value and a is a number from 0 - 1 prolly .6. a = 0, its a straight line
		//misc values for the drive team to know whats up
		//SmartDashboard.putNumber("LIDAR Inches", lidar.getDistanceIn(false));//the boolean is for whether its rounded or not
		if(intake.isCargoIn())
		{
			SmartDashboard.putString("Intake Status", "Full");
		}
		else
		{
			SmartDashboard.putString("Intake Status", "Empty");
		}
		SmartDashboard.putNumber("Right Motor Enc", driveBase.getEncValue(true));//true is right, false is left, sends enc values
		SmartDashboard.putNumber("Left Motor Enc", driveBase.getEncValue(false));
		SmartDashboard.putNumber("FBar Enc", bar.getEncValue());
		//SmartDashboard.putNumber("Tail Enc", tail.getEncValue());
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
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
		motor.configClosedloopRamp(0.55, 0);
	}
	
	public static void initTalonBrake(TalonSRX motor) {
		motor.setNeutralMode(NeutralMode.Brake);
		motor.neutralOutput();
		motor.setSensorPhase(false);
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configPeakOutputForward(1.0, 0);
		motor.configPeakOutputReverse(-1.0, 0);
		motor.configClosedloopRamp(0.55, 0);//used to be .75, test further, this value is fine prolly
	}
	public static void initTalonCoast(VictorSPX motor) {
		motor.setNeutralMode(NeutralMode.Coast);
		motor.neutralOutput();
		motor.setSensorPhase(false);
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configPeakOutputForward(1.0, 0);
		motor.configPeakOutputReverse(-1.0, 0);
		motor.configClosedloopRamp(0.55, 0);
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
	
	}
}
