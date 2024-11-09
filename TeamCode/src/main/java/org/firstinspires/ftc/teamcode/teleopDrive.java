package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.MecanumEncoder;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Slide;

@TeleOp
public class teleopDrive extends OpMode {

    private String TESTBOT = "24342-B-RC";
    private Telemetry.Item telPathDebug = null;
    private MecanumEncoder drive = new MecanumEncoder(this);
    private Slide liftSlide = new Slide("liftslide", Slide.ExtendMotorDirection.Forward, 1300, 1.0, 114.28);
    private Slide clawSlide = new Slide("clawslide", Slide.ExtendMotorDirection.Reverse, 3500, 1.0,114);
    private Intake claw = new Intake();
    private String wifiSsid = "";

    private Gamepad prevGamepad1 = null;
    private Gamepad currGamepad1 = new Gamepad();
    private Gamepad prevGamepad2 = null;
    private Gamepad currGamepad2 = new Gamepad();

    public void ProcessClaw() {
        //right_bumper - slide 100% extended, and pickup mechanism in ready position
        //left_bumper - slide 0% extended, and pickup mechanism in the drive position
        //right trigger (as a button) - open pincher then drop to pickup position, then close pincher, then pickup to ready position
        //left trigger (as a button) - drop off position, then open pincher, then drive position( if lide is not extended) otherwise then ready position if slide is extended
        //

        //determine if we are extended or retracted
        boolean extended = clawSlide.GetExtendedInches() > 10;

        if(!prevGamepad1.right_bumper && prevGamepad1.right_bumper) {
            //extend the intake slide
            clawSlide.MoveTo(12.0, 1);
        }
        else if(!prevGamepad1.left_bumper && prevGamepad1.left_bumper) {
            //retract the intake slide
            clawSlide.MoveTo(0.0, 1);
        }

        if(extended && prevGamepad1.right_trigger < .1 && currGamepad1.right_trigger >= .1) {
            //pickup routine
            claw.Pickup();
        }
        else if(extended && prevGamepad1.right_trigger >= .1 && currGamepad1.right_trigger < .1) {
            //return to ready position holding the piece
            claw.GoToReadyPosition();
        }

        if(extended) {
            //place intake in ready position
            claw.GoToReadyPosition();
        }
        else {
            //place in the drive position
            claw.GoToDrivePosition();
        }
    }

    public void ProcessLiftSlide() {
        if(!prevGamepad1.triangle && currGamepad1.triangle) {
            liftSlide.MoveTo(16,1);
        }
        else if(!prevGamepad1.circle && currGamepad1.circle) {
            liftSlide.MoveTo(12,1);
        }
        else if(!prevGamepad1.x && currGamepad1.x) {
            liftSlide.MoveTo(12,1);
        }
    }
    @Override
    public void init() {
        //Continue defining motors
        liftSlide.Init(hardwareMap);
        clawSlide.Init(hardwareMap);
        claw.Init(hardwareMap);
        // run once when init is pressed
        wifiSsid = WifiUtil.getConnectedSsid();

        drive.initHardware(hardwareMap, wifiSsid.equals(TESTBOT) ? MecanumEncoder.Bot.TestBot : MecanumEncoder.Bot.CompBot);
        telemetry.clearAll();
        telemetry.setAutoClear(false);
        telPathDebug = telemetry.addData("PathDebug:", "");

        claw.GoToDrivePosition();
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

        ProcessClaw();
        ProcessLiftSlide();

        prevGamepad1.copy(currGamepad1);
        prevGamepad2.copy(currGamepad2);
    }
}

