package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "Concept: TensorFlow Object Deteleop", group = "TeleOp")

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
    Servo gripper;


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
        gripper = hardwareMap.servo.get("gripper");
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

                if (gamepad2.right_bumper) {
                    Loader.setPosition(.74);
                    sleep(434);
                    Loader.setPosition(.96);

                }
                //launcher
                if (gamepad1.y) {
                    Launcher.setPower(.62);

                }
                if (gamepad1.a) {
                    Launcher.setPower(0);
                }
                if (gamepad2.b) {// stop
                    Intake.setPower(0.0);
                }
                if (gamepad2.y) {// reverse
                    Intake.setPower(-1.0);
                }
                if (gamepad2.a) {// forward
                    Intake.setPower(.85);
                }
                if (gamepad2.left_bumper) {
                    Hopper.setPower(-.45);
                    Intake.setPower(0);

                }

                if (gamepad2.x) {
                    Hopper.setPower(0);
                    Intake.setPower(1.0);
                }
                if (gamepad2.dpad_right) {
                    intakerelease.setPosition(.36);
                }
                if (gamepad1.left_bumper){
                    gripper.setPosition(.46);
                }
                if (gamepad1.right_bumper){
                    gripper.setPosition(.97);
                }
                if (gamepad1.dpad_up){
                    Wobble.setPower(.65);
                    sleep(623);
                    Wobble.setPower(0.14);
                }
                if (gamepad1.dpad_down){
                    Wobble.setPower(-.25);
                    sleep(623);
                    Wobble.setPower(0);
                }
                if (gamepad1.x){
                    Wobble.setPower(0);
                }

            }

            //Gamepad 1 left joystick x strafe
            LeftFront.setPower(0);
            RightFront.setPower(0);
            RightRear.setPower(0);
            LeftRear.setPower(0);

            if (gamepad2.right_bumper) {
                Loader.setPosition(.74);
                sleep(434);
                Loader.setPosition(.96);

            }
            //launcher
            if (gamepad1.y) {
                Launcher.setPower(.62);

            }
            if (gamepad1.a) {
                Launcher.setPower(0);
            }
            if (gamepad2.b) {// stop
                Intake.setPower(0.0);
            }
            if (gamepad2.y) {// reverse
                Intake.setPower(-1.0);
            }
            if (gamepad2.a) {// forward
                Intake.setPower(.85);
            }
            if (gamepad2.left_bumper) {
                Hopper.setPower(-.45);
                Intake.setPower(0);

            }

            if (gamepad2.x) {
                Hopper.setPower(0);
                Intake.setPower(1.0);
            }
            if (gamepad2.dpad_right) {
                intakerelease.setPosition(.36);
            }
            if (gamepad1.left_bumper){
                gripper.setPosition(.46);
            }
            if (gamepad1.right_bumper){
                gripper.setPosition(.97);
            }
            if (gamepad1.dpad_up){
                Wobble.setPower(.65);
                sleep(623);
                Wobble.setPower(0.14);
            }
            if (gamepad1.dpad_down){
                Wobble.setPower(-.25);
                sleep(623);
                Wobble.setPower(0);
            }
            if (gamepad1.x){
                Wobble.setPower(0);
            }

            idle();
        }
    }
}