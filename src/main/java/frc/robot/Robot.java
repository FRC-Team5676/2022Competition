// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//Imports for Motor Control
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

import javax.lang.model.element.VariableElement;

import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.InvertType;

//For Controller (physical, this is not the motor controllers.)
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;

//For Drive
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

//For Pneumatics
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

//For Cams
import edu.wpi.first.cameraserver.CameraServer;

//

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  
  //Drive callbacks
  private static WPI_TalonSRX driveRF = new WPI_TalonSRX(10); 
  private static WPI_TalonSRX driveLF = new WPI_TalonSRX(11); 
  private static WPI_TalonSRX driveRR = new WPI_TalonSRX(12);
  private static WPI_TalonSRX driveLR = new WPI_TalonSRX(13);
  private static DifferentialDrive robot = new DifferentialDrive(driveLF, driveRF);

  //Screw callbacks
  private static WPI_TalonSRX rightScrew = new WPI_TalonSRX(20);
  private static WPI_TalonSRX leftScrew = new WPI_TalonSRX(21);

  //Wench callbacks
  private static WPI_TalonSRX rightWench = new WPI_TalonSRX(30);
  private static WPI_TalonSRX leftWench = new WPI_TalonSRX(31);

  //Joysticks
  private static XboxController ctl1 = new XboxController(0);
  private static XboxController ctl2 = new XboxController(1);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

  //Chooser Setup
  CameraServer.startAutomaticCapture(0);
  CameraServer.startAutomaticCapture(1);

  //Factory default all drives
  driveRF.configFactoryDefault();
  driveLF.configFactoryDefault();
  driveLR.configFactoryDefault();
  driveRR.configFactoryDefault();
  rightScrew.configFactoryDefault();
  leftScrew.configFactoryDefault();
  rightWench.configFactoryDefault();
  leftWench.configFactoryDefault();

  //setup followers
  driveRR.follow(driveRF);
  driveLR.follow(driveLF);

  //flip values so robot moves forwardard when stick-forwardard/green LEDS
  driveRF.setInverted(true);
  driveLF.setInverted(false);
  driveRR.setInverted(InvertType.FollowMaster);
  driveLR.setInverted(InvertType.FollowMaster);

  //Sensor phasing
  driveRF.setSensorPhase(true);
  driveLF.setSensorPhase(true);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    //Intake Balls
    if (ctl1.ButtonA() || ctl2.ButtonA()) {

    }

    //Shoot High
    if (ctl1.ButtonY() || ctl2.ButtonY()) {

    }

    //Shoot Low
    if (ctl1.ButtonB() || ctl2.ButtonB()) {

    }

    //Climb
    if (ctl1.ButtonStart() || ctl2.ButtonStart()) {

    }

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
