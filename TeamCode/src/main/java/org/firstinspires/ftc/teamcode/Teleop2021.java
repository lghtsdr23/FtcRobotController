package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "teleop2021", group = "TeleOp")
@Disabled
public class Teleop2021 extends LinearOpMode {
    DcMotor LeftFront;
    DcMotor LeftRear;
    DcMotor RightFront;
    DcMotor RightRear;
    DcMotor Intake;
    DcMotorEx Launcher;
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
        //  Launcher = hardwareMap.dcMotor.get("MotorLauncher");
        Hopper = hardwareMap.dcMotor.get("MotorHopper");
        Wobble = hardwareMap.dcMotor.get("MotorWobble");
        gripper = hardwareMap.servo.get("gripper");
        Loader = hardwareMap.servo.get("MotorLoader");
        intakerelease = hardwareMap.servo.get("intakerelease");
        Launcher = hardwareMap.get(DcMotorEx.class, "MotorLauncher");
        Launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,new PIDFCoefficients(30,0,0,22, MotorControlAlgorithm.PIDF));
        RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        RightRear.setDirection(DcMotorSimple.Direction.REVERSE);


        Launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



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

                controls();

            }

            //Gamepad 1 left joystick x strafe
            LeftFront.setPower(0);
            RightFront.setPower(0);
            RightRear.setPower(0);
            LeftRear.setPower(0);
            controls();

            idle();
        }
    }


    public void controls(){
        if (gamepad2.right_bumper) {
            Loader.setPosition(.70);
            sleep(404);
            Loader.setPosition(.97);

        }
        //launcher
        if (gamepad1.y) {
            Launcher.setVelocity(1155);

        }
        if (gamepad1.a) {
            Launcher.setPower(0);
        }
        if (gamepad2.b) {// stop
            Intake.setPower(0.0);
        }
        if (gamepad2.y) {// reverse
            Intake.setPower(-0.9);
        }
        if (gamepad2.a) {// forward
            Intake.setPower(0.95);
        }
        if (gamepad2.left_bumper) {
            Hopper.setPower(-1);
            Intake.setPower(0);

        }
        Hopper.setPower(-0.05);
        if (gamepad2.x) {
            Hopper.setPower(1);
            Intake.setPower(.95);
        }
        if (gamepad2.dpad_left) {
            intakerelease.setPosition(.36);
        }
        if (gamepad1.right_bumper){
            gripper.setPosition(.46);
        }
        if (gamepad1.left_bumper){
            gripper.setPosition(1);
        }
        if (gamepad1.dpad_down){
            Wobble.setPower(.85);
            sleep(623);
            Wobble.setPower(0.14);
        }
        if (gamepad1.dpad_up){
            Wobble.setPower(-.65);
            sleep(423);
            Wobble.setPower(0);
        }
        if (gamepad1.x){
            Wobble.setPower(0);
        }
        if (gamepad1.b){
            gripper.setPosition(.65);
        }
        if (gamepad2.dpad_down){
            Launcher.setVelocity(1020);
        }
    }
}