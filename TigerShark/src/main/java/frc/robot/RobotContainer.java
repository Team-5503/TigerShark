// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.WheelOfDeath;
import frc.robot.positionConfig;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    private double positionSelected;

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.07).withRotationalDeadband(MaxAngularRate * 0.07) // Add a 7% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);
    private final CommandXboxController operator = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    public final Elevator elevator = new Elevator();
    public final WheelOfDeath wheelOfDeath = new WheelOfDeath();

    public final positionConfig[] positions = 
    {
        new positionConfig(0, 0, 0, 0), // arm stow
        // intake
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, 0), // algae ground intake
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, MaxAngularRate), // coral bay intake
        // low
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, MaxAngularRate), // algae processor shoot
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, MaxAngularRate), // coral L1 shoot
        // L2
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, MaxAngularRate), // algae above L2 intake
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, MaxAngularRate), // coral L2 shoot
        // L3
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, MaxAngularRate), // algae above L3 intake
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, MaxAngularRate), // coral L3 shoot 
        // high
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, MaxAngularRate), // algae barge shoot
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, MaxAngularRate), // coral L4 shoot
        // other
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, 0), // starting position
        new positionConfig(MaxAngularRate, MaxSpeed, MaxAngularRate, 0), // test for feedforward (both sides out)
    };

    //Function that changes velocty depending if it is odd or even

    //set grab , setposition set the arm
    //CALL FROM CONTAINER
    public int currentPosition = 0;

    public void changeVel(){
        positions[currentPosition].getVelTar();

    }

    //end
    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    //Change This
    public void changePosition(int pos){
        positionSelected = pos;
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
