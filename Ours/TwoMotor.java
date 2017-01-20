package org.firstinspires.ftc.robotcontroller.Ours;

import android.hardware.Camera;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * Created by chris on 11/15/16.
 */

@TeleOp(name="TwoMotor",group = "test")
public class TwoMotor extends OpMode{

    DcMotor left=null, right=null, flicker=null;// Pointers for all 3 motors
    DcMotorController cont=null;// Not used, and AFAIK not needed

    HardwareMap hwMap;// Convenience

    double joy_left=0,joy_right=0;// FP variables used for joystick input
    boolean drive_motors=false,flicker_motor=false,sloMo=false;// Status booleans

    Camera cam = Camera.open();
    Camera.Parameters p;
    boolean camera=false;

    public void init(){
        hwMap = hardwareMap;

        try {
            p = cam.getParameters();
            camera=true;
        }catch(Exception e){}

        cont = hwMap.dcMotorController.get("wheels");// As above, probably unnecessary
        try {
telemetry.addData("INIT","Started!");
            left = hwMap.dcMotor.get("left_drive");
            right = hwMap.dcMotor.get("right_drive");

            left.setDirection(DcMotor.Direction.FORWARD);
            right.setDirection(DcMotor.Direction.REVERSE);

            left.setPower(0.0);// Assurance
            right.setPower(0.0);

            drive_motors=true;// Both mapped successfully
        } catch(Exception e){// Allows program to run through errors
            telemetry.addData("ERROR","One or Both drive motors failed to initialize");
        }
        try{
            flicker = hwMap.dcMotor.get("flicker");
            flicker.setDirection(DcMotor.Direction.FORWARD);
            flicker.setPower(0.0);
            flicker_motor=true;// Scoring device mapped successfully
        } catch(Exception e){ telemetry.addData("ERROR","Flicker not detected"); }
//telemetry.addData("VARS","drive_motors=%b\nflicker=%b",drive_motors,flicker_motor);
        telemetry.addData("INFO","Init done!");
    }
    public void init_loop(){

    }

    long startTime;
    public void start() {
        telemetry.addData("TEST", "Started");
        startTime = System.currentTimeMillis();// Used for possible timing.
    }


    double factor=0.46;// Decimal factor for motor power in SloMo for TeleOp
    boolean held=false,held2=false;// Vars for SloMo
    int accel=30,tick=1;// Vars for acceleration
    int sum=0;// Used for the average
    double avg=0.0, pow_left=0.0, pow_right=0.0;// Avg is only for timing

    int wheel_errors=0;
    public void loop() {// Avg. loops per sec is ~170 (~5.88ms per loop)
        try {
            try {// Motor try-catch block
                joy_left = -gamepad1.left_stick_y;// Grab joystick values
                joy_right = -gamepad1.right_stick_y;

// The tick is used for the acceleration bit. Currently given a 5% margin on either end of 0.0
                if ((joy_left < 0.05 && joy_left > -0.05) && (joy_right < 0.05 && joy_right > -0.05))
                    tick = 1;// Reset tick

                if (gamepad2.x && !held) {
                    sloMo = !sloMo;
                    held = true;
                }// Simple on/off switch using 'X' button
                else if (gamepad2.x) ;
                else held = false;
                if (gamepad1.left_stick_button && gamepad1.right_stick_button && !held2) {
                    sloMo = !sloMo;
                    held2 = true;
                } else if (gamepad1.left_stick_button && gamepad1.right_stick_button) ;
                else held2 = false;// This one assures the driver still has full control

                pow_left = joy_left * ((double) tick / accel); // Time-based acceleration
                pow_right = joy_right * ((double) tick / accel); // Works-well

                //pow_left = Math.pow(joy_left,3); // Cubic acceleration curve
                //pow_right = Math.pow(joy_right,3);// Almost no different from lack of acceleration

            /*if(joy_left < 0) {
                pow_left = -Math.log10(9 * -joy_left + 1);// Logarithmic curve (left)
            } else {
                pow_left = Math.log10(9 * joy_left + 1);// Doesnt work well either
            }
            if(joy_right < 0){
                pow_right = -Math.log10(9 * -joy_right + 1);// Logarithmic curve (right)
            } else{
                pow_right = Math.log10(9 * joy_right + 1);// ""
            }
            */
                if (camera && sloMo) {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                } else if (camera) {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    cam.setParameters(p);
                    cam.startPreview();
                }
                if (drive_motors && sloMo) {// Set motor powers
                    left.setPower(pow_left * factor);
                    right.setPower(pow_right * factor);
                    telemetry.addData("SloMo", "Active");

                } else if (drive_motors) {
                    left.setPower(pow_left);
                    right.setPower(pow_right);
                    telemetry.addData("SloMo", "Disabled");
                }

                if (tick < 30) tick++;

            } catch (Exception e) {
                telemetry.addData("ERROR", "Motors powering failed!");
                wheel_errors++;
            }// End Motor try-catch block

            if (flicker_motor) {
                if (Math.ceil(gamepad1.left_trigger) == 1) flicker.setPower(0.2);// Gentle speed
                else if (Math.ceil(gamepad1.right_trigger) == 1) flicker.setPower(-0.2);
                else if (gamepad1.left_bumper) flicker.setPower(1.0);// Normal speed
                else if (gamepad1.right_bumper) flicker.setPower(-1.0);
                else flicker.setPower(0.0);
                //         telemetry.addData("DEBUG","\rLT: %f",gamepad1.left_trigger);
            }

            telemetry.addData("Left", "\r%.2f", joy_left);
            telemetry.addData("Right", "\r%.2f", joy_right);
            telemetry.addData("DEBUG", "wheel_errors: %d", wheel_errors);

//        avg = (++sum)/((System.currentTimeMillis() - startTime)/1000.0);// Measures loops/sec
//        telemetry.addData("DEBUG","\r%.2f lps",avg);
        }catch(Exception e){
            telemetry.addData("ERROR","Main loop failed somewhere\nException: '%s'",e.getMessage());
        }
    }

    public void stop(){// Assures everything stops when stopped
        if(drive_motors) {
            left.setPower(0.0);
            right.setPower(0.0);
        }
        if(flicker_motor) flicker.setPower(0.0);
    }
}
