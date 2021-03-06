package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Arms {

    private static DigitalInput _leftExtMax = new DigitalInput(0);
    private static DigitalInput _leftRetMax = new DigitalInput(1);
    private static DigitalInput _rightExtMax = new DigitalInput(6);
    private static DigitalInput _rightRetMax = new DigitalInput(4);

    private static WPI_TalonSRX _rightMotor = new WPI_TalonSRX(20);
    private static WPI_TalonSRX _leftMotor = new WPI_TalonSRX(21);
    private static MotorControllerGroup _arms = new MotorControllerGroup(_leftMotor, _rightMotor);

    public static void Init() {
        _rightMotor.configFactoryDefault();
        _leftMotor.configFactoryDefault();
    }

    public static void DioStatus() {
        System.out.println("Arm - Left Ext: " + _leftExtMax.get());
        System.out.println("Arm - Left Ret: " + _leftRetMax.get());
        System.out.println("Arm - Right Ext: " + _rightExtMax.get());
        System.out.println("Arm - Right Ret: " + _rightRetMax.get());
    }

    public static void RobotUp(double speed) {
        if (_leftRetMax.get() && _rightRetMax.get())
            ArmsRetract(speed);
        else if (_leftRetMax.get() && !_rightRetMax.get()) {
            RightStop();
            LeftRetract(speed);
        }
        else if (!_leftRetMax.get() && _rightRetMax.get()) {
            LeftStop();
            RightRetract(speed);
        }
        else
            RobotStop();
    }

    public static void RobotDown(double speed) {
        if (_leftExtMax.get() && _rightExtMax.get())
            ArmsExtend(speed);
        else if (_leftExtMax.get() && !_rightExtMax.get()) {
            RightStop();
            LeftExtend(speed);
        }
        else if (!_leftExtMax.get() && _rightExtMax.get()) {
            LeftStop();
            RightExtend(speed);
        }
        else
            RobotStop();
    }

    public static void RobotStop() {
        _arms.stopMotor();
    }

    private static void ArmsExtend(double speed) {
        speed = Math.abs(speed);
        _arms.set(-speed);
    }

    private static void ArmsRetract(double speed) {
        speed = Math.abs(speed);
        _arms.set(speed);
    }

    private static void LeftExtend(double speed) {
        speed = Math.abs(speed);
        _leftMotor.set(-speed);
    }

    private static void LeftRetract(double speed) {
        speed = Math.abs(speed);
        _leftMotor.set(speed);
    }

    private static void LeftStop() {
        _leftMotor.stopMotor();
    }

    private static void RightExtend(double speed) {
        speed = Math.abs(speed);
        _rightMotor.set(-speed);
    }

    private static void RightRetract(double speed) {
        speed = Math.abs(speed);
        _rightMotor.set(speed);
    }

    private static void RightStop() {
        _rightMotor.stopMotor();
    }
}
