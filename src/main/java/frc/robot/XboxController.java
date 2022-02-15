package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class XboxController {
    final double DeadBand = 0.05;
    private int _controllerId;
    private Joystick _joystick;

    public XboxController(int controllerId) {
        _controllerId = controllerId;
        _joystick = new Joystick(_controllerId);
    }

    public boolean ButtonA() {
        // 1
        return _joystick.getRawButton(1);
    }

    public boolean ButtonB() {
        // 2
        return _joystick.getRawButton(2);
    }

    public boolean ButtonX() {
        // 3
        return _joystick.getRawButton(3);
    }

    public boolean ButtonY() {
        // 4
        return _joystick.getRawButton(4);
    }

    public boolean BumperLeft() {
        // 5
        return _joystick.getRawButton(5);
    }

    public boolean BumperRight() {
        // 6
        return _joystick.getRawButton(6);
    }

    public boolean ButtonBack() {
        // 7
        return _joystick.getRawButton(7);
    }

    public boolean ButtonStart() {
        // 8
        return _joystick.getRawButton(8);
    }

    public boolean LeftStickDown() {
        // 9
        return _joystick.getRawButton(9);
    }

    public boolean RightStickDown() {
        // 10
        return _joystick.getRawButton(10);
    }

    public double LeftStickX() {
        // 0
        return GetPositionUsingDeadBand(_joystick.getRawAxis(0));
    }

    public double LeftStickY() {
        // 1
        return GetPositionUsingDeadBand(_joystick.getRawAxis(1));
    }

    public double LeftTrigger() {
        // 2
        return _joystick.getRawAxis(2);
    }

    public double RightTrigger() {
        // 3
        return _joystick.getRawAxis(3);
    }

    public double RightStickX() {
        // 4
        return GetPositionUsingDeadBand(_joystick.getRawAxis(4));
    }

    public double RightStickY() {
        // 5
        return GetPositionUsingDeadBand(_joystick.getRawAxis(5));
    }

    public boolean RumbleLeft() {
        return false;
    }

    public boolean RumbleRight() {
        return false;
    }

    private double GetPositionUsingDeadBand(double position) {
        /* Controller Deadband */
        if (Math.abs(position) < DeadBand) {
            position = 0;
        }

        return position;
    }
}