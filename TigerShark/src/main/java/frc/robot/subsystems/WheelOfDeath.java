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
  SparkClosedLoopController closedLoopControllerMain;
  AbsoluteEncoder mainWheelEncoder;
  public WheelOfDeath() 
  {
    mainWheelMotor = new SparkMax(15, MotorType.kBrushless);
    closedLoopControllerMain = mainWheelMotor.getClosedLoopController();  
    coralMotor = new SparkMax(16, MotorType.kBrushless);
    algaeMotor = new SparkMax(17, MotorType.kBrushless);
    mainWheelEncoder = mainWheelMotor.getAbsoluteEncoder();
   

    SparkMaxConfig  mainWheelMotorConfig = new SparkMaxConfig();

    SparkMaxConfig coralMotorConfig = new SparkMaxConfig();
    SparkMaxConfig algaeMotorConfig = new SparkMaxConfig();
    
    
    /// You change these value because I won't remember-JAM
    mainWheelMotorConfig
      .smartCurrentLimit(50)
      .idleMode(IdleMode.kBrake);
    //TODO: TUNE IT
    mainWheelMotorConfig.closedLoop
      .p(.3)
      .i(0)
      .d(.05)
      .outputRange(.2,.2);
    mainWheelMotorConfig.softLimit
      .forwardSoftLimitEnabled(true)
      .forwardSoftLimit(50) //TODO: YOU FIGURE THIS ONE OUT
      .reverseSoftLimitEnabled(true)
      .reverseSoftLimit(-1);

    // Something to do with the Neo 550 Current Change these Value
    coralMotorConfig
      .smartCurrentLimit(20)
      .idleMode(IdleMode.kBrake);
    coralMotorConfig.softLimit
      .forwardSoftLimitEnabled(true)
      .reverseSoftLimitEnabled(true)
      .reverseSoftLimit(-1);

    algaeMotorConfig
      .smartCurrentLimit(20)
      .idleMode(IdleMode.kBrake);
    algaeMotorConfig.softLimit
      .forwardSoftLimitEnabled(true)
      .reverseSoftLimitEnabled(true)
      .reverseSoftLimit(-1);


    mainWheelMotor.configure(mainWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    coralMotor.configure(coralMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    algaeMotor.configure(algaeMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

   //fix this
  public double getWheelPositionRaw(){
    return mainWheelEncoder.getPosition();
  }
  //setReference not setposition. HERE
  public void setMainPos(){
    closedLoopControllerMain.setReference(getWheelPositionAngle(), null)
  }


  public double getWheelPositionAngle(){
   
    return mainWheelEncoder.getPosition()*360;
  }
  

}
