package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class AirCylinder {

    final int delay = 250;
    private long lastTime;
    private static DoubleSolenoid valve;

    public AirCylinder(int moduleNumber, int forwardChannel, int reverseChannel, PneumaticsModuleType moduleType) {
        valve = new DoubleSolenoid(moduleNumber, moduleType, forwardChannel, reverseChannel);
    }

    public void Extend(Boolean extend) {
        if (System.currentTimeMillis() - lastTime > delay) {
            if (extend) {
                valve.set(DoubleSolenoid.Value.kForward);
            } else {
                valve.set(DoubleSolenoid.Value.kReverse);
            }
            lastTime = System.currentTimeMillis();
        }
    }

    public Value Get() {
        return valve.get();
    }
}
