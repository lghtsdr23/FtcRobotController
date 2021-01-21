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

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "RPM Measure", group = "Concept")
//@Disabled
public class RobotShooterTestSecondRendition extends LinearOpMode  {

    /** THIS PROJECT WAS CREATED TO FIND THE AMOUNT OF TICKS PER ROTATION PER MINUTE **/

    DcMotor RingLauncher;
    DcMotor RingLauncher2;

    ElapsedTime poemElapsed = new ElapsedTime();

    int HIGH_RPM = 5400;
    int MEDIUM_RPM = 2500;
    int LOW_RPM = 0;
    int rpmGoal = MEDIUM_RPM;
    int rpmQue = rpmGoal;
    int TICKS_PER_REVOLUTION = 20;
    int SAMPLE_RATE = 3;


    double currentTicks = 0;
    double previousTicks = 0;
    double differenceInTicks = 0;
    double differenceInTime = 0;
    double ticksPerMilliseconds = 0;
    double revolutions = 0;
    double RPM = 0;
    double rpmError = 0;
    double setSpeed = 0;

    double readTime = 0;
    double encoderTicks = 0;

    boolean speedUp = true;
    boolean touchingButton = false;

    @Override public void runOpMode() {

        RingLauncher  = hardwareMap.get(DcMotor.class, "RingLauncher");
        RingLauncher2  = hardwareMap.get(DcMotor.class, "RingLauncher2");


        //RingLauncher.setDirection(DcMotor.Direction.REVERSE);
        RingLauncher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RingLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        /* we keep track of how long it's been since the OpMode was started, just
         * to have some interesting data to show */
        ElapsedTime opmodeRunTime = new ElapsedTime();

        // Reset to keep some timing stats for the post-'start' part of the opmode

        waitForStart();

        opmodeRunTime.reset();
        readTime = opmodeRunTime.milliseconds();

        while (opModeIsActive()) {

            // Y For Raising RPM's, A For Lowering RPM's
/*
            if (gamepad1.y && !touchingButton) {
                rpm = rpm + 500;
                touchingButton = true;

            }
            else if (gamepad1.a && !touchingButton) {
                rpm = rpm - 500;
                touchingButton = true;

            } else if (!gamepad1.a && !gamepad1.y){
                touchingButton = false;

            }

            telemetry.addData("rpm Set Speed: ", rpm);
            telemetry.update();
*/

            //THE MOTOR IS REVERSED SO SOME VALUES MIGHT LOOK STRANGE, DONT CHANGE THEM THIS WORKS.
            // LINES FOR FINDING RPM
            // Waiting 15 milliseconds before updating
            if (readTime + SAMPLE_RATE < opmodeRunTime.milliseconds()){

                // getting the current encoder ticks from the motor
                currentTicks = RingLauncher.getCurrentPosition();
                // getting the difference in time from our last RPM read
                differenceInTime = (opmodeRunTime.milliseconds() - readTime);
                // resetting read time variable
                readTime = opmodeRunTime.milliseconds();
                // getting the difference in encoder ticks from the last read
                differenceInTicks = (currentTicks - previousTicks);
                // figuring out our ticks per milliseconds
                ticksPerMilliseconds = differenceInTicks/differenceInTime;
                // now that we have our ticks per milliseconds, we can get our RPMS
                revolutions = ticksPerMilliseconds/TICKS_PER_REVOLUTION;
                // Multiplying ticks per milliseconds by 60000, which is exactly 1 minute
                RPM = revolutions * 60000;
                // resetting the ticks
                previousTicks = currentTicks;


                Log.i("FTCRobot", "RPM's: " + RPM);
                Log.i("FTCRobot", "Current Ticks: " + currentTicks);
                Log.i("FTCRobot", "Launcher Power: " + RingLauncher.getPower());

                rpmError = rpmGoal - RPM;

                /*
                if (Math.abs(rpmError) < 300) {
                    setSpeed = RingLauncher.getPower();
                    speedUp = false;
                }
                 */

                //RingLauncher.setPower(0.3);
//                if (!speedUp) {
//                    RingLauncher.setPower(setSpeed);
//                } else {
                    RingLauncher.setPower(Range.clip(rpmError / 1000 + RingLauncher.getPower(), -1, 1));
                    RingLauncher2.setPower(RingLauncher.getPower());
                //}

            }

            /* just thinking over stuff
            //100 ticks in 5 milliseconds.
            //100 / 7 = 14
            // 14 revolutions in 5 millisecond. weve got 12000 5 milliseconds
            //60000/5


            /* in theory the formula should be:

                difference of ticks / TICKS_PER_ROTATION = importantnumber
                60000/importantnumber = another important number
                another important number/TICKS_PER_ROTATION = RPM


             */

            /*while (Math.abs(rpmGoal - RPM) > 300){

                //rpmError/100
//                if (RPM > rpmGoal){
//                    RingLauncher.setPower(RingLauncher.getPower() + 0.01);
//                } else if (RPM < rpmGoal){
//                    RingLauncher.setPower(RingLauncher.getPower() - 0.01);
//                }

                if (readTime + 15 < opmodeRunTime.milliseconds()){

                    // getting the current encoder ticks from the motor
                    currentTicks = -RingLauncher.getCurrentPosition();
                    // getting the difference in time from our last RPM read
                    differenceInTime = (opmodeRunTime.milliseconds() - readTime);
                    // resetting read time variable
                    readTime = opmodeRunTime.milliseconds();
                    // getting the difference in encoder ticks from the last read
                    differenceInTicks = (currentTicks - previousTicks);
                    // figuring out our ticks per milliseconds
                    ticksPerMilliseconds = differenceInTicks/differenceInTime;
                    // now that we have our ticks per milliseconds, we can get our RPMS
                    revolutions = ticksPerMilliseconds/TICKS_PER_REVOLUTION;
                    // Multiplying ticks per milliseconds by 60000, which is exactly 1 minute
                    RPM = revolutions * 60000;
                    // resetting the ticks
                    previousTicks = currentTicks;



                    rpmError = RPM - rpmGoal;

                    RingLauncher.setPower(Range.clip(rpmError/990,-1,1));

                }

                telemetry.addData("RPM: ",RPM);
                telemetry.addData("Motor Power: ", RingLauncher.getPower());
                telemetry.addData("Robot Is Running To The Set Speed", "");
                telemetry.addData("rpm Intended Speed Speed", rpmQue);
                telemetry.update();
            }
*/

            encoderTicks = RingLauncher.getCurrentPosition();

            if (gamepad1.dpad_up && !touchingButton) {
                rpmQue = rpmQue + 50;
                touchingButton = true;

            }
            else if (gamepad1.dpad_down && !touchingButton) {
                rpmQue = rpmQue - 50;
                touchingButton = true;

            } else if (!gamepad1.dpad_up && !gamepad1.dpad_down){
                touchingButton = false;

            }

            if (gamepad1.a) {

                speedUp = true;
                rpmGoal = rpmQue;

            }


            //log these values
            telemetry.addData("RPM: ",RPM);
            telemetry.addData("Motor Power: ", RingLauncher.getPower());
            telemetry.addData("rpm Set Speed: ", rpmGoal);
            telemetry.addData("rpm Que Speed", rpmQue);
            telemetry.update();
        }
    }
}