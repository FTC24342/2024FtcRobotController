package org.firstinspires.ftc.teamcode.hardware;

public enum Methods {
    STRAFE, // RoadRunner .strafeTo(new Vector2D(xPos, yPos))
    STRAFE_WITH_HEADING, // RoadRunner .strafeToLinearHeading(new Vector2D(xPos, yPos), Math.toRadians(180))
    SPLINE, // RoadRunner .splineTo(new Vector2D(xPos, yPos))
    SPLINE_WITH_HEADING, // RoadRunner .splineToLinearHeading(new Vector2D(xPos, yPos), Math.toRadians(180))
    NONE

}
