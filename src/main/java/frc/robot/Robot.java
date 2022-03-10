/* Copyright (c) FIRST and other WPILib contributors.
 * Open Source Software; you can modify and/or share it under the terms of
 * the WPILib BSD license file in the root directory of this project.
*/
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/* Imports for Motor Control */
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;

/* For Drive */
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/* For Pneumatics */
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

  /* Spark drives */
  private static PWMVictorSPX upperIntake = new PWMVictorSPX(0);
  private static PWMVictorSPX lowerIntake = new PWMVictorSPX(1);

  /* Joysticks */
  private static XboxController ctl1 = new XboxController(0);
  private static XboxController ctl2 = new XboxController(1);

  /* Pneumatics */
  private static AirCylinder intakeExtension = new AirCylinder(1, 0, 1, PneumaticsModuleType.CTREPCM);
  private static AirCylinder rampLift = new AirCylinder(1, 3, 2, PneumaticsModuleType.CTREPCM);
  private static AirCylinder armLatch = new AirCylinder(1, 4, 5, PneumaticsModuleType.CTREPCM);
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

    /* Init Static Classes */
    Lifts.Init();
    Arms.Init();
    //ArmRotate.Init();

    /* Camera Setup */
    CameraServer.startAutomaticCapture(0);
    CameraServer.startAutomaticCapture(1);

    /* Setup Drivetrain Followers */
    driveRR.follow(driveRF);
    driveLR.follow(driveLF);

    /* Set Pneumatic Start Positions */
    rampLift.Extend(false);
    intakeExtension.Extend(false);
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

  /*
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

  /* This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /* This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    /* Set Buttons */
    boolean intakeBalls = ctl1.ButtonA() || ctl2.ButtonA();
    boolean ballSuckBack = ctl1.ButtonB() || ctl2.ButtonB();
    boolean raiseRamp = ctl1.ButtonY() || ctl2.ButtonY();
    boolean shootHigh = ctl1.BumperLeft() || ctl2.BumperLeft();
    boolean shootLow = ctl1.BumperRight() || ctl2.BumperRight();
    boolean latch = ctl1.DpadUp() || ctl1.DpadUp();
    boolean halfSpeed = ctl1.DpadRight() || ctl1.DpadRight();

    /* Arm Latch */
    armLatch.Extend(latch);

    /* Intake */
    if (intakeBalls) {
      intakeExtension.Extend(true);
      rampLift.Extend(false);
      upperIntake.set(-0.60);
      lowerIntake.set(-0.50);
    } else if (ballSuckBack) {
      rampLift.Extend(true);
      upperIntake.set(-0.60);
    } else if (shootHigh) {
      intakeExtension.Extend(false);
      rampLift.Extend(raiseRamp);
      upperIntake.set(0.15);
      lowerIntake.set(-1.0);
    } else if (shootLow) {
      intakeExtension.Extend(false);
      rampLift.Extend(raiseRamp);
      upperIntake.set(0.15);
      lowerIntake.set(-0.60);
    } else {
      intakeExtension.Extend(false);
      rampLift.Extend(raiseRamp);
      upperIntake.stopMotor();
      lowerIntake.stopMotor();
    }

    /* Lifts */
    double liftSpeed = ctl1.LeftTrigger() + ctl2.LeftTrigger();
    if (liftSpeed > 0) {
      if (ctl1.ButtonX())
        Lifts.RobotDown(liftSpeed);
      else
        Lifts.RobotUp(liftSpeed);
    } else {
      Lifts.RobotStop();
    }

    /* Arms */
    Arms.DioStatus();
    double armSpeed = ctl1.RightTrigger() + ctl2.RightTrigger();
    if (armSpeed > 0) {
      if (ctl1.ButtonX())
        Arms.RobotDown(armSpeed);
      else
        Arms.RobotUp(armSpeed);
    } else {
      Arms.RobotStop();
    }

    /* Arm Rotate */
    double rotSpeed = ctl1.RightStickY() * ctl1.RightStickY();
    rotSpeed = rotSpeed + ctl2.RightStickY() * ctl2.RightStickY();
    if (ctl1.RightStickY() > 0.0 || ctl2.RightStickY() > 0.0) {
      ArmRotate.RotateUp(rotSpeed);
    } else if (ctl1.RightStickY() < 0.0 || ctl2.RightStickY() < 0.0) {
      ArmRotate.RotateDown(rotSpeed);
    } else {
      ArmRotate.RotateStop();
    }

    // Drive
    double driveSpeed = 1.0;
    if (halfSpeed) driveSpeed = 0.5;
    if (ctl1.LeftStickY() != 0 || ctl1.LeftStickX() != 0) {
      robot.arcadeDrive(driveSpeed * ctl1.LeftStickX(), driveSpeed * -ctl1.LeftStickY());
    }
    if (ctl2.LeftStickY() != 0 || ctl2.LeftStickX() != 0) {
      robot.arcadeDrive(driveSpeed * ctl2.LeftStickX(), driveSpeed * -ctl2.LeftStickY());
    }
  }

  /* This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /* This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /* This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /* This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
      intakeExtension.Extend(ctl1.ButtonA());
      rampLift.Extend(ctl1.ButtonB());
  }
}