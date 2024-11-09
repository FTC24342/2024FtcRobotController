package org.firstinspires.ftc.teamcode.hardware;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Intake {
    private Servo axis1 = null;
    private Servo axis2 = null;
    private Servo pinch = null;
    public void Init(HardwareMap hardwareMap) {
        axis1 = hardwareMap.get(Servo.class, "axis1");
        axis2 = hardwareMap.get(Servo.class, "axis2");
        pinch = hardwareMap.get(Servo.class, "pinch");
    }
    public void GoToReadyPosition() {
        // make arm ready to pickup sample/specimen
        axis1.setPosition(0.5);
        axis2.setPosition(0.5);
        pinch.setPosition(0.5);//open
    }
    public void GoToPickupPosition() {
        // make arm ready to give sample/specimen to main claw
        axis1.setPosition(0.5);
        axis2.setPosition(0.5);
        pinch.setPosition(0.5);//open
    }
    public void GoToDrivePosition() {
        // make arm ready to pickup sample/specimen
        axis1.setPosition(0.5);
        axis2.setPosition(0.5);
        pinch.setPosition(0.5);//close
    }
    public void Pickup() {
        // bring arm wrist up so we can drive without issues
        axis1.setPosition(0.5);
        axis2.setPosition(0.5);
        pinch.setPosition(0.5);//close
    }
    public void DropOff() {
        // bring arm wrist up to main claw to transfer
        axis1.setPosition(0.5);
        axis2.setPosition(0.5);
        pinch.setPosition(0.5);//open
    }
}
