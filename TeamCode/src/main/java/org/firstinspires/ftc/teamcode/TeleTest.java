package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slide;
import org.firstinspires.ftc.teamcode.hardware.MecanumEncoder;
import org.firstinspires.ftc.teamcode.hardware.SpecimenGrabber;

import org.firstinspires.ftc.teamcode.hardware.PushBar;
@TeleOp(name = "TeleTest")
public class TeleTest extends OpMode {

    private MecanumDrive drive = null;
    private PushBar pushBar = null;
    Gamepad prevGamepad1 = new Gamepad();
    Gamepad currGamepad1 = new Gamepad();
    Gamepad prevGamepad2 = new Gamepad();
    Gamepad currGamepad2 = new Gamepad();

    public void processPusher() {
        if (currGamepad1.x && !prevGamepad1.x) {
            if(pushBar.pusherPos == PushBar.PusherPositions.IN) {
                pushBar.out();
            } else if (pushBar.pusherPos == PushBar.PusherPositions.OUT) {
                pushBar.in();
            }
        }
    }

    @Override
    public void init() {
        prevGamepad1.copy(gamepad1);
        currGamepad1.copy(gamepad1);
        prevGamepad2.copy(gamepad2);
        currGamepad2.copy(gamepad2);
        pushBar = new PushBar(hardwareMap);
        pushBar.init();
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
    }



    @Override
    public void loop() {
        currGamepad1.copy(gamepad1);
        currGamepad2.copy(gamepad2);

        drive.setPowersFeildCentric(new PoseVelocity2d(
                new Vector2d(
                        currGamepad1.left_stick_x,
                        -currGamepad1.left_stick_y
                ),
                currGamepad1.right_stick_x
        ), 1.0);
        processPusher();
        prevGamepad1.copy(currGamepad1);
        prevGamepad2.copy(currGamepad2);

    }
}
    //END ACTIONS





