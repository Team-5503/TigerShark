// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.ArmFeedforward;

public class WheelOfDeath extends SubsystemBase {
  /** Creates a new WheelOfDeath. */
  SparkMax mainWheelMotor, coralMotor, algaeMotor;
  AbsoluteEncoder mainWheelEncoder;
  public WheelOfDeath() 
  {
    mainWheelMotor = new SparkMax(15, MotorType.kBrushless);
    coralMotor = new SparkMax(16, MotorType.kBrushless);
    algaeMotor = new SparkMax(17, MotorType.kBrushless);
    mainWheelEncoder = mainWheelMotor.getAbsoluteEncoder();
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

   //fix this
  public double getWheelPositionRaw(){
    return mainWheelEncoder.getPosition();
  }

  public double getWheelPositionAngle(){
   
    return mainWheelEncoder.getPosition()*360;
  }
  
}
