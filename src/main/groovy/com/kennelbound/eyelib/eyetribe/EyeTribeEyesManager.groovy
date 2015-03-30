package com.kennelbound.eyelib.eyetribe

import com.kennelbound.eyelib.AbstractEyesManager
import com.kennelbound.eyelib.events.HardwareConnectionStateEvent
import com.theeyetribe.client.GazeManager
import com.theeyetribe.client.IConnectionStateListener
import com.theeyetribe.client.IGazeListener
import com.theeyetribe.client.data.GazeData

/**
 * Created by samalsto on 3/23/15.
 */
class EyeTribeEyesManager extends AbstractEyesManager implements IConnectionStateListener, IGazeListener {
    void start() {
        GazeManager.instance.activate(GazeManager.ApiVersion.VERSION_1_0, GazeManager.ClientMode.PUSH, this);
        GazeManager.instance.addGazeListener(this);
    }

    void onConnectionStateChanged(boolean isConnected) {
        this.hardwareConnectionStateEvent(isConnected ? HardwareConnectionStateEvent.State.CONNECTED : HardwareConnectionStateEvent.State.NOT_CONNECTED);
    }

    void onGazeUpdate(GazeData gazeData) {
        // Send the eye event information
        double distance, leftX, leftY, leftPupilSize, rightX, rightY, rightPupilSize
        boolean leftOpen, rightOpen

        distance = 0.0;

        leftX = gazeData.leftEye.pupilCenterCoordinates.x
        leftY = gazeData.leftEye.pupilCenterCoordinates.y
        leftPupilSize = gazeData.leftEye.pupilSize
        leftOpen = leftPupilSize > 0

        rightX = gazeData.rightEye.pupilCenterCoordinates.x
        rightY = gazeData.rightEye.pupilCenterCoordinates.y
        rightPupilSize = gazeData.rightEye.pupilSize
        rightOpen = rightPupilSize > 0

        eyesLocationEvent(distance, leftX, leftY, leftPupilSize, leftOpen, rightX, rightY, rightPupilSize, rightOpen)

        // View event
        double x, y
        x = gazeData.smoothedCoordinates.x
        y = gazeData.smoothedCoordinates.y

        viewEvent(x, y)
    }
}
