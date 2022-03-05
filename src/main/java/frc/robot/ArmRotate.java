package frc.robot;

/* For Pneumatics */
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class ArmRotate {

    private static AirCylinder armLatch = new AirCylinder(0, 4, 5, PneumaticsModuleType.CTREPCM);
    private static boolean latch = true;

    ArmRotate() {

    }

    /* Arm Latch */
    boolean latchChange = false;
    boolean latchButton = ctl1.ButtonY();
    if (latchButton) {
      latch = !latch;
      latchChange = true;
    }
    if (latchChange) {
      if (latch) {
        armLatch.Extend(false);
        armRotate.set(-0.5);
        armLatch.Extend(true);
        armRotate.stopMotor();
      } else {
        armLatch.Extend(false);
      }
      latchChange = false;
    }

        /* Arm Rotate */
        double rotSpeed = ctl1.RightStickY() * ctl1.RightStickY();
        if (armLatch.IsExtended())
          rotSpeed = 0.0;
        if (ctl1.RightStickY() > 0.0) {
          armRotate.set(rotSpeed);
        } else if (ctl1.RightStickY() < 0.0) {
          armRotate.set(-rotSpeed);
        } else {
          armRotate.set(0);
        }
    
    
}
