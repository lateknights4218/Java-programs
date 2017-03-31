package org.firstinspires.ftc.robotcontroller.Ours;

/**
 * Created by Reagan on 12/6/16.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;


@Autonomous(name="Auton: Forward", group="Auto")
public class AutonomousForward extends LinearOpMode {

    DcMotor left = null, right = null, flicker = null;// Pointers for each motor
    DcMotorController cont = null;// Probably useless motor controller

    HardwareMap hwMap;

    boolean drive_motors = false, flicker_motor = false, sloMo = false;// Status booleans
    private ElapsedTime runtime = new ElapsedTime();// Keeps track of time. Like a watch


    static final double     FORWARD_SPEED = 1;// Constants controlling drive speeds
    static final double     TURN_SPEED    = 0.5;

    @Override
    public void runOpMode() {

        hwMap = hardwareMap;

        cont = hwMap.dcMotorController.get("wheels");// Probably useless

        try {// Standard init stuff
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

        if(drive_motors) {// Aaaand we're off to the races
            left.setPower(FORWARD_SPEED);
            right.setPower(FORWARD_SPEED);
        }

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.55)) {// Runs for X amount of time
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        if(drive_motors) {// STOP
            left.setPower(0.0);
            right.setPower(0.0);
        }
    }
}
