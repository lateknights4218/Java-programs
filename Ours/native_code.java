package org.firstinspires.ftc.robotcontroller.Ours;

import java.io.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


// Program to test if we can execute native code on phone

@TeleOp(name="Native",group = "test")
public class native_code extends OpMode{

	Runtime rt = null;
	Process p = null;
	BufferedReader stdIn;

	public void init(){
		try{
		  rt = Runtime.getRuntime();
		}catch(Exception e){}
	}
	public void init_loop(){}

	public void start(){}

	int count=0;
	String s = null;
	public void loop(){
		try{
		  p = rt.exec(new String[]{"./hello",Integer.toString(count)});// Will fail. Need to compile with NDK
		  stdIn = new BufferedReader(new InputStreamReader(p.getInputStream()));
		  
		  s = stdIn.readLine();
		}catch(Exception e){
			telemetry.addData("ERROR","FUCK %d",count);
		}

		if(s != null) telemetry.addData("Output","%s",s);
//		telemetry.addData("LOG","count: %d\n",count);
		count++;
	}

	public void stop(){}
}
