// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

public class elevator extends SubsystemBase {

  SparkMax m_elevator;
  /** Creates a new elevator. */
  public elevator() {
    m_elevator = new SparkMax(0, MotorType.kBrushless);
    
    SparkMaxConfig elevatorConfig = new SparkMaxConfig();
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
