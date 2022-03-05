package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class MainLift {

    private static DigitalInput _leftTopMax = new DigitalInput(2);
    private static DigitalInput _leftBottomMax = new DigitalInput(3);
    private static DigitalInput _rightTopMax = new DigitalInput(5);
    private static DigitalInput _rightBottomMax = new DigitalInput(7);

    private static WPI_TalonSRX _rightMotor = new WPI_TalonSRX(22);
    private static WPI_TalonSRX _leftMotor = new WPI_TalonSRX(23);
    private static MotorControllerGroup _lift = new MotorControllerGroup(_rightMotor, _leftMotor);

    public MainLift() {
        _rightMotor.configFactoryDefault();
        _leftMotor.configFactoryDefault();
    }

    public void DioStatus() {
        System.out.println("Lift - Left Top: " + _leftTopMax.get());
        System.out.println("Lift - Left Bottom: " + _leftBottomMax.get());
        System.out.println("Lift - Right Top: " + _rightTopMax.get());
        System.out.println("Lift - Right Bottom: " + _rightBottomMax.get());
    }

    public void RobotUp(double speed) {
        if (_leftBottomMax.get() && _rightBottomMax.get())
            LiftUp(speed);
        else if (_leftBottomMax.get() && !_rightBottomMax.get()) {
            RightStop();
            LeftUp(speed);
        }
        else if (!_leftBottomMax.get() && _rightBottomMax.get()) {
            LeftStop();
            RightUp(speed);
        }
        else
            RobotStop();
    }

    public void RobotDown(double speed) {
        if (_leftTopMax.get() && _rightTopMax.get())
            LiftDown(speed);
        else if (_leftTopMax.get() && !_rightTopMax.get()) {
            RightStop();
            LeftDown(speed);
        }
        else if (!_leftTopMax.get() && _rightTopMax.get()) {
            LeftStop();
            RightDown(speed);
        }
        else
            RobotStop();
    }

    public void RobotStop() {
        _lift.stopMotor();
    }

    private void LiftUp(double speed) {
        speed = Math.abs(speed);
        _lift.set(speed);
    }

    private void LiftDown(double speed) {
        speed = Math.abs(speed);
        _lift.set(-speed);
    }

    private void LeftUp(double speed) {
        speed = Math.abs(speed);
        _leftMotor.set(speed);
    }

    private void LeftDown(double speed) {
        speed = Math.abs(speed);
        _leftMotor.set(-speed);
    }

    private void LeftStop() {
        _leftMotor.stopMotor();
    }

    private void RightUp(double speed) {
        speed = Math.abs(speed);
        _rightMotor.set(speed);
    }

    private void RightDown(double speed) {
        speed = Math.abs(speed);
        _rightMotor.set(-speed);
    }

    private void RightStop() {
        _rightMotor.stopMotor();
    }
}
