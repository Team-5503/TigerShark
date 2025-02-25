// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;




/**TODO: Work on the climber as a possible replacement
 * 
 * Mr Ferrell's hope in the bot getting finised is at an all time low, so get the climber in a working state to climb.
 **/
public class Climber extends SubsystemBase {
   
  SparkMax m_pivotMotor;
  private SparkClosedLoopController closedLoopControllerCl;
  private RelativeEncoder climb;
  private double position;
  /** Creates a new Climber. */
  public Climber() {
    m_pivotMotor = new SparkMax(14, MotorType.kBrushless); //Change the device id to correct one
    closedLoopControllerCl = m_pivotMotor.getClosedLoopController();
    climb = m_pivotMotor.getEncoder();
    //position = climb.getPosition();

    SparkMaxConfig climbConfig = new SparkMaxConfig();

    climbConfig
      .smartCurrentLimit(50)
      .idleMode(IdleMode.kBrake);
    climbConfig.closedLoop
      //Change values later
      .p(.3)
      .i(0)
      .d(.05)
      .outputRange(0, 0);
    climbConfig.softLimit
      .forwardSoftLimitEnabled(true)
      .forwardSoftLimit(40)
      .reverseSoftLimitEnabled(true)
      .reverseSoftLimit(-1);

    m_pivotMotor.configure(climbConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
  }
  
/* Review later 
  public double getPosition(){
    return position;
  }

  public void setPosition(){
    m_pivotMotor.setPosition(setvalue); 
  }

*/
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }
  

}


//60:1 gear ratio
//Convert Degrees to rotation
//Get Position function.