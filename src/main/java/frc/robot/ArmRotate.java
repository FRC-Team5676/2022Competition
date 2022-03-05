package frc.robot;

/* For Drive */
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/* For Pneumatics */
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class ArmRotate {

    private static WPI_TalonSRX armRotate = new WPI_TalonSRX(24);
    private static AirCylinder armLatch = new AirCylinder(0, 4, 5, PneumaticsModuleType.CTREPCM);
    private static boolean IsLatched = true;

    public static void Init() {
        armRotate.configFactoryDefault();
    }

    public static void Latch() {
        IsLatched = true;
        armLatch.Extend(false);
        armRotate.set(-0.5);
        armLatch.Extend(true);
        armRotate.stopMotor();
    }

    public static void Unlatch() {
        IsLatched = false;
        armLatch.Extend(false);
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
