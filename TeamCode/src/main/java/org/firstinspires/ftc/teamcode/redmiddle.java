package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "RED middle")
public class redmiddle extends LinearOpMode {

    // Motor Declaration
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

    // Gobilda Motor Specs
    double COUNTS_PER_MOTOR_GOBUILDA = 706;    // gobilda
    double DRIVE_GEAR_REDUCTION = 1;    // 1:1
    double WHEEL_DIAMETER_CM = 10;     // mecanum wheels

    double COUNTS_PER_CM_GOBUILDA = ((COUNTS_PER_MOTOR_GOBUILDA * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * Math.PI)) / 2;
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY =
            "ARCqabf/////AAABmSZ0wCx5pkN9gr0iWwVr7nMSv8dNn49XZv9+SuCY8CXlSs7do7TvoCHOTxqs/CMQyBaw1+pT8rdDN0FKyl5Ap4zlogqXslDSNAGclnsVNeXhcGb5Hf53rNWKPFselj4XpjzWVewj3GvmqeB3HStCAvWPwP1yjUf0e6I1p7SRs/IknkADhosUfsuMfGHgHEDPancZYTZuomf7aEiKXkvDeBuz1mA7IQJeQnp2Sw6kvGLqmxzgdCw6GpcJevSThLxm+Wt6AlYealSqWGLUjoAr58+PwbNeIE/h+knnbmH5O5KyKDmlYxUBne9T/MYN+2lVZmM7zY/9J5I39oHhl3q/aWC0rTOI9cLgT77kEyWU+UAM";

    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;


    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();
        //defining motors
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
        // reversing motors
        LeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        gripper.setPosition(.97);
        sleep(1000);
        Wobble.setPower(.75);
        sleep(623);
        Wobble.setPower(0.14);
        intakerelease.setPosition(.05);
        //waitForStart();
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();

//              /**
//         * Activate TensorFlow Object Detection before we wait for the start command.
//         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
//         **/
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

        }


            /** Wait for the game to begin */
            telemetry.addData(">", "Press Play to start op mode");
            telemetry.update();
            waitForStart();
            String ring = "default";
        int t= 0;
            while (opModeIsActive()) {
                straightDriveEncoder(.6,25,2 );
                Hopper.setPower(-.45);
                Launcher.setPower(.67);


                    while (t<4&& opModeIsActive()) {
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
                                    ring = recognition.getLabel();
                                    t=t+1;

                                }
                                telemetry.update();
                            }
                        }
                        t=t+1;
                    }




            strafeDriveEncoder(0.7, 45, "LEFT", 2.7);
            straightDriveEncoder(0.7, 125, 2.5);

            strafeDriveEncoder(.5, 55, "RIGHT", 2.8);
            Wobble.setPower(-0.1);
            sleep(1000);
                Loader.setPosition(.74);
                sleep(434);
                Loader.setPosition(.96);
                sleep(2000);
                Loader.setPosition(.74);
                sleep(434);
                Loader.setPosition(.96);
                sleep(2000);
                Loader.setPosition(.74);
                sleep(434);
                Loader.setPosition(.96);
                sleep(500);
                Launcher.setPower(0);
                Wobble.setPower(.75);
                sleep(623);
                Wobble.setPower(0.14);
//            straightDriveEncoder(.3,-50,2);
//            straightDriveEncoder(.3,50,2);
            // would be intake
