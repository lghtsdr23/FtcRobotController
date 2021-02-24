package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Autonomous(name = "RED middleclaire")
@Disabled
public class redmiddlecl extends Auto_Methods {
    private TFObjectDetector tfod;
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY =
            "ARCqabf/////AAABmSZ0wCx5pkN9gr0iWwVr7nMSv8dNn49XZv9+SuCY8CXlSs7do7TvoCHOTxqs/CMQyBaw1+pT8rdDN0FKyl5Ap4zlogqXslDSNAGclnsVNeXhcGb5Hf53rNWKPFselj4XpjzWVewj3GvmqeB3HStCAvWPwP1yjUf0e6I1p7SRs/IknkADhosUfsuMfGHgHEDPancZYTZuomf7aEiKXkvDeBuz1mA7IQJeQnp2Sw6kvGLqmxzgdCw6GpcJevSThLxm+Wt6AlYealSqWGLUjoAr58+PwbNeIE/h+knnbmH5O5KyKDmlYxUBne9T/MYN+2lVZmM7zY/9J5I39oHhl3q/aWC0rTOI9cLgT77kEyWU+UAM";

    private VuforiaLocalizer vuforia;
    @Override
    public void runOpMode() {
       initrobot();
        String ring = "default";
        int t = 0;





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
                shoot();
                shoot();
                shoot();
                Wobble.setPower(.75);
                sleep(623);
                Wobble.setPower(0.14);
        telemetry.addData("ring",ring);
            switch (ring) {
                case "Quad":
                    if (!isStopRequested() && opModeIsActive()) {
                        telemetry.addLine("Quad");
                        sleep(834);
                        straightDriveEncoder(.6, 200, 3.5);
                        strafeDriveEncoder(.3, 80, "RIGHT", 3.2);

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

        }



    }
    private void initTfod(){
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

}