package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class MainLift {

    private DigitalInput _leftTopMax = new DigitalInput(2);
    private DigitalInput _leftBottomMax = new DigitalInput(3);
    private DigitalInput _rightTopMax = new DigitalInput(4); // SET Channel
    private DigitalInput _rightBottomMax = new DigitalInput(5); // SET Channel

    private WPI_TalonSRX _rightMotor = new WPI_TalonSRX(22);
    private WPI_TalonSRX _leftMotor = new WPI_TalonSRX(23);
    private MotorControllerGroup _lift = new MotorControllerGroup(_leftMotor, _rightMotor);

    public MainLift() {
    }

    public void RobotUp(double speed) {
        if (!_leftTopMax.get() && !_rightTopMax.get())
            LiftUp(speed);
        else if (!_leftTopMax.get() && _rightTopMax.get())
            LeftUp(speed);
        else if (_leftTopMax.get() && !_rightTopMax.get())
            RightUp(speed);
        else
            LiftStop();
    }

    public void RobotDown(double speed) {
        if (!_leftBottomMax.get() && !_rightBottomMax.get())
            LiftDown(speed);
        else if (!_leftBottomMax.get() && _rightBottomMax.get())
            LeftDown(speed);
        else if (_leftBottomMax.get() && !_rightBottomMax.get())
            RightDown(speed);
        else
            LiftStop();
    }

    public void LeftUp(double speed) {
        speed = Math.abs(speed);
        _leftMotor.set(speed);
    }

    public void LeftDown(double speed) {
        speed = Math.abs(speed);
        _leftMotor.set(-speed);
    }

    public void LeftStop() {
        _leftMotor.set(0);
    }

    public void RightUp(double speed) {
        speed = Math.abs(speed);
        _rightMotor.set(speed);
    }

    public void RightDown(double speed) {
        speed = Math.abs(speed);
        _rightMotor.set(-speed);
    }

    public void RightStop() {
        _rightMotor.set(0);
    }

    private void LiftUp(double speed) {
        speed = Math.abs(speed);
        _lift.set(speed);
    }

    private void LiftDown(double speed) {
        speed = Math.abs(speed);
        _lift.set(-speed);
    }

    private void LiftStop() {
        _lift.set(0);
    }
}
