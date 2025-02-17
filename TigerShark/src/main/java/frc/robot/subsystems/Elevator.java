// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.ElevatorFeedforward;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;

public class Elevator extends SubsystemBase {

  SparkMax m_elevatorL1, m_elevatorL2;
  private SparkClosedLoopController closedLoopControllerL1, ClosedLoopControllerL2;
  private RelativeEncoder encoderL1, encoderL2;
  /** Creates a new elevator. */
  public Elevator() {
    m_elevatorL1 = new SparkMax(13, MotorType.kBrushless);
    closedLoopControllerL1 = m_elevatorL1.getClosedLoopController();
    encoderL1 = m_elevatorL1.getEncoder();

    m_elevatorL2 = new SparkMax(14, MotorType.kBrushless);
    closedLoopControllerL1 = m_elevatorL1.getClosedLoopController();
    encoderL1 = m_elevatorL1.getEncoder();

    SparkMaxConfig elevatorConfigL1 = new SparkMaxConfig();

    SparkMaxConfig elevatorConfigL2 = new SparkMaxConfig();

    elevatorConfigL1
      .smartCurrentLimit(50)
      .idleMode(IdleMode.kBrake);
    elevatorConfigL1.closedLoop
    //TODO: tune this beatch
      .p(.3)
      .i(0)
      .d(.05)
      .outputRange(0, 0);
    elevatorConfigL1.softLimit
      .forwardSoftLimitEnabled(true)
      .forwardSoftLimit(50)//TODO: get measurements in rev hardware client
      .reverseSoftLimitEnabled(true)
      .reverseSoftLimit(-1);

    elevatorConfigL2
      .smartCurrentLimit(50)
      .idleMode(IdleMode.kBrake);
    elevatorConfigL2.closedLoop
    //TODO: tune this beatch as well
      .p(.3)
      .i(0)
      .d(.05)
      .outputRange(0, 0);
    elevatorConfigL2.softLimit
      .forwardSoftLimitEnabled(true)
      .forwardSoftLimit(35)//TODO: get measurements in rev hardware client
      .reverseSoftLimitEnabled(true)
      .reverseSoftLimit(-1);
    
    m_elevatorL1.configure(elevatorConfigL1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    m_elevatorL2.configure(elevatorConfigL2, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
