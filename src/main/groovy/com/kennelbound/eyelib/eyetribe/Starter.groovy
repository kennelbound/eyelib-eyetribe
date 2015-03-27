package com.kennelbound.eyelib.eyetribe

/**
 * Created by samalsto on 3/23/15.
 */
class Starter {
    public static void main(String ... args) {
        EyeTribeEyesManager manager = new EyeTribeEyesManager();
        manager.init();
        manager.start();
        while(1) {
            manager.viewEvent(0, 0);
            //
        }
    }
}
