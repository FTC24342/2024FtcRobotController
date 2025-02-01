package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.PathBuilder;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slide;

import org.firstinspires.ftc.teamcode.hardware.SpecimenGrabber;
import org.firstinspires.ftc.teamcode.hardware.Sweeper;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;


@Autonomous(name="TestAuto")
public class SpecAuto extends LinearOpMode {

    private MecanumDrive drive = null;
    private Telemetry.Item debugOutout = null;
    private Sweeper sweeper = new Sweeper();
    private Intake intake = new Intake();
    private Slide intakeSlide = new Slide("slide", "", Slide.ExtendMotorDirection.Forward, 1300, 1.0, 114.28);
    private Slide clawSlide = new Slide("lift", "resetlift", Slide.ExtendMotorDirection.Reverse, 2600, 1.0,68.568);
    private SpecimenGrabber specimenGrabber = new SpecimenGrabber();

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
    @Override
    public void runOpMode()  throws InterruptedException
    {
        // run once when init is pressed
        drive = new MecanumDrive(this.hardwareMap, new Pose2d(-62.175, -3, 0));
        clawSlide.Init(hardwareMap);
        intakeSlide.Init(hardwareMap);
        specimenGrabber.Init(hardwareMap);
        intake.Init(hardwareMap);
        sweeper.init(hardwareMap);
        sweeper.sweeperIn();
        telemetry.clearAll();
        telemetry.setAutoClear(false);
        //debugOutout = telemetry.addData("Debug:", imu.getRobotYawPitchRollAngles().toString());

        // Delcare Trajectory as such
        Action TrajectoryAction1 = drive.actionBuilder(drive.pose)
                .stopAndAdd(new LiftToTopBar()) // lift to be ready to hang
                .waitSeconds(.100)
                .lineToX(-32.625) // move forward
                .stopAndAdd(new LiftToHookPosition()) // lower the lift to hang
                .waitSeconds(.250)
                .stopAndAdd(new OpenGrabber()) //open claw
                .lineToX(-48)
                //first hang completed
                .stopAndAdd(new SlideOut())
                .waitSeconds(.250)
                .strafeToLinearHeading(new Vector2d(-33, -35), 120)
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
