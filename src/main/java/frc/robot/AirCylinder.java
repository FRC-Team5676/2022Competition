package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class AirCylinder {

    final int Delay = 250;
    private long _lastTime;
    private DoubleSolenoid _valve;
    private boolean _extended;

    public AirCylinder(int moduleNumber, int forwardChannel, int reverseChannel, PneumaticsModuleType moduleType) {
        _valve = new DoubleSolenoid(moduleNumber, moduleType, forwardChannel, reverseChannel);
    }

    public void Extend(Boolean extend) {
        if (System.currentTimeMillis() - _lastTime > Delay) {
            if (extend && !_extended) {
                _valve.set(DoubleSolenoid.Value.kForward);
                _extended = true;
            } else if (!extend && _extended) {
                _valve.set(DoubleSolenoid.Value.kReverse);
                _extended = false;
            }
            _lastTime = System.currentTimeMillis();
        }
    }

    public boolean IsExtended() {
        if (_valve.get() == Value.kForward) {
            _extended = true;
            return true;
        }
        else {
            _extended = false;
            return false;
        }
    }
}
