package org.firstinspires.ftc.teamcode.hardware;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;


public class Intake {
    public enum IntakePositon {
        UNKNOWN, PICKUP, DRIVE, Init, SWEEP
    }

    public Servo axis1 = null;
    public Servo axis2 = null;
    private CRServo left = null;
    private CRServo right = null;
    private Double maxPickupMovement = .022;
    public IntakePositon currentPosition = IntakePositon.UNKNOWN;
    public double axis1Init = 0.117222;
    public double axis2Init = 0.323889;
    public double axis1Drive = 0.147778;
    public double axis2Drive = 0.446111;
    public double axis1Pickup = 0.800556;
    public double axis2Pickup = 0.501667;
    public double positionMargin = 0.005; //this is to allow for logical positions that can not be exactly meet in hardware

    public void Init(HardwareMap hardwareMap) {
        axis1 = hardwareMap.get(Servo.class, "axis1");
        axis2 = hardwareMap.get(Servo.class, "axis2");
        left = hardwareMap.get(CRServo.class, "left");
        right = hardwareMap.get(CRServo.class, "right");
        //we need to use set positions here because get position is not valid until after the first set position
        axis1.setPosition(axis1Init);
        axis2.setPosition(axis2Init);
        currentPosition = IntakePositon.Init;
    }

    private void goToPos(double axis1TargetPos, double axis2TargetPos, IntakePositon targetIntakePosition) {
        double axis1Pos = axis1.getPosition();
        double axis2Pos = axis2.getPosition();
        if (
                (axis1Pos >= axis1TargetPos - positionMargin && axis1Pos <= axis1TargetPos + positionMargin)
                        && (axis2Pos >= axis2TargetPos - positionMargin && axis2Pos <= axis2TargetPos + positionMargin)
        ) {
            currentPosition = targetIntakePosition;
        } else {
            if (axis1TargetPos > axis1Pos) {
                if (axis1Pos + maxPickupMovement >= axis1TargetPos) {
                    axis1.setPosition(axis1TargetPos);
                } else {
                    axis1.setPosition(axis1Pos + maxPickupMovement);
                }
            } else {
                if (axis1Pos - maxPickupMovement <= axis1TargetPos) {
                    axis1.setPosition(axis1TargetPos);
                } else {
                    axis1.setPosition(axis1Pos - maxPickupMovement);
                }
            }

            if (axis2TargetPos > axis2Pos) {
                if (axis2Pos + maxPickupMovement >= axis2TargetPos) {
                    axis2.setPosition(axis2TargetPos);
                } else {
                    axis2.setPosition(axis2Pos + maxPickupMovement);
                }
            } else {
                if (axis2Pos - maxPickupMovement <= axis2TargetPos) {
                    axis2.setPosition(axis2TargetPos);
                } else {
                    axis2.setPosition(axis2Pos - maxPickupMovement);
                }
            }
        }
    }

    public void goToDrive() {
        goToPos(axis1Drive, axis2Drive, IntakePositon.DRIVE);
    }

    public void goToPickup() {
        goToPos(axis1Pickup, axis2Pickup, IntakePositon.PICKUP);
    }

    public void goToHome() {
        goToPos(axis1Init, axis2Init, IntakePositon.Init);
    }

    public void intakeIn() {
        left.setPower(-1);
        right.setPower(1);
    }

    public void intakeOut() {
        left.setPower(1);
        right.setPower(-1);
    }

    public void intakeStop() {
        left.setPower(0);
        right.setPower(0);
    }
    public void intakeSweep() {
        axis1.setPosition(0.805000);
        axis2.setPosition(1.0);
    }
    public void goToInit() {
        axis1.setPosition(axis1Init);
        axis2.setPosition(axis2Init);
    }
}

