// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.generated.TunerConstants;



/** Add your docs here. */
public class positionConfig {
    private double targetHeightL1;
    private double targetHeightL2;
    private double targetAngle;
    private double targetVelocity;
    private double kInchesPerRot = 1;

    public positionConfig(double L1, double L2, double angle, double velocity){
        targetHeightL1 = L1/kInchesPerRot;
        targetHeightL2 = L2/kInchesPerRot;
        targetAngle = angle;
        targetVelocity = velocity;

    }
}
