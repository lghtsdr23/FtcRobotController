/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

//import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//region Literally just the code lol
@TeleOp(name = "Teleop 2021", group = "Concept")
@Disabled
public class Teleop2021 extends LinearOpMode  {
    // Declaring Motors
    DcMotor LeftFront;
    DcMotor LeftRear;
    DcMotor RightFront;
    DcMotor RightRear;

    //DcMotor RingLauncher;
    DcMotorEx RingLauncher;

    DcMotor Intake;
    DcMotor Hopper;
    DcMotor Wobble;

    DcMotor OdometryLeft;
    DcMotor OdometryRight;
    DcMotor OdometryCenter;

    Servo LoadServo;
    Servo IntakeServo;
    Servo WobbleServo;

    ElapsedTime opmodeRunTime = new ElapsedTime();

// Read the PIDF coefficients






    double motorMultiplyer = 1;

    double X = 0;
    double Y = 0;
    double Z = 0;

    double readTime = 0;

    int wobbleArmCurrentPos = 1; //1 = up, 0 = down

    boolean holdingLeftTrigger = false;
    boolean hopperUp = false;

    int RINGLAUNCHERFULLPOWER = 1300;
    int RINGLAUNCHERPOWERSHOT = 700;

    @Override public void runOpMode() {
        LeftFront = hardwareMap.get(DcMotor.class, "MotorLeftFront");
        LeftRear = hardwareMap.get(DcMotor.class, "MotorLeftRear");
        RightFront = hardwareMap.get(DcMotor.class, "MotorRightFront");
        RightRear = hardwareMap.get(DcMotor.class, "MotorRightRear");

        RingLauncher = hardwareMap.get(DcMotorEx.class, "MotorLauncher");

        PIDFCoefficients PIDValue = RingLauncher.getPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER);

        PIDValue.p = 30;
        PIDValue.i = 0;
        PIDValue.d = 0;
        PIDValue.f = 22;

        RingLauncher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDValue);

        Intake = hardwareMap.get(DcMotor.class, "MotorIntake");
        Hopper = hardwareMap.get(DcMotor.class, "MotorHopper");
        Wobble = hardwareMap.get(DcMotor.class, "MotorWobble");

        OdometryCenter = hardwareMap.get(DcMotor.class, "MotorWobble");
        OdometryLeft = hardwareMap.get(DcMotor.class, "MotorIntake");
        OdometryRight = hardwareMap.get(DcMotor.class, "MotorHopper");

        LoadServo = hardwareMap.get(Servo.class, "MotorLoader");
        IntakeServo = hardwareMap.get(Servo.class, "intakerelease");
        WobbleServo = hardwareMap.get(Servo.class, "gripper");

        telemetry.addData("Status ", "Motors Have Been Mapped");
        telemetry.update();
        //endregion

        LeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LeftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        LeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LeftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        RingLauncher.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        RingLauncher.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        //region Reset Encoders
        // Reset Motor Encoders. Right Front Does not have an associated Encoder
        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        OdometryLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        OdometryRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        OdometryCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Setting the runmode so that the motors can run again
        LeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryCenter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status ", "Encoders Have Been Reset");
        telemetry.update();
        //endregion

        //region Reverse Left Side //Originally flipped right side, but everything was negative
        LeftFront.setDirection(DcMotor.Direction.REVERSE);
        LeftRear.setDirection(DcMotor.Direction.REVERSE);
        //endregion

        WobbleServo.setPosition(0.65);

        //region MENU
        // Print to see if opModeIsActive is true, or false
        telemetry.addData("opModeIsActive", opModeIsActive());
        telemetry.addData("Status", "Waiting For Program To begin!");
        telemetry.update();

        LoadServo.setPosition(.96);
        waitForStart();

        opmodeRunTime.reset();
        readTime = opmodeRunTime.milliseconds();

        while (opModeIsActive()) {

            //region GAMEPAD 1

            //region Driving
            Z = gamepad1.right_stick_x;
            Y = -gamepad1.left_stick_y;
            X = gamepad1.left_stick_x;

            if (gamepad1.left_bumper){
                motorMultiplyer = 1;
            } else if (gamepad1.right_bumper){
                motorMultiplyer = 0.5;
            }

            LeftFront.setPower((Range.clip(X + Y + Z, -1, 1)) * motorMultiplyer);
            RightRear.setPower((Range.clip(X + Y + -Z, -1, 1)) * motorMultiplyer);
            LeftRear.setPower((Range.clip(-X + Y + Z, -1, 1)) * motorMultiplyer);
            RightFront.setPower((Range.clip(-X + Y + -Z, -1, 1)) * motorMultiplyer);
            //endregion

            //region Ring Launcher
            if (gamepad1.y || hopperUp){
                RingLauncher.setVelocity(RINGLAUNCHERFULLPOWER);
            } else if (gamepad1.b){
                RingLauncher.setVelocity(RINGLAUNCHERPOWERSHOT);
            } else if (gamepad1.a){
                RingLauncher.setVelocity(0);
            }
            //endregion

            //region Wobble Claw
            if (gamepad1.right_trigger > 0.4){
                WobbleServo.setPosition(0.45);
            } else {
                WobbleServo.setPosition(0.65);
            }
            //endregion

            //region Wobble Arm
            if (gamepad1.left_trigger > 0.4){
                if (!holdingLeftTrigger) {

                    holdingLeftTrigger = true;

                    if (wobbleArmCurrentPos == 1) {
                        Wobble.setPower(-0.4);
                        wobbleArmCurrentPos = 0;
                    } else if (wobbleArmCurrentPos == 0){
                        Wobble.setPower(0.6);
                        wobbleArmCurrentPos = 1;
                    }
                }
            } else {
                holdingLeftTrigger = false;
                Wobble.setPower(0);
            }
            //endregion

//endregion

            //region GAMEPAD 2
            //region Load Servo
            if (gamepad2.left_bumper) {
                LoadServo.setPosition(.76);
            } else {
                LoadServo.setPosition(.96);
            }
            //endregion

            //region intake
            if (gamepad2.a) {// forward
                Intake.setPower(1.0);

            } else if (gamepad2.y){// reverse

                Intake.setPower(-1.0);
            } else if (gamepad2.b){// stop
                Intake.setPower(0.0);

            }
            //endregion

            //region Hopper
            if (gamepad2.right_bumper) {
                Hopper.setPower(-.55);
                hopperUp = true;
            } else if (hopperUp == true){
                Hopper.setPower(-.20);
            }


            if (gamepad2.x) {
                Hopper.setPower(0);
                RingLauncher.setVelocity(0);
                hopperUp = false;
            }
            //endregion
            //endregion
        }
    }
} //endregion

