package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class PushBar {
    public enum PusherPositions {
        IN, OUT
    }
    public  PusherPositions pusherPos = PusherPositions.IN;
    private  Servo pusherServo = null;
    public PushBar(HardwareMap hardwareMap) {
        pusherServo = hardwareMap.get(Servo.class, "pushbar");
    }
    public void init() {
        pusherServo.setPosition(0.82);
    }
    public  void out() {
        pusherServo.setPosition(0.3);
        pusherPos = PusherPositions.OUT;
    }

    public  void in() {
        pusherServo.setPosition(0.82);
        pusherPos = PusherPositions.IN;
    }
}
