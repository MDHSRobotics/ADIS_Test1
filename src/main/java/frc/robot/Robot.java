/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

// This program reads gyro data from an ADIS 16448 device as well as the Built-in
// Analog gyro and reports the information to the SmartDashboard and to Shuffleboard dashboard


package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.networktables.NetworkTableEntry;

import java.util.Map;


public class Robot extends TimedRobot {

  // Create object for the ADIS16448 gyro
  private static final ADIS16448_IMU imu = new ADIS16448_IMU();

  // Create object for the analog gyro
  private static final int analogGyroChannel = 0;    // Not sure about the channel
  private static final AnalogGyro analogGyro = new AnalogGyro(analogGyroChannel);

  // Shuffleboard objects
  private ShuffleboardTab tabGyro;
  private NetworkTableEntry entryAngleX;
  private NetworkTableEntry entryAngleY;
  private NetworkTableEntry entryAngleZ;
  private NetworkTableEntry entryAnalogAngle;

  // String constants for titles of Shuffleboard tabs, widgets, etc.
  private static String kTabTitle_Gyro = "Gyro";

  private static String kWidgetTitle_AngleX = "Angle X";
  private static String kWidgetTitle_AngleY = "Angle Y";
  private static String kWidgetTitle_AngleZ = "Angle Z";
  private static String kWidgetTitle_AnalogAngle = "Analog Angle";
  private static String kWidgetTitle_Gyro   = "IMU Gyro";

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    // Set up Shuffleboard tab
    // Create a separate Shuffleboard tab for the gyro data
    tabGyro = Shuffleboard.getTab(kTabTitle_Gyro);

    // Add simple widgets for the x, y, z gyro angles; these must be updated explicitly
    entryAngleX = tabGyro.add(kWidgetTitle_AngleX,-999.)
        .withWidget(BuiltInWidgets.kTextView)
        .getEntry();
    entryAngleY = tabGyro.add(kWidgetTitle_AngleY,-999.)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", -180, "max", 180))
        .getEntry();
    entryAngleZ = tabGyro.add(kWidgetTitle_AngleZ,-999.)
        .withWidget(BuiltInWidgets.kDial)
        .getEntry();

    entryAnalogAngle = tabGyro.add(kWidgetTitle_AnalogAngle,-999.)
        .withWidget(BuiltInWidgets.kTextView)
        .getEntry();

    // Add a complex widget for the gyro; since it is a sendable object it will be updated automatically
    tabGyro.add(kWidgetTitle_Gyro,imu)
        .withWidget(BuiltInWidgets.kGyro);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    // Get and save the angle values from gyro
    double angleX = imu.getAngleX();
    double angleY = imu.getAngleY();
    double angleZ = imu.getAngleZ();

    double analogAngle = analogGyro.getAngle();

    // Report all Gyro settings to the SmartDashboard

    SmartDashboard.putNumber("Gyro-X", angleX);
    SmartDashboard.putNumber("Gyro-Y", angleY);
    SmartDashboard.putNumber("Gyro-Z", angleZ);
    
    SmartDashboard.putNumber("Accel-X", imu.getAccelX());
    SmartDashboard.putNumber("Accel-Y", imu.getAccelY());
    SmartDashboard.putNumber("Accel-Z", imu.getAccelZ());
    
    SmartDashboard.putNumber("Pitch", imu.getPitch());
    SmartDashboard.putNumber("Roll", imu.getRoll());
    SmartDashboard.putNumber("Yaw", imu.getYaw());
    
    SmartDashboard.putNumber("Pressure: ", imu.getBarometricPressure());
    SmartDashboard.putNumber("Temperature: ", imu.getTemperature());

    SmartDashboard.putNumber("Analog Angle", analogAngle);

    // Report some of these settings to Shuffleboard as well
    // Note that these are all simple widgets containing a single number and must be updated explicitly
    //     whereas the Gyro widget is a complex widget containing a sendable object and should be updated
    //     automatically
    entryAngleX.setDouble(angleX);
    entryAngleY.setDouble(angleY);
    entryAngleZ.setDouble(angleZ);

    entryAnalogAngle.setDouble(analogAngle);
  }

  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
