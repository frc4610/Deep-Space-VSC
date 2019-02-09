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
  public sandAutoPlace(String pos) {
    requires(Robot.driveBase);
    requires(Robot.tail);//may change to to the intake's hatch thing. still uncertain
    //delays at beginning
    addSequential(new Delay(Robot.prefs.getDouble("Delay", 0)));//simply moves forward then places a hatch. Still need to meausre drop off values
    //moves off the HAB, may be 5-8 ft instead, test
    addSequential(new forward(Robot.encMultiFt*4/*number of move, multi by the enc multi value*/, Robot.autoSpeed/*speed left then right*/, Robot.autoSpeed));// enc values must be needed distance. test extensivly
    //turns towards nearest cargoship hatch panel place
    if(pos.equals("L"))
    {
      addSequential(new turn(10, Robot.autoSpeed));
    }
    else if (pos.equals("R"))
    {
      addSequential(new turn(-10, Robot.autoSpeed));
    }
    //moves towards the cargoship
    addSequential(new forward(Robot.encMultiFt*10/*number of move, multi by the enc multi value*/, Robot.autoSpeed/*speed left then right*/, Robot.autoSpeed));// enc values must be needed distance. test extensivly
    //places the preloaded hatch panel
    addSequential(new place("Hatch", true));//params in command

    //for future commands, most likely add turns to get to a good angle, drive forward and grab. turn again to re-aim and place again for "d".
    //I have yet to do this as so many placeholders will be there. Test using my list before this gets furthered
    //after the forst place, you need to move ~9.8 ft to be alighned with the center of loading dock
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
