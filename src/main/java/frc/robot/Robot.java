// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//Imports for Motor Control
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

import javax.lang.model.element.VariableElement;

import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.InvertType;

//For Controller (physical, this is not the motor controllers.)
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;

//For Drive
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase.MotorType;

//For Pneumatics
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

//For Cams
import edu.wpi.first.cameraserver.CameraServer;

//halo sensors (these tell the bot to stop climbing.)
import edu.wpi.first.wpilibj.DigitalInput;

/**
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

  // Drivetrain drives
  private static CANSparkMax driveRF = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static CANSparkMax driveLF = new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static CANSparkMax driveRR = new CANSparkMax(12, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static CANSparkMax driveLR = new CANSparkMax(13, CANSparkMaxLowLevel.MotorType.kBrushless);
  private static DifferentialDrive robot = new DifferentialDrive(driveLF, driveRF);

  // Lifts & Arms
  private static MainLift lift = new MainLift();
  private static Arms arms = new Arms();
  private static WPI_TalonSRX armRotate = new WPI_TalonSRX(24);

  // Spark drives
  private static PWMVictorSPX upperIntake = new PWMVictorSPX(0);
  private static PWMVictorSPX lowerIntake = new PWMVictorSPX(1);

  // Joysticks
  private static XboxController ctl1 = new XboxController(0);
  private static XboxController ctl2 = new XboxController(1);

  // Pneumatics
  // private static AirCylinder intakeExtension = new AirCylinder(0, 5, 6,
  // PneumaticsModuleType.CTREPCM);
  // private static AirCylinder rampLift = new AirCylinder(0, 7, 8,
  // PneumaticsModuleType.CTREPCM);

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Chooser Setup
    CameraServer.startAutomaticCapture(0);
    CameraServer.startAutomaticCapture(1);

    // setup followers
    driveRR.follow(driveRF);
    driveLR.follow(driveLF);
  }

  /**
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

    // Intake Balls
    Boolean buttonA = ctl1.ButtonA() || ctl2.ButtonA();
    // intakeExtension.Extend(buttonA);
    if (buttonA) {
      upperIntake.set(0.30);
      lowerIntake.set(0.85);
    }

    // Shoot High
    Boolean buttonY = ctl1.ButtonY() || ctl2.ButtonY();
    if (buttonY) {
      // intakeExtension.Extend(false);
      upperIntake.set(-0.30);
      lowerIntake.set(0.85);
    }

    // Shoot Low
    Boolean buttonB = ctl1.ButtonB() || ctl2.ButtonB();
    if (buttonB) {
      // intakeExtension.Extend(false);
      upperIntake.set(-0.50);
      lowerIntake.set(0.85);
    }

    // Climb
    if (ctl1.ButtonStart() || ctl2.ButtonStart()) {

    }

    /* Main Lifts */
    double liftSpeed = ctl1.LeftTrigger() + ctl2.LeftTrigger();
    if (liftSpeed > 0) {
      if (ctl1.ButtonX() || ctl2.ButtonX()) {
        lift.Down(liftSpeed);
      } else {
        lift.Up(liftSpeed);
      }
    } else {
      lift.Stop();
    }

    /* Arms */
    double armSpeed = ctl1.RightTrigger() + ctl2.RightTrigger();
    if (armSpeed > 0) {
      if (ctl1.ButtonX() || ctl2.ButtonX()) {
        arms.Retract(armSpeed);
      } else {
        arms.Extend(armSpeed);
      }
    } else {
      arms.Stop();;
    }


    // Arm Rotate
    if (ctl1.RightStickY() != 0) {
      Double rotSpeed = ctl1.RightStickY() * ctl1.RightStickY();
      armRotate.set(rotSpeed);
    }
    if (ctl2.RightStickY() != 0) {
      Double rotSpeed = ctl2.RightStickY() * ctl2.RightStickY();
      armRotate.set(rotSpeed);
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
