package org.firstinspires.ftc.teamcode.hardware;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
public class StepHandler {
    public static int currentStep = 1;
    public static Map<Integer, PathData> stepMap = new HashMap<>();

    public static void createNewStep(Methods method, double x, double y, double heading ) {
        PathData data = new PathData(method, x, y, heading);
        stepMap.put(currentStep, data);
        currentStep += 1;
    }
    public static String buildString (PathData data) {
        if (data.method == Methods.STRAFE) {
            return ".strafeTo(new Vector2D(" + data.xPosition + ", " + data.yPosition + "))";
        } else if (data.method == Methods.STRAFE_WITH_HEADING) {
            return ".strafeToLinearHeading(new Vector2D("+ data.xPosition + ", " +data.yPosition +")"+ ", "+ data.heading +")";
        } else if (data.method == Methods.SPLINE) {
            return ".splineTo(new Vector2D("+ data.xPosition + ", " + data.yPosition +"))";
        } else if (data.method == Methods.SPLINE_WITH_HEADING) {
            return ".splineToWithLinearHeading(new Vector2D(" + data.xPosition + ", " + data.yPosition + ")" + ", " + data.heading +")";
        } else {
            return "error";
        }
    }
    public static String generateStepsString() {
        String finalString = "";
        stepMap.forEach((step, data) -> finalString + buildString(data) + "\n";
        finalString += "\n" + ".build;";
    }




    public void createFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            writer.write(generateStepsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



























}
