package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.teamcode.hardware.Pincher;
import org.firstinspires.ftc.teamcode.hardware.MecanumEncoder;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Slide;

@TeleOp
public class teleopDrive extends OpMode {

    private String TESTBOT = "24342-B-RC";
    private Telemetry.Item telPathDebug = null;
    private MecanumEncoder drive = new MecanumEncoder(this);
    private Slide lift = new Slide("lift", Slide.ExtendMotorDirection.Reverse, 1300, 1.0, 114.28);
    private Slide slide = new Slide("slide", Slide.ExtendMotorDirection.Forward, 1300, 1.0,113.66);
    private Pincher pincher = new Pincher();
    private String wifiSsid = "";

    private Gamepad prevGamepad1 = null;
    private Gamepad currGamepad1 = new Gamepad();
    private Gamepad prevGamepad2 = null;
    private Gamepad currGamepad2 = new Gamepad();

    public void ProcessSlide() {
        //right_bumper - slide 100% extended, and pickup mechanism in ready position
        //left_bumper - slide 0% extended, and pickup mechanism in the drive position
        //right trigger (as a button) - open pincher then drop to pickup position, then close pincher, then pickup to ready position
        //left trigger (as a button) - drop off position, then open pincher, then drive position( if lide is not extended) otherwise then ready position if slide is extended
        //

        //determine if we are extended or retracted
        boolean extended = slide.GetExtendedInches() < 10;
        //telPathDebug = telemetry.addData(String.valueOf(slide.GetExtendedInches()));

        if(!prevGamepad1.right_bumper && currGamepad1.right_bumper) {
            //extend the intake slide
            slide.MoveTo(11.0, 1);
            pincher.GoToReadyPosition();
        }
        else if(!prevGamepad1.left_bumper && currGamepad1.left_bumper) {
            //retract the intake slide
            slide.MoveTo(0.0, 1);
            pincher.GoToDrivePosition();

        }

        if(extended && prevGamepad1.right_trigger < .1 && currGamepad1.right_trigger >= .1) {
            //pickup routine
            pincher.Pickup();
        }
        else if(extended && prevGamepad1.right_trigger >= .1 && currGamepad1.right_trigger < .1) {
            //return to ready position holding the piece
            pincher.GoToReadyPosition();
        }

//        if(extended) {
//            //place intake in ready position
//            pincher.GoToDrivePosition();
//        }
//        else {
//            //place in the drive position
//            pincher.GoToReadyPosition();
//
//        }
    }

    public void ProcessLiftSlide() {
        if(!prevGamepad2.right_bumper && currGamepad2.right_bumper) {
            lift.MoveTo(16,1);
        }
        else if(!prevGamepad2.circle && currGamepad2.left_bumper) {
            lift.MoveTo(0,1);
        }
        else if(!prevGamepad2.x && currGamepad2.x) {
            lift.MoveTo(12,1);
        }
    }

    public void ProcessPincher() {
        if (prevGamepad1.right_trigger >= 0.1) {
            pincher.Pickup();
        } else if (prevGamepad1.left_trigger >= 0.1) {
            pincher.DropOff();
        }
        if (!prevGamepad1.right_stick_button && currGamepad1.right_stick_button && pincher.isOpen) {
            pincher.PincherClose();
        }
        else if (!prevGamepad1.right_stick_button && currGamepad1.right_stick_button && !pincher.isOpen){
            pincher.PincherOpen();
        }

    }
    @Override
    public void init() {
        //Continue defining motors
        lift.Init(hardwareMap);
        slide.Init(hardwareMap);
        pincher.Init(hardwareMap);
        // run once when init is pressed
        wifiSsid = WifiUtil.getConnectedSsid();

        drive.initHardware(hardwareMap, wifiSsid.equals(TESTBOT) ? MecanumEncoder.Bot.TestBot : MecanumEncoder.Bot.CompBot);
        telemetry.clearAll();
        telemetry.setAutoClear(false);
        telPathDebug = telemetry.addData("debug", "");
        telPathDebug.setValue(slide.maxTicks);

        pincher.GoToDrivePosition();
    }

    @Override
    public void init_loop() {
        // add stuff here for the init loop
        telPathDebug.setValue(wifiSsid);

        telemetry.update();
    }

    @Override
    public void loop() {
        // runs while in play
        if(prevGamepad1 == null) {
          prevGamepad1 = new Gamepad();
          prevGamepad2 = new Gamepad();
          prevGamepad1.copy(gamepad1);
          prevGamepad2.copy(gamepad2);
        }
        currGamepad1.copy(gamepad1);
        currGamepad2.copy(gamepad2);

        drive.driverInput(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, 1.0, MecanumEncoder.DriveMode.FieldCentric);

        ProcessSlide();
        ProcessLiftSlide();
        ProcessPincher();

        prevGamepad1.copy(currGamepad1);
        prevGamepad2.copy(currGamepad2);
    }
}

