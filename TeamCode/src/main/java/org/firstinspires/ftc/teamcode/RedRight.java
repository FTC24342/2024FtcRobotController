package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.teamcode.hardware.MecanumEncoder;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Pincher;
import org.firstinspires.ftc.teamcode.hardware.Slide;

@Autonomous(name="Red Right")
public class RedRight extends LinearOpMode {

    private MecanumEncoder drive = new MecanumEncoder(this);
    private String TESTBOT = "24342-RC-test";
    private String wifiSsid = "";
    private Slide lift = new Slide("lift", Slide.ExtendMotorDirection.Reverse, 1300, 1.0, 114.28);
    private Slide slide = new Slide("slide", Slide.ExtendMotorDirection.Forward, 1300, 1.0,113.66);
    private IMU imu;
    private Pincher pincher = new Pincher();
    private double botSpeed = 0.8;
    private double wallGrabSpeed = 0.3;

    private Telemetry.Item debugOutout = null;
    @Override
    public void runOpMode()  throws InterruptedException
    {
        wifiSsid = WifiUtil.getConnectedSsid();
        lift.Init(hardwareMap);
        slide.Init(hardwareMap);
        pincher.Init(hardwareMap);
        long start = System.currentTimeMillis();

        // run once when init is pressed
        drive.initHardware(this.hardwareMap, wifiSsid.equals(TESTBOT) ? MecanumEncoder.Bot.TestBot : MecanumEncoder.Bot.CompBot, MecanumEncoder.StartSidePointingToOtherSide.Back);
        //imu = drive.getImu();
        drive.resetYaw();
        telemetry.clearAll();
        telemetry.setAutoClear(false);
        //debugOutout = telemetry.addData("Debug:", imu.getRobotYawPitchRollAngles().toString());

        // After we are done initializing our code, we wait for Start button.
        waitForStart();

        pincher.GoToDrivePosition();
        //Hang starting specimen
        lift.MoveTo(16.75,1);
        Thread.sleep(800);
        drive.driveBackwardInches(botSpeed, 27.5, 10);
        lift.MoveTo(12.5,1);
        Thread.sleep(800);
        lift.MoveTo(0.0, 1);
        //Move to Observation
        drive.driveForwardInches(botSpeed, 13, 10);
        drive.driveLeftInches(botSpeed,45, 10);
        Thread.sleep(800);
        drive.rotateClockwise(botSpeed, 43, 10.0);
        //back up to specimen on the wall
        drive.driveBackwardInches(wallGrabSpeed,15.5,10);
        Thread.sleep(700);
        lift.MoveTo(16.75, 1);
        Thread.sleep(700);
        //hang second specimen
        drive.driveForwardInches(botSpeed, 15, 10);
        lift.MoveTo(16.75, 1);
        drive.rotateClockwise(botSpeed - 0.2, 43, 10.0);
        drive.driveRightInches(botSpeed,49, 10);
        drive.driveBackwardInches(botSpeed, 13, 10);
        Thread.sleep(700);
        lift.MoveTo(11.5,1);
        //Touch Bar
        drive.driveForwardInches(botSpeed, 13, 10);
        lift.MoveTo(0, 1);
        drive.driveLeftInches(botSpeed, 35,10);
        drive.driveBackwardInches(botSpeed, 33,10);
        //drive.rotateCounterClockwise(1,22.5,10);
        drive.driveRightInches(botSpeed,14 ,10);
        //drive.driveBackwardInches(botSpeed, 4, 10);















        drive.stop();
    }
}
