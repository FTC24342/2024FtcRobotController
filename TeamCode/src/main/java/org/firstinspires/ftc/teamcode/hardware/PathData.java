package org.firstinspires.ftc.teamcode.hardware;

public class PathData {
    public Methods method = null;
    public double xPosition = 0;
    public double yPosition = 0;
    public double heading = 0;

    PathData (Methods method,  double xPosition, double yPosition, double heading) {
        this.method = method;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.heading = heading;
    }
}
