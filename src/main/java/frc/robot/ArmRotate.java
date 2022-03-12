package frc.robot;

/* For Drive */
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class ArmRotate {

    private static WPI_TalonSRX _armRotate = new WPI_TalonSRX(24);
    private static boolean _isLatched;

    public static void Init() {
        _armRotate.configFactoryDefault();
    }

    public static void RotateUp(double speed) {
        if (_isLatched)
            _armRotate.stopMotor();
        else {
            speed = Math.abs(speed);
            _armRotate.set(speed);
        }
    }

    public static void RotateDown(double speed) {
        if (_isLatched)
            _armRotate.stopMotor();
        else {
            speed = Math.abs(speed);
            _armRotate.set(-speed);
        }
    }

    public static void RotateStop() {
        _armRotate.stopMotor();
    }
}
