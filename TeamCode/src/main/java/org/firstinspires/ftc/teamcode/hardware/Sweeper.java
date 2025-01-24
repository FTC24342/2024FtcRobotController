package org.firstinspires.ftc.teamcode.hardware;



import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Sweeper {
    private Servo servo = null;
    private double outPosition = 0.9;
    private double inPosition = 0;
    private double range = 0.020;
    public void init(HardwareMap hardwareMap) {
        this.servo = hardwareMap.get(Servo.class, "sweeper");
        servo.setPosition(inPosition);
    }

    public void sweeperOut() {
        if (servo.getPosition() <=  outPosition - range) {
            servo.setPosition(outPosition);
        }

    }

    public void sweeperIn() {
        if (servo.getPosition() >= inPosition + range)
            servo.setPosition(inPosition);
        }

    }
