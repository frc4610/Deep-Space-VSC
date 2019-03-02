package org.usfirst.frc.team4610.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team4610.robot.Robot;
import org.usfirst.frc.team4610.robot.commands.forward;
/**
 *
 */
public class sandAutoBasic extends CommandGroup {

    public sandAutoBasic() {
        requires(Robot.driveBase);
        requires(Robot.bar);
        //dealys at beginning
    	//addSequential(new Delay(SmartDashboard.getNumber("Delay", 0)));//simply moves forward then places a hatch
        //simply leaves the HAB, may need to be 5-8, test
        addSequential(new autoBarMoving());
        addSequential(new forward(Robot.encMultiFt*8/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));
    	//addSequential(new place(/*hatch*/));
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
