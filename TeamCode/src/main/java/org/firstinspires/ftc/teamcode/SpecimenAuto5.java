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


@Autonomous(name="5 specimen auto", group = "./test")
public class SpecimenAuto5 extends LinearOpMode {

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
        drive = new MecanumDrive(this.hardwareMap, new Pose2d(-62, -3, 0));
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
                .stopAndAdd(new LiftToTopBar()) // lifts lift to the top chamber
                .waitSeconds(.100)
                .strafeTo(new Vector2d(-32.379, -3)) // robot to te bar
                .stopAndAdd(new LiftToHookPosition()) // hooks specimen on top chamber
                .waitSeconds(.250)
                .stopAndAdd(new OpenGrabber()) // lets go of specimen
                .stopAndAdd(new LiftToBottom())
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
                .splineToLinearHeading(new Pose2d(-62, -38.87, Math.toRadians(180)), Math.toRadians(180))
                .stopAndAdd(new InitPos())
                .stopAndAdd(new SlideIn())


                .strafeTo(new Vector2d(-62, -42.5)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(.250)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-32.1, -2.1), Math.toRadians(0))
                .stopAndAdd(new LiftToHookPosition()) // lower the lift to hang
                .waitSeconds(.250)
                .stopAndAdd(new OpenGrabber()) //open claw
                .stopAndAdd(new LiftToBottom())
                //2 spec^

                //start 3rd spec
                .strafeToLinearHeading(new Vector2d(-62, -42.5), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(.250)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-32.1, -3.1), Math.toRadians(0))
                .stopAndAdd(new LiftToHookPosition()) // lower the lift to hang
                .waitSeconds(.250)
                .stopAndAdd(new OpenGrabber()) //open claw
                .stopAndAdd(new LiftToBottom())
                //3 spec^

                // start 4th
                .strafeToLinearHeading(new Vector2d(-62, -42.5), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(.250)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-32.1, -4.1), 0)
                .stopAndAdd(new LiftToHookPosition()) // lower the lift to hang
                .waitSeconds(.250)
                .stopAndAdd(new OpenGrabber()) //open claw
                .stopAndAdd(new LiftToBottom())
                //4 spec^

                //start 5th
                .strafeToLinearHeading(new Vector2d(-62, -42.5), Math.toRadians(180)) // move to grab specimen from the wall
                .stopAndAdd(new CloseGrabber())
                .waitSeconds(.250)
                .stopAndAdd(new LiftToTopBar())
                .strafeToLinearHeading(new Vector2d(-32.1, 5.1), 0)
                .stopAndAdd(new LiftToHookPosition()) // lower the lift to hang
                .waitSeconds(.250)
                .stopAndAdd(new OpenGrabber()) //open claw
                .stopAndAdd(new LiftToBottom())


                .waitSeconds(2)
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
