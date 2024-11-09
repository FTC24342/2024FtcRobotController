package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "servo test")
public class ServoTest extends OpMode {


    private Servo servo = null;

    private String testingServoName = "axis1";
    private double increment = 0.0001;
    private double servoPosition = 0.5;
    private Telemetry.Item telPosition = null;

    public void ProcessWrist() {
        servo.setPosition((gamepad1.right_stick_x+1.0)/2);
    }
    @Override
    public void init() {
        // run once when init is pressed
        servo = hardwareMap.get(Servo .class, testingServoName);
        telemetry.clearAll();
        telemetry.setAutoClear(false);
        telPosition = telemetry.addData("Position:", "");
        servo.setPosition(servoPosition);
    }

    @Override
    public void init_loop() {
        // add stuff here for the init loop
    }

    @Override
    public void loop() {
        // runs while in play

        if(gamepad1.triangle) {
            if(servoPosition < 1.0 - increment) {
                servoPosition = servoPosition + increment;
            }
        }

        if(gamepad1.x) {
            if(servoPosition > 0.0 + increment) {
                servoPosition = servoPosition - increment;
            }
        }

        telPosition.setValue("%f0.5", servo.getPosition());
        telemetry.update();

    }
}
