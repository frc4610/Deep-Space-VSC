/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.commands;

import org.usfirst.frc.team4610.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class sandAutoPlace extends CommandGroup {
  /**
   * Add your docs here.
   */
  public sandAutoPlace() {
    requires(Robot.driveBase);
    requires(Robot.tail);//may change to tohe intake' hatch thing. still uncertain
    addSequential(new Delay(Robot.prefs.getDouble("Delay", 0)));//simply moves forward then places a hatch
    addSequential(new forward(Robot.encMultiFt/*number of move, multi by the enc multi value*/, Robot.autoSpeed/*speed left then right*/, Robot.autoSpeed));// enc values must be needed distance. test extensivly
    addSequential(new place("Hatch", true));//params in command

    //for future commands, most likely add turns to get to a good angle, drive forward and grab. turn again to re-aim and place again for "d".
    //I have yet to do this as so many placeholders will be there. Test using my list before this gets furthered

    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
  }
}
