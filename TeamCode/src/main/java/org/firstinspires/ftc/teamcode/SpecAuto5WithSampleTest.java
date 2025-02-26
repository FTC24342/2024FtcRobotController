package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
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

import java.util.Arrays;
import java.util.List;


@Autonomous(name="5 specimen auto with sample ", group = "./test")
public class SpecAuto5WithSampleTest extends LinearOpMode {

    private MecanumDrive drive = null;
    private Telemetry.Item debugOutout = null;
    private Sweeper sweeper = new Sweeper();
    private Intake intake = new Intake();
    private IntakeSlide intakeSlide = new IntakeSlide();
    private ClawSlide clawSlide = new ClawSlide();
    private Telemetry.Item PoseX = null;
    private Telemetry.Item PoseY = null;
    private Telemetry.Item PoseHeading = null;
    private Telemetry.Item LiftTicks = null;
    private Telemetry.Item IntakeTicks = null;
    private SpecimenGrabber specimenGrabber = new SpecimenGrabber();

    public class OpenGrabber implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            specimenGrabber.Open();
            return false;
        }
    }
    public class UpdateAutoTelem implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            drive.updatePoseEstimate();
            PoseX.setValue("%f2.3 in", drive.pose.position.x);
            PoseY.setValue("%f2.3 in", drive.pose.position.y);
            PoseHeading.setValue("%f3.2 degrees", Math.toDegrees(drive.pose.heading.toDouble()));
            LiftTicks.setValue("%d", clawSlide.motor.getCurrentPosition());
            IntakeTicks.setValue("%d", intakeSlide.motor.getCurrentPosition());
            telemetry.update();
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

    public class HangSpecimen implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if(clawSlide.GetExtendedInches() == 12.4) {
                specimenGrabber.Open();
                return false;
            }
            else {
                clawSlide.MoveTo(12.4, 1.0);
                return true;
            }
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
    public class LiftToBucket implements Action {
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            clawSlide.MoveToHighBasketPosition(1.0);
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
            intake.intakeSweepDown();
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
        drive = new MecanumDrive(this.hardwareMap, new Pose2d(-62, -3, 0));
        clawSlide.Init(hardwareMap);
        intakeSlide.Init(hardwareMap);
        specimenGrabber.Init(hardwareMap);
        specimenGrabber.Close();
        intake.Init(hardwareMap);
        sweeper.init(hardwareMap);
        //debugOutout = telemetry.addData("Debug:", imu.getRobotYawPitchRollAngles().toString());

        PoseX = telemetry.addData("X: ", "");
        PoseY = telemetry.addData("Y: ", "");
        PoseHeading = telemetry.addData("Heading: ", "");
        LiftTicks = telemetry.addData("LiftTicks: ", "");
        IntakeTicks = telemetry.addData("IntakeTicks: ", "");
        telemetry.clearAll();
        telemetry.setAutoClear(false);

        double secondsToWaitBeforeOpeningGrabber = .300;
        double secondsToWaitBeforeClosingGrabber = .250;

//        MecanumDrive.FollowTrajectoryAction trajectoryFromBarToFirstPiece = new MecanumDrive.FollowTrajectoryAction();
        //      trajectoryFromBarToFirstPiece.
        // Delcare Trajectory as such
        Action TrajectoryAction1 = drive.actionBuilder(drive.pose)
                .stopAndAdd(new LiftToTopBar()) // lifts lift to the top chamber
                .waitSeconds(.350)
                .strafeTo(new Vector2d(-32.5, -3)) // robot to te bar
                .stopAndAdd(new LiftToBottom())
                .waitSeconds(.200)
                .stopAndAdd(new OpenGrabber()) // lets go of specimen
                .stopAndAdd(new SlideOut())

                // .strafeToLinearHeading(new Vector2d(-39.2, -30.4), Math.toRadians(90))

                .splineToLinearHeading(new Pose2d(-34.8, -30.3, Math.toRadians(145)), Math.toRadians(145))
                .stopAndAdd(new SweepPos())
                .waitSeconds(.350)
                .strafeToLinearHeading(new Vector2d(-52.0, -28.3), Math.toRadians(73.0))

                .stopAndAdd(new InitPos())

                .strafeToLinearHeading(new Vector2d(-31.8, -40.1), Math.toRadians(128.8))
                .stopAndAdd(new SweepPos())
                .waitSeconds(.350)
                .strafeToLinearHeading(new Vector2d(-52.0, -40.1), Math.toRadians(73.0))

                .stopAndAdd(new InitPos())

                .strafeToLinearHeading(new Vector2d(-31.8, -47.0), Math.toRadians(128.8))
                .stopAndAdd(new SweepPos())
                .waitSeconds(.350)
                .strafeToLinearHeading(new Vector2d(-48.0, -46.5), Math.toRadians(54.0))

                .stopAndAdd(new ParallelAction(
                                Arrays.asList(
                                        new InitPos(),
                                        new SlideIn()
                                )
                        )
                )
                //grab
                .strafeToSplineHeading(new Vector2d(-62, -36.5), Math.toRadians(180))
                .waitSeconds(.200)
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(.250)
                .stopAndAdd(new LiftToTopBar())

                // Hang 2
                .strafeToLinearHeading(new Vector2d(-32.5, 0), Math.toRadians(-0.1))
                .stopAndAdd(new LiftToBottom())
                .waitSeconds(.300)
                .stopAndAdd(new OpenGrabber()) // lets go of specimen

                //grab
                .strafeToSplineHeading(new Vector2d(-62, -36.5), Math.toRadians(180))
                .waitSeconds(.200)
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(.250)
                .stopAndAdd(new LiftToTopBar())

                // Hang 3
                .strafeToLinearHeading(new Vector2d(-32.5, -1), Math.toRadians(-0.1))
                .stopAndAdd(new LiftToBottom())
                .waitSeconds(.250)
                .stopAndAdd(new OpenGrabber()) // lets go of specimen

                //grab
                .strafeToLinearHeading(new Vector2d(-62,-38.4), Math.toRadians(180))
                .waitSeconds(.200)
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(.250)
                .stopAndAdd(new LiftToTopBar())


                // Hang 4
                .strafeToLinearHeading(new Vector2d(-32.5, -2), Math.toRadians(-0.1))
                .stopAndAdd(new LiftToBottom())
                .waitSeconds(.250)
                .stopAndAdd(new OpenGrabber()) // lets go of specimen


                //grab
                .strafeToLinearHeading(new Vector2d(-62,-38.4), Math.toRadians(180))
                .waitSeconds(.200)
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(.250)
                .stopAndAdd(new LiftToTopBar())


                // Hang 5
                .strafeToLinearHeading(new Vector2d(-32.5, -3), Math.toRadians(-0.1))
                .stopAndAdd(new LiftToBottom())
                .waitSeconds(.200)
                .stopAndAdd(new OpenGrabber()) // lets go of specimen

                //grab
                .strafeToLinearHeading(new Vector2d(-62,-38.4), Math.toRadians(180))
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(.250)
                .stopAndAdd(new LiftToTopBar())

                //Go to bucket
                .stopAndAdd(new LiftToBucket())
                .strafeTo(new Vector2d(-47, 47))
                .strafeToLinearHeading(new Vector2d(-55.93, 60.34), Math.toRadians(137.4))
                .stopAndAdd(new OpenGrabber())
                .waitSeconds(.150)
                .strafeToLinearHeading(new Vector2d(-55, 60.34), Math.toRadians(0))
                .stopAndAdd(new LiftToBottom())
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
