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
import org.firstinspires.ftc.teamcode.hardware.ClawSlide;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.IntakeSlide;
import org.firstinspires.ftc.teamcode.hardware.Slide;
import org.firstinspires.ftc.teamcode.hardware.SpecimenGrabber;
import org.firstinspires.ftc.teamcode.hardware.Sweeper;


@Autonomous(name="5 specimen auto push first", group = "./test")
public class SpecimenAuto5PushFirst extends LinearOpMode {

    private MecanumDrive drive = null;
    private Telemetry.Item debugOutout = null;
    private Sweeper sweeper = new Sweeper();
    private Intake intake = new Intake();
    private IntakeSlide intakeSlide = new IntakeSlide();
    private ClawSlide clawSlide = new ClawSlide();
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
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
//            if(clawSlide.IsAtTopBarReadyPosition(0.5)) {
//                return false;
//            }
//            else {
//                clawSlide.MoveToTopBarReadyPosition(1.0);
//                clawSlide.ProcessLoop();
//                return true;
//            }
            //tell it to go and exit as if it is done
            clawSlide.MoveToTopBarReadyPosition(1.0);
            return false;

        }
    }
    public class LiftToBottom implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
//            if(clawSlide.GetExtendedInches() < 0.1) {
//                return false;
//            }
//            else {
//                clawSlide.MoveTo(0, 1.0);
//                return true;
//            }
            clawSlide.MoveToWallPickupPosition(0.8);
            return false;
        }
    }

    public class LiftToHookPosition implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
//            if(clawSlide.IsAtTopBarHookSpecimanPosition(0.5)) {
//                specimenGrabber.Open();
//                return false;
//            }
//            else {
//                clawSlide.MoveToTopBarHookSpecimanPosition(1.0);
//                return true;
//            }
            clawSlide.MoveToTopBarHookSpecimanPosition(1.0);
            return false;
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
        drive = new MecanumDrive(this.hardwareMap, new Pose2d(-62, -23, Math.toRadians(180)));
        clawSlide.Init(hardwareMap);
        intakeSlide.Init(hardwareMap);
        specimenGrabber.Init(hardwareMap);
        intake.Init(hardwareMap);
        sweeper.init(hardwareMap);
        telemetry.clearAll();
        
        telemetry.setAutoClear(false);
        //debugOutout = telemetry.addData("Debug:", imu.getRobotYawPitchRollAngles().toString());
        double secondsToWaitBeforeOpeningGrabber = .300;
        double secondsToWaitBeforeClosingGrabber = .250;
        // Delcare Trajectory as such
        Action TrajectoryAction1 = drive.actionBuilder(drive.pose)
                .stopAndAdd(new SlideOut()) // extends slide to out position
                .stopAndAdd(new SweepPos()) // moves intake to sweep position
                .splineToLinearHeading(new Pose2d(-33.04, -25.66, Math.toRadians(125)), Math.toRadians(125)) // moves behind sample
                .strafeToLinearHeading(new Vector2d(-31.87, -30.36), Math.toRadians(125)) // Scoot behind piece 1
                .strafeToLinearHeading(new Vector2d(-52.47, -35.75), Math.toRadians(58)) // Push to observation zone
                .strafeToLinearHeading(new Vector2d(-31.87, -30.36), Math.toRadians(125)) //Scoot behind piece 1
                .strafeToLinearHeading(new Vector2d(-31.87, -39.03), Math.toRadians(125)) //Scoot behind piece 2
                .strafeToLinearHeading(new Vector2d(-52.47, -42.42), Math.toRadians(58)) // Push to observation zone
                .strafeToLinearHeading(new Vector2d(-31.87, -39.03), Math.toRadians(125)) // Scoot behind piece 2
                .strafeToLinearHeading(new Vector2d(-31.87, -49.16), Math.toRadians(125)) // Scoot behind piece 3
                .strafeToLinearHeading(new Vector2d(-52.47, -47.7), Math.toRadians(58)) // Push to observation zone // 49.93 old
                .stopAndAdd(new OpenGrabber())
                .stopAndAdd(new OpenGrabber())
                .splineToLinearHeading(new Pose2d(-61.81, -38.87, Math.toRadians(179)), Math.toRadians(179))
                .stopAndAdd(new InitPos())
                .stopAndAdd(new SlideIn())

                .stopAndAdd(new CloseGrabber())
                .waitSeconds(secondsToWaitBeforeClosingGrabber)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-31.5, -2.1), Math.toRadians(0))
                .stopAndAdd(new LiftToBottom())
                .stopAndAdd(new OpenGrabber()) //open claw
                //2 spec^

                //start 3rd spec
                .strafeToLinearHeading(new Vector2d(-62.5, -42.5), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(secondsToWaitBeforeClosingGrabber)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-31.5, -3.1), Math.toRadians(0))
                .stopAndAdd(new LiftToBottom()) // lower the lift to hang
                .stopAndAdd(new OpenGrabber()) //open claw
                //3 spec^

                // start 4th
                .strafeToLinearHeading(new Vector2d(-62.5, -42.5), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(secondsToWaitBeforeClosingGrabber)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-31.5, -4.1), 0)
                .stopAndAdd(new LiftToBottom()) // lower the lift to hang
                .stopAndAdd(new OpenGrabber()) //open claw
                //4 spec^

                //start 5th
                .strafeToLinearHeading(new Vector2d(-62.5, -42.5), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(secondsToWaitBeforeClosingGrabber)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-31.5, -5.1), 0)
                .stopAndAdd(new LiftToHookPosition()) // lower the lift to hang
                .stopAndAdd(new OpenGrabber()) //open claw


                .waitSeconds(4)
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
