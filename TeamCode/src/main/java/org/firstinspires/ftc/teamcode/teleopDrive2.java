package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.teamcode.hardware.Pincher;
import org.firstinspires.ftc.teamcode.hardware.Slide;
import org.firstinspires.ftc.teamcode.hardware.SpecimenGrabber;


@TeleOp()
public class teleopDrive2 extends OpMode {

    private String TESTBOT = "24342-RC";
    private Telemetry.Item telPathDebug = null;
    private MecanumDrive drive = null;
    private Slide intakeSlide = new Slide("slide", "", Slide.ExtendMotorDirection.Forward, 1300, 1.0, 114.28);
    private Slide clawSlide = new Slide("lift", "resetlift", Slide.ExtendMotorDirection.Reverse, 2600, 1.0,68.568);
    private SpecimenGrabber specimanGrabber = new SpecimenGrabber();
    private Pincher pincher = new Pincher();
    private enum PincherPostion {DRIVE, READY, PICKUP}
    private PincherPostion currPincherPos = null;

    private String wifiSsid = "";
    Gamepad prevGamepad1 = new Gamepad();
    Gamepad currGamepad1 = new Gamepad();
    Gamepad prevGamepad2 = new Gamepad();
    Gamepad currGamepad2 = new Gamepad();


    //    public void processLift() {
//        if (currGamepad2.left_trigger == 0 && currGamepad2.right_trigger == 0) {
//            clawSlide.Stop();
//        }
//        else if (currGamepad2.left_trigger != 0 && currGamepad2.right_trigger == 0){
//            clawSlide.Retract(currGamepad2.left_trigger);
//        }
//        else if (currGamepad2.right_trigger != 0 && currGamepad2.left_trigger == 0) {
//            clawSlide.Extend(currGamepad2.right_trigger);
//        }
//    }
    public void processClawStop() {
        if (currGamepad2.dpad_right && !prevGamepad2.dpad_right) {
            clawSlide.Stop();
        }
    }
    public void processClawManualDown() {
        if(currGamepad2.left_trigger > 0.1) {
            clawSlide.Retract(currGamepad1.left_trigger);
        }
    }
    public void hangSpecimen() {
        try {
            clawSlide.MoveTo(0, 0.75);
            Thread.sleep(200);
            specimanGrabber.Open();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void processLiftDPad() {
        if (currGamepad2.dpad_up && !prevGamepad2.dpad_up) {
            clawSlide.MoveTo(20,1);
        }
        else if (currGamepad2.dpad_down && !prevGamepad2.dpad_down) {
            //hang specimen
            hangSpecimen();

        }
    }
    public void processSlide() {
        if (currGamepad1.left_trigger == 0 && currGamepad1.right_trigger == 0) {
            intakeSlide.Stop();
        }
        else if (currGamepad1.left_trigger != 0 && currGamepad1.right_trigger == 0){
            intakeSlide.Retract(currGamepad1.left_trigger);//1/8th speed of input trigger
        }
        else if (currGamepad1.right_trigger != 0 && currGamepad1.left_trigger == 0) {
            intakeSlide.Extend(currGamepad1.right_trigger);
        }
        else if(currGamepad1.left_trigger > 0 && currGamepad1.right_trigger > 0) {
            intakeSlide.Stop();
        }
    }
    public void processSpecimanGrabber() {
        if(currGamepad2.x) {
            specimanGrabber.Close();
        } else if (currGamepad2.y){
            specimanGrabber.Open();
        }
    }
    public void processPincherActions() {
        if (currGamepad1.left_bumper && !prevGamepad1.left_bumper) {
            if (currPincherPos == PincherPostion.DRIVE) {
                pincher.GoToReadyPosition();
                currPincherPos = PincherPostion.READY;
            } else {
                pincher.GoToDrivePosition();
                currPincherPos = PincherPostion.DRIVE;
            }
        }
        else if (currGamepad1.right_bumper && !prevGamepad1.right_bumper) {
            pincher.GoToPickupPosition();
            currPincherPos = PincherPostion.PICKUP;
        }
        else if (currGamepad1.x && !prevGamepad1.x) {
            if (pincher.isOpen) {
                pincher.PincherClose();
            }
            else {
                pincher.PincherOpen();
            }
        }
    }
    @Override
    public void init() {
        //Continue defining motors
        intakeSlide.Init(hardwareMap);
        clawSlide.Init(hardwareMap);
        pincher.Init(hardwareMap);
        currPincherPos = PincherPostion.DRIVE;
        specimanGrabber.Init(hardwareMap);

        // run once when init is pressed
        wifiSsid = WifiUtil.getConnectedSsid();

        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        telemetry.clearAll();
        telemetry.setAutoClear(false);
        telPathDebug = telemetry.addData("PathDebug:", "");

        prevGamepad1.copy(gamepad1);
        currGamepad1.copy(gamepad1);
        prevGamepad2.copy(gamepad2);
        currGamepad2.copy(gamepad2);
    }

    @Override
    public void init_loop() {
        // add stuff here for the init loop
        telPathDebug.setValue(wifiSsid);
        telemetry.update();
    }

    @Override
    public void loop() {
        currGamepad1.copy(gamepad1);
        currGamepad2.copy(gamepad2);

        // runs while in play
        drive.setPowersFeildCentric(new PoseVelocity2d(
                new Vector2d(
                        currGamepad1.left_stick_x,
                        -currGamepad1.left_stick_y
                ),
                currGamepad1.right_stick_x
        ), 1.0);

        // processLift();
        processSlide();
        processSpecimanGrabber();
        processLiftDPad();
        processPincherActions();
        processClawStop();
        processClawManualDown();
        clawSlide.ProcessLoop();
        intakeSlide.ProcessLoop();

        prevGamepad1.copy(currGamepad1);
        prevGamepad2.copy(currGamepad2);
    }
}

