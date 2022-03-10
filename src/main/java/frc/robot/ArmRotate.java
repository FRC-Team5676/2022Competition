package frc.robot;

/* For Drive */
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class ArmRotate {

    private static WPI_TalonSRX armRotate = new WPI_TalonSRX(24);
    private static boolean IsLatched;

    public static void Init() {
        armRotate.configFactoryDefault();
        Latch();
    }

    public static void Latch() {
        if (!IsLatched) {
            RotateDown(-0.2);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            armRotate.stopMotor();
            IsLatched = true;
        }
    }

    public static void Unlatch() {
        IsLatched = false;
    }

    public static void RotateUp(double speed) {
        if (IsLatched)
            armRotate.stopMotor();
        else {
            speed = Math.abs(speed);
            armRotate.set(speed);
        }
    }

    public static void RotateDown(double speed) {
        if (IsLatched)
            armRotate.stopMotor();
        else {
            speed = Math.abs(speed);
            armRotate.set(-speed);
        }
    }

    public static void RotateStop() {
        armRotate.stopMotor();
    }
}
