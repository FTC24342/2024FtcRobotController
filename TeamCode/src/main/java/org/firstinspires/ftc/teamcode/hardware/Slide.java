package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.robotcontroller.external.samples.UtilityOctoQuadConfigMenu;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slide {
    private DcMotorEx motor = null;
    private String motorName = "";
    public int maxTicks = 0;
    private double maxSpeed = 0.0;
    private double ticksPerInch = 0;
    private ExtendMotorDirection extendMotorDirection = ExtendMotorDirection.Forward;

    public enum ExtendMotorDirection {Forward, Reverse};
    public Slide(String motor, ExtendMotorDirection extendMotorDirection, int maxTicks, double maxSpeed, double ticksPerInch) {
        this.motorName = motor;
        this.extendMotorDirection = extendMotorDirection;
        this.maxTicks = maxTicks;
        this.maxSpeed = maxSpeed;
        this.ticksPerInch = ticksPerInch;
    }

    public void Init(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, motorName);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setDirection(extendMotorDirection == ExtendMotorDirection.Forward ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);


    }

    public void Extend(double power) {
        //extend with the power
        motor.setTargetPosition(maxTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(Math.min(power, maxSpeed));
    }

    public void Retract(double power) {
        //extend with the power
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(Math.min(power, maxSpeed));
    }

    public double GetExtendedInches() {
        return motor.getCurrentPosition()*ticksPerInch;
    }

    public void MoveTo(double inches, double power) {
        int pos = (int) (inches*ticksPerInch);
        motor.setTargetPosition(pos);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(Math.min(power, maxSpeed));
    }
    public  void Stop() {
        //stop the motor
        motor.setTargetPosition(motor.getCurrentPosition());
        motor.setPower(0);
    }

}

