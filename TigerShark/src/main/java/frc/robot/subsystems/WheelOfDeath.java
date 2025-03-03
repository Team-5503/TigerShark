// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.RobotContainer;

public class WheelOfDeath extends SubsystemBase {
  /** Creates a new WheelOfDeath. */
  LaserCan start, end;
  SparkMax mainWheelMotor, coralMotor, algaeMotor;
  SparkClosedLoopController closedLoopControllerMain;
  AbsoluteEncoder mainWheelEncoder;
  WaitCommand wait;
  double vel;
  int position;
  //public int pos = 
  public WheelOfDeath() 
  {
    mainWheelMotor = new SparkMax(15, MotorType.kBrushless);
    closedLoopControllerMain = mainWheelMotor.getClosedLoopController();  
    coralMotor = new SparkMax(16, MotorType.kBrushless);
    algaeMotor = new SparkMax(17, MotorType.kBrushless);
    mainWheelEncoder = mainWheelMotor.getAbsoluteEncoder();
    start = new LaserCan(18);
    end = new LaserCan(19);

    position = 0;
   

    SparkMaxConfig  mainWheelMotorConfig = new SparkMaxConfig();

    SparkMaxConfig coralMotorConfig = new SparkMaxConfig();
    SparkMaxConfig algaeMotorConfig = new SparkMaxConfig();
    
    
    /// You change these value because I won't remember-JAM
    mainWheelMotorConfig
      .smartCurrentLimit(50)
      .idleMode(IdleMode.kBrake);
    //TODO: TUNE IT
    mainWheelMotorConfig.closedLoop
      .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
      .p(.05)
      .i(0)
      .d(.05)
      .outputRange(.2,.2);

    // Something to do with the Neo 550 Current Change these Value
    coralMotorConfig
      .smartCurrentLimit(20)
      .idleMode(IdleMode.kBrake);

    algaeMotorConfig
      .smartCurrentLimit(20)
      .idleMode(IdleMode.kBrake);

    try {
    end.setRangingMode(LaserCan.RangingMode.SHORT);
    end.setRegionOfInterest(new LaserCan.RegionOfInterest(8, 8, 16, 16));
    end.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_33MS);
    } catch (ConfigurationFailedException e){
      System.out.println("configuration failed: " + e);
    }

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
  public double getWheelPositionAngle(){
   
    return mainWheelEncoder.getPosition()*360;
  }
  //setReference. HERE
  public void setPosNum(int num){
    position = num;
  }
  public void setMainPos(int pos, double ang, double velo){
    closedLoopControllerMain.setReference(ang, ControlType.kPosition);
    position = pos;
    vel = velo;
  }
  public void intakeOrOutake(){
    LaserCan.Measurement measurement = end.getMeasurement();
    if (position % 2 != 0){
      for(int i = 0; i > 7; i++){
        algaeMotor.set(vel);
        new WaitCommand(.2);
        algaeMotor.set(0);
        new WaitCommand(.1);
      }
    }
    else {
      if (position == 1){
        while((measurement.status != LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT) || (measurement.distance_mm > 15)){
          coralMotor.set(vel);
        }
      }
      else{
        while((measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT) && (measurement.distance_mm <= 20)){
          coralMotor.set(vel);
        }
      }
    }
  }
  //What do I add to this part of the section of the code.
  //int in ele arm pos robot container change change in ele and arm
  //To keep the same in RobotContainer.
  public int getPos(){
    return position;
  }
}
