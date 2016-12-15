package org.firstinspires.ftc.robotcontroller.Ours;

/**
 * Created by Reagan on 12/9/16.
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.Telemetry;


@Autonomous(name="Auton: ForwardCorner", group="Auto")
public class AutonomousForwardCorner extends LinearOpMode {

    DcMotor left = null, right = null, flicker = null;
    DcMotorController cont = null;

    HardwareMap hwMap;

    double joy_left = 0, joy_right = 0;
    boolean drive_motors = false, flicker_motor = false, sloMo = false;
    private ElapsedTime runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 1;
    static final double     TURN_SPEED    = 0.5;

    @Override
    public void runOpMode() {

        hwMap = hardwareMap;

        cont = hwMap.dcMotorController.get("wheels");
        try {
            left = hwMap.dcMotor.get("left_drive");
            right = hwMap.dcMotor.get("right_drive");
//telemetry.addData("TEST","MApped");
            left.setDirection(DcMotor.Direction.FORWARD);
            right.setDirection(DcMotor.Direction.REVERSE);
//telemetry.addData("TEST","Directed");
            left.setPower(0.0);
            right.setPower(0.0);

            drive_motors = true;

        } catch (Exception e) {}
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if(drive_motors) {
            left.setPower(FORWARD_SPEED);
            right.setPower(FORWARD_SPEED);
        }

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.9)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        if(drive_motors) {
            left.setPower(0.0);
            right.setPower(0.0);
        }
    }

}
