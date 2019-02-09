/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4610.robot.commands;
import org.usfirst.frc.team4610.robot.Robot;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class sandAutoDouble extends CommandGroup {
  /**
   * Add your docs here.
   */
  public sandAutoDouble(String pos) {
    requires(Robot.driveBase);
    requires(Robot.tail);//may change to tohe intake' hatch thing. still uncertain
    //delay at beginning
    addSequential(new Delay(Robot.prefs.getDouble("Delay", 0)));//simply moves forward then places a hatch. Still need to meausre drop off values
    //moves off of HAB, may need to be somewhere between 5-8 ft instead, test
    addSequential(new forward(-Robot.encMultiFt*4/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));// enc values must be needed distance. test extensivly
    //turns towards the nearest hatch panel on front of the cargo ship, depening on the starting position
    if(pos.equals("L"))
    {
      addSequential(new turn(-10, Robot.autoSpeed));
    }
    else if (pos.equals("R"))
    {
      addSequential(new turn(10, Robot.autoSpeed));
    }
    //finishes moving towards the cargoship's front
    addSequential(new forward(-Robot.encMultiFt*10/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));// enc values must be needed distance. test extensivly
    //places the pre-loaded hatch panel
    addSequential(new place("Hatch", true));//params in command
    //backs up slightly as to not catch on the cargo ship when turning
    addSequential(new forward(Robot.encMultiFt*1/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));
    //turns towards the nearest loading station, using a 90 degree angle and turns again later so the HAB lvl 1 doesn't get in the way
    if(pos.equals("L")||pos.equals("l"))
    {
      addSequential(new turn(90, Robot.autoSpeed));
    }
    else if (pos.equals("R")||pos.equals("r"))
    {
      addSequential(new turn(-90, Robot.autoSpeed));
    }
    //if it already is 10 degrees off from the turn post HAB lvl 2, compensate for that
    if(pos.equals("L"))
    {
      addSequential(new turn(10, Robot.autoSpeed));
    }
    else if (pos.equals("R"))
    {
      addSequential(new turn(-10, Robot.autoSpeed));
    }
    //goes the nessecary amount of ft to make the 90 degree turn for loading station
    addSequential(new forward(-Robot.encMultiFt*9.8/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));
    //makes the 90 degree turn for the loading station
    if(pos.equals("L")||pos.equals("l"))
    {
      addSequential(new turn(90, Robot.autoSpeed));
    }
    else if (pos.equals("R")||pos.equals("r"))
    {
      addSequential(new turn(-90, Robot.autoSpeed));
    }
    //finishes moving to the loading station, will simoy pickup (hopefully) by raming into the loading station, may want to be 17
    addSequential(new forward(-Robot.encMultiFt*18/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));// 19 may be a bit large, test furhter
    //moves back towards the cargo ship
    addSequential(new forward(Robot.encMultiFt*8/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));
    //turn to face the nearest hatch panel on the cargo ship
    if(pos.equals("L")||pos.equals("l"))
    {
      addSequential(new turn(146, Robot.autoSpeed));
    }
    else if (pos.equals("R")||pos.equals("r"))
    {
      addSequential(new turn(-146, Robot.autoSpeed));
    }
    //moves towards the nearest cargoship hatch panel slot
    addSequential(new forward(-Robot.encMultiFt*13.5/*number of move, multi by the enc multi value, Robot.autoSpeed/*speed left then right, Robot.autoSpeed*/));
    //places 2nd hatch
    addSequential(new place("Hatch", true));
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
