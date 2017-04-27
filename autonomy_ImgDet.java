package org.firstinspires.ftc.robotcontroller.Ours;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

/**
 * Created by chris on 12/3/16.
 */

// Currently only tracks images
@Autonomous(name = "Concept: autonomy_ImgDet", group = "Concept")
public class autonomy_ImgDet extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor left = hardwareMap.dcMotor.get("left_drive");
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        DcMotor right = hardwareMap.dcMotor.get("right_drive");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        left.setPower(0.0);
        right.setPower(0.0);

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

        VuforiaTrackableDefaultListener wheels = (VuforiaTrackableDefaultListener) beacons.get(0).getListener();
        beacons.activate();

        telemetry.addData("DEBUG","Point 1");
        telemetry.update();

        left.setPower(0.3);
        right.setPower(0.3);

        while(opModeIsActive() && wheels.getPose() == null) idle();
        left.setPower(0.0);
        right.setPower(0.0);

        telemetry.addData("DEBUG","Point 2");
        telemetry.update();

        VectorF angles = anglesFromTarget(wheels);
        VectorF trans = navOffWall(wheels.getPose().getTranslation(), Math.toDegrees(angles.get(0) )-90, new VectorF(500,0,0));

        telemetry.addData("DEBUG","Point 3");
        telemetry.update();

        if(trans.get(0) > 0){
            left.setPower(-0.12);
            right.setPower(0.12);
        } else{
            left.setPower(0.12);
            right.setPower(-0.12);
        }

        while(opModeIsActive() && Math.abs(trans.get(0)) > 10){
            if(wheels.getPose() != null){
                trans = navOffWall(wheels.getPose().getTranslation(), Math.toDegrees(angles.get(0) )-90, new VectorF(500,0,0));
            }
            idle();
        }
        left.setPower(0.0);
        right.setPower(0.0);

        /*
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
        } */
    }

    public VectorF navOffWall(VectorF trans, double robotAngle, VectorF offWall){
        return new VectorF((float) (trans.get(0) - offWall.get(0) * Math.sin(Math.toRadians(robotAngle)) - offWall.get(2) * Math.cos(Math.toRadians(robotAngle))), trans.get(1), (float) (trans.get(2) + offWall.get(0) * Math.cos(Math.toRadians(robotAngle)) - offWall.get(2) * Math.sin(Math.toRadians(robotAngle))));
    }

    public VectorF anglesFromTarget(VuforiaTrackableDefaultListener image){
        float [] data = image.getRawPose().getData();
        float [] [] rotation = {{data[0], data[1]}, {data[4], data[5], data[6]}, {data[8], data[9], data[10]}};
        double thetaX = Math.atan2(rotation[2][1], rotation[2][2]);
        double thetaY = Math.atan2(-rotation[2][0], Math.sqrt(rotation[2][1] * rotation[2][1] + rotation[2][2] * rotation[2][2]));
        double thetaZ = Math.atan2(rotation[1][0], rotation[0][0]);
        return new VectorF((float)thetaX, (float)thetaY, (float)thetaZ);
    }
}
