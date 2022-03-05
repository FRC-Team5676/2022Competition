/* Copyright (c) FIRST and other WPILib contributors.
 * Open Source Software; you can modify and/or share it under the terms of
 * the WPILib BSD license file in the root directory of this project.
*/
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/* Imports for Motor Control */
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

/* For Drive */
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/* For Pneumatics */
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

/* For Cameras */
import edu.wpi.first.cameraserver.CameraServer;

/*
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  /* Drivetrain drives */
  private static CANSparkMax driveRF = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static CANSparkMax driveLF = new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static CANSparkMax driveRR = new CANSparkMax(12, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static CANSparkMax driveLR = new CANSparkMax(13, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static DifferentialDrive robot = new DifferentialDrive(driveLF, driveRF);

  /* Lifts & Arms */
  private static MainLift lift = new MainLift();
  private static Arms arms = new Arms();
  private static WPI_TalonSRX armRotate = new WPI_TalonSRX(24);

  /* Spark drives */
  private static PWMVictorSPX upperIntake = new PWMVictorSPX(0);
  private static PWMVictorSPX lowerIntake = new PWMVictorSPX(1);

  /* Joysticks */
  private static XboxController ctl1 = new XboxController(0);
  private static XboxController ctl2 = new XboxController(1);

  /* Pneumatics */
  private static AirCylinder intakeExtension = new AirCylinder(0, 1, 2, PneumaticsModuleType.CTREPCM);
  private static AirCylinder rampLift = new AirCylinder(0, 3, 4, PneumaticsModuleType.CTREPCM);
  private static AirCylinder armLatch = new AirCylinder(0, 5, 6, PneumaticsModuleType.CTREPCM);
  boolean latch = true;

  /*
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    /* Camera Setup */
    CameraServer.startAutomaticCapture(0);
    CameraServer.startAutomaticCapture(1);

    /* Setup Drivetrain Followers */
    driveRR.follow(driveRF);
    driveLR.follow(driveLF);

    /* Set Pneumatic Start Positions */
    rampLift.Extend(false);
    intakeExtension.Extend(false);
    armLatch.Extend(true);
  }

  /*
   * This function is called every robot packet, no matter the mode. Use this for
   * items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and
   * test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
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
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    /* Arm Release Cylinder */
    boolean latchButton = ctl1.ButtonY();
    if (latchButton) {
      latch = !latch;
    }
    if (latch) {
      armLatch.Extend(false);
      armRotate.set(-0.5);
      armLatch.Extend(true);
      armRotate.stopMotor();
    } else {
      armLatch.Extend(false);
    }

    // Intake Balls
    boolean intakeBalls = ctl1.ButtonA();
    intakeExtension.Extend(intakeBalls);
    if (intakeBalls) {
      rampLift.Extend(false);
      upperIntake.set(0.30);
      lowerIntake.set(0.85);
    }

    // Shoot High
    boolean shootHigh = ctl1.BumperLeft();
    if (shootHigh) {
      intakeExtension.Extend(false);
      upperIntake.set(-0.30);
      lowerIntake.set(0.85);
      rampLift.Extend(true);
    }

    // Shoot Low
    boolean shootLow = ctl1.BumperRight();
    if (shootLow) {
      intakeExtension.Extend(false);
      upperIntake.set(-0.50);
      lowerIntake.set(0.85);
      rampLift.Extend(true);
    }

    /* Main Lifts */
    double liftSpeed = ctl1.LeftTrigger();
    if (liftSpeed > 0) {
      if (ctl1.ButtonX())
        lift.RobotDown(liftSpeed);
      else
        lift.RobotUp(liftSpeed);
    } else {
      lift.RobotStop();
    }

    /* Arms */
    double armSpeed = ctl1.RightTrigger();
    if (armSpeed > 0) {
      if (ctl1.ButtonX())
        arms.RobotDown(armSpeed);
      else
        arms.RobotUp(armSpeed);
    } else {
      arms.RobotStop();
    }

    /* Arm Rotate */
    double rotSpeed = ctl1.RightStickY() * ctl1.RightStickY();
    if (armLatch.IsExtended()) rotSpeed = 0.0;
    if (ctl1.RightStickY() > 0.0) {
      armRotate.set(rotSpeed);
    } else if (ctl1.RightStickY() < 0.0) {
      armRotate.set(-rotSpeed);
    } else {
      armRotate.set(0);
    }

    // Drive
    if (ctl1.LeftStickY() != 0 || ctl1.LeftStickX() != 0) {
      robot.arcadeDrive(ctl1.LeftStickX(), -ctl1.LeftStickY());
    }
    if (ctl2.LeftStickY() != 0 || ctl2.LeftStickX() != 0) {
      robot.arcadeDrive(ctl2.LeftStickX(), -ctl2.LeftStickY());
    }

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
}
