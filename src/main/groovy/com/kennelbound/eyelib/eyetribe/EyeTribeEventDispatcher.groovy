package com.kennelbound.eyelib.eyetribe

import com.kennelbound.eyelib.events.Eye
import com.kennelbound.eyelib.events.HardwareConnectionStateEvent
import com.kennelbound.eyelib.events.HeadLocationEvent
import com.theeyetribe.client.GazeManager
import com.theeyetribe.client.IConnectionStateListener
import com.theeyetribe.client.IGazeListener
import com.theeyetribe.client.data.GazeData
import net.engio.mbassy.bus.MBassador
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct

/**
 * Created by samalsto on 3/23/15.
 */
class EyeTribeEventDispatcher implements IConnectionStateListener, IGazeListener {
    @Autowired
    MBassador eventBus;

    @PostConstruct
    void start() {
        GazeManager.instance.activate(GazeManager.ApiVersion.VERSION_1_0, GazeManager.ClientMode.PUSH, this);
        GazeManager.instance.addGazeListener(this);
    }

    void onConnectionStateChanged(boolean isConnected) {
        eventBus.publish(new HardwareConnectionStateEvent(isConnected ? HardwareConnectionStateEvent.State.CONNECTED : HardwareConnectionStateEvent.State.NOT_CONNECTED, 'Hardware state changed.'))
    }

    void onGazeUpdate(GazeData gazeData) {
        // Send the eye event information
        double distance = 0.0

        HeadLocationEvent headLocationEvent = new HeadLocationEvent()
        headLocationEvent.distance = distance
        headLocationEvent.left = getEye(gazeData.leftEye)
        headLocationEvent.right = getEye(gazeData.rightEye)
        headLocationEvent.smoothedViewX = gazeData.smoothedCoordinates.x
        headLocationEvent.smoothedViewY = gazeData.smoothedCoordinates.y
        eventBus.publish(headLocationEvent);
    }

    Eye getEye(GazeData.Eye gazeEye) {
        Eye eye = new Eye()
        eye.x = gazeEye.pupilCenterCoordinates.x
        eye.y = gazeEye.pupilCenterCoordinates.y
        eye.pupilSize = gazeEye.pupilSize
        eye.viewX = gazeEye.smoothedCoordinates.x
        eye.viewY = gazeEye.smoothedCoordinates.y

        return eye;
    }
}
