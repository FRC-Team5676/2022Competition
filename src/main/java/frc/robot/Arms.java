package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Arms {

    private DigitalInput _leftExtMax = new DigitalInput(0);
    private DigitalInput _leftRetMax = new DigitalInput(1);
    private DigitalInput _rightExtMax = new DigitalInput(6);
    private DigitalInput _rightRetMax = new DigitalInput(4);

    private WPI_TalonSRX _rightMotor = new WPI_TalonSRX(20);
    private WPI_TalonSRX _leftMotor = new WPI_TalonSRX(21);
    private MotorControllerGroup _arms = new MotorControllerGroup(_leftMotor, _rightMotor);

    public Arms() {
    }

    public void RobotArmsExtend(double speed) {
        if (!_leftExtMax.get() && !_rightExtMax.get())
            ArmsExtend(speed);
        else if (!_leftExtMax.get() && _rightExtMax.get())
            LeftExtend(speed);
        else if (_leftExtMax.get() && !_rightExtMax.get())
            RightExtend(speed);
        else
            ArmsStop();
    }

    public void RobotArmsRetract(double speed) {
        if (!_leftRetMax.get() && !_rightRetMax.get())
            ArmsRetract(speed);
        else if (!_leftRetMax.get() && _rightRetMax.get())
            LeftRetract(speed);
        else if (_leftRetMax.get() && !_rightRetMax.get())
            RightRetract(speed);
        else
            ArmsStop();
    }

    public void LeftExtend(double speed) {
        speed = Math.abs(speed);
        _leftMotor.set(speed);
    }

    public void LeftRetract(double speed) {
        speed = Math.abs(speed);
        _leftMotor.set(-speed);
    }

    public void LeftStop() {
        _leftMotor.set(0);
    }

    public void RightExtend(double speed) {
        speed = Math.abs(speed);
        _rightMotor.set(speed);
    }

    public void RightRetract(double speed) {
        speed = Math.abs(speed);
        _rightMotor.set(-speed);
    }

    public void RightStop() {
        _rightMotor.set(0);
    }

    private void ArmsExtend(double speed) {
        speed = Math.abs(speed);
        _arms.set(speed);
    }

    private void ArmsRetract(double speed) {
        speed = Math.abs(speed);
        _arms.set(-speed);
    }

    private void ArmsStop() {
        _arms.set(0);
    }

}
