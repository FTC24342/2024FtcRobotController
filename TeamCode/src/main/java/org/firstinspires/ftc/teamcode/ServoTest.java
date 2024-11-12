package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Pincher;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "servo test")
public class ServoTest extends OpMode {


    private Servo axis1 = null;
    private Servo axis2 = null;
    private Servo pinch = null;
    private Pincher pincher = new Pincher();
    private String testingServoAxis1 = "axis1";
    private String testingServoAxis2 = "axis2";
    private String testingServoPinch = "pinch";
    private double increment = 0.0001;
    private double servoPositionAxis1 = 0.85;
    private double servoPositionAxis2 = 1.0;
    private double servoPositionPinch = 0.5;
    private Telemetry.Item telPosition = null;

    public void ProcessWrist() {
        axis1.setPosition((gamepad1.right_stick_x + 1.0) / 2);
    }
    public void Axis2Test() {
        if (gamepad1.dpad_up) {
            pincher.GoToDrivePosition();
        } else if (gamepad1.dpad_down) {
            pincher.GoToReadyPosition();
        } else if (gamepad1.right_bumper) {
            pincher.Pickup();
        } else if (gamepad1.left_bumper) {
            pincher.DropOff();
        }
    }
    @Override
    public void init() {
        // run once when init is pressed
        axis1 = hardwareMap.get(Servo .class, testingServoAxis1);
        axis2 = hardwareMap.get(Servo .class, testingServoAxis2);
        pinch = hardwareMap.get(Servo .class, testingServoPinch);
        telemetry.clearAll();
        telemetry.setAutoClear(false);
        telPosition = telemetry.addData("Position:", "");
        axis1.setPosition(servoPositionAxis1);
        axis2.setPosition(servoPositionAxis2);
        pinch.setPosition(servoPositionPinch);
    }

    @Override
    public void init_loop() {
        // add stuff here for the init loop
    }

    @Override
    public void loop() {

//        Axis2Test();
        // runs while in play

      //  if(gamepad1.triangle) {
        //    if(servoPosition > + increment
        }

     //   telPosition.setValue("%f0.5", servo.getPosition());
     //   telemetry.update();

    //}


}
