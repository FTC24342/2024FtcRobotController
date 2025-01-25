package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slide;
import org.firstinspires.ftc.teamcode.hardware.SpecimenGrabber;
import org.firstinspires.ftc.teamcode.hardware.Sweeper;


@Autonomous(name="5 specimen auto push first", group = "./test")
public class SpecimenAuto5PushFirst extends LinearOpMode {

    private MecanumDrive drive = null;
    private Telemetry.Item debugOutout = null;
    private Sweeper sweeper = new Sweeper();
    private Intake intake = new Intake();
    private Slide intakeSlide = new Slide("slide", "", Slide.ExtendMotorDirection.Forward, 1300, 1.0, 114.28);
    private Slide clawSlide = new Slide("lift", "resetlift", Slide.ExtendMotorDirection.Reverse, 2600, 1.0,68.568);
    private SpecimenGrabber specimenGrabber = new SpecimenGrabber();

    public class OpenGrabber implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            specimenGrabber.Open();
            return false;
        }
    }
    public class CloseGrabber implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            specimenGrabber.Close();
            return false;
        }
    }
    public class LiftToTopBar implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if(clawSlide.GetExtendedInches() == 20) { // 18.25
                return true;
            }
            else {
                clawSlide.MoveTo(20, 1.0); //18.25
                return false;
            }
        }
    }
    public class LiftToBottom implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if(clawSlide.GetExtendedInches() < 0.1) {
                return true;
            }
            else {
                clawSlide.MoveTo(0, 1.0);
                clawSlide.ProcessLoop();
                return false;
            }
        }
    }

    public class LiftToHookPosition implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if(clawSlide.GetExtendedInches() == 14.75) {
                return true;
            }
            else {
                clawSlide.MoveTo(15.0, 1.0);
                return false;
            }
        }
    }
    public class SlideOut implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intakeSlide.Extend(1);

            return false;
        }
    }
    public class SlideIn implements  Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intakeSlide.Retract(1);
            return false;
        }

    }

    public class SweepPos implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.intakeSweep();
            return false;
        }
    }

    public class InitPos implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.goToInit();
            return false;
        }
    }

    @Override
    public void runOpMode()  throws InterruptedException
    {
        // run once when init is pressed
        drive = new MecanumDrive(this.hardwareMap, new Pose2d(-62, -26.25, Math.toRadians(180)));
        clawSlide.Init(hardwareMap);
        intakeSlide.Init(hardwareMap);
        specimenGrabber.Init(hardwareMap);
        specimenGrabber.Close();
        intake.Init(hardwareMap);
        sweeper.init(hardwareMap);
        telemetry.clearAll();
        telemetry.setAutoClear(false);
        //debugOutout = telemetry.addData("Debug:", imu.getRobotYawPitchRollAngles().toString());

        // Delcare Trajectory as such
        Action TrajectoryAction1 = drive.actionBuilder(drive.pose)
                .stopAndAdd(new SlideOut()) // extends slide to out position
                .splineToLinearHeading(new Pose2d(-46.96, -19.23, Math.toRadians(90)), Math.toRadians(90)) // moves in front of observation zone
                .stopAndAdd(new SweepPos()) // moves intake to sweep position
                .splineToLinearHeading(new Pose2d(-33.04, -25.66, Math.toRadians(125)), Math.toRadians(125)) // moves behind sample
                .strafeToLinearHeading(new Vector2d(-31.87, -30.36), Math.toRadians(125)) // Scoot behind piece 1
                .strafeToLinearHeading(new Vector2d(-52.47, -35.75), Math.toRadians(58)) // Push to observation zone
                .strafeToLinearHeading(new Vector2d(-31.87, -30.36), Math.toRadians(125)) //Scoot behind piece 1
                .strafeToLinearHeading(new Vector2d(-31.87, -39.03), Math.toRadians(125)) //Scoot behind piece 2
                .strafeToLinearHeading(new Vector2d(-52.47, -42.42), Math.toRadians(58)) // Push to observation zone
                .strafeToLinearHeading(new Vector2d(-31.87, -39.03), Math.toRadians(125)) // Scoot behind piece 2
                .strafeToLinearHeading(new Vector2d(-31.87, -49.16), Math.toRadians(125)) // Scoot behind piece 3
                .strafeToLinearHeading(new Vector2d(-52.47, -49.93), Math.toRadians(58)) // Push to observation zone
                .splineToLinearHeading(new Pose2d(-61.81, -38.87, Math.toRadians(179)), Math.toRadians(179))
                .stopAndAdd(new InitPos())
                .stopAndAdd(new SlideIn())
                .build();

        Actions.runBlocking(
                new SequentialAction(
                        new CloseGrabber()
                )
        );

        // After we are done initializing our code, we wait for Start button.
        waitForStart();

        // Start button pressed, off we go.

        //go to the bar and hook specimen one
        Actions.runBlocking(TrajectoryAction1);


    }
}
