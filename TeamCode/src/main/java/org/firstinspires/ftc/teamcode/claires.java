package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Disabled
@Autonomous(name = "ComplexPowershot1ALeft", group = "Auto")
public class claires extends Motor_Classes {

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
    public void runOpMode() {
        int timeA;
        int side;
        int back;
        int Timeb;
        int RingCount;
        initrobot();
        waitForStart();
        timeA = 1700;
        side = 1;
        back = 1700;
        Timeb = 1300;
        initVuforia();
        initTfod();

        String ring = "default";
        int t = 0;

        // 1 is left, 0 is right
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();
            WobbleUp();

        }



        RingCount=0;
        motorpower(0.4);
        sleep(timeA);
        motorpower(0);
        while (t < 4 && opModeIsActive()) {
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
                        // if you find an object use a break to break out of a loop.  incrementing t twice in the loop if an object is detected.  _________________________
                        t = t + 1;

                    }
                    telemetry.update();
                }
            }
            t = t + 1;
        }

        sleep(1000);
        switch(ring){
            case"Single":
            {
                motorpower(0.4);
                sleep(1700);
                motorpower(0);
                Wobble.setPower(-0.1);
                sleep(1200);
                gripper.setPosition(.46);


            }
            case"Quad": {
                motorpower(0.4);
                sleep(2700);
                motorpower(0);
                strafeleft(0.4);
                sleep(500);
                straferight(0);
                Wobble.setPower(-0.1);
                sleep(1200);
                gripper.setPosition(.46);
            }

            case"Default":
            {
                motorpower(0.4);
                sleep(500);
                motorpower(0);
                strafeleft(0.4);
                sleep(500);
                straferight(0);
                Wobble.setPower(-0.1);
                sleep(1200);
                gripper.setPosition(.46);
            }
        }
        if (side==1) {
            strafeleft(0.45);
            sleep(timeA);
            strafeleft(0);
            sleep(1000);

            sleep(1000);

            motorpower(-0.4);
            sleep(625);
            motorpower(0);
            motorpower(1000);

            Launcher.setPower(0.67);


            motorpower(0.4);
            sleep(625);
            motorpower(0);
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

            strafeleft(0.45);
            sleep(timeA);
            strafeleft(0);
            sleep(1000);
            // detcedt correct area

            Wobble.setPower(-0.1);
            sleep(1000);

            motorpower(0.4);
            sleep(Timeb);
            motorpower(0);
            sleep(1000);

            straferight(0.45);
            sleep(2500);
            straferight(0);
            sleep(1000);
        }
        else {
            straferight(0.45);
            sleep(timeA/2);
            straferight(0);
            sleep(1000);

            motorpower(-0.4);
            sleep(625);
            motorpower(0);
            sleep(1000);
            Launcher.setPower(0.67);

            motorpower(0.4);
            sleep(625);
            motorpower(0);
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

            straferight(0.45);
            sleep(timeA);
            straferight(0);
            sleep(1000);
            // detect correct are
            Wobble.setPower(-0.1);
            sleep(1000);

            motorpower(0.4);
            sleep(Timeb);
            motorpower(0);
            sleep(1000);

            strafeleft(0.45);
            sleep(2500);
            strafeleft(0);
            sleep(1000);

        }

        Launcher.setPower(0);
        motorpower(-0.4);
        sleep(back);
        motorpower(0);


    }



    public void motorpower(double speed) {
        LeftFront.setPower(speed+0.02);
        RightFront.setPower(speed);
        LeftRear.setPower(speed+0.02);
        RightRear.setPower(speed);

    }

    public void straferight(double speed) {
        RightFront.setPower(-speed);
        RightRear.setPower(speed-0.06);
        LeftFront.setPower(speed);
        LeftRear.setPower(-speed);
    }

    public void strafeleft(double speed){
        LeftFront.setPower(-speed);
        LeftRear.setPower(speed+0.03);
        RightFront.setPower(speed);
        RightRear.setPower(-speed+0.3);
    }
    //   }
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
    public void WobbleUp(){
        Wobble.setPower(.85);
        sleep(723);
        Wobble.setPower(0.14);
    }
    public void LoaderShoot(){
        Loader.setPosition(.65);
        sleep(434);
        Loader.setPosition(.96);
        sleep(400);
    }
}




