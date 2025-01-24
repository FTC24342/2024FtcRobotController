package org.firstinspires.ftc.teamcode;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slide;
import org.firstinspires.ftc.teamcode.hardware.SpecimenGrabber;

@TeleOp(name = "SweepTest")
public class TestClass extends OpMode {
    private MecanumDrive drive = null;
    private Intake intake = new Intake();  // get the intake class
    private Slide intakeSlide = new Slide("slide", "", Slide.ExtendMotorDirection.Forward, 1300, 1.0, 114.28);
    private Slide clawSlide = new Slide("lift", "resetlift", Slide.ExtendMotorDirection.Reverse, 3192, 1.0, 86); //68.568
    private SpecimenGrabber specimanGrabber = new SpecimenGrabber();
    private Telemetry.Item output = null;
    private Telemetry.Item output2 = null;
    Gamepad prevGamepad1 = new Gamepad();
    Gamepad currGamepad1 = new Gamepad();
    Gamepad prevGamepad2 = new Gamepad();
    Gamepad currGamepad2 = new Gamepad();
    Intake.IntakePositon desiredPosition = Intake.IntakePositon.Init;


    @Override
    public void init() {
        prevGamepad1.copy(gamepad1);
        currGamepad1.copy(gamepad1);
        prevGamepad2.copy(gamepad2);
        clawSlide.Init(hardwareMap);
        output = telemetry.addData("ticks", clawSlide.motor.getCurrentPosition());
        currGamepad2.copy(gamepad2);
        specimanGrabber.Init(hardwareMap);
        intakeSlide.Init(hardwareMap);
        intake.Init(hardwareMap);
        specimanGrabber.Init(hardwareMap);
        clawSlide.Init(hardwareMap);
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        intake.axis1.setPosition(0.805000);
        intake.axis2.setPosition(1.0);

    }
    @Override
    public void loop() {
        currGamepad1.copy(gamepad1);
        currGamepad2.copy(gamepad2);
        output.addData("ticks", clawSlide.motor.getCurrentPosition());
        drive.setPowersFeildCentric(new PoseVelocity2d(
                new Vector2d(
                        currGamepad1.left_stick_x,
                        -currGamepad1.left_stick_y
                ),
                currGamepad1.right_stick_x
        ), 1.0);

        prevGamepad1.copy(currGamepad1);
        prevGamepad2.copy(currGamepad2);

    }

}