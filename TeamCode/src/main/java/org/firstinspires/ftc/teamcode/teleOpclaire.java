package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "Teleop", group = "TeleOp")

public class teleOpclaire extends Motor_Classes {

    @Override
    public void runOpMode() {
        initrobot();
        waitForStart();



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
                Loader.setPosition(.96);

            }
            //launcher
            if (gamepad1.y) {
                Launcher.setVelocity(1082);

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