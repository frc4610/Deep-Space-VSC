/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.commands;

import org.usfirst.frc.team4610.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class sandAutoSideHatch extends CommandGroup {
  /**
   * Add your docs here.
   */
  public sandAutoSideHatch(String pos) {
    requires(Robot.driveBase);
    requires(Robot.bar);
    addSequential(new autoBarMoving());
    addSequential(new forward(Robot.encMultiFt*8/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));
    if(pos.equals("L"))
    {
      addSequential(new turn(10, Robot.autoSpeed));
    }
    else if (pos.equals("R"))
    {
      addSequential(new turn(-10, Robot.autoSpeed));
    }
    //goes the nessecary amount of ft to make the 90 degree turn for loading station
    addSequential(new forward(Robot.encMultiFt*3/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));
    //makes the 90 degree turn for the loading station
    if(pos.equals("L")||pos.equals("l"))
    {
      addSequential(new turn(-100, Robot.autoSpeed));
    }
    else if (pos.equals("R")||pos.equals("r"))
    {
      addSequential(new turn(100, Robot.autoSpeed));
    }
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
