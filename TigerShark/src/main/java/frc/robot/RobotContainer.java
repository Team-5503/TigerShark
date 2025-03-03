// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.WheelOfDeath;
import frc.robot.positionConfig;
import frc.robot.subsystems.Climber;

import frc.robot.commands.climb.climbReach;
import frc.robot.commands.climb.climbStop;
import frc.robot.commands.climb.climbStow;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.55).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    private double positionSelected;
    public static int currentPosition = 0;

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.07).withRotationalDeadband(MaxAngularRate * 0.07) // Add a 7% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);
    private final CommandXboxController operator = new CommandXboxController(1);
    private final XboxController opcon = operator.getHID();

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    public final Climber climb = new Climber(); //TODO: enable
    // public final Elevator elevator = new Elevator();
    // public final WheelOfDeath wheelOfDeath = new WheelOfDeath();

    public final positionConfig[] positions = 
    {
        new positionConfig(0, 0, .925, 0), // arm stow
        // intake
        new positionConfig(0, 0, .6, 0), // algae ground intake
        new positionConfig(-21.4, 0, .99, -.17), // coral bay intake
        // low
        new positionConfig(0, 0, .167, .3), // algae processor shoot
        new positionConfig(-8.4, 0, .95, -.2), // coral L1 shoot
        // L2
        new positionConfig(-14.3, 0, .65, -.17), // algae above L2 intake
        new positionConfig(-10.1, 0, 0.2, .3), // coral L2 shoot
        // L3
        new positionConfig(-29.9, 0, .63, -.17), // algae above L3 intake
        new positionConfig(-21, 0, .26, .3), // coral L3 shoot 
        // high
        new positionConfig(-30.6, -24.7, .83, .3), // algae barge shoot
        new positionConfig(30.5, 24.5, .2, .3), // coral L4 shoot
        // other
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, 0), // starting position
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, 0), // test for feedforward (both sides out)
    };

    //Function that changes velocty depending if it is odd or even

    //set grab , setposition set the arm
    //CALL FROM CONTAINER

    public void changeVel(){
        positions[currentPosition].getVelTar();

    }

    //END

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        // wheelOfDeath.setDefaultCommand(new RunCommand(() -> {wheelOfDeath.setMainPos(currentPosition, positions[currentPosition].getAngleTar(), positions[currentPosition].getVelTar());}, wheelOfDeath));
        climb.setDefaultCommand(new RunCommand(() -> {climb.setStow();}, climb)); //TODO: enable
        //wheelOfDeath.setDefaultCommand(wheeldefaultCMD());
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        // joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick.y().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));
        //climb buttons //TODO: enable
        final Trigger cStow = joystick.leftBumper();
        cStow.onTrue(new climbStow(climb));
        final Trigger cReach = joystick.rightBumper();
        cReach.onTrue(new climbReach(climb));
        final Trigger cStop = joystick.leftTrigger(.7);
        cStop.onTrue(new climbStop(climb)); 

        

        

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    private static EventLoop applyRequest(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyRequest'");
    }

    //Change THis
    public void changePosition(int pos){
        currentPosition = pos;
    }

    public int getCurrentPos(){
        return currentPosition;
    }
    // public void operatorControl(){
    //     //operator controls
    //     if (opcon.getAButton()){
    //         System.out.println("a pressed");
    //         changePosition(4);
    //     }
    //     if (opcon.getXButton()){
    //         changePosition(6);
    //     }
    //     if (opcon.getBButton()){
    //         changePosition(8);
    //     }
    //     if (opcon.getYButton()){
    //         changePosition(10);
    //     }

    //     if (opcon.getPOV(0) == 0){
    //         changePosition(3);
    //     }
    //     if (opcon.getPOV(0) == 90){
    //         changePosition(5);
    //     }
    //     if (opcon.getPOV(0) == 180){
    //         changePosition(7);
    //     }
    //     if (opcon.getPOV(0) == 270){
    //         changePosition(9);
    //     }

    //     if(operator.leftBumper().getAsBoolean()){
    //         changePosition(1);
    //     }
    //     if(operator.rightBumper().getAsBoolean()){
    //         changePosition(2);
    //     }
    //     if(operator.leftBumper().getAsBoolean()){
    //         wheelOfDeath.intakeOrOutake();
    //     }
    //     elevator.setPos(currentPosition, positions[currentPosition].getL1Tar(),positions[currentPosition].getL2Tar());
    //     wheelOfDeath.setMainPos(currentPosition, positions[currentPosition].getAngleTar(),positions[currentPosition].getVelTar());
    //     System.out.println("e pos: "+elevator.getPos()+ " current: "+getCurrentPos());
    //     System.out.println("wheel pos: "+wheelOfDeath.getPos()+ " current: "+getCurrentPos());
    // }

    public Command getAutonomousCommand() {
        return new PathPlannerAuto("New Auto");
    }
    // goes through the elevator and arm to check positions, if they aren't equal, the subsystems's positions are changed

    // public void checkAndChange(){
    //     System.out.println("e pos: "+elevator.getPos()+ " current: "+getCurrentPos());

    //     if(elevator.getPos() != getCurrentPos()){
    //         System.out.println("change elevator");
    //         elevator.setPos(currentPosition, positions[currentPosition].getL1Tar(),positions[currentPosition].getL2Tar());
    //     }
    //     System.out.println("wheel pos: "+wheelOfDeath.getPos()+ " current: "+getCurrentPos());

    //     if(wheelOfDeath.getPos() != currentPosition){
    //         System.out.println("Change wgeel");
    //         wheelOfDeath.setMainPos(currentPosition, positions[currentPosition].getAngleTar(),positions[currentPosition].getVelTar());
    //     }
    // }
}
