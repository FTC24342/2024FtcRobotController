package org.firstinspires.ftc.teamcode.hardware;

public class ClawSlide extends Slide{
    private double TOPBARREADYINCHES = 16.875;
    private double TOPBARHOOKSPECIMANINCHES = 13.75;
    public ClawSlide() {
        super("lift", "resetlift", Slide.ExtendMotorDirection.Reverse, 3200, 1.0,84.6);
//rev encoder        super("lift", "resetlift", Slide.ExtendMotorDirection.Reverse, 197500, 1.0,5225);
    }
    public void MoveToTopBarReadyPosition(double power){
        this.MoveTo( TOPBARREADYINCHES, power);
    }

    public Boolean IsAtTopBarReadyPosition(double margin) {
        return this.IsAtPosition(TOPBARREADYINCHES, margin);
    }

    public void MoveToTopBarHookSpecimanPosition(double power) {
        this.MoveTo( TOPBARHOOKSPECIMANINCHES, power);
    }

    public Boolean IsAtTopBarHookSpecimanPosition(double margin) {
        return this.IsAtPosition(TOPBARHOOKSPECIMANINCHES, margin);
    }
    public void MoveToWallPickupPosition(double power){
        this.MoveTo( 0, power);
    }

    public void MoveToHighBasketPosition(double power){
        this.MoveTo( 20, power);
    }

    public void MoveToLowBasketPosition(double power){
        this.MoveTo( 15, power);
    }
}