//            double intakeamount = 2;
//            int intakeclipped = 2;
//            int intake = 2;
//            intakeamount = Range.clip(intakeclipped, 0, 2);
//            intake = (int) intakeamount;
        telemetry.addData("ring",ring);
            switch (ring) {
                case "Quad":
                    if (!isStopRequested() && opModeIsActive()) {
                        telemetry.addLine("Quad");
                        sleep(834);
                        straightDriveEncoder(.6, 200, 3.5);
                        strafeDriveEncoder(.3, 80, "RIGHT", 3.2);
//                        straightDriveEncoder(.2, -130, 3.2);
                        Wobble.setPower(-0.1);
                        sleep(1200);
                        gripper.setPosition(.46);
                        straightDriveEncoder(.6, -150, 3.5);
                        intakerelease.setPosition(.36);

                        break;
                    }

                case "Single":
                    if (!isStopRequested() && opModeIsActive()) {
                        telemetry.addLine("One");
                        strafeDriveEncoder(.4,30,"RIGHT",2.5);
                        straightDriveEncoder(.6, 120, 3.6);
//                        straightDriveEncoder(.4, -60, 3.2);
                        Wobble.setPower(-0.1);
                        sleep(1200);
                        gripper.setPosition(.46);
                        straightDriveEncoder(.6,-85,3.5);

                        intakerelease.setPosition(.36);
                        break;
                    }

                case "None":
                    if (!isStopRequested() && opModeIsActive()) {
                        telemetry.addLine("None");
                        strafeDriveEncoder(.5, 75, "RIGHT", 3.1);
//                        straightDriveEncoder(.5,48,2);
                        straightDriveEncoder(.5,30,3.2);
                        Wobble.setPower(-0.1);
                        sleep(1200);
                        gripper.setPosition(.46);
                        intakerelease.setPosition(.36);
                        break;
                    }

                case "default":
                    if (!isStopRequested() && opModeIsActive()) {
                        telemetry.addLine("None");
                        strafeDriveEncoder(.5, 75, "RIGHT", 3.1);
                        straightDriveEncoder(.5,40,3.2);
                        Wobble.setPower(-.05);
                        sleep(2000);
                        gripper.setPosition(.46);
                        sleep(1000);
                        Wobble.setPower(.65);
                        sleep(623);
                        Wobble.setPower(0.14);
                        intakerelease.setPosition(.36);
                        break;
                    }
                break;
            }
            break;
        }



    }

    public void straightDriveEncoder(double speed, double distanceCM, double timeCutOff) {
        int LeftFrontTarget;
        int backLeftTarget;
        int RightFrontTarget;
        int backRightTarget;
        double end = 0;
        double t = 0;
        // Setting Zero Behavior for Drive Train Motors
        LeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LeftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if (opModeIsActive()) {

            LeftFront.setMode(DcMotor.RunMode.RESET_ENCODERS);
            RightFront.setMode(DcMotor.RunMode.RESET_ENCODERS);
            LeftRear.setMode(DcMotor.RunMode.RESET_ENCODERS);
            RightRear.setMode(DcMotor.RunMode.RESET_ENCODERS);

            // Determine new target position, and pass to motor controller
            LeftFrontTarget = LeftFront.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA);
            RightFrontTarget = RightFront.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA);
            backLeftTarget = LeftRear.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA);
            backRightTarget = RightRear.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA);


            // set target position to each motor
            LeftFront.setTargetPosition(LeftFrontTarget);
            RightFront.setTargetPosition(RightFrontTarget);
            LeftRear.setTargetPosition(backLeftTarget);
            RightRear.setTargetPosition(backRightTarget);

            // Turn on run to position
            LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LeftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            LeftFront.setPower(Math.abs(speed));
            RightFront.setPower(Math.abs(speed));
            LeftRear.setPower(Math.abs(speed));
            RightRear.setPower(Math.abs(speed));

            t = getRuntime();
            end = getRuntime() + timeCutOff; //TODO THIS CAN BE AUTOMATED

            while (opModeIsActive() && !isStopRequested() &&
                    (getRuntime() <= end) &&
                    (LeftFront.isBusy() || RightFront.isBusy() || LeftRear.isBusy() || RightRear.isBusy())) {

                // Display it for the driver.
                telemetry.addData("RUN TIME CURRENT: ", "" + getRuntime());
                telemetry.addData("RUN TIME END: ", "" + end);
                telemetry.addData("FRONT LEFT MOTOR", " DRIVING TO: %7d CURRENTLY AT: %7d", LeftFrontTarget, LeftFront.getCurrentPosition());
                telemetry.addData("FRONT RIGHT MOTOR", "DRIVING TO: %7d CURRENTLY AT: %7d", RightFrontTarget, RightFront.getCurrentPosition());
                telemetry.addData("BACK LEFT MOTOR", "DRIVING TO: %7d CURRENTLY AT: %7d", backLeftTarget, LeftRear.getCurrentPosition());
                telemetry.addData("BACK RIGHT MOTOR", "DRIVING TO: %7d CURRENTLY AT: %7d", backRightTarget, RightRear.getCurrentPosition());
                telemetry.update();
            }

            telemetry.clearAll();
            telemetry.addData("FINISHED RUN: ", "" + (end - t));
            telemetry.addData("FRONT LEFT MOTOR", " DRIVING TO: %7d CURRENTLY AT: %7d", LeftFrontTarget, LeftFront.getCurrentPosition());
            telemetry.addData("FRONT RIGHT MOTOR", "DRIVING TO: %7d CURRENTLY AT: %7d", RightFrontTarget, RightFront.getCurrentPosition());
            telemetry.addData("BACK LEFT MOTOR", "DRIVING TO: %7d CURRENTLY AT: %7d", backLeftTarget, LeftRear.getCurrentPosition());
            telemetry.addData("BACK RIGHT MOTOR", "DRIVING TO: %7d CURRENTLY AT: %7d", backRightTarget, RightRear.getCurrentPosition());
            telemetry.update();

            // Stop all motion;
            LeftFront.setPower(0);
            RightFront.setPower(0);
            LeftRear.setPower(0);
            RightRear.setPower(0);

            //Turn off run to position
            LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            LeftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void strafeDriveEncoder(double speed, double distanceCM, String direction, double timeCutOff) {
        int frontLeftTarget = 0;
        int backLeftTarget = 0;
        int frontRightTarget = 0;
        int backRightTarget = 0;
        double end = 0;
        double t = 0;
        // Setting Zero Behavior for Drive Train Motors
        LeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LeftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        switch (direction) {
            case "LEFT":
                // Determine new target position, and pass to motor controller
                frontLeftTarget = LeftFront.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA * -1.4);
                frontRightTarget = RightFront.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA * 1.4);
                backLeftTarget = LeftRear.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA * 1.4);
                backRightTarget = (RightRear.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA * -1.4));


                // set target position to each motor
                LeftFront.setTargetPosition(frontLeftTarget);
                RightFront.setTargetPosition(frontRightTarget);
                LeftRear.setTargetPosition(backLeftTarget);
                RightRear.setTargetPosition(backRightTarget);

                // Turn on run to position
                LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LeftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                //Meant to calibrate drift in meccanum drive
                LeftFront.setPower(Math.abs(speed) * 1);// Change this if you want the robot to strafe more backwards
                RightFront.setPower(Math.abs(speed) * 1);// Change this if you want the robot to strafe more forwards
                LeftRear.setPower(Math.abs(speed) * 1);// Change this if you want the robot to strafe more forwards
                RightRear.setPower(Math.abs(speed) * 1);// Change this if you want the robot to strafe more backwards

                break;
            case "RIGHT":
                // Determine new target position, and pass to motor controller
                frontLeftTarget = LeftFront.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA * 1.4);
                frontRightTarget = RightFront.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA * -1.4);
                backLeftTarget = LeftRear.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA * -1.4);
                backRightTarget = RightRear.getCurrentPosition() + (int) (distanceCM * COUNTS_PER_CM_GOBUILDA * 1.4);

                // set target position to each motor
                LeftFront.setTargetPosition(frontLeftTarget);
                RightFront.setTargetPosition(frontRightTarget);
                LeftRear.setTargetPosition(backLeftTarget);
                RightRear.setTargetPosition(backRightTarget);

                // Turn on run to position
                LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                LeftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                //Meant to calibrate drift in meccanum drive
                LeftFront.setPower(Math.abs(speed) * 1);// Change this if you want the robot to strafe more backwards
                RightFront.setPower(Math.abs(speed) * 1);// Change this if you want the robot to strafe more forwards
                LeftRear.setPower(Math.abs(speed) * 1);// Change this if you want the robot to strafe more forwards
                RightRear.setPower(Math.abs(speed) * 1);// Change this if you want the robot to strafe more backwards

                break;
        }
        if (opModeIsActive()) {

            t = getRuntime();
            end = timeCutOff + getRuntime();//TODO THIS CAN BE AUTOMATED

            while (opModeIsActive() && !isStopRequested() &&
                    (getRuntime() <= end) &&
                    (LeftFront.isBusy() || RightFront.isBusy() || LeftRear.isBusy() || RightRear.isBusy())) {


                //TODO this is where you can correct the robot while its driving
                /*
                Use parallel encoder to the direction in which your traveling
                if(parallel encoder > 0){
                    // this means that the heading has drifted c
                }else if(parallel encoder < 0){
                // this means that the heading has drifted cc
                }
                 */

                // Display it for the driver.
                telemetry.addData("RUN TIME CURRENT: ", "" + getRuntime());
                telemetry.addData("RUN TIME END: ", "" + end);
                telemetry.addData("FRONT LEFT MOTOR", " DRIVING TO: %7d CURRENTLY AT: %7d", frontLeftTarget, LeftFront.getCurrentPosition());
                telemetry.addData("FRONT RIGHT MOTOR", "DRIVING TO: %7d CURRENTLY AT: %7d", frontRightTarget, RightFront.getCurrentPosition());
                telemetry.addData("BACK LEFT MOTOR", "DRIVING TO: %7d CURRENTLY AT: %7d", backLeftTarget, LeftRear.getCurrentPosition());
                telemetry.addData("BACK RIGHT MOTOR", "DRIVING TO: %7d CURRENTLY AT: %7d", backRightTarget, RightRear.getCurrentPosition());
                telemetry.update();
            }

            telemetry.clearAll();
            telemetry.addData("FINISHED RUN: ", "" + (end - t));
            telemetry.update();

            // Stop all motion;
            LeftFront.setPower(0);
            RightFront.setPower(0);
            LeftRear.setPower(0);
            RightRear.setPower(0);

            //Turn off run to position
            LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            LeftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }

    }
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
}
