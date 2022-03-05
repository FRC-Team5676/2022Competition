package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class AirCylinder {

    final int delay = 250;
    private long lastTime;
    private DoubleSolenoid valve;
    private boolean extended;

    public AirCylinder(int moduleNumber, int forwardChannel, int reverseChannel, PneumaticsModuleType moduleType) {
        valve = new DoubleSolenoid(moduleNumber, moduleType, forwardChannel, reverseChannel);
    }

    public void Extend(Boolean extend) {
        if (System.currentTimeMillis() - lastTime > delay) {
            if (extend && !extended) {
                valve.set(DoubleSolenoid.Value.kForward);
                extended = true;
            } else if (!extend && extended) {
                valve.set(DoubleSolenoid.Value.kReverse);
                extended = false;
            }
            lastTime = System.currentTimeMillis();
        }
    }

    public boolean IsExtended() {
        if (valve.get() == Value.kForward) {
            extended = true;
            return true;
        }
        else {
            extended = false;
            return false;
        }
    }
}
