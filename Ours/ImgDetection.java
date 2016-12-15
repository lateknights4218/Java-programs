package org.firstinspires.ftc.robotcontroller.Ours;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

/**
 * Created by chris on 12/3/16.
 */

// Currently only tracks images
@Autonomous(name = "Concept: ImgDetection", group = "Concept")
public class ImgDetection extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AXk33bP/////AAAAGXxSK5xdMUdDvW12So5IF6l9MVfJ1NbqqT9Cr/NwP/NCJsTDHHBC7XNNy1mDN8Q8h8j3kgDjn87ZU2wH9MJIsngA4kctZWVSmL+LqrBAfwF8m2zoB4jFu6KIRor/2VwIQLfej7AJhisDfwVCgXLYbyVEnyOeWR2lSe5iEVu3lia1Xr5Iit+nijiBRKZcgQdNtCJsT2e13/u/UyZHvvCDYos2q/3WByUd8gq3Uc5Lum7cQNa0WH4ryFWHhwmDU7h2sGmrkfkvfZjqDffGzBPkeAr3n8EEX0djnM2t0U0apBsKe2Cxw5SrZv39OoYLVqiXmD1qHjmNmiropT2fmwDAQWoGLEGyBR7kJhsAMYtufc9l";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS,4);

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");

        waitForStart();

        beacons.activate();

        while (opModeIsActive()) {
            for (VuforiaTrackable T : beacons) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) T.getListener()).getPose();

                if (pose != null) {
                    VectorF vector = pose.getTranslation();

                    telemetry.addData("\r" + T.getName() + "-Translation", vector);

                    double degToTurn = Math.toDegrees(Math.atan2(vector.get(1), vector.get(2)));
                }
            }
            telemetry.update();
        }
    }
}
