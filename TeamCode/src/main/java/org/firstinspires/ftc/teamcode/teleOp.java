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
    NormalizedColorSensor colorSensor;

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

        RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        RightRear.setDirection(DcMotorSimple.Direction.REVERSE);

            // You can give the sensor a gain value, will be multiplied by the sensor's raw value before the
            // normalized color values are calculated. Color sensors (especially the REV Color Sensor V3)
            // can give very low values (depending on the lighting conditions), which only use a small part
            // of the 0-1 range that is available for the red, green, and blue values. In brighter conditions,
            // you should use a smaller gain than in dark conditions. If your gain is too high, all of the
            // colors will report at or near 1, and you won't be able to determine what color you are
            // actually looking at. For this reason, it's better to err on the side of a lower gain
            // (but always greater than  or equal to 1).
            float gain = 2;

            // Once per loop, we will update this hsvValues array. The first element (0) will contain the
            // hue, the second element (1) will contain the saturation, and the third element (2) will
            // contain the value. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
            // for an explanation of HSV color.
            final float[] hsvValues = new float[3];

            // xButtonPreviouslyPressed and xButtonCurrentlyPressed keep track of the previous and current
            // state of the X button on the gamepad
            boolean xButtonPreviouslyPressed = false;
            boolean xButtonCurrentlyPressed = false;

            // Get a reference to our sensor object. It's recommended to use NormalizedColorSensor over
            // ColorSensor, because NormalizedColorSensor consistently gives values between 0 and 1, while
            // the values you get from ColorSensor are dependent on the specific sensor you're using.
            colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");

            // If possible, turn the light on in the beginning (it might already be on anyway,
            // we just make sure it is if we can).
            if (colorSensor instanceof SwitchableLight) {
                ((SwitchableLight)colorSensor).enableLight(true);
            }
        //waitForStart();
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();




        }
        // printIng out the encoder counts
        telemetry.addData(" Left Front DRIVING TO: %7d ", LeftFront.getCurrentPosition());

        telemetry.addData("Left Rear DRIVING TO: %7d ", LeftRear.getCurrentPosition());
        telemetry.addData("Right Rear DRIVING TO: %7d ", RightRear.getCurrentPosition());
        telemetry.update();

        while (opModeIsActive()) {

            //Gamepad 1 left joystick x strafe
            while ((Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) && opModeIsActive()) {
                LeftRear.setPower(gamepad1.left_stick_y * 1 + gamepad1.left_stick_x * 1 + gamepad1.right_stick_x * -1);
                LeftFront.setPower(gamepad1.left_stick_y * 1 + gamepad1.left_stick_x * -1 + gamepad1.right_stick_x * -1);
                RightFront.setPower(gamepad1.left_stick_y * 1 + gamepad1.left_stick_x * 1 + gamepad1.right_stick_x * 1);
                RightRear.setPower(gamepad1.left_stick_y * 1 + gamepad1.left_stick_x * -1 + gamepad1.right_stick_x * 1);
                sleep(345);
                          }
            //Gamepad 1 left joystick x strafe
           LeftFront.setPower(0);
            RightFront.setPower(0);
            RightRear.setPower(0);
            LeftRear.setPower(0);
//            // Explain basic gain information via telemetry
//            telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.\n");
//            telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n");
//
//            // Update the gain value if either of the A or B gamepad buttons is being held
//            if (gamepad1.a) {
//                // Only increase the gain by a small amount, since this loop will occur multiple times per second.
//                gain += 0.005;
//            } else if (gamepad1.b && gain > 1) { // A gain of less than 1 will make the values smaller, which is not helpful.
//                gain -= 0.005;
//            }
//
//            // Show the gain value via telemetry
//            telemetry.addData("Gain", gain);
//
//            // Tell the sensor our desired gain value (normally you would do this during initialization,
//            // not during the loop)
//            colorSensor.setGain(gain);
//
//
//
//            // Get the normalized colors from the sensor
//            NormalizedRGBA colors = colorSensor.getNormalizedColors();
//
//
//            // Update the hsvValues array by passing it to Color.colorToHSV()
//            Color.colorToHSV(colors.toColor(), hsvValues);
//
//            telemetry.addLine()
//                    .addData("Red", "%.3f", colors.red)
//                    .addData("Green", "%.3f", colors.green)
//                    .addData("Blue", "%.3f", colors.blue);
//            telemetry.addLine()
//                    .addData("Hue", "%.3f", hsvValues[0])
//                    .addData("Saturation", "%.3f", hsvValues[1])
//                    .addData("Value", "%.3f", hsvValues[2]);
//            telemetry.addData("Alpha", "%.3f", colors.alpha);
//
//
//            telemetry.update();
//


            idle();
        }
    }
}