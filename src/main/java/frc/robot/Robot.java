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
  private String _autoSelected;
  private final SendableChooser<String> _chooser = new SendableChooser<>();

  /* Drivetrain drives */
  private static CANSparkMax _driveRF = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static CANSparkMax _driveLF = new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static CANSparkMax _driveRR = new CANSparkMax(12, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static CANSparkMax _driveLR = new CANSparkMax(13, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static DifferentialDrive _robot = new DifferentialDrive(_driveLF, _driveRF);

  /* Spark drives */
  private static PWMVictorSPX _upperIntake = new PWMVictorSPX(0);
  private static PWMVictorSPX _lowerIntake = new PWMVictorSPX(1);

  /* Joysticks */
  private static XboxController _ctl1 = new XboxController(0);
  private static XboxController _ctl2 = new XboxController(1);

  /* Pneumatics */
  private static AirCylinder _intakeExtension = new AirCylinder(1, 0, 1, PneumaticsModuleType.CTREPCM);
  private static AirCylinder _rampLift = new AirCylinder(1, 3, 2, PneumaticsModuleType.CTREPCM);
  private static AirCylinder _armLatch = new AirCylinder(1, 4, 5, PneumaticsModuleType.CTREPCM);
  boolean extendIntake = false;

  /* Delay Times */
  long _intakeLastTime;

  /*
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    _chooser.setDefaultOption("Default Auto", kDefaultAuto);
    _chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", _chooser);

    /* Init Static Classes */
    Lifts.Init();
    Arms.Init();
    // ArmRotate.Init();

    /* Camera Setup */
    CameraServer.startAutomaticCapture(0);
    CameraServer.startAutomaticCapture(1);

    /* Setup Drivetrain Followers */
    _driveRR.follow(_driveRF);
    _driveLR.follow(_driveLF);

    /* Set Pneumatic Start Positions */
    _rampLift.Extend(false);
    _intakeExtension.Extend(false);
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
    _autoSelected = _chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + _autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (_autoSelected) {
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
    boolean latch = _ctl1.DpadUp() || _ctl2.DpadUp();
    boolean intakeExt = _ctl1.ButtonB() || _ctl2.ButtonB();
    boolean intakeBalls = (_ctl1.ButtonA() || _ctl2.ButtonA()) && _intakeExtension.IsExtended();
    boolean ballSuckBack = (_ctl1.ButtonA() || _ctl2.ButtonA()) && !_intakeExtension.IsExtended();
    boolean raiseRamp = _ctl1.ButtonY() || _ctl2.ButtonY();
    boolean shootHigh = _ctl1.BumperLeft() || _ctl2.BumperLeft();
    boolean shootLow = _ctl1.BumperRight() || _ctl2.BumperRight();
    boolean c1Down = _ctl1.ButtonX() && _ctl1.LeftTrigger() > 0;
    boolean c2Down = _ctl2.ButtonX() && _ctl2.LeftTrigger() > 0;
    boolean c1Ret = _ctl1.ButtonX() && _ctl1.RightTrigger() > 0;
    boolean c2Ret = _ctl2.ButtonX() && _ctl2.RightTrigger() > 0;
    boolean halfSpeed = _ctl1.DpadRight() || _ctl2.DpadRight();

    /* Arm Latch */
    _armLatch.Extend(latch);

    /* Intake */
    if (intakeExt) {
      if (System.currentTimeMillis() - _intakeLastTime > 250) {
        extendIntake = !extendIntake;
        _intakeLastTime = System.currentTimeMillis();
      }
    }
    _intakeExtension.Extend(extendIntake);

    /* Intake, Suckback & Shoot */
    if (intakeBalls) {
      /* Intake Balls */
      _rampLift.Extend(false);
      _upperIntake.set(-1.0);
      _lowerIntake.set(-0.5);
    } else if (ballSuckBack) {
      /* Ball Suckback */
      _rampLift.Extend(true);
      _upperIntake.set(-1.0);
    } else if (shootHigh) {
      /* Shoot High */
      _intakeExtension.Extend(false);
      _rampLift.Extend(raiseRamp);
      _upperIntake.set(0.8);
      _lowerIntake.set(-1.0);
    } else if (shootLow) {
      /* Shoot Low */
      _intakeExtension.Extend(false);
      _rampLift.Extend(raiseRamp);
      _upperIntake.set(0.4);
      _lowerIntake.set(-0.6);
    } else {
      /* At Rest */
      _rampLift.Extend(raiseRamp);
      _upperIntake.stopMotor();
      _lowerIntake.stopMotor();
    }

    /* Lifts */
    double liftSpeed = _ctl1.LeftTrigger() + _ctl2.LeftTrigger();
    if (liftSpeed > 0) {
      if (c1Down || c2Down)
        Lifts.RobotDown(liftSpeed);
      else
        Lifts.RobotUp(liftSpeed);
    } else {
      Lifts.RobotStop();
    }

    /* Arms */
    double armSpeed = _ctl1.RightTrigger() + _ctl2.RightTrigger();
    if (armSpeed > 0) {
      if (c1Ret || c2Ret)
        Arms.RobotDown(armSpeed);
      else
        Arms.RobotUp(armSpeed);
    } else {
      Arms.RobotStop();
    }

    /* Arm Rotate */
    double rotSpeed = _ctl1.RightStickY() * _ctl1.RightStickY();
    rotSpeed = rotSpeed + _ctl2.RightStickY() * _ctl2.RightStickY();
    if (_ctl1.RightStickY() > 0.0 || _ctl2.RightStickY() > 0.0) {
      ArmRotate.RotateUp(rotSpeed);
    } else if (_ctl1.RightStickY() < 0.0 || _ctl2.RightStickY() < 0.0) {
      ArmRotate.RotateDown(rotSpeed);
    } else {
      ArmRotate.RotateStop();
    }

    // Drive
    double driveSpeed = 1.0;
    if (halfSpeed)
      driveSpeed = 0.5;
    if (_ctl1.LeftStickY() != 0 || _ctl1.LeftStickX() != 0) {
      _robot.arcadeDrive(driveSpeed * _ctl1.LeftStickX(), driveSpeed * -_ctl1.LeftStickY());
    }
    if (_ctl2.LeftStickY() != 0 || _ctl2.LeftStickX() != 0) {
      _robot.arcadeDrive(driveSpeed * _ctl2.LeftStickX(), driveSpeed * -_ctl2.LeftStickY());
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
  }
}