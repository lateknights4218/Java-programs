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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.Telemetry;


@Autonomous(name="Auton: Ramp_Red" , group="Auto")
public class AutonomousRampRed extends LinearOpMode {

    DcMotor left = null, right = null, flicker = null;
    DcMotorController cont = null;

    HardwareMap hwMap;

    double joy_left = 0, joy_right = 0;
    boolean drive_motors = false, flicker_motor = false, sloMo = false;
    private ElapsedTime runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 1;
    static final double     TURN_SPEED    = 1;


    void moveForward(double moveTime, double motorSpeed){
        if(drive_motors) {
            left.setPower(motorSpeed * FORWARD_SPEED);
            right.setPower(motorSpeed * FORWARD_SPEED);
        }

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < moveTime)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        if(drive_motors) {
            left.setPower(0.0);
            right.setPower(0.0);
        }
    }

    void turnLeft(double turnTime, double motorSpeed){
        if(drive_motors) {
            left.setPower(-1 * motorSpeed * TURN_SPEED);
            right.setPower(motorSpeed * TURN_SPEED);
        }

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < turnTime)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        if(drive_motors) {
            left.setPower(0.0);
            right.setPower(0.0);
        }
    }
    void turnRight(double turnTime, double motorSpeed){
        if(drive_motors) {
            left.setPower(motorSpeed * TURN_SPEED);
            right.setPower(-1 * motorSpeed * TURN_SPEED);
        }

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < turnTime)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        if(drive_motors) {
            left.setPower(0.0);
            right.setPower(0.0);
        }
    }

    void score(double time){
        if(flicker_motor) flicker.setPower(1);

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        if(flicker_motor) flicker.setPower(0.0);
    }
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
        try{
            flicker = hwMap.dcMotor.get("flicker");
            flicker.setDirection(DcMotorSimple.Direction.REVERSE);
            flicker_motor = true;
        }catch(Exception e){}
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        moveForward(0.7, 0.5);// Turn time, turn speed
        sleep(500);
        turnLeft(0.6, 0.55);
        sleep(500);
        moveForward(0.75, 0.5);
        sleep(500);
        turnLeft(0.45, 0.5);
        sleep(500);
        moveForward(0.5,1);

        score(1.5);
    }
}