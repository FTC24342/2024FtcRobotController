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


@Autonomous(name="5 specimen auto", group = "./test")
public class SpecimenAuto5 extends LinearOpMode {

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
                .waitSeconds(.100)
                .strafeTo(new Vector2d(-32.325, -3)) // robot to te bar
                .stopAndAdd(new LiftToBottom())
                .waitSeconds(.150)
                .stopAndAdd(new OpenGrabber()) // lets go of specimen
                .stopAndAdd(new ParallelAction(
                        Arrays.asList(
                                new SlideOut(),
                                new SweepPos()
                            )
                        )
                )
                .splineToLinearHeading(new Pose2d(-46.96, -19.23, Math.toRadians(90)), Math.toRadians(90)) // moves in front of observation zone
                .splineToLinearHeading(new Pose2d(-33.04, -25.66, Math.toRadians(125)), Math.toRadians(125)) // moves behind sample
                .strafeToLinearHeading(new Vector2d(-31.87, -30.36), Math.toRadians(125))
                .strafeToLinearHeading(new Vector2d(-52.47, -35.75), Math.toRadians(58)) // Push to observation zone
                .strafeToLinearHeading(new Vector2d(-31.87, -35.75), Math.toRadians(125))
                .strafeToLinearHeading(new Vector2d(-31.87, -40.03), Math.toRadians(125)) //Scoot behind piece 2
                .strafeToLinearHeading(new Vector2d(-52.47, -40.03), Math.toRadians(58)) // Push to observation zone
                .strafeToLinearHeading(new Vector2d(-31.87, -40.03), Math.toRadians(125)) // Scoot behind piece 2
                .strafeToLinearHeading(new Vector2d(-31.87, -49.16), Math.toRadians(125)) // Scoot behind piece 3
                .strafeToLinearHeading(new Vector2d(-52.47, -49.93), Math.toRadians(58)) // Push to observation zone
                .stopAndAdd(new ParallelAction(
                                Arrays.asList(
                                        new InitPos(),
                                        new SlideIn()
                                        )
                    )
                )

                .strafeToLinearHeading(new Vector2d(-62.5, -37), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(secondsToWaitBeforeClosingGrabber)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-31.5, -4), Math.toRadians(-1))
                .stopAndAdd(new LiftToBottom()) // lower the lift to hang
                .waitSeconds(.160)
                .stopAndAdd(new OpenGrabber()) //open claw
                //2 spec^

                //start 3rd spec
                .strafeToLinearHeading(new Vector2d(-62.5, -37), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(secondsToWaitBeforeClosingGrabber)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-30.2, -5), Math.toRadians(-1))
                .stopAndAdd(new LiftToBottom())
                .waitSeconds(.160)
                .stopAndAdd(new OpenGrabber()) //open claw
                //3 spec^


                // start 4th
                .strafeToLinearHeading(new Vector2d(-62.5, -37), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(secondsToWaitBeforeClosingGrabber)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-30.2, -6), Math.toRadians(-1))
                .stopAndAdd(new LiftToBottom())
                .waitSeconds(.160)
                .stopAndAdd(new OpenGrabber()) //open claw

                //4 spec^

                //start 5th
                .strafeToLinearHeading(new Vector2d(-62.5, -37), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(secondsToWaitBeforeClosingGrabber)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-30.2, -7), Math.toRadians(-1))
                .stopAndAdd(new LiftToBottom())
                .waitSeconds(.160)
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
