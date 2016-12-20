package org.firstinspires.ftc.robotcontroller.Ours;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
//import com.qualcomm.robotcore.util.Range;

/**
 * Created by chris on 11/15/16.
 */

@TeleOp(name="TwoMotor",group = "test")
public class TwoMotor extends OpMode{

    DcMotor left=null, right=null, flicker=null;
    DcMotorController cont=null;

    HardwareMap hwMap;

    double joy_left=0,joy_right=0;
    boolean drive_motors=false,flicker_motor=false,sloMo=false;

    public void init(){
        hwMap = hardwareMap;

        cont = hwMap.dcMotorController.get("wheels");
        try {
telemetry.addData("INIT","Started!");
            left = hwMap.dcMotor.get("left_drive");
            right = hwMap.dcMotor.get("right_drive");
//telemetry.addData("TEST","Mapped");
            left.setDirection(DcMotor.Direction.FORWARD);
            right.setDirection(DcMotor.Direction.REVERSE);
//telemetry.addData("TEST","Directed");
            left.setPower(0.0);
            right.setPower(0.0);

            drive_motors=true;
        } catch(Exception e){
            telemetry.addData("ERROR","One or Both drive motors failed to initialize");
        }
        try{
            flicker = hwMap.dcMotor.get("flicker");
            flicker.setDirection(DcMotor.Direction.FORWARD);
            flicker.setPower(0.0);
            flicker_motor=true;
        } catch(Exception e){ telemetry.addData("ERROR","Flicker not detected"); }
        telemetry.addData("VARS","drive_motors=%b\nflicker=%b",drive_motors,flicker_motor);
        telemetry.addData("INFO","Init done!");
    }
    public void init_loop(){

    }

    long startTime;
    public void start() {
        telemetry.addData("TEST", "Started");
        startTime = System.currentTimeMillis();
    }

    double factor=0.4;// Decimal factor for motor power in SloMo for TeleOp
    int delay=21,count=0, accel=30,tick=1;
    int sum=0;
    double avg=0.0, pow_left=0.0, pow_right=0.0;
    public void loop(){
        try {
            joy_left = -gamepad1.left_stick_y;
            joy_right = -gamepad1.right_stick_y;
            if((joy_left < 0.05 && joy_left > -0.05) && (joy_right < 0.05 && joy_right > -0.05)) tick=1;
//            telemetry.addData("THING", "got ze numbrs");

            if(gamepad1.x && count > delay){ sloMo = !sloMo; count=0; }// Simple on/off switch using 'X' button
            count++;

            pow_left = joy_left*((double)tick/accel); // Time-based acceleration
            pow_right = joy_right*((double)tick/accel); // Works-well
            //pow_left = Math.pow(joy_left,3); // Cubic acceleration curve
            //pow_right = Math.pow(joy_right,3);// Almost no different from no acceleration

            /*if(joy_left < 0) {
                pow_left = -Math.log10(9 * -joy_left + 1);// Logarithmic curve
            } else {
                pow_left = Math.log10(9 * joy_left + 1);// Doesnt work well either
            }
            if(joy_right < 0){
                pow_right = -Math.log10(9 * -joy_right + 1);// Logarithmic curve
            } else{
                pow_right = Math.log10(9 * joy_right + 1);
            }
            */

            if (drive_motors && sloMo) {
                left.setPower(pow_left * factor);
                right.setPower(pow_right * factor);
                telemetry.addData("SloMo","Active");
            } else if(drive_motors){
                left.setPower(pow_left);
                right.setPower(pow_right);
                telemetry.addData("SloMo","Disabled");
                tick++;
            }
        } catch(Exception e){ telemetry.addData("ERROR","Motors powering failed!"); }

        if(flicker_motor) {
            if(Math.ceil(gamepad1.left_trigger) == 1) flicker.setPower(0.2);
            else if(Math.ceil(gamepad1.right_trigger) == 1) flicker.setPower(-0.2);
            else if (gamepad1.left_bumper) flicker.setPower(1.0);
            else if (gamepad1.right_bumper) flicker.setPower(-1.0);
            else flicker.setPower(0.0);
   //         telemetry.addData("DEBUG","\rLT: %f",gamepad1.left_trigger);
        }

        sum++;
        avg = sum/((System.currentTimeMillis() - startTime)/1000.0);
        telemetry.addData("Left","\r%.2f",joy_left);
        telemetry.addData("Right","\r%.2f",joy_right);
//        telemetry.addData("DEBUG","\r%.2f lps",avg);
//        telemetry.addData("DEGUB","\r%d/%ds",sum,(System.currentTimeMillis() - startTime)/1000);
    }

    public void stop(){
        if(drive_motors) {
            left.setPower(0.0);
            right.setPower(0.0);
        }
        if(flicker_motor) flicker.setPower(0.0);
    }
}
