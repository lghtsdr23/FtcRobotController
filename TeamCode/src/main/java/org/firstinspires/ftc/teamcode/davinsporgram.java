        package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Autonomous Program", group="Linear Opmode")
//@Disabled
public class davinsporgram extends LinearOpMode {

    //region Declaring Motors
    // Declaring Motors
    DcMotor LeftFront;
    DcMotor LeftRear;
    DcMotor RightFront;
    DcMotor RightRear;

    DcMotor OdometryLeft;
    DcMotor OdometryRight;
    DcMotor OdometryCenter;
    //endregion

    //region Creating all Variables

    int color = -1; // -1 = red, 1 = blue, Defaults to red.
    int side = -1; // -1 = right, 1 = left, Defaults to right.
    int loopsThrough = 0;
    int rings = 0;

    //    double inchesDriven = 0;
//    double currentSpeed = 0.2;
//    double inchesToDrive = 100;
//    double topSpeed = 0.6;
    double LEFT_ODOMETRY_TICKS = 1517.89;
    double STRAFING_ODOMETRY_TICKS = 1500;

    String colorUI = "Red";
    String sideUI = "Right";

    //this variable is just for debug steps in the program. Remove it before commercial use.
    boolean DEBUG = true;

    boolean isOpmodeactive = false;
    //endregion

    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    private static final String VUFORIA_KEY =
            "ARCqabf/////AAABmSZ0wCx5pkN9gr0iWwVr7nMSv8dNn49XZv9+SuCY8CXlSs7do7TvoCHOTxqs/CMQyBaw1+pT8rdDN0FKyl5Ap4zlogqXslDSNAGclnsVNeXhcGb5Hf53rNWKPFselj4XpjzWVewj3GvmqeB3HStCAvWPwP1yjUf0e6I1p7SRs/IknkADhosUfsuMfGHgHEDPancZYTZuomf7aEiKXkvDeBuz1mA7IQJeQnp2Sw6kvGLqmxzgdCw6GpcJevSThLxm+Wt6AlYealSqWGLUjoAr58+PwbNeIE/h+knnbmH5O5KyKDmlYxUBne9T/MYN+2lVZmM7zY/9J5I39oHhl3q/aWC0rTOI9cLgT77kEyWU+UAM";
    private TFObjectDetector tfod;
    private VuforiaLocalizer vuforia;

    // Begin Elapsed Time, to keep track of how much time the program has been running for
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status ", "Initialized");
        telemetry.update();

        //region Mapping Hardware
        // Initialize the hardware variables. Note that the strings used here as parameters
        LeftFront = hardwareMap.get(DcMotor.class, "MotorLeftFront");
        LeftRear = hardwareMap.get(DcMotor.class, "MotorLeftRear");
        RightFront = hardwareMap.get(DcMotor.class, "MotorRightFront");
        RightRear = hardwareMap.get(DcMotor.class, "MotorRightRear");

        OdometryCenter = hardwareMap.get(DcMotor.class, "OdometryCenter");
        OdometryLeft = hardwareMap.get(DcMotor.class, "OdometryLeft");
        OdometryRight = hardwareMap.get(DcMotor.class, "OdometryRight");

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

        //region MENU
        // Print to see if opModeIsActive is true, or false
        telemetry.addData("opModeIsActive", opModeIsActive());
        telemetry.addData("Status", "Waiting For Program To begin!");
        telemetry.update();

        while (!gamepad1.a && !opModeIsActive()) {
            //region Setting Color, according to the color of the button
            // Waiting for the driver to select a color
            while (!gamepad1.x && !gamepad1.b && !opModeIsActive()) {
                sleep(1);

                telemetry.addData("Select What Color Your Alliance is: ", "");
                telemetry.addData("X for Blue Alliance, B For Red Alliance", "");
                telemetry.update();

            }

            // As soon as the button is hit the loop will be dropped out of, giving the driver no time to let go of whatever button they are holding.
            if (gamepad1.x) { // Blue

                // String 'colorUI' has nothing to do with the program. It is strictly for the drivers visuals.
                colorUI = "Blue";
                color = 1;

            } else if (gamepad1.b) { // Red

                // String 'colorUI' has nothing to do with the program. It is strictly for the drivers visuals.
                colorUI = "Red";
                color = -1;

            }
            //endregion

            //so that the driver is not holding down a button into the next while
            sleep(500);

            //region Selecting side, Right or Left
            while (!gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.b && !opModeIsActive()) {
                sleep(1);

                telemetry.addData("Select What Side Your Robot is on: ", "");
                telemetry.addData("Dpad Right for Right Side, Dpad Left for Left Side", "");
                telemetry.addData("Press 'b' to Re-do Last Selection: ", colorUI);
                telemetry.update();
            }

            if (gamepad1.dpad_left) {
                colorUI = "Left";
                color = 1;
            } else if (gamepad1.dpad_right) {
                colorUI = "Right";
                color = -1;
            }

            while (!gamepad1.a && !gamepad1.b && !opModeIsActive()) {
                sleep(1);

                telemetry.addData("Press 'a' to confirm your selection.", "");
                telemetry.addData("Press 'b' to restart the whole process.", "");
                telemetry.addData("The Robot is On the Color: ", colorUI);
                telemetry.addData("The Robot is On The ", " ' ", sideUI, " ' Side");
                telemetry.update();
            }
            //endregion

            if (gamepad1.b) {
                sleep(500);
            }

        }
        //endregion

