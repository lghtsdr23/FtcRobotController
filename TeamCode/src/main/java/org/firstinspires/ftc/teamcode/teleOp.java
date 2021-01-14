package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.external.samples.SampleRevBlinkinLedDriver;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;


@TeleOp(name = "TeleOp", group = "TeleOp")

public class TELEOP extends LinearOpMode {
    DcMotor LeftFront;
    DcMotor LeftRear;
    DcMotor RightFront;
    DcMotor RightRear;
    DcMotor Intake;
    DcMotor Launcher;
    DcMotor Hopper;
    DcMotor Wobble;
    Servo Loader;
    Servo intakerelease;


    /** The relativeLayout field is used to aid in providing interesting visual feedback
     * in this sample application; you probably *don't* need this when you use a color sensor on your
     * robot. Note that you won't see anything change on the Driver Station, only on the Robot Controller. */
    View relativeLayout;

    @Override
    public void runOpMode() {

        LeftFront = hardwareMap.dcMotor.get("MotorLeftFront");
        LeftRear = hardwareMap.dcMotor.get("MotorLeftRear");
        RightFront = hardwareMap.dcMotor.get("MotorRightFront");
        RightRear = hardwareMap.dcMotor.get("MotorRightRear");
        Intake = hardwareMap.dcMotor.get("MotorIntake");
        Launcher = hardwareMap.dcMotor.get("MotorLauncher");
        Hopper = hardwareMap.dcMotor.get("MotorHopper");
        Wobble = hardwareMap.dcMotor.get("MotorWobble");

        Loader = hardwareMap.servo.get("MotorLoader");
        intakerelease = hardwareMap.servo.get("intakerelease");

        RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        RightRear.setDirection(DcMotorSimple.Direction.REVERSE);


        //waitForStart();
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();




        }


        while (opModeIsActive()) {

            //Gamepad 1 left joystick x strafe
            while ((Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) && opModeIsActive()) {
                LeftRear.setPower(gamepad1.left_stick_y * 1 + gamepad1.left_stick_x * 1 + gamepad1.right_stick_x * -1);
                LeftFront.setPower(gamepad1.left_stick_y * 1 + gamepad1.left_stick_x * -1 + gamepad1.right_stick_x * -1);
                RightFront.setPower(gamepad1.left_stick_y * 1 + gamepad1.left_stick_x * 1 + gamepad1.right_stick_x * 1);
                RightRear.setPower(gamepad1.left_stick_y * 1 + gamepad1.left_stick_x * -1 + gamepad1.right_stick_x * 1);


            }

            //Gamepad 1 left joystick x strafe
           LeftFront.setPower(0);
            RightFront.setPower(0);
            RightRear.setPower(0);
            LeftRear.setPower(0);

            if (gamepad2.right_bumper){
                Loader.setPosition(.77);
                sleep(434);
                Loader.setPosition(.96);
            }
            //launcher
            if (gamepad1.y){
                Launcher.setPower(.63);

            }
            if (gamepad1.a){
                Launcher.setPower(0);
            }
            if(gamepad2.b){// stop
                Intake.setPower(0.0);
            }
            if(gamepad2.y){// reverse
                Intake.setPower(-1.0);
            }
            if(gamepad2.a){// forward
                Intake.setPower(1.0);
            }
            if (gamepad2.dpad_left){
                Hopper.setPower(-.45);

            }

            if (gamepad2.x){
                Hopper.setPower(0);
            }
            if (gamepad2.dpad_right){
                intakerelease.setPosition(.36);
            }


            idle();
        }
    }
}