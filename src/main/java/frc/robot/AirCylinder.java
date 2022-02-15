package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class AirCylinder {

    final int delay = 250;
    private long lastTime;
    private static DoubleSolenoid valve;
    private Value value;

    public AirCylinder(int moduleNumber, int forwardChannel, int reverseChannel, PneumaticsModuleType moduleType) {
        valve = new DoubleSolenoid(moduleNumber, moduleType, forwardChannel, reverseChannel);
        value = valve.get();
    }

    public void Extend(bool extend) {
        if (System.currentTimeMillis() - lastTime > delay) {
            value = valve.get();
        }
        valve.set(DoubleSolenoid.Value.kForward);
    }
}