        telemetry.addData("The Robot is On the Color: ", colorUI);
        telemetry.addData("The Robot is On The ", " ' ", sideUI, " ' Side");
        telemetry.addData("Have a good match! :D ", "");
        telemetry.update();

        //region INNIT CAMERA
        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 1.78 or 16/9).

            // Uncomment the following line if you want to adjust the magnification and/or the aspect ratio of the input images.
            //tfod.setZoom(2.5, 1.78);
        }
        //endregion

        // Wait for the game to start
        waitForStart();
        //Starting opmode Boolean
        isOpmodeactive = opModeIsActive();
        // Reset timer to start program
        runtime.reset();

        /*
        //region VUFORIA
        if (opModeIsActive()) {
            while (opModeIsActive()) {

                while (loopsThrough < 200) { //change to when it finds the picture, but this works for now.

                    loopsThrough = loopsThrough + 1;

                    if (tfod != null) {
                        // getUpdatedRecognitions() will return null if no new information is available since
                        // the last time that call was made.
                        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                        if (updatedRecognitions != null) {
                            telemetry.addData("# Object Detected", updatedRecognitions.size());
                            // step through the list of recognitions and display boundary info.
                            int i = 0;
                            for (Recognition recognition : updatedRecognitions) {
                                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                        recognition.getLeft(), recognition.getTop());
                                telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                        recognition.getRight(), recognition.getBottom());

                                if (recognition.getLabel() == "Quad"){

                                    rings = 4;

                                } else if(recognition.getLabel() == "Single"){

                                    rings = 1;

                                } else {

                                    rings = 0;

                                }
                            }
                            telemetry.update();
                        }
                    }
                }
                while (opModeIsActive()){
                    telemetry.addData("RINGS",rings);
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
        //endregion
         */

        //double
        LeftFront.setPower(0);
        RightRear.setPower(0);
        LeftRear.setPower(0);
        RightFront.setPower(0);

        //region Running until opModeIsActive is false
        // run until the end of the match (driver presses STOP)
        //while (opModeIsActive()) {

        DriveToInches(60, 0.5);

        //}
        //endregion

        //region Ending Program, Stopping Motors
        telemetry.addData("Status: ", "Program Finished, Motors Reset.");
        telemetry.update();

        // Reset Motors at the end of the test so robot stops moving.
        LeftFront.setPower(0);
        RightRear.setPower(0);
        LeftRear.setPower(0);
        RightFront.setPower(0);
        //endregion

    }


    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public void DriveToInches(double inchesToDrive, double topSpeed) {

        OdometryLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        OdometryRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        OdometryCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        OdometryRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryCenter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double inchesDriven = 0;
        double currentSpeed = 0.2;
        double backwards = 1;

        if (inchesToDrive < 0){
            backwards = -1;
            inchesToDrive = Math.abs(inchesToDrive);
        }

        //work on going backwards
        while ((inchesDriven < inchesToDrive * 0.25) & (opModeIsActive())) {
            if (currentSpeed < topSpeed) {
                //was OdometryLeft.getCurrentPosition()
                currentSpeed = (backwards * OdometryLeft.getCurrentPosition()) / ((LEFT_ODOMETRY_TICKS * inchesToDrive) * 0.25);
            }

            telemetry.addData("Status: ", "SPEED UP (DRIVING)");
            telemetry.addData("Inches Gone: ", inchesDriven);
            telemetry.addData("Encoder Ticks: ", backwards * OdometryLeft.getCurrentPosition());
            telemetry.addData("Goal: ", inchesToDrive);
            telemetry.addData("Current Speed: ", currentSpeed);
            telemetry.update();

            Range.clip(currentSpeed, -1, 1);
            //WAS odometryleft
            inchesDriven = ((backwards * OdometryLeft.getCurrentPosition()) / LEFT_ODOMETRY_TICKS);

            if (currentSpeed > 0.19) {
                LeftFront.setPower(backwards * currentSpeed);
                RightRear.setPower(backwards * currentSpeed);
                LeftRear.setPower(backwards * currentSpeed);
                RightFront.setPower(backwards * currentSpeed);
            } else {
                LeftFront.setPower(backwards * 0.2);
                RightRear.setPower(backwards * 0.2);
                LeftRear.setPower(backwards * 0.2);
                RightFront.setPower(backwards * 0.2);
            }
        }

        while ((inchesDriven < inchesToDrive * 0.75) && opModeIsActive()) {
            //was OdometryLeft.getCurrentPosition()
            inchesDriven = (backwards * OdometryLeft.getCurrentPosition() / LEFT_ODOMETRY_TICKS);
            idle();
        }

        while ((inchesDriven < inchesToDrive) && opModeIsActive()) {
            if (currentSpeed < topSpeed) {
                currentSpeed = (inchesToDrive * 0.25) / inchesDriven;
            }

            telemetry.addData("Status: ", "SLOW DOWN");
            telemetry.update();

            Range.clip(currentSpeed, -1, 1);

            inchesDriven = (LEFT_ODOMETRY_TICKS * (backwards * OdometryLeft.getCurrentPosition()));

            if (currentSpeed > 0.19) {
                LeftFront.setPower(backwards * currentSpeed);
                RightRear.setPower(backwards * currentSpeed);
                LeftRear.setPower(backwards * currentSpeed);
                RightFront.setPower(backwards * currentSpeed);
            } else {
                LeftFront.setPower(backwards * 0.2);
                RightRear.setPower(backwards * 0.2);
                LeftRear.setPower(backwards * 0.2);
                RightFront.setPower(backwards * 0.2);
            }
        }
    }

    public void StrafLeft(double inchesToDrive, double topSpeed) {

        OdometryLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        OdometryRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        OdometryCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        OdometryRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryCenter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double inchesDriven = 0;
        double currentSpeed = 0.2;

        while ((inchesDriven < inchesToDrive * 0.25) & (opModeIsActive())) {
            if (currentSpeed < topSpeed) {
                //was OdometryLeft.getCurrentPosition()
                currentSpeed = OdometryCenter.getCurrentPosition() / ((STRAFING_ODOMETRY_TICKS * inchesToDrive) * 0.25);
            }

            telemetry.addData("Status: ", "SPEED UP (STRAF LEFT)");
            telemetry.addData("Inches Gone: ", inchesDriven);
            telemetry.addData("Encoder Ticks: ", OdometryCenter.getCurrentPosition());
            telemetry.addData("Goal: ", inchesToDrive);
            telemetry.addData("Current Speed: ", currentSpeed);
            telemetry.update();

            Range.clip(currentSpeed, -1, 1);
            //WAS odometryleft
            inchesDriven = (OdometryCenter.getCurrentPosition() / STRAFING_ODOMETRY_TICKS);

            if (currentSpeed > 0.19) {
                LeftFront.setPower(currentSpeed);
                RightRear.setPower(currentSpeed);
                LeftRear.setPower(-currentSpeed);
                RightFront.setPower(-currentSpeed);
            } else {
                LeftFront.setPower(0.2);
                RightRear.setPower(0.2);
                LeftRear.setPower(-0.2);
                RightFront.setPower(-0.2);
            }
        }

        while (inchesDriven < inchesToDrive) {
            idle();
        }
    }

    public void StrafRight(double inchesToDrive, double topSpeed) {

        OdometryLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        OdometryRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        OdometryCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        OdometryRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryCenter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double inchesDriven = 0;
        double currentSpeed = 0.2;

        while ((inchesDriven < inchesToDrive * 0.25) & (opModeIsActive())) {
            if (currentSpeed < topSpeed) {
                //was OdometryLeft.getCurrentPosition()
                currentSpeed = -OdometryCenter.getCurrentPosition() / ((STRAFING_ODOMETRY_TICKS * inchesToDrive) * 0.25);
            }

            telemetry.addData("Status: ", "SPEED UP (STRAF RIGHT)");
            telemetry.addData("Inches Gone: ", inchesDriven);
            telemetry.addData("Encoder Ticks: ", OdometryCenter.getCurrentPosition());
            telemetry.addData("Goal: ", inchesToDrive);
            telemetry.addData("Current Speed: ", currentSpeed);
            telemetry.update();

            Range.clip(currentSpeed, -1, 1);
            //WAS odometryleft
            inchesDriven = (-OdometryCenter.getCurrentPosition() / STRAFING_ODOMETRY_TICKS);

            if (currentSpeed > 0.19) {
                LeftFront.setPower(-currentSpeed);
                RightRear.setPower(-currentSpeed);
                LeftRear.setPower(currentSpeed);
                RightFront.setPower(currentSpeed);
            } else {
                LeftFront.setPower(-0.2);
                RightRear.setPower(-0.2);
                LeftRear.setPower(0.2);
                RightFront.setPower(0.2);
            }
        }

        while (inchesDriven < inchesToDrive) {
            idle();
        }
    }
}