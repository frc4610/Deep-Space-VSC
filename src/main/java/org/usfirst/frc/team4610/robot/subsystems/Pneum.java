package org.usfirst.frc.team4610.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Pneum extends Subsystem {

	@SuppressWarnings("unused")
	private Compressor compressor;
	private DoubleSolenoid driveDS67;
	
	public Pneum(int first, int second) {
		//sets up pnuematics, first is the 1st port the ds is plugged into, second is the second. Pretty self-explanitory
		this.compressor = new Compressor();
		this.driveDS67 = new DoubleSolenoid(0,first,second);
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public void driveHighG()//change so high/low gear is correct, also make it a command w/ a button
	{
		driveDS67.set(DoubleSolenoid.Value.kForward);
	}
	public void driveLowG()
	{
		driveDS67.set(DoubleSolenoid.Value.kReverse);
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

